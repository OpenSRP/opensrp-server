function (head, req) {
	start({
		'headers' : {
			'Content-Type' : 'text/csv'
		}
	});
	Array.prototype.unique = function () {
		var a = this;
		for (var i = 0; i < a.length; ++i) {
			for (var j = i + 1; j < a.length; ++j) {
				if (a[i] === a[j])
					a.splice(j, 1);
			}
		}
		return a;
	};
	String.prototype.replaceAll = function (search, replacement) {
		var target = this;
		return target.split(search).join(replacement);
	};
	var getheaders = function (name, value, parent, ignore) {
		var ret = '';
		if (ignore.indexOf(name) == -1) {
			if (typeof(value) == 'object') {
				for (var x in value) {
					var family = name;
					if (parent != '')
						family = parent + '.' + name;
					ret = ret + getheaders(x, value[x], family, ignore);
				}
			} else {
				if (!isNaN(name)) {
					ret = ret + parent + '[' + name + '],';
				} else {
					if (parent != '') {
						ret = ret + parent + '.' + name + ',';
					} else {
						ret = ret + name + ',';
					}
				}
			}
		}
		return ret;
	};
	var tocsv = function (therow, heads) {
		ret = '';
		for (var h in heads) {
			ret = ret + '\"' + valueByPath(therow, heads[h].toString()) + '\",';
		}
		return ret;
	};
	var valueByPath = function (obj, path) {
		var result = obj;
		var parr = path.split('.');
		for (var idx = 0; idx < parr.length; idx++) {
			try {
				result = result[parr[idx]];
			} catch (e) {
				return '';
			}
		}
		return result;
	};
	var row;
	var rows = [];
	var headers = [];
	var yasalio = false;
	while (row = getRow()) {
		header = getheaders('', row, '', ['fields', 'existing_birth_date', 'e_opv3', 'existing_ethnicity', 'e_opv2', 'e_opv1', 'e_opv0', 'first_name_note', 'existing_province', 'child_was_suffering_from_a_disease_at_birth_note', 'mother_name_note', 'existing_mother_name', 'program_client_id_note', 'e_measles1', 'existing_union_council', 'existing_union_councilname', 'e_measles2', 'existing_townname', 'existing_provincename', 'existing_epi_card_number', 'e_bcg', 'existing_reminders_approval', 'calc_dob_note', 'exisiting_ethnicity_note', 'existing_town', 'existing_city_village', 'existing_landmark', 'existing_contact_phone_number', 'existing_client_reg_date_note', 'existing_address1', 'existing_gender', 'last_name_note', 'existing_child_was_suffering_from_a_disease_at_birth', 'existing_client_reg_date', 'existing_last_name', 'e_penta3', 'e_penta2', 'e_penta1', 'existing_city_villagename', 'e_pcv2', 'e_pcv1', 'gender_note', 'e_pcv3', 'existing_first_name', 'e_ipv', 'existing_program_client_id']);
		header = header.split(',');
		headers = headers.concat(header).unique();
		rows.push(row);
	}
	for (var y = 0; y < headers.length; y++) {
		var ss = headers[y].toString().split('.');
		send('\"' + ss[ss.length - 1] + '\",');
	}
	send('\\n');
	for (var x in rows) {
		send(tocsv(rows[x], headers) + '\\n');
	}
	return '';
}