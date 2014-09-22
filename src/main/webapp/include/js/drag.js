var DragManager = {};

DragManager.DragProxy = null;

DragManager.onMouseOver = function (evt,ele) {
	if(DragManager.DragFlag){
		var dragOver = ele.getAttribute("dragOver");	
		if(dragOver){
			var func = eval(dragOver);
			func.apply(ele,arguments);
		}
	}
};

DragManager.onMouseOut = function (evt,ele) {
	if(DragManager.DragFlag){
		var dragOut = ele.getAttribute("dragOut");	
		if(dragOut){
			var func = eval(dragOut);
			func.apply(ele,arguments);
		}
	}
};

DragManager.onMouseDown = function (evt,ele) {
	var evt = getEvent(evt);
	var dragFlag = $Z(evt.srcElement).getParentByAttr("drag","false");
	if(dragFlag){
		return;	
	}
	DragManager.DragSource = ele;
	DragManager.StartFlag = true;
	if(isIE){
		//焦点丢失
		DragManager.DragSource.attachEvent("onlosecapture",DragManager.onDragExit);
		//设置鼠标捕获
		//DragManager.DragSource.setCapture();
	}else{
		//焦点丢失
		window.attachEvent("onblur",DragManager.onDragExit);
		//阻止默认动作
		evt.preventDefault();
	};
};

DragManager.onMouseMove = function (evt) {
	evt = getEvent(evt);	
	if(DragManager.StartFlag){
		DragManager.DragFlag = true;
		DragManager.StartFlag = false;
		var dragStart = DragManager.DragSource.getAttribute("dragStart");
		if(dragStart){
			var func = eval(dragStart);
			func.apply(DragManager.DragSource,arguments);
		}
	}else	if(DragManager.DragFlag){
		if(DragManager.DragProxy){
			if(!evt.srcElement){
				return;
			}
			//如果是子窗口响应了父窗口发起的事件，则需要绝对定位.todo:尚未考虑多层iframe嵌套的问题
			var pos = null;
			if(window==$ZE.getTopLevelWindow()){
				pos = getEventPosition(evt);
			}else{
				pos = getEventPositionLocal(evt);	
			}
			
			DragManager.DragProxy.style.left = (pos.x-DragManager.DragProxy.cx)+"px";
			if(DragManager.DragProxy==DragManager.DragSource){
				DragManager.DragProxy.style.top = (pos.y-DragManager.DragProxy.cy)+"px";
			}else{
				DragManager.DragProxy.style.top = (pos.y+5)+"px";
			}
		}

		//加入draging
		var draging = DragManager.DragSource.getAttribute("draging");
		if(draging){
			var func = eval(draging);
			func.apply(DragManager.DragSource,arguments);
		}
		
		//end draging
	}else	if(DragManager.ChildDragFlag){//有可能是iframe中发起的拖拽
		if(DragManager.DragProxy){
			var pos = getEventPosition(evt);
			DragManager.DragProxy.style.left = (pos.x-DragManager.DragProxy.cx)+"px";
			if(DragManager.DragProxy==DragManager.DragSource){
				DragManager.DragProxy.style.top = (pos.y-DragManager.DragProxy.cy)+"px";
			}else{
				DragManager.DragProxy.style.top = (pos.y+5)+"px";
			}
		}
	}else{//可能是父级窗口发起的拖动,如对话框
		var pw = $ZE.getTopLevelWindow();
		if(pw.DragManager!=null&&pw.DragManager.DragFlag){
			pw.DragManager.onMouseMove(evt);
		}
	}
};

