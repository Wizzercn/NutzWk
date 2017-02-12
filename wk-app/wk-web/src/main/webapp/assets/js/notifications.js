var demoNotifications=function(){var i=-1,toastCount=0,toastlast,$showDuration=$('#showDuration'),$hideDuration=$('#hideDuration'),$timeOut=$('#timeOut'),$extendedTimeOut=$('#extendedTimeOut'),$showEasing=$('#showEasing'),$hideEasing=$('#hideEasing'),$showMethod=$('#showMethod'),$hideMethod=$('#hideMethod'),msgs=['My name is Inigo Montoya. You killed my father. Prepare to die!','<div><input class="form-control no-b" value="textbox"/>&nbsp;<a href="http://johnpapa.net" target="_blank">This is a hyperlink</a></div><div><button type="button" id="okBtn" class="btn btn-primary">Close me</button><button type="button" id="surpriseBtn" class="btn" style="margin: 0 8px 0 8px">Surprise me</button></div>','Are you the six fingered man?','Inconceivable!','I do not think that means what you think it means.','Have fun storming the castle!'];function events(){$(document).on("click","#clearlasttoast",function(){toastr.clear(getLastToast());});$(document).on("click","#cleartoasts",function(){toastr.clear();});$(document).on("click","#showtoast",showToast);}
function showToast(){var shortCutFunction=$("#toastTypeGroup input:radio:checked").val(),msg=$('#message').val(),title=$('#title').val()||'',toastIndex=toastCount++;toastr.options={closeButton:$('#closeButton').prop('checked'),debug:$('#debugInfo').prop('checked'),positionClass:$('#positionGroup input:radio:checked').val()||'toast-top-right',onclick:null};if($('#addBehaviorOnToastClick').prop('checked')){toastr.options.onclick=function(){alert('You can perform some custom action after a toast goes away');};}
if($showDuration.val().length){toastr.options.showDuration=$showDuration.val();}
if($hideDuration.val().length){toastr.options.hideDuration=$hideDuration.val();}
if($timeOut.val().length){toastr.options.timeOut=$timeOut.val();}
if($extendedTimeOut.val().length){toastr.options.extendedTimeOut=$extendedTimeOut.val();}
if($showEasing.val().length){toastr.options.showEasing=$showEasing.val();}
if($hideEasing.val().length){toastr.options.hideEasing=$hideEasing.val();}
if($showMethod.val().length){toastr.options.showMethod=$showMethod.val();}
if($hideMethod.val().length){toastr.options.hideMethod=$hideMethod.val();}
if(!msg){msg=getMessage();}
$("#toastrOptions").text("Command: toastr["+ shortCutFunction+"](\""+ msg+(title?"\", \""+ title:'')+"\")\n\ntoastr.options = "+ JSON.stringify(toastr.options,null,2));var $toast=toastr[shortCutFunction](msg,title);toastlast=$toast;if($toast.find('#okBtn').length){$toast.delegate('#okBtn','click',function(){alert('you clicked me. i was toast #'+ toastIndex+'. goodbye!');$toast.remove();});}
if($toast.find('#surpriseBtn').length){$toast.delegate('#surpriseBtn','click',function(){alert('Surprise! you clicked me. i was toast #'+ toastIndex+'. You could perform an action here.');});}}
function getMessage(){i++;if(i===msgs.length){i=0;}
return msgs[i];}
function getLastToast(){return toastlast;}
return{init:function(){events();}};}();$(function(){"use strict";demoNotifications.init();});