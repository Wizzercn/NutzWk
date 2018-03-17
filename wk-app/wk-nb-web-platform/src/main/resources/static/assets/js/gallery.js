var appGallery=function(){function events(){$(".portfolio-ajax").addClass("loading");$(window).on("load",layoutItems);$(window).on("resize",layoutItems);$(document).on("click touchstart",".toggle-sidebar",function(){layoutItems();});$(".superbox").imagesLoaded().always(function(){$(".superbox").fadeIn();$(".portfolio-ajax").removeClass("loading");}).done(function(){$(".gallery-loader").addClass("hide");$(".superbox").removeClass("hide");layoutItems();});}
function layoutItems(){initLayout();}
function initLayout(){var portfolioWidth=getWidth();$(".superbox").find(".superbox-list").each(function(){$(this).css({width:portfolioWidth+"px"});});}
function getWidth(){var wi=$(".superbox").width(),col=Math.floor(wi/1);if(wi>1024){col=Math.floor(wi/5);}else if(wi>767){col=Math.floor(wi/4);}else if(wi>480){col=Math.floor(wi/2);}else if(wi>320){col=Math.floor(wi/1);}
return col;}
function superbox(){$('.superbox').SuperBox();}
return{init:function(){events();superbox();}};}();$(function(){"use strict";appGallery.init();});