ko.bindingHandlers.selectgeo = {
	init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
		var input = $(element);
		var params = valueAccessor();
		console.info("init: value: %s", params.value());
		input.jeoCityAutoComplete({
			callback : function (details) {
				console.debug("callback: details: %s", details);
				if (!details || !details.countryCode || !details.countryName || !details.toponymName || !details.lat || !details.lng) {
					params.value({countryCode: "", country: "", city: "", latitude: "", longitude: ""});
				} else {
					params.value({
							countryCode: details.countryCode, 
							country: details.countryName, 
							city: details.toponymName, 
							latitude: details.lat, 
							longitude: details.lng
						});
				}
			},
			displayNameFunc : function (item) {
				return item.toponymName + ", " + item.countryName;
			}
		});
		input.on("autocompletechange", function(event, ui) {
			if (!ui.item) {
				params.value({countryCode: "", country: "", city: ""});
			}
		});
		if (params.value() && params.value().city && params.value().country) {
			input.val(params.value().city + ", " + params.value().country);
		}
	}
};

ko.bindingHandlers.selectdict = {
	init: function (element, valueAccessor, allBindingsAccessor, viewModel) {	
		var select = $(element);
		var params = valueAccessor();
		var data = [];
		api.$bind(params.dict, data);
		data.$load().then(function() {             
			console.info("selectdict.init: dict: %s data: %s", params.dict, data);
			$.each(data, function(k, v) {
				$("<option />", {text: v[params.label()], val: v[params.label()]}).appendTo(select);
			});
			if (params.value()) {
				if (select.find("option[value='" + params.value()[params.label()] + "']").length) {
					select.val(params.value()[params.label()]);
				}
			}
			function onSelect(val) {
				$.each(data, function(k, v) {
					if (v[params.label()] == val) {
						params.value(v);
					}
				});
			}
			select.selectmenu({
				change: function (event, ui) {
					console.info("selectdict.change: ui: %s val: %s", ui, select.val());
					onSelect(select.val());
				}, 
				create: function( event, ui ) {
					console.info("selectdict.create: ui: %s val: %s", ui, select.val());
					onSelect(select.val());
				}
			});
			return data;           
		});
	}
};

ko.bindingHandlers.meteoservice = {
	init: function (element, valueAccessor, allBindingsAccessor, viewModel) {	
		var div = $(element);
		var params = valueAccessor();
		function loadMeteodata(service, latitude, longitude) {
			var data = [];
			api.$bind("meteo-data/search/findByServiceNameIsAndLatitudeIsAndLongitudeIs", data);
			data.$load({service: service, latitude: latitude, longitude: longitude}).then(function() {
				console.info("meteoservice.init: data: %s", data);
				if (!data || data.length < 1) {
					console.error("fault load meteo data");
					params.value({temperature : "error", humidity : "error", precipitation: "error"});
					return data;
				}
				params.value({temperature : data[0].temperature, humidity : data[0].humidity, precipitation: data[0].precipitation});
			});
		}
		params.geo.subscribe(function () {
			loadMeteodata(params.service()["name"], params.geo()["latitude"], params.geo()["longitude"]);
		});
		params.service.subscribe(function () {
			loadMeteodata(params.service()["name"], params.geo()["latitude"], params.geo()["longitude"]);
		});
		if (params.service() && params.service()["name"] && params.geo && params.geo()["latitude"] && params.geo()["longitude"]) {
			loadMeteodata(params.service()["name"], params.geo()["latitude"], params.geo()["longitude"]);
		}
	}
};