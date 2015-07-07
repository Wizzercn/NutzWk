// Demo datatables - blog articles
// ----------------------------------- 


(function(window, document, $, undefined){

  $(function(){

    //
    // Zero configuration
    // 
    if($.fn.dataTable)
      $('#datatable-articles').dataTable({
          'paging':   true,  // Table pagination
          'ordering': true,  // Column ordering 
          'info':     true,  // Bottom left status text
          // Text translation options
          // Note the required keywords between underscores (e.g _MENU_)
          oLanguage: {
              sSearch:      'Search all columns:',
              sLengthMenu:  '_MENU_ records per page',
              info:         'Showing page _PAGE_ of _PAGES_',
              zeroRecords:  'Nothing found - sorry',
              infoEmpty:    'No records available',
              infoFiltered: '(filtered from _MAX_ total records)'
          }
      });


  });

})(window, document, window.jQuery);

