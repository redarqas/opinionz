function Chart(width, height)  {
	

    this.barWidth = 10;
    this.barWidthOffset = 5;

	this.paper = Raphael(0, 0, width, height);

	this.frame = function() {
		this.paper.rect(0, 0, width, height);
	}
 
	this.axis = function () {
        this.paper.rect(0, height/2, width, 1);        
    };

    this.positive_bar = function (index, percent) {
        var barHeight = ((percent/100) * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index* this.barWidthOffset), height/2 - barHeight, this.barWidth, barHeight);
        bar.attr("fill", "#92d139");
    }

    this.negative_bar = function (index, percent) {
        var barHeight = ((percent/100) * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index * this.barWidthOffset), height/2, this.barWidth, barHeight);
        bar.attr("fill", "#FA5858");
    }

    this.bar = function(index, positive, negative) {
        this.positive_bar(index, positive);
        this.negative_bar(index, negative);
    }


}