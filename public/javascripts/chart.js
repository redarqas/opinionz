function Chart(id, width, height, units)  {
	
    this.barWidth = 0.6 * (width / units);
    this.barWidthOffset = 0.4 * (width / units);

	this.paper = Raphael(id, width, height);

    this.norm = 0;

	this.frame = function() {
		this.paper.rect(0, 0, width, height);
	}
 
	this.axis = function () {
        this.paper.rect(0, height/2, width, 1);        
    };

    this.neutral_bar = function (index, percent) {
        var barHeight = (percent * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index* this.barWidthOffset), height/2 - barHeight/2, this.barWidth, barHeight);
        bar.attr("fill", "#E78D41");
        bar.attr("stroke-width", "0");
    }

    this.positive_bar = function (index, percent) {
        var barHeight = (percent * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index* this.barWidthOffset), height/2 - barHeight, this.barWidth, barHeight);
        bar.attr("fill", "#92d139");
        bar.attr("stroke-width", "0");
    }

    this.negative_bar = function (index, percent) {
        var barHeight = (percent * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index * this.barWidthOffset), height/2, this.barWidth, barHeight);
        bar.attr("fill", "#FA5858");
    }

    this.bar = function(index, neutral, positive, negative) {
        this.positive_bar(index, (positive/this.norm));
        this.negative_bar(index, (negative/this.norm));
        this.neutral_bar(index, (neutral/this.norm));
    }

    this.clear = function() {
        this.paper.clear();
    }

}