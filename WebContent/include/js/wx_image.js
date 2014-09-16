/**
 * Created by Wizzer on 14-4-12.
 */
function initPrettyPhoto() {
    $("iframe").contents().find("a[rel^='prettyPhoto']").prettyPhoto({
        social_tools:'',
        animation_speed:'fast',
        slideshow:2000,
        autoplay_slideshow: false,
        changepicturecallback:function(arguments){
            var _src = $("#fullResImage").attr('src');
            var _img=findImage();
            if(_img){
                var id=_img.data('id');

                $(".pp_social").html("<a href='javascript:selTvOne("+id+")'>选入节目</a>");
                getRow(id,function(res){
                    var htmlstr="";
                    if(res==0){
                        htmlstr="<input id='status0' name='status' type='radio' value='0' checked>显示 <input id='status1' name='status' type='radio' value='1' >隐藏";
                    }else if(res==1){
                        htmlstr="<input id='status0' name='status' type='radio' value='0'>显示 <input id='status1' name='status' type='radio' value='1' checked>隐藏";
                    }
                    $(".pp_social").html("&nbsp;&nbsp;"+htmlstr+"&nbsp;&nbsp;<input id='selInTv' type='button' class='inputButton' value='选入节目'/> ");
                    $("#status0").bind("click",function(){changeStatus(id,0);});
                    $("#status1").bind("click",function(){changeStatus(id,1);});
                    $("#selInTv").bind("click",function(){selTvOne(id);});
                });
            }
        },
        callback:function () {
            //alert('window is closed!')// invoked after closing this popup window
        }
    });
    function changeStatus(id,status){
        top.Page.MinMessage.Show({
            text: '处理中',
            type: 'load',
            timeout:500
        });
        jQuery.ajax({
            url : CONTEXTPATH+"/private/wx/image/changeStatus?id="+id+"&status="+status,
            type: 'POST',
            success : function (res) {
                $("#mainFramee000200030002")[0].contentWindow["loadData"]();

                return false;
            }
        });
    }
    function getRow(id,callback){
        jQuery.ajax({
            url : CONTEXTPATH+"/private/wx/image/getRow?id="+id,
            type: 'POST',
            success : function (res) {
                callback(res);
                return false;
            },
            fail : function(res) {
                Dialog.alert("系统错误?!");
            }
        });
    }
    function findImage(){
        var _src = $("#fullResImage").attr('src');
        var ret = false;
        $("iframe").contents().find("#js_data_list_inner").find("img").each(function(){
            if($(this).attr('src') == _src){
                ret = $(this);
                return false;
            }
        });
        return ret;
    }
    function selTvOne(id){
        var d = new Dialog("toTv");
        d.Width = 650;
        d.Height = 450;
        d.Title = "选入节目";
        d.URL = CONTEXTPATH+"/private/wx/image/totv?ids="+id;
        d.onLoad = function(){
            //$DW.$Z("name").focus();
        };
        d.OKEvent = function(){
            if($DW.Verify.hasError()){
                return;
            }
            tvSave();
        };
        d.show();
        d.OKButton.value="保存选择";
        d.addButton("tvPub","发布节目",function(){tvPub(0);});
        d.addButton("tvDown","撤回节目",function(){tvPub(1);});
    }
    function tvPub(status){
        var tvid=$DW.$("#tvid").val();
        top.Page.MinMessage.Show({
            text: '处理中',
            type: 'load'
        });
        jQuery.ajax({
            url : CONTEXTPATH+"/private/tv/show/changeStatus?id="+tvid+"&status="+status,
            type: 'POST',
            success : function (res) {
                top.Page.MinMessage.Hide();
                if(res=="true"){
                    Dialog.alert("操作成功");
                    parent.DialogRefresh("toTv");
                    $("#mainFramee000200030002")[0].contentWindow["loadData"]();
                }else{
                    Dialog.alert("操作失败");
                }
                return false;
            },
            fail : function(res) {
                Dialog.alert("系统错误?!");
            }
        });
    }
    function tvSave(){
        var ids=$DW.$("#selids").val().split(",");
        var tvid=$DW.$("#tvid").val();
        var num=$DW.sel3num;
        if(num<1){
            Dialog.alert("未选择照片");
            return;
        }
        top.Page.MinMessage.Show({
            text: '处理中',
            type: 'load'
        });
        jQuery.ajax({
            url : CONTEXTPATH+"/private/wx/image/sel?tvid="+tvid+"&ids="+ids,
            type: 'POST',
            success : function (res) {
                top.Page.MinMessage.Hide();
                if(res=="true"){
                    Dialog.alert("操作成功");
                    parent.DialogRefresh("toTv");
                    $("#mainFramee000200030002")[0].contentWindow["loadData"]();
                }else{
                    Dialog.alert("操作失败");
                }
                return false;
            },
            fail : function(res) {
                Dialog.alert("系统错误?!");
            }
        });
    }
}