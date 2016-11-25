function (head, req) {
	start({
		'headers' : {
			'Content-Type' : 'text/csv'
		}
	});
	try {
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
		var toAggregatedRow = function (obj, existing, columns, received) {
			if (!existing) {
				existing = {};
				existing['other'] = new Array();
			}
			var onlyOther = true;
			for (var vac in columns) {
				var v = valueByPath(obj, 'doc.formInstance.form.fieldsAsMap.' + columns[vac]);
				if (v && v !== '') {
					onlyOther = false;
					if (existing[columns[vac]] == null) {
						existing[columns[vac]] = new Array();
					}
					existing[columns[vac]].push(v);
				}
			}
			if (onlyOther) {
				existing['other'].push(received);
			}
			return existing;
		};
		var row;
		var rows = [];
		var finalRes = {};
		var columns = ['vaccines_2', 'tt1', 'tt2', 'tt3', 'tt4', 'tt5'];
		while (row = getRow()) {
			var c = valueByPath(row, 'doc.formInstance.form.fieldsAsMap.provider_uc');
			var vr = valueByPath(row, 'doc.formInstance.form.fieldsAsMap.vaccines_2');
			finalRes[c] = toAggregatedRow(row, finalRes[c], columns, vr);
			rows.push(row);
		}
		send('\"UC\",\"Total\",\"TT1\",\"TT2\",\"TT3\",\"TT4\",\"TT5\",\\n');
		for (var k in finalRes) {
			send('\"' + k + '\",');
			for (var vac = 0; vac < columns.length; vac++) {
				var cnt = (finalRes[k][columns[vac]]);
				send('\"' + (cnt ? cnt.length : '') + '\",');
			}
			send('\\n');
		}
	} catch (e) {
		send(e + '');
	}
	return '';
}