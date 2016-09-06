'use strict';

var demoCustomForm = function () {
  return {
    init: function () {
      var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));

      // Colored switches
      var blue = document.querySelector('.js-switch-blue');
      var s1 = new Switchery(blue, {
        color: '#17c3e5'
      });

      var pink = document.querySelector('.js-switch-pink');
      var s2 = new Switchery(pink, {
        color: '#ff7791',
        disabled: true
      });

      var green = document.querySelector('.js-switch-green');
      var s3 = new Switchery(green, {
        color: '#2dcb73'
      });

      var red = document.querySelector('.js-switch-red');
      var s4 = new Switchery(red, {
        color: '#FF604F'
      });

      $('.categorized').tagsinput({
        tagClass: function (item) {
          switch (item.continent) {
          case 'Europe':
            return 'label label-primary';
          case 'America':
            return 'label label-danger';
          case 'Australia':
            return 'label label-success';
          case 'Africa':
            return 'label label-color';
          case 'Asia':
            return 'label label-warning';
          }
        },
        itemValue: 'value',
        itemText: 'text'
      });
      $('.categorized').tagsinput('add', {
        'value': 1,
        'text': 'Amsterdam',
        'continent': 'Europe'
      });
      $('.categorized').tagsinput('add', {
        'value': 4,
        'text': 'Washington',
        'continent': 'America'
      });
      $('.categorized').tagsinput('add', {
        'value': 7,
        'text': 'Sydney',
        'continent': 'Australia'
      });
      $('.categorized').tagsinput('add', {
        'value': 10,
        'text': 'Beijing',
        'continent': 'Asia'
      });
      $('.categorized').tagsinput('add', {
        'value': 13,
        'text': 'Cairo',
        'continent': 'Africa'
      });
    }
  };
}();

$(function () {
  demoCustomForm.init();
});
