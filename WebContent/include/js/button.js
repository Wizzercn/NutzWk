function initBtn(){
	$("a[ztype='zPushBtn']").each(function(){
     
       $Z($(this).attr("id")).disable();
   });
	
}