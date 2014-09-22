var _TipIDCounter = 0;

function Tip(ele,msg){
	this.Element = $Z(ele);
	this.Message = msg;
	this.AutoClose = false;
	this.Clock = 9;
	this.initHtml();
}

Tip.AutoCloseTips = [];

Tip.prototype.initHtml = function(){
	var arr = [];
	arr.push("  <table border='0' cellspacing='0' cellpadding='0' class='tooltiptable'>");
	arr.push("  	<tr><td class='corner topleft'> </td><td class='topcenter'> </td>");
	arr.push("  			<td class='corner topright'> </td></tr><tr><td class='bodyleft'> </td>");
	arr.push("				<td class='tooltipcontent'>"+this.Message+"</td>");
	arr.push("				<td class='bodyright'> </td></tr>");
	arr.push("		<tr><td class='corner footerleft'> </td><td class='footercenter'> </td>");
	arr.push("				<td class='corner footerright'> </td></tr></table>");
	arr.push("		<div class='tooltipfang'></div>");
	this.Html = arr.join('');
};

Tip.prototype.show = function(){
	var div = document.createElement("div");	
	div.id = ""+_TipIDCounter++;
	div.style.position = "absolute";
	div.style.left = "0px";
	div.style.top = "0px";
 	$T("body")[0].appendChild(div);
 	div = $Z(div);
 	div.innerHTML = this.Html;
 	 	
 	var pos = this.Element.getPosition();
	var dim = this.Element.getDimensions();
	if(this.Element.$A("ztype")&&this.Element.$A("ztype").toLowerCase()=="date"){//后边还有图标
		var dim3 = $Z(this.Element.nextSibling).getDimensions();
		dim.width += dim3.width;
	}
	var dim2 = div.getDimensions();
	var mw = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth);
	if(mw-pos.x-dim.width<100){
		this.Clock = 3;
	}
	var c = this.Clock;
	var x,y;
	if(c==2||c==3||c==4){
		x = pos.x-dim.width+18;
	}
	if(c==8||c==9||c==10){
		x = pos.x+dim.width;
	}
	if(c==11||c==12||c==1){
		y = pos.y+dim.height;
	}
	if(c==5||c==6||c==7){
		y = pos.y-dim.height;
	}
	if(c==9){
		y = pos.y-dim2.height/2;
	}
	if(c==3){
		y = pos.y-15;
	}
	div.className = "tooltip callout"+this.Clock;
		
	div.style.left = x+"px";
	div.style.top = y+"px";
	div.style.zIndex = 850;
	$Z(div).show();
	this.Div = div;
	if(this.AutoClose){
		Tip.AutoCloseTips.push(this);
	}
};

Tip.prototype.close = function(){
	if(this.Div){
		this.Div.outerHTML = "";
		this.Div = null;
	}
};

Tip.show = function(ele,msg,autoClose,clock){
	ele = $Z(ele);
	var tip = new Tip(ele,msg);
	tip.AutoClose = autoClose;
	if(clock){
		tip.Clock = clock;
	}
	tip.show();
	if(tip.Div.style.left=="0px"&&tip.Div.style.left=="0px")
		setTimeout(function(){tip.close();tip.show();}, 10);//如果没有得到正确的控件位置延迟10毫秒再试一次。
	if(!tip.AutoClose){
		if(!ele._Tips){
			ele._Tips = [];
		}
		ele._Tips.push(tip);
	}
	return tip;
};

Tip.getTipCount = function(ele){
	ele = $Z(ele);
	if(!ele._Tips){
		return 0;	
	}
	return ele._Tips.length;
};

Tip.close = function(ele){//将与ele有关的Tip都关闭掉
	ele = $Z(ele);
	if(ele._Tips){
		for(var i=0;i<ele._Tips.length;i++){
			if(ele._Tips[i]){
				ele._Tips[i].close();
			}
		}
		ele._Tips = [];
	}
};

Page.onMouseDown(function(){
	var arr = Tip.AutoCloseTips;
	for(var i=arr.length;i>0;i--){
		arr[i-1].close();
		arr.splice(i-1, 1);
	}	
});