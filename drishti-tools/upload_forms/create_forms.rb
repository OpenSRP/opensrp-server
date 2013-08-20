require_relative 'lib/read_ecs_from_xlsx.rb'
require_relative 'lib/read_ancs_from_xlsx.rb'
require_relative 'lib/forms.rb'
require_relative 'lib/mobile_workers.rb'

Dir['output/*.xml'].each { |file| FileUtils.rm_f file }
Dir['output/DONE/*.xml'].each { |file| FileUtils.rm_f file }

user_name = ARGV[0].to_s

mobile_worker = MobileWorkers.new.find_by_user_name user_name
puts "Creating forms for user: '#{user_name}' with spreadsheet '#{mobile_worker.spreadsheet}'"

ecs = ECs.new(mobile_worker.spreadsheet).ecs
ancs_per_ec = ANCs.new(mobile_worker.spreadsheet).ancs_grouped_per_couple
ancs_in_area = ancs_per_ec.reject { |k, v| v[0]['OA'].downcase == 'yes'}

#anc_services_per_ec = ANCServices.new(mobile_worker.spreadsheet).anc_services_per_ec

puts "Got: ECs: #{ecs.size}"

ecs.each do |ec|
  anc_for_ec = ancs_in_area.select { |k, v|
    k == [ec['Village Code'].village.downcase, ec['Wife Name'].downcase, ec['Husband Name'].downcase]
  }
  form = Forms.new(mobile_worker, ec, anc_for_ec.values, [])
  form.fill_for_in_area
end

ancs_per_ec.each do |anc_key, anc_values|
  if anc_values[0]['OA'].downcase == "yes" then
    Forms.new(mobile_worker, nil, anc_values, []).fill_for_out_of_area
  end
end
