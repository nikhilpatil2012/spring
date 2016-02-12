
 // set bar width
  var barWidth = 20;
  
  // set chart height
  var chartHeight = 420;
  
  // our dataset
  
  var data = [4,8,16,32,64];
  
  // set chart dimentions
  
  var chart = d3.select(".chart")
                    .attr("width", chartHeight)
                    .attr("height", chartHeight);
  
  // join dataset values (domain) with range(output)
  
  // Implement the function for transforming values from domain to a range
  var linearFunction = d3.scale.linear().domain([0,d3.max(data, function(d){return d})+1]).range([0,chartHeight]);
  
  // Let's draw the bars
  
  var bar = chart.selectAll("g").data(data).enter().append("g");
  
  // add position for each bar
 
  for(var i = 0; i<data.length; i++){
      
        
    bar.append("rect")
      .attr("width", barWidth)
      .attr("height", chartHeight)
      .attr("fill","purple")
      .attr("transform", "translate(" + i * (barWidth+1) +"," + (chartHeight-linearFunction(data[i])) +")");    
      
  }
 

  
