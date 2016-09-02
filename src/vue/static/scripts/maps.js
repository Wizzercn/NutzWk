'use strict';

var googlemap = function () {

  var map;

  return {
    init: function () {
      map = new GMaps({
        div: '#map',
        lat: -12.043333,
        lng: -77.028333
      });
      map.addMarker({
        lat: -12.043333,
        lng: -77.03,
        title: 'Lima',
        details: {
          database_id: 42,
          author: 'HPNeo'
        },
        click: function (e) {
          if (console.log) {
            console.log(e);
          }
          alert('You clicked on this marker');
        }
      });
      map.addMarker({
        lat: -12.042,
        lng: -77.028333,
        title: 'Marker with InfoWindow',
        infoWindow: {
          content: '<p>HTML Content</p>'
        }
      });
    }
  };
}();

$(function () {
  googlemap.init();
});
