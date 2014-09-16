var Tab = {};


Tab.initFrameHeight = function(id) {
    var f = $("#"+id);
    var ch = document.compatMode == "BackCompat" ? document.body.clientHeight : document.documentElement.clientHeight;
   
    f.height(ch - 118 - (isIE ? 1 : 0));//IE8需要-1
    

};

Tab.initFrameLeftWidth = function(id) {
    var f = $("#"+id);
    if (Application.isHidden) {
    	f.width(0);
        
    } else {
       f.width(192);
    }

};

Tab.initFrameWidth = function(id) {
    var f = $("#"+id);
    var ch = document.compatMode == "BackCompat" ? document.body.clientWidth : document.documentElement.clientWidth;
   

    if (Application.isHidden) {
    	f.width(ch - 28 - (isIE ? 1 : 0)); //IE8需要-1
    } else {
    	f.width(ch - 192 - (isIE ? 1 : 0)); //IE8需要-1
    }


};
 
var Onresize = {};
Onresize.layoutAdjust = function() {
 
    Tab.initFrameHeight("leftFrame"); 

};

$(document).ready(function(){
    Onresize.layoutAdjust();
});

if (document.attachEvent) {
    window.attachEvent('onresize', Onresize.layoutAdjust);
} else {
    window.addEventListener('resize', Onresize.layoutAdjust, false);
}
