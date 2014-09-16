function openOne(s1,s2,s3){
    $.prettyPhoto.open(s1,s2,s3);
    $(".pp_overlay").css("z-index",$(".pp_overlay").css("z-index")+100);
    $(".pp_pic_holder").css("z-index",$(".pp_pic_holder").css("z-index")+110);
}
function initOpen(){
    $("a[rel^='prettyOne']").prettyPhoto({
        social_tools:'',
        animation_speed:'fast',
        slideshow:2000,
        autoplay_slideshow: false,
        changepicturecallback:function(arguments){
        },
        callback:function () {
            //alert('window is closed!')// invoked after closing this popup window
        }
    });
}