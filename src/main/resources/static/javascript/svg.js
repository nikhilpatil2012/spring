var width = 420;
var height = 20;

var data = [4,8,10,14,16,18];

var chart = d3.select(".chart").attr("width", width); 
chart.attr("height", data.length * 31);

// range and domain is set
var x = d3.scale.linear().domain([0, d3.max(data, function(d){return d})+1]).range([0,width]);


var bar = chart.selectAll("g").data(data).enter().append("g");

bar.attr("transform", function(data, i){
    
    return "translate(0,"+ i * (height+1)  + ")";
    
});

bar.append("rect")
      .attr("width", function(data) { return x(data); })
      .attr("height", height);
      
bar.append("text")
        .attr("x", function(data){ return x(data) + 3})
        .attr("y", height / 2)
        .attr("dy", ".35em")
        .text(function(data){ return data;});
