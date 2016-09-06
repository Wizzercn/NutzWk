'use strict';

var demoAnimated = function () {
  function testAnim(x) {
    $('.service > div').removeClass().addClass(x + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
      $(this).removeClass();
    });
  }
  return {
    init: function () {
      $('.chosen').on('change', function () {
        var anim = $(this).val();
        testAnim(anim);
      });
    }
  };
}();

$(function () {
  demoAnimated.init();
});
