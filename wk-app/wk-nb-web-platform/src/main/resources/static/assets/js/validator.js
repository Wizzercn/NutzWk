var Validator = {
		init : function(){
			//引入必要的样式+JS文件
			$("body").append("<script type=\"text/javascript\" src=\""+base+"/include/plugins/parsley.min.js\"></script>");
			$("body").append("<script type=\"text/javascript\" src=\""+base+"/include/plugins/parsley.zh_cn.js\"></script>");
		}
};
$(document).ready(function () {
	Validator.init();
});
