function StreamChart(key, selector, container, svg){
  this.key = key
  this.selector = selector
  this.container = container
  this.svg = svg
}
StreamChart.prototype.tweetdata = []
StreamChart.prototype.getTweetData = function() {
    return [
            {
              values: this.tweetdata,
              key: this.key
            }
          ];
      }



StreamChart.prototype.pushTweet = function (tweet) {
	  var opinion = (tweet.opinion.mood == "positive") ? {prob: tweet.opinion.prob, color: 'blue'} : {prob: -tweet.opinion.prob, color: 'red'}
	  this.tweetdata.push({x: twitterStringToDate(tweet.created_at).getTime(), y: opinion.prob, size: tweet.opinion.prob, color: opinion.color});
	  this.redraw()
}
StreamChart.prototype.popTweet = function (tweet) {
	this.tweetdata.shift()
	this.redraw()
}
StreamChart.prototype.setChartViewBox = function(width, height, zoom, chart) {
	var w = width * zoom,
	h = height * zoom;
	chart
	.width(w)
	.height(h);
	d3.select(this.selector)
	.attr('viewBox', '0 0 ' + w + ' ' + h)
	.transition().duration(500)
	.call(chart);
 }

StreamChart.prototype.resizeChart = function(chart) {
	var container = d3.select(this.container);
	var svg = container.select(this.svg);
		// resize based on container's width
		var aspect = chart.width() / chart.height();
		var targetWidth = parseInt(container.style('width'));
		svg.attr("width", targetWidth);
		svg.attr("height", Math.round(targetWidth / aspect));
	
};

StreamChart.prototype.redraw = function() {
	var self = this
	 nv.addGraph(function() {
		    var chart = nv.models.scatterChart();
		    var width = 500;
		    var height = 155;
		    var zoom = 3;

		    chart.xAxis
		        .axisLabel('Date')
		        .tickFormat(function(d) { return d3.time.format.utc("%X")(new Date(d)); });

		    chart.yAxis
		        .axisLabel('Opinion (v)')
		        .tickFormat(d3.format(',.1f'));
            d3.select(self.selector)
		        .attr('perserveAspectRatio', 'xMinYMid')
		        .attr('width', width)
		        .attr('height', height)
		        .datum(self.getTweetData());

		    
		    self.setChartViewBox(width, height, zoom, chart);
		    self.resizeChart(chart);

		    nv.utils.windowResize(self.resizeChart(chart));

		    return chart;
		  });
		}
