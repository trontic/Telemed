/**
 * ---------------------------------------
 * This demo was created using amCharts 5.
 *
 * For more information visit:
 * https://www.amcharts.com/
 *
 * Documentation is available at:
 * https://www.amcharts.com/docs/v5/
 * ---------------------------------------
 */

// Create root element
// https://www.amcharts.com/docs/v5/getting-started/#Root_element
var root = am5.Root.new("chartdiv");

const myTheme = am5.Theme.new(root);

// Move minor label a bit down
myTheme.rule("AxisLabel", ["minor"]).setAll({
  dy: 1
});

// Tweak minor grid opacity
myTheme.rule("Grid", ["minor"]).setAll({
  strokeOpacity: 0.08
});

// Set themes
// https://www.amcharts.com/docs/v5/concepts/themes/
root.setThemes([
  am5themes_Animated.new(root),
  myTheme
]);


// Create chart
// https://www.amcharts.com/docs/v5/charts/xy-chart/
var chart = root.container.children.push(am5xy.XYChart.new(root, {
  panX: false,
  panY: false,
  wheelX: "panX",
  wheelY: "zoomX",
  paddingLeft: 0
}));


// Add cursor
// https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
  behavior: "zoomX"
}));
cursor.lineY.set("visible", false);

// Create axes
// https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
var xAxis = chart.xAxes.push(am5xy.DateAxis.new(root, {
  maxDeviation: 0,
  baseInterval: {
    timeUnit: "day",
    count: 1
  },
  renderer: am5xy.AxisRendererX.new(root, {
    minorGridEnabled: true,
    minGridDistance: 200,
    minorLabelsEnabled: true
  }),
  tooltip: am5.Tooltip.new(root, {})
}));

xAxis.set("minorDateFormats", {
  day: "dd",
  month: "MM"
});

var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
  renderer: am5xy.AxisRendererY.new(root, {})
}));


// Add series
// https://www.amcharts.com/docs/v5/charts/xy-chart/series/
var series = chart.series.push(am5xy.LineSeries.new(root, {
  name: "Series",
  xAxis: xAxis,
  yAxis: yAxis,
  valueYField: "value",
  valueXField: "date",
  tooltip: am5.Tooltip.new(root, {
    labelText: "{valueY}"
  })
}));

// Actual bullet
series.bullets.push(function () {
  var bulletCircle = am5.Circle.new(root, {
    radius: 5,
    fill: series.get("fill")
  });
  return am5.Bullet.new(root, {
    sprite: bulletCircle
  })
})

// Add scrollbar
// https://www.amcharts.com/docs/v5/charts/xy-chart/scrollbars/
chart.set("scrollbarX", am5.Scrollbar.new(root, {
  orientation: "horizontal"
}));

var data = [
             { date: '2024-02-01', value: 120 },
             { date: '2024-02-02', value: 130 },
             { date: '2024-02-03', value: 110 },
             { date: '2024-02-04', value: 125 },
             { date: '2024-02-05', value: 115 },
             { date: '2024-02-06', value: 105 },
             { date: '2024-02-07', value: 135 },
             { date: '2024-02-08', value: 125 },
             { date: '2024-02-09', value: 140 },
             { date: '2024-02-10', value: 130 },
             { date: '2024-02-11', value: 115 },
             { date: '2024-02-12', value: 125 },
             { date: '2024-02-13', value: 110 },
             { date: '2024-02-14', value: 120 },
             { date: '2024-02-15', value: 130 },
             { date: '2024-02-16', value: 140 },
             { date: '2024-02-17', value: 120 },
             { date: '2024-02-18', value: 110 },
             { date: '2024-02-19', value: 130 },
             { date: '2024-02-20', value: 125 },
             { date: '2024-02-21', value: 115 },
             { date: '2024-02-22', value: 135 },
             { date: '2024-02-23', value: 125 },
             { date: '2024-02-24', value: 130 },
             { date: '2024-02-25', value: 120 },
             { date: '2024-02-26', value: 110 },
             { date: '2024-02-27', value: 125 },
             { date: '2024-02-28', value: 135 },
             { date: '2024-02-29', value: 120 },
             { date: '2024-03-01', value: 130 }
           ];

data.forEach(item => {
  item.date = new Date(item.date + 'T00:00:00Z');
});

series.data.setAll(data);


// Make stuff animate on load
// https://www.amcharts.com/docs/v5/concepts/animations/
series.appear(1000);
chart.appear(1000, 100);