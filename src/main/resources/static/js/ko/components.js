ko.components.register("selectgeo", {
	template: "<div class='componentContainer'>" +
				"	<input class='selectgeo' type='text' data-bind='selectgeo: {value: value}' placeholder='input city to view weather'/>" +
				"</div>",
	viewModel: function (params) {
		var self = this;
		self.value = params.value;
	}
});

ko.components.register("selectdict", {
	template: "<div>" +
				"	<label class='componentLabel' for='selectdict'><span>select meteo service:</span></label>" +
				"	<select class='selectdict' data-bind='selectdict: {value: value, dict: dict, columns: columns, label: label}'></select>" +
				"</div>",
	viewModel: function (params) {
		var self = this;
		self.value = params.value;
		self.dict = params.dict;
		self.columns = params.columns;
		self.label = params.label;
	}
});

ko.components.register("meteoview", {
	template: "<div class='componentContainer'>" +
				"	<div class=\"selectGeoContainer\" data-bind=\"component: {name: 'selectgeo', params: {value: selectedGeoValue}}\"></div>" +
				"	<div class='selectServiceContainer'" +
				"		data-bind='component: {name: \"selectdict\", params: {value: selectedServiceValue, dict: dictSelectService, columns: meteoServiceColumns, label: meteoServiceDisplayColumn}}'>" +
				"	</div>" +
				"	<div class'meteoservice' data-bind='meteoservice: {value: resultValue, service: selectedServiceValue, geo: selectedGeoValue}'>" +
				"		<div class='temperature'><label class='componentLabel' for='temperature'><span>temperature:</span></label><span data-bind='text: temperature'></span></div>" +
				"		<div class='humidity'><label class='componentLabel' for='humidity'><span>humidity:</span></label><span data-bind='text: humidity'></span></div>" +
				"		<div class='precipitation'><label class='componentLabel' for='precipitation'><span>precipitation:</span></label><span data-bind='text: precipitation'></span></div>" +
				"	</div>" +
				"</div>",
	viewModel: function (params) {
		var self = this;
		self.selectedGeoValue = ko.observable(utils.getLocalData("selectgeo-localcache", {countryCode: "", country: "", city: "", latitude: "", longitude: ""}));
		self.selectedGeoValue.subscribe(function () {
			console.info("selectedGeoValue: %s", self.selectedGeoValue());
			utils.setLocalData("selectgeo-localcache", self.selectedGeoValue());
		});
		self.selectedServiceValue = ko.observable(utils.getLocalData("select-service-localcache", {id : "", name : "", label : ""}));
		self.dictSelectService = params.dictSelectService;
		self.meteoServiceColumns = ko.observableArray(["id", "name", "label"]);
		self.meteoServiceDisplayColumn = ko.observable("label");
		self.selectedServiceValue.subscribe(function () {
			console.info("selectedServiceValue: %s", self.selectedServiceValue());
			utils.setLocalData("select-service-localcache", self.selectedServiceValue());
		});
		self.resultValue = ko.observable({temperature : "", humidity : "", precipitation: ""});
		self.temperature = ko.pureComputed(function() {
			return self.resultValue().temperature;
		}, self);
		self.humidity = ko.pureComputed(function() {
			return self.resultValue().humidity;
		}, self);
		self.precipitation = ko.pureComputed(function() {
			return self.resultValue().precipitation;
		}, self);
	}
});