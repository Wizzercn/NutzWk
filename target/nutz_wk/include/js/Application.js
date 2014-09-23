var Application = {};
 
Application.isHMenu = false;
Application.isHidden = false; 

function showWindow(id,title,width,height,url){
	 var d = new Dialog(id);
        d.Width = width;
        d.Height = height;
        d.Title = title;

        d.URL = url;
        d.OKEvent = function() {
            if ($DW.Verify.hasError()) {
                return;
            }
           if($DW.dosave()!=false){
	           			dosave();
	           		}
        };
        d.onLoad = function() {
            $DW.$("oldpassword").focus();
        };
        d.show();
	
}
