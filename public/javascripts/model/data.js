function Data() {

	this.interval = 3000;

	this.units = [];

	this.add = function (tweet) {
		//TODO we need to analyse tweet date to see if we create a new unit based on the interval
		var unit = new TimeUnit();
		unit.add(tweet);
		this.units.push(unit);
	}
}