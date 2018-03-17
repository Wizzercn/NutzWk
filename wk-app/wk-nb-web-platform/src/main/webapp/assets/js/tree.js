var _Tree = {
    init : function(){
        //引入必要的样式+JS文件
        $("head").append("<link rel=\"stylesheet\" href=\""+base+"/include/plugins/jstree/themes/default/style.min.css\">");
        $("body").append("<script type=\"text/javascript\" src=\""+base+"/include/plugins/jstree/jstree.min.js\"></script>");
    }
}
$(document).ready(function () {
    _Tree.init();
});