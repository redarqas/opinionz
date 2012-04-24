function Viewport(data, size) {

	this.size = size;
	this.data = data;

	//this.end = 0;
	this.start = -1;

	this.backward = function() {
		//if ((this.size - this.data.units.length + 1) == this.end)
		//	return;
		//this.end--;
		if (this.start == -1) {
			this.start = this.data.units.length - this.size;
		}
		this.start--;
	}

	this.forward = function() {
		//if (this.end == 0)
		//	return;
		//this.end++;
		if (this.start == -1)
			return;
		
		this.start++;

		if (this.start == this.data.units.length - this.size) {
			this.start = -1
		}
	}

	this.units = function() {

		//var slice_end = this.data.units.length + this.end - 1;
		//var slice_start = Math.max(0, slice_end - this.size);

		if (this.start == -1) {
			var slice_end = Math.max(0, this.data.units.length - 1);
			var slice_start = Math.max(0, slice_end - this.size);
			return this.data.units.slice(slice_start, slice_end);
		} else {
			var slice_start = this.start;
			var slice_end = this.start + this.size;
			return this.data.units.slice(slice_start, slice_end);	
		}
	}

	Array.prototype.max = function () {
    	return Math.max.apply(Math, this);
	};

	this.max = function() {

		var units = this.units();

		if (units.length == 0)
			return 0;

		return units.map(function(unit) {
			return unit.max();
		}).max(); 
	}

}