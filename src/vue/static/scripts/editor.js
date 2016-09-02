'use strict';

var editableContent = function () {
  return {
    init: function () {
      var editor = new MediumEditor('.editable', {
        buttonLabels: 'fontawesome'
      });
    }
  };
}();

$(function () {
  editableContent.init();
});
