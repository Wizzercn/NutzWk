'use strict';

var demoSortableLists = function () {

  function events() {
    $(document).on('click', '#nestable-menu', function (e) {
      var target = $(e.target),
        action = target.data('action');
      if (action === 'expand-all') {
        $('.dd').nestable('expandAll');
      }
      if (action === 'collapse-all') {
        $('.dd').nestable('collapseAll');
      }
    });
  }

  function updateOutput(e) {
    var list = e.length ? e : $(e.target),
      output = list.data('output');
    if (window.JSON) {
      output.text(window.JSON.stringify(list.nestable('serialize')));
    } else {
      output.text('JSON browser support required for this demo.');
    }
  }

  function plugins() {
    // activate Nestable for list 1
    $('#nestable').nestable({
      group: 1
    }).on('change', updateOutput);

    // activate Nestable for list 2
    $('#nestable2').nestable({
      group: 1
    }).on('change', updateOutput);

    // output initial serialised data
    updateOutput($('#nestable').data('output', $('#nestable-output')));

    updateOutput($('#nestable2').data('output', $('#nestable2-output')));

    $('#nestable3').nestable();
  }

  return {
    init: function () {
      events();
      plugins();
    }
  };
}();

$(function () {
  demoSortableLists.init();
});
