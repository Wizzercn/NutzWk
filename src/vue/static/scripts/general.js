'use strict';

var general = function () {
  return {
    init: function () {
      $('[data-toggle=tooltip]').tooltip();

      $('[data-toggle=popover]')
        .popover()
        .click(function (e) {
          e.preventDefault();
        });
    }
  };
}();

$(function () {
  general.init();
});
