// Demo Panels
// ----------------------------------- 


(function(window, document, $, undefined){

  $(function(){

    /**
     * This functions show a demonstration of how to use
     * the panel tools system via custom event. 
     */

    $('.panel.panel-demo')
      .on('panel.refresh', function(e, panel){

        // perform any action when a .panel triggers a the refresh event
        setTimeout(function(){

          // when the action is done, just remove the spinner class
          panel.removeSpinner();
      
        }, 3000);

      })
      .on('hide.bs.collapse', function(event){

        console.log('Panel Collapse Hide');

      })
      .on('show.bs.collapse', function(event){

        console.log('Panel Collapse Show');

      })
      .on('panel.remove', function(event, panel, deferred){
        console.log('Removing panel');
        // Call resolve() to continue removing the panel
        // perform checks to avoid removing panel if some user action is required
        deferred.resolve();
      })
      .on('panel.removed', function(event, panel){

        console.log('Removed panel');

      })
      ;

  });


})(window, document, window.jQuery);
  