var utils = {
	jsonParse: function (data, defaultValue) {
		try {
			if (data) {
				return JSON.parse(data);
			} else {
				return defaultValue;
			}
		} catch (e) {
			return defaultValue;
		}
	},	
	jsonStringify: function (data, defaultValue) {
		var result = defaultValue;
		if ($.isPlainObject(data)) {
			if (!$.isEmptyObject(data)) {
				result = JSON.stringify(data);
			}
		} else if ($.isArray(data)) {
			if (data.length != 0) {
				result = JSON.stringify(data);
			} 
		} 
		return result;
	},
	setLocalData: function (name, value) {
		var stringValue = utils.jsonStringify(value, null);
		if (stringValue) {
			localStorage.setItem(name, stringValue);
		} else {
			localStorage.removeItem(name);
		}
	},
	getLocalData: function (name, defaultValue) {
		var jsonString = localStorage.getItem(name);
		return utils.jsonParse(jsonString, defaultValue);
	}	
};