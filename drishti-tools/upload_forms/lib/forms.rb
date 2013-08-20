class Forms
  def initialize mobile_worker, ec_data, anc_data, anc_services_data
    @mobile_worker = mobile_worker
    @ec = ec_data
    @ancs = anc_data
    @anc_services = anc_services_data
  end

  def fill_for_in_area
    fill_ec_form
    fill_anc_registration_forms if has_anc?
    #fill_anc_services_forms if has_anc? and has_services?
    #fill_anc_outcome_forms if has_anc? and has_services? and has_outcome?
  end

  def fill_for_out_of_area
    fill_out_of_area_anc_registration_forms if has_anc?
    #fill_anc_services_forms if has_anc? and has_services?
    #fill_anc_outcome_forms if has_anc? and has_services? and has_outcome?
  end

  def fill_ec_form
    puts "EC: #{@ec['Wife Name']} - #{@ec['Husband Name']} - #{@ec['Entity ID']}"

    form_instance_erb = ERB.new(File.read('templates/ec_form_instance.erb'))
    ec_registration_erb = ERB.new(File.read('templates/ec_registration.erb'))

    ec = @ec
    user_name = @mobile_worker.user_name
    form_instance = form_instance_erb.result(binding)
    ec_registration_json = ec_registration_erb.result(binding)
    File.open("output/EC_#{ec['Entity ID']}.json", "w") do |f| f.puts ec_registration_json end
  end

  def fill_out_of_area_anc_registration_forms
    @ancs.each do |anc|

      puts "    Out of area ANC registration: #{anc['Wife Name']} - #{anc['Husband Name']} - #{anc['LMP']} - #{anc['Entity ID']}"

      form_instance_erb = ERB.new(File.read('templates/anc_oa_form_instance_erb.json'))
      out_of_area_anc_registration_erb = ERB.new(File.read('templates/anc_registration.erb'))

      ec = @ec
      user_id = @mobile_worker.user_id
      user_name = @mobile_worker.user_name
      form_name = "anc_registration_oa"
      instance_id = anc['Instance ID']
      entity_id = anc['Entity ID']

      form_instance = form_instance_erb.result(binding)
      out_of_area_anc_registration_json = out_of_area_anc_registration_erb.result(binding)
      File.open("output/ANCOutOfArea_#{anc['Entity ID']}.json", "w") do |f| f.puts out_of_area_anc_registration_json end
    end
  end

  def fill_anc_registration_forms
    @ancs.each do |value|
      anc = get_safe_map(value)
      puts "    ANC registration: #{anc['LMP']} - #{anc['Entity ID']}"

      form_instance_erb = ERB.new(File.read('templates/anc_form_instance_erb.json'))
      anc_registration_erb = ERB.new(File.read('templates/anc_registration.erb'))

      ec = @ec
      user_id = @mobile_worker.user_id
      user_name = @mobile_worker.user_name
      form_name = "anc_registration"
      instance_id = anc['Instance ID']
      entity_id = anc['Entity ID']

      form_instance = form_instance_erb.result(binding)
      anc_registration_json = anc_registration_erb.result(binding)
      File.open("output/ANC_#{anc['Entity ID']}.json", "w") do |f| f.puts anc_registration_json end
    end
  end

  def fill_anc_services_forms
    (@anc_services.sort_by {|service| service['Visit Number']}).each_with_index do |anc_service, index|
      puts "        ANC service: Visit number: #{anc_service['Visit Number']}"

      anc_visit_erb = ERB.new(File.read('templates/anc_visit.erb'))

      anc = @ancs.find {|anc| anc['a.Thayi Card Number'] == anc_service['Thayi Card Number']}
      anc = @ancs.last if anc.nil?
      ec = @ec
      user_id = @mobile_worker.user_id
      user_name = @mobile_worker.user_name

      anc_visit_xml = anc_visit_erb.result(binding)
      File.open("output/ANCVisit_#{index.to_s.rjust(4, '0')}_#{anc_service['Instance ID']}.xml", "w") do |f| f.puts anc_visit_xml end
    end
  end

  def fill_anc_outcome_forms
    anc_outcome_erb = ERB.new(File.read('templates/anc_outcome.erb'))

    anc_service = @anc_services.first
    anc = @ancs.find {|anc| anc['a.Thayi Card Number'] == anc_service['Thayi Card Number']}
    anc = @ancs.last if anc.nil?
    user_id = @mobile_worker.user_id
    user_name = @mobile_worker.user_name

    puts "    Have ANC outcome: On #{anc_service['Date of Delivery']}. Result: #{anc_service['Outcomes']}"

    anc_outcome_xml = anc_outcome_erb.result(binding)
    File.open("output/ANCOutcome_#{anc_service['Outcome Instance ID']}.xml", "w") do |f| f.puts anc_outcome_xml end
  end

  def has_anc?
    not (@ancs.nil? or @ancs.to_a.empty?)
  end

  def has_services?
    not (@anc_services.nil? or @anc_services.to_a.empty?)
  end

  def has_outcome?
    @anc_services.any? {|service| not service['Date of Delivery'].nil? and not service['Date of Delivery'].empty? }
  end

  private
  def get_safe_map(value)
    raise "Multiple values found for key : [#{value[0]['Village Code'].village}, #{value[0]['Wife Name']}, #{value[0]['Husband Name']}]" if value.size > 1
    value[0]
  end

end

