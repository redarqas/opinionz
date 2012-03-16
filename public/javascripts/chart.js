function Chart(id, width, height)  {
	

    this.barWidth = 7;
    this.barWidthOffset = 5;

	this.paper = Raphael(id, width, height);

	this.frame = function() {
		this.paper.rect(0, 0, width, height);
	}
 
	this.axis = function () {
        this.paper.rect(0, height/2, width, 1);        
    };


    this.popup = function (bar, text) {
        var paper = this.paper;
        
        var txt;

        bar.hover(
            function () {
                txt = paper.text(bar.attr("x") + 6 , bar.attr("y") , text);  
            },
            function () {
                txt.remove();
            }
        );
    }

    this.neutral_bar = function (index, percent, num_ne) {
        var barHeight = ((percent/100) * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index* this.barWidthOffset), height/2 - barHeight/2, this.barWidth, barHeight);
        bar.attr("fill", "#E78D41");
        bar.attr("stroke-width", "0");
    }

    this.positive_bar = function (index, percent, num_po) {
        var barHeight = ((percent/100) * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index* this.barWidthOffset), height/2 - barHeight, this.barWidth, barHeight);
        bar.attr("fill", "#92d139");
        bar.attr("stroke-width", "0");
        this.popup(bar, num_po + " Tweets");
    }

    this.negative_bar = function (index, percent, num_ng) {
        var barHeight = ((percent/100) * (height/2));
        var bar = this.paper.rect((index * this.barWidth) + (index * this.barWidthOffset), height/2, this.barWidth, barHeight);
        bar.attr("fill", "#FA5858");
        bar.attr("stroke-width", "0");
    }

    this.bar = function(index, neutral, positive, negative, num_ne, num_po, num_ng) {
        this.positive_bar(index, positive, num_po);
        this.negative_bar(index, negative, num_ng);
        this.neutral_bar(index, neutral, num_ne);
    }


}