DragManager.onMouseUp = function (evt,ele) {
	var evt = getEvent(evt);
	var dragFlag = $Z(evt.srcElement).getParentByAttr("drag","false");
	if(dragFlag){
		return;	
	}

	if(DragManager.DragFlag){
		DragManager.onMouseOut.apply(ele,arguments);

		var dragEnd = ele.getAttribute("dragEnd");
		var func = eval(dragEnd);
		func.apply(ele,arguments);		
		
		if(DragManager.DragProxy&&DragManager.DragSource!=DragManager.DragProxy){
			DragManager.DragProxy.style.display = "none";
		}
	}
	Misc.unlockSelect();
	if(isIE&&DragManager.DragSource){
		DragManager.DragSource.detachEvent("onlosecapture", DragManager.onDragExit);
		//DragManager.DragSource.releaseCapture();
	}else{
		window.detachEvent("onblur",DragManager.onDragExit);
	};
	DragManager.DragProxy = null;
	DragManager.DragFlag = false;
	DragManager.StartFlag = false;
	DragManager.DragSource = null;
};

DragManager.onDragExit = function(){
	var pw = $ZE.getTopLevelWindow();
	DragManager.DragFlag = false;
	pw.DragManager.DragFlag = false;
	if(!DragManager.DragSource){
		return;
	}
	var dragExit = DragManager.DragSource.getAttribute("dragExit");
	if(dragExit){
		var func = eval(dragExit);
		func.apply(DragManager.DragSource,arguments);
	}
	var dragOut = DragManager.DragSource.getAttribute("dragOut");
	if(dragOut){
		var func = eval(dragOut);
		func.apply(DragManager.DragSource,arguments);
	}
	if(DragManager.DragProxy&&DragManager.DragSource!=DragManager.DragProxy){
		DragManager.DragProxy.style.display = "none";
	}	
	Misc.unlockSelect();
	if(isIE&&DragManager.DragSource){
		DragManager.DragSource.detachEvent("onlosecapture", DragManager.onDragExit);
		//DragManager.DragSource.releaseCapture();
	}else{
		window.detachEvent("onblur",DragManager.onDragExit);
	};
	DragManager.DragProxy = null;
	DragManager.DragFlag = false;
	DragManager.StartFlag = false;
	DragManager.DragSource = null;
};

DragManager.doDrag = function(evt,proxy){
	var dragProxy;
	var pos1 = $ZE.getPosition(DragManager.DragSource);
	var dim = $ZE.getDimensions(DragManager.DragSource);
	evt = getEvent(evt);
	var pos2 = getEventPositionLocal(evt);

	if(!proxy){
		dragProxy = DragManager.DragSource;
		if(dragProxy.style.position != "absolute"){
			dragProxy.style.width = dim.width+"px";
			dragProxy.style.top = pos1.y+"px";
			dragProxy.style.position = "absolute";
		}
	}else{
		if(typeof(proxy)=="string"){
			//必须去掉Proxy的事件，因为快速移动的过程中鼠标可能会在Proxy身上引发事件
			proxy = proxy.replace(/on\w*?=([\'\"]).*?\1/gi,"");
			proxy = proxy.replace(/drag\w*?=([\'\"]).*?\1/gi,"");
			var div = $Z("_DragProxy");
			if(!div){
				div = document.createElement('div');
				div.id = "_DragProxy";
				div.style.position = "absolute";
				div.style.zIndex = 999;
				Misc.lockSelect(div);
				document.body.appendChild(div) ; 
			}
			div.innerHTML = proxy;
			div.style.display = "";
			div.style.left = (2*pos1.x-pos2.x)+"px";
			div.style.top = (2*pos1.y-pos2.y)+"px";
			dragProxy = div;
		}else{
			dragProxy = proxy;
			dragProxy.style.top = pos1.y+"px";
			dragProxy.style.position = "absolute";
			if(dragProxy!=DragManager.DragSource){
				dragProxy.style.zIndex = 999;
			}
		}
	}
	dragProxy.cx = pos2.x - pos1.x; 
	dragProxy.cy = pos2.y - pos1.y; 
	DragManager.DragProxy = dragProxy;
	Misc.lockSelect();
};

Page.onMouseUp(DragManager.onDragExit);
Page.onMouseMove(DragManager.onMouseMove);
