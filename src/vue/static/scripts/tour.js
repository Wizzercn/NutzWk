'use strict';

var tour = function () {
  return {
    init: function () {
      //initialize instance
      var enjoyhint_instance = new EnjoyHint({});

      //simple config.
      //Only one step - highlighting(with description) "New" button
      //hide EnjoyHint after a click on the button.
      var enjoyhint_script_steps = [
        {
          selector: '.off-right', //jquery selector
          event: 'click',
          description: 'This is your user menu. Click to show options'
            },
        {
          selector: '#options', //jquery selector
          event_type: 'next',
          description: 'Your sub menu pops up here. Click next to continue'
            },
        {
          selector: '#main-nav', //jquery selector
          event_type: 'next',
          description: 'This is your main sidebar navigation. Click next',
          top: -500
            },
        {
          selector: '#secondary-nav', //jquery selector
          event_type: 'next',
          description: 'and finally this is your secondary navigation.'
            },
        {
          selector: '.navbar-brand', //jquery selector
          event_type: 'next',
          description: 'Want to see the tour again? Just reload your browser.'
            }];

      //set script config
      enjoyhint_instance.setScript(enjoyhint_script_steps);

      if ($(window).width() > 767) {
        //run Enjoyhint script
        enjoyhint_instance.runScript();
      } else {
        $('.wrapper').text('View tour no higher viewport');
      }
    }
  };
}();

$(function () {
  tour.init();
});
