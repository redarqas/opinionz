var tweetdata = []
function getTweetData() {
    return [
      {
        values: tweetdata,
        key: "Sine Wave"
      }
    ];
}
function twitterStringToDate (s) {
    var b = s.split(/[: ]/g);
    var m = {jan:0, feb:1, mar:2, apr:3, may:4, jun:5, jul:6, aug:7, sep:8, oct:9, nov:10, dec:11};
    return new Date(Date.UTC(b[7], m[b[1].toLowerCase()], b[2], b[3], b[4], b[5]));
}
function pushTweet(tweet) {
  var opinion = (tweet.opinion.mood == "positive") ? {prob: tweet.opinion.prob, color: 'blue'} : {prob: -tweet.opinion.prob, color: 'red'}
  tweetdata.push({x: twitterStringToDate(tweet.created_at).getTime(), y: opinion.prob, size: tweet.opinion.prob, color: opinion.color});
  redraw()
}

function redraw() {
 nv.addGraph(function() {
    var chart = nv.models.scatterChart();
    var fitScreen = false;
    var width = 500;
    var height = 100;
    var zoom = 3;

    chart.xAxis
        .axisLabel('Date')
        .tickFormat(function(d) { return d3.time.format.utc("%X")(new Date(d)); });

    chart.yAxis
        .axisLabel('Opinion (v)')
        .tickFormat(d3.format(',.1f'));

    d3.select('#chart1 svg')
        .attr('perserveAspectRatio', 'xMinYMid')
        .attr('width', width)
        .attr('height', height)
        .datum(getTweetData());

    setChartViewBox();
    resizeChart();

    nv.utils.windowResize(resizeChart);

    function setChartViewBox() {
      var w = width * zoom,
          h = height * zoom;
      chart
          .width(w)
          .height(h);
      d3.select('#chart1 svg')
          .attr('viewBox', '0 0 ' + w + ' ' + h)
        .transition().duration(500)
          .call(chart);
    }

    function resizeChart() {
      var container = d3.select('#chart1');
      var svg = container.select('svg');

      if (fitScreen) {
        // resize based on container's width AND HEIGHT
        var windowSize = nv.utils.windowSize();
        svg.attr("width", windowSize.width);
        svg.attr("height", windowSize.height);
      } else {
        // resize based on container's width
        var aspect = chart.width() / chart.height();
        var targetWidth = parseInt(container.style('width'));
        svg.attr("width", targetWidth);
        svg.attr("height", Math.round(targetWidth / aspect));
      }
    };
    return chart;
  });
}
