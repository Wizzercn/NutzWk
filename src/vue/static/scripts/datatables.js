'use strict';

var demoDataTables = function () {
  return {
    init: function () {
      $('.datatable').dataTable({
        'ajax': 'data/datatables-arrays.txt',
        'sPaginationType': 'bootstrap'
      });

      $('.chosen').chosen({
        width: '80px'
      });
    }
  };
}();

$(function () {
  demoDataTables.init();
});
