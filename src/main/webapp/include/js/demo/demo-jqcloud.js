// JQCloud
// ----------------------------------- 


(function(window, document, $, undefined){

  $(function(){

    //Create an array of word objects, each representing a word in the cloud
    var word_array = [
      {
        text: 'Lorem',
        weight: 13
        //link: 'http://themicon.co'
      }, {
        text: 'Ipsum',
        weight: 10.5
      }, {
        text: 'Dolor',
        weight: 9.4
      }, {
        text: 'Sit',
        weight: 8
      }, {
        text: 'Amet',
        weight: 6.2
      }, {
        text: 'Consectetur',
        weight: 5
      }, {
        text: 'Adipiscing',
        weight: 5
      }, {
        text: 'Sit',
        weight: 8
      }, {
        text: 'Amet',
        weight: 6.2
      }, {
        text: 'Consectetur',
        weight: 5
      }, {
        text: 'Adipiscing',
        weight: 5
      }
    ];


    $("#jqcloud").jQCloud(word_array, {
      width: 240,
      height: 200,
      steps: 7
    });

  });

})(window, document, window.jQuery);