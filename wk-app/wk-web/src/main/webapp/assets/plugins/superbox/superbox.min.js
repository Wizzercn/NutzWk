/*
	SuperBox v1.0.0
	by Todd Motto: http://www.toddmotto.com
	Latest version: https://github.com/toddmotto/superbox
	
	Copyright 2013 Todd Motto
	Licensed under the MIT license
	http://www.opensource.org/licenses/mit-license.php

	SuperBox, the lightbox reimagined. Fully responsive HTML5 image galleries.
*/
;(function(e){e.fn.SuperBox=function(t){var n=e('<div class="superbox-show"></div>');var r=e('<img src="" class="superbox-current-img">');var i=e('<div class="superbox-close"></div>');n.append(r).append(i);return this.each(function(){e(".superbox-list").click(function(){var t=e(this).find(".superbox-img");var i=t.data("img");r.attr("src",i);if(e(".superbox-current-img").css("opacity")==0){e(".superbox-current-img").animate({opacity:1})}if(e(this).next().hasClass("superbox-show")){n.toggle()}else{n.insertAfter(this).css("display","block")}e("html, body").animate({scrollTop:n.position().top-t.width()},"medium")});e(".superbox").on("click",".superbox-close",function(){e(".superbox-current-img").animate({opacity:0},200,function(){e(".superbox-show").slideUp()})})})}})(jQuery);