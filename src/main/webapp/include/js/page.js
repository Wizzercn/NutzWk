Page.bindData = function(id, json, pre ) { 
	//清空ID容器下div块内容
	$('#'+id+' div').each(function(i){
		if (jQuery(this).prop('tagName') == "DIV") {
			jQuery(this).html("");
		} else {
			jQuery(this).val("");
		}
	});
	//给div赋值
	var obj = jQuery.parseJSON(json);
	jQuery.each(obj, function(index, term) {
		var tid = index;
		if (typeof pre != "undefined")
			tid = pre + index;
		if (jQuery('#' + tid).prop('tagName') == "DIV") {
			jQuery('#' + tid).html(term);
		} else {
			jQuery('#' + tid).val(term);
		}
	});
};
//将所有按钮禁用
Page.initBtn=function(){	
   jQuery("a[ztype='zPushBtn']").each(function(){  
       $Z(jQuery(this).attr("id")).disable();
   });
};
// 通过ID取值
Page.getValue = function( id ) { 
	if (jQuery('#' + id).prop('tagName') == "DIV"||jQuery('#' + id).prop('tagName') == "SPAN") {
		return jQuery('#' + id).html();
	} else {
		return jQuery('#' + id).val();
	}

};  
//通过ID赋值
Page.setValue = function( id,val ) { 
	if (jQuery('#' + id).prop('tagName') == "DIV"||jQuery('#' + id).prop('tagName') == "SPAN") {
		jQuery('#' + id).html(val);
	} else {
		jQuery('#' + id).val(val);
	}

};  