'use strict';

var dashboard = function () {

  var sparkData = [4, 6, 7, 1, 4, 5, 7, 9, 6, 5, 3, 7, 1, 2, 8, 7, 3, 8, 9, 2, 1, 7, 4, 9, 1, 7],
    data = [],
    totalPoints = 100,
    updateInterval = 800,

    plot;

  function events() {
    $(window).on('resize', initSparkline);
  }

  function getRandomData() {

    if (data.length > 0) {
      data = data.slice(1);
    }

    // Do a random walk

    while (data.length < totalPoints) {

      var prev = data.length > 0 ? data[data.length - 1] : 50,
        y = prev + Math.random() * 10 - 5;

      if (y < 0) {
        y = 0;
      } else if (y > 100) {
        y = 100;
      }

      data.push(y);
    }

    // Zip the generated y values with the x values

    var res = [];
    for (var i = 0; i < data.length; ++i) {
      res.push([i, data[i]]);
    }

    return res;
  }

  function update() {

    plot.setData([getRandomData()]);

    // Since the axes don't change, we don't need to call plot.setupGrid()

    plot.draw();
    setTimeout(update, updateInterval);
  }

  function initMap() {
    $('.map').vectorMap({
      map: 'world_mill_en',
      scaleColors: ['#C8EEFF', '#0071A4'],
      normalizeFunction: 'polynomial',
      hoverOpacity: 0.7,
      hoverColor: false,
      zoomOnScroll: false,
      regionStyle: {
        initial: {
          fill: '#9f9f9f',
          'fill-opacity': 0.9,
          stroke: '#fff',
        },
        hover: {
          'fill-opacity': 0.7
        },
        selected: {
          fill: '#1A94E0'
        }
      },
      markerStyle: {
        initial: {
          fill: '#e04a1a',
          stroke: '#FF604F',
          'fill-opacity': 0.5,
          'stroke-width': 1,
          'stroke-opacity': 0.4,
        },
        hover: {
          stroke: '#C54638',
          'stroke-width': 2
        },
        selected: {
          fill: '#C54638'
        },
      },
      backgroundColor: '#f1f4f9',
      markers: [
        {
          latLng: [41.90, 12.45],
          name: 'Vatican City'
                },
        {
          latLng: [43.73, 7.41],
          name: 'Monaco'
                },
        {
          latLng: [-0.52, 166.93],
          name: 'Nauru'
                },
        {
          latLng: [-8.51, 179.21],
          name: 'Tuvalu'
                },
        {
          latLng: [43.93, 12.46],
          name: 'San Marino'
                },
        {
          latLng: [47.14, 9.52],
          name: 'Liechtenstein'
                },
        {
          latLng: [7.11, 171.06],
          name: 'Marshall Islands'
                },
        {
          latLng: [17.3, -62.73],
          name: 'Saint Kitts and Nevis'
                },
        {
          latLng: [3.2, 73.22],
          name: 'Maldives'
                },
        {
          latLng: [35.88, 14.5],
          name: 'Malta'
                },
        {
          latLng: [12.05, -61.75],
          name: 'Grenada'
                },
        {
          latLng: [13.16, -61.23],
          name: 'Saint Vincent and the Grenadines'
                },
        {
          latLng: [13.16, -59.55],
          name: 'Barbados'
                },
        {
          latLng: [17.11, -61.85],
          name: 'Antigua and Barbuda'
                },
        {
          latLng: [-4.61, 55.45],
          name: 'Seychelles'
                },
        {
          latLng: [7.35, 134.46],
          name: 'Palau'
                },
        {
          latLng: [42.5, 1.51],
          name: 'Andorra'
                },
        {
          latLng: [14.01, -60.98],
          name: 'Saint Lucia'
                },
        {
          latLng: [6.91, 158.18],
          name: 'Federated States of Micronesia'
                },
        {
          latLng: [1.3, 103.8],
          name: 'Singapore'
                },
        {
          latLng: [1.46, 173.03],
          name: 'Kiribati'
                },
        {
          latLng: [-21.13, -175.2],
          name: 'Tonga'
                },
        {
          latLng: [15.3, -61.38],
          name: 'Dominica'
                },
        {
          latLng: [-20.2, 57.5],
          name: 'Mauritius'
                },
        {
          latLng: [26.02, 50.55],
          name: 'Bahrain'
                },
        {
          latLng: [0.33, 6.73],
          name: 'São Tomé and Príncipe'
                },
            ]
    });
  }

  function initSparkline() {
    $('.dash-line').sparkline(sparkData, {
      type: 'line',
      width: '100%',
      height: '40',
      lineWidth: 1,
      lineColor: '#fff',
      spotColor: '#f1f4f9',
      fillColor: '',
      spotRadius: '2',
    });
  }

  function initFlot() {
    plot = $.plot('.tile-line', [getRandomData()], {
      colors: ['#fff'],
      series: {
        shadowSize: 0,
        lines: {
          show: true,
          lineWidth: 0.5,
        }
      },
      grid: {
        borderWidth: 0
      },
      yaxis: {
        min: 0,
        show: false
      },
      xaxis: {
        show: false
      }
    });
  }

  return {
    init: function () {
      events();
      initMap();
      initSparkline();
      initFlot();
      update();
    }
  };
}();

$(function () {
  dashboard.init();
});
