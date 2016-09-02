var timer;

// test to ensure DOM options are respected
module('DOM options');
timer = $('<div data-from="1" data-to="3" data-speed="3000" data-refresh-interval="1000"/>');
runTests(timer);

// test to ensure JS options are respected
module('JS options');
timer = $('<div/>');
runTests(timer, {
  from: 1,
  to: 3,
  speed: 3000,
  refreshInterval: 1000
});

// test to ensure JS options override DOM options
module('DOM and JS options');
timer = $('<div data-from="0" data-to="5" data-speed="1000" data-refresh-interval="500"/>');
runTests(timer, {
  from: 1,
  to: 3,
  speed: 3000,
  refreshInterval: 1000
});

function runTests(element, options) {
  element = $(element);
  options = options || {};
  
  asyncTest('onComplete is called when counting completes', 4, function () {
    element.countTo($.extend({}, options, {
      onComplete: function (value) {
        ok(true, 'onComplete was called');
        ok(this == element[0], 'this is the updated dom element');
        ok(value == 3, 'value matches data-to');
        ok(this.innerText == 3, 'innerText matches data-to');
        start();
      }
    }));
  });
  
  asyncTest('onUpdate is called once per refresh', 3, function () {
    element.countTo($.extend({}, options, {
      onUpdate: function () {
        ok(true, 'onUpdate was called');
      },
      onComplete: function () {
        start();
      }
    }));
  });
  
  asyncTest('custom formatter is called', 1, function () {
    element.countTo($.extend({}, options, {
      formatter: function () {
        return 'foobar';
      },
      onComplete: function () {
        ok(this.innerText == 'foobar', 'innerText matches formatted value');
        start();
      }
    }));
  });
}
