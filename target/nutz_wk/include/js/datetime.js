$ZE.computePositionEx = function(ele1,ele2){
	var pos = $ZE.getPositionEx(ele1);
	var dim = $ZE.getDimensions(ele1);
	var dim2 = $ZE.getDimensions(ele2);
	return $ZE.computePosition(pos.x+dim.width,pos.y,pos.x+dim.width,pos.y+dim.height,"all",dim2.width,dim2.height,$ZE.getTopLevelWindow());
}

var Calendar = {
	monthNames : ["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
	weekNames : ["日","一","二","三","四","五","六"]
}

var TimeSelector = {};

var DateTime = {};
DateTime.getCurrentDate = function(){
	return DateTime.toString(new Date());
}

DateTime.getCurrentTime = function(){
	var date = new Date();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	var arr = [];
	arr.push(h>9?h:"0"+h);
	arr.push(m>9?m:"0"+m);
	arr.push(s>9?s:"0"+s);
	return arr.join(":");
}

DateTime.toString = function(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var arr = [];
	arr.push(y);
	arr.push(m>9?m:"0"+m);
	arr.push(d>9?d:"0"+d);
	return arr.join("-");
}

DateTime.parseDate = function(str){//解析形如yyyy-MM-dd hh:mm:ss的日期字符串为Date对象
	var regex = /^(\d{4})-(\d{1,2})-(\d{1,2})(\s(\d{1,2}):(\d{1,2})(:(\d{1,2}))?)?$/;
	if(!regex.test(str)){
		//alert("DateTime.parseDate:错误的日期"+str);
	}
	regex.exec(str);
	var y = RegExp.$1;
	var M = RegExp.$2;
	var d = RegExp.$3;
	var h = RegExp.$5;
	var m = RegExp.$6;
	var s = RegExp.$8;
	var date = new Date();
	date.setYear(y);
	date.setMonth(M-1);
	date.setDate(d);
	if(!h){
		h = 0;
		m = 0;
	}		
	date.setHours(h);
	date.setMinutes(m);
	if(!s){
		s=0;
	}
	date.setSeconds(s);
	return date;	
}

DateTime.LastID = new Date().getTime();
DateTime.initCtrl = function(ele){
	ele = $Z(ele);
	var ztype = ele.$A("ztype");
	if(ztype){
		var str;
		var date = new Date();
		if(ztype.toLowerCase()=="date"){
			str =  "Calendar";
		}else if(ztype.toLowerCase()=="time"){
			str =  "TimeSelector";
		}else{
			return;	
		}
		if(ele.$A("init")=="true"){
			if(ztype.toLowerCase()=="date"){
				ele.value = DateTime.getCurrentDate();	
			}else{
				ele.value = DateTime.getCurrentTime();	
			}
		}
    var id = ele.id;
    if(id==null||id==""){
    	ele.id = id = DateTime.LastID++;
    }
    ele.insertAdjacentHTML("afterEnd","<img src='"+Server.ContextPath+"Framework/Images/"+str+".gif' align='absmiddle' vspace='1' style='position:relative; left:-20px; margin-right:-20px; cursor:pointer;' onmousedown=\"DateTime.onImageMouseDown(event,'"+str+"','"+id+"');\">");
    ele.attachEvent("onfocus",function(){
    	eval(str+".show('"+id+"')");
    });
    ele.onmousedown = DateTime.onMouseDown;
	}
}

DateTime.onImageMouseDown = function(evt,str,id){
	Calendar.close();
	TimeSelector.close();
	var pw = $ZE.getTopLevelWindow();
	if(pw.DateTime&&id==pw.DateTime.showingID){
		return;
	}
	eval(str+".show(id)");
	stopEvent(evt);
}

DateTime.onMouseDown = function(evt){
	var pw = $ZE.getTopLevelWindow();
	if(this.id==pw.DateTime.showingID&&pw.SourceWindow==window){
		cancelEvent(evt);
	}
}

Page.onMouseDown(function(){
		Calendar.close();
		TimeSelector.close();
});

function _LeftPad(str,c,count){
	str = ""+str;
	return str.leftPad(c,count);
}

TimeSelector.setTime = function(time){
	var h,m,s;
	if(time){
		if(!/\d{1,2}\:\d{1,2}(\:\d{1,2})?/.test(time)){
			Dialog.alert("错误的时间:"+time);
		}
		var arr = time.split(":");
		h = parseInt(arr[0]);
		m = parseInt(arr[1]);
		s = parseInt(arr[2]);
	}else{
		var d = new Date();
		h = d.getHours();
		m = 0;
		s = 0;
	}
	h = h>23?23:h;
	m = m>59?59:m;
	s = s>59?59:s;
	m = m>=10?m:"0"+m;
	s = s>=10?s:"0"+s;
	var win = $Z("_TimeSelector_Frame").contentWindow;
	var arr = win.$Z('divWrapper').getElementsByTagName('td');
	var len = arr.length;
	for(var i=0;i<len;i++){
		arr[i].className='';
	} 
	win.$Z("selectorHour").innerHTML = h;
	win.$Z("selectorMinute").innerHTML = m;
	win.$Z("selectorSecond").innerHTML = s;
	win.$Z("_TimeSelector_Tip").innerHTML = h+":"+m+":"+s;
	win.$Z("divHour").getElementsByTagName("td")[parseInt(h)].className = "selected";
	win.$Z("divMinute").getElementsByTagName("td")[parseInt(m)].className = "selected";
	win.$Z("divSecond").getElementsByTagName("td")[parseInt(s)].className = "selected";
	TimeSelector.showType('Hour');//重置当前选择类别
	return true;
}

TimeSelector.setTip = function(){
	$Z("_TimeSelector_Tip").innerText = [$V("_TimeSelector_Hour"),$V("_TimeSelector_Minute"),$V("_TimeSelector_Second")].join(":");
}

TimeSelector.setNow = function(){
	$S(Control,DateTime.getCurrentTime());
	var _evt = Control.getAttribute("onchange");
	if(_evt){
		eval(_evt);
	}
	TimeSelector.close();
}

TimeSelector.returnTime = function(flag){
	var win = $Z("_TimeSelector_Frame").contentWindow;
	if(flag){
		$S(win.Control,DateTime.getCurrentTime());
	}else{
		var arr = [win.$Z("selectorHour").innerHTML,win.$Z("selectorMinute").innerHTML,win.$Z("selectorSecond").innerHTML];
		$S(win.Control,arr.join(":"));
	}
	TimeSelector.close();
}

TimeSelector.showType = function(type){
	var win = $Z("_TimeSelector_Frame").contentWindow;
	var arr = ["Hour","Minute","Second"];
	for(var i=0;i<arr.length;i++){
		win.$Z("selector"+arr[i]).className = "selector";
		win.$Z('div'+arr[i]).style.display = 'none';
	}
	win.$Z('div'+type).style.display = '';
	win.$Z('selector'+type).className = 'selector_current';
	TimeSelector.adjustSize();
}

TimeSelector.show = function(ctrl,time){
	var pw = $ZE.getTopLevelWindow();
	ctrl = $Z(ctrl);
	try{ctrl.onfocus.apply(ctrl,[]);}catch(ex){}
	time = time?time:$V(ctrl);
	var ele;
	if(!pw.$Z("_TimeSelector")){
		ele = pw.document.createElement('div');
		ele.id = "_TimeSelector";
		ele.style.position = "absolute";
		ele.style.zIndex = 999;
		ele.innerHTML = "<iframe id='_TimeSelector_Frame' frameborder=0 scrolling=no width=194 height=153></iframe>";
		ele.style.width = "194px";
		pw.document.body.appendChild(ele) ; 
		ele.style.display = '';

		var win = pw.$Z("_TimeSelector_Frame").contentWindow;
		var doc = win.document;
		doc.open();
		var arr = [];
		arr.push("<style>");
		arr.push("body {margin: 0px;}");
		arr.push(".timetable {}");
		arr.push(".timetable {position: absolute;	border-top: 1px solid #777;	border-right: 1px solid #555;border-bottom: 1px solid #444;font-family: tahoma,verdana,sans-serif;");
		arr.push("border-left: 1px solid #666;font-size: 11px;cursor: default;background: #fff;}");
		arr.push(".timetable .buttonNow {text-align: center;	background-color:#def;	border-right: 1px solid #999;color:#000;font-size: 12px;}");
		arr.push(".timetable .buttonConfirm {text-align: center;	background-color:#def;	border-left: 1px solid #999;color:#000;font-size: 12px;}");
		arr.push(".timetable .buttonclose {color:#06c;text-align: center;	background-color:#def;border-left: 1px solid #999;font-size:9px;width:16px}");
		arr.push(".timetable td.selected {font-weight: bold;border: 1px solid #39f;	background: #c3e4FF;}");
		arr.push(".timetable td.now {font-weight: bold; color: #03f;}");
		arr.push(".timetable .tipnow {font-weight: bold;font-size:12px;color: #258;text-align: left;}");
		arr.push(".timetable td.over {border:1px solid #06c;background: #EDFBD2;}");
		arr.push(".selector {color:#258;padding:0 8px;border-right: 1px solid #999;border-left: 1px solid #999;background: #def;}");
		arr.push(".selector_current {color:#fff;padding:0 8px;border-right: 1px solid #999;border-left: 1px solid #999;background: #ff8800;}");
		arr.push(".wrapper {background-color:#fff;border-top: 1px solid #999;	border-bottom: 1px solid #999;text-align: center;}");
		arr.push(".wrapper td{border: 1px solid #fff;	font-size: 12px;text-align: center;	color: #06c;}");
		arr.push("</style>");
		arr.push("<body><div class='timetable' id='_TimeSelector_Table'>");
		arr.push("<table border='0' cellpadding='0' cellspacing='0' onselectstart='return false;' oncontextmenu='return false'>");
		arr.push("<tr><td><table width='100%' border='0' cellpadding='0' cellspacing='0'>");
		arr.push("<tr><td height='18' class='tipnow'>");
		arr.push("<table width='100' height='100%' border='0' cellpadding='0' cellspacing='0'>");
		arr.push("<tr class='tipnow'>");
		arr.push("<td valign='middle' class='selector_current' id='selectorHour' onclick=\"TopWindow.TimeSelector.showType('Hour')\">0</td>");
		arr.push("<td valign='middle' align='center'><span style='padding:3px;'>:</span></td>");
		arr.push("<td valign='middle' class='selector' id='selectorMinute' onclick=\"TopWindow.TimeSelector.showType('Minute')\">00</td>");
		arr.push("<td valign='middle' align='center'><span style='padding:3px;'>:</span></td>");
		arr.push("<td valign='middle' class='selector' id='selectorSecond' onclick=\"TopWindow.TimeSelector.showType('Second')\">00</td>");
		arr.push("</tr></table></td>");
		arr.push("<td width='16'><table height='100%' height='100%' border='0' cellpadding='0' cellspacing='0'><tr><td class='buttonclose' title='取消' valign='middle' onclick=\"TopWindow.TimeSelector.close();this.style.backgroundColor='#def'\" onmouseover=\"this.style.backgroundColor='#9cf'\" onmouseout=\"this.style.backgroundColor='#def'\">×</td></tr></table></td>");
		arr.push("</tr></table>");
		arr.push("<div class='wrapper' id='divWrapper'>");
		arr.push("<div id='divHour'>");
		arr.push("<table width='210' height='60' border='0' cellpadding='0' cellspacing='0' style='font-size:13px'>");
		for(var i=0;i<24;i++){
			if(i%8==0){
				arr.push("<tr>");		
			}
			if(i%12==0){
				arr.push("<td onclick='TopWindow.TimeSelector.onClick(this)' onmouseover='TopWindow.TimeSelector.onMouseOver(this)' onmouseout='TopWindow.TimeSelector.onMouseOut(this)' style='color: #e70'>"+i+"</td>");
			}else{
				arr.push("<td onclick='TopWindow.TimeSelector.onClick(this)' onmouseover='TopWindow.TimeSelector.onMouseOver(this)' onmouseout='TopWindow.TimeSelector.onMouseOut(this)'>"+i+"</td>");
			}
			if(i%8==7){
				arr.push("</tr>");
			}
		}
		arr.push("</table>");
		arr.push("</div>");
		arr.push("<div id='divMinute' style='display:none'>");
		var html = [];
		html.push("<table width='210' height='120' border='0' cellpadding='0' cellspacing='0'>");
		for(var i=0;i<60;i++){
			if(i%10==0){
				html.push("<tr>");
				html.push("<td onclick='TopWindow.TimeSelector.onClick(this)' onmouseover='TopWindow.TimeSelector.onMouseOver(this)' onmouseout='TopWindow.TimeSelector.onMouseOut(this)' style='color: #e70'>"+(i>=10?i:"0"+i)+"</td>");
			}else{
				html.push("<td onclick='TopWindow.TimeSelector.onClick(this)' onmouseover='TopWindow.TimeSelector.onMouseOver(this)' onmouseout='TopWindow.TimeSelector.onMouseOut(this)'>"+(i>=10?i:"0"+i)+"</td>");
			}
			if(i%10==9){
				html.push("</tr>");
			}
		}
		html.push("</table>");
		html.push("</div>");
		arr.push(html.join('\n'));
		arr.push("<div id='divSecond' style='display:none'>");
		arr.push(html.join('\n'));
		arr.push("</div>");
		arr.push("<table width='100%' border='0' align='left' cellpadding='0' cellspacing='0'>");
		arr.push("<tr>");
		arr.push("<td width='17%' height='20' class='buttonNow' onclick=\"TopWindow.TimeSelector.returnTime(true);this.style.backgroundColor='#def'\" onmouseover=\"this.style.backgroundColor='#9cf'\" onmouseout=\"this.style.backgroundColor='#def'\">现在</td>");
		arr.push("<td width='66%' style='font-size:11px;background-color:#fff6cc;font-weight:bold;color:#258;' id='_TimeSelector_Tip' align='center'>0:00:00</td>");
		arr.push("<td width='17%' height='20' class='buttonConfirm' onclick=\"TopWindow.TimeSelector.returnTime();this.style.backgroundColor='#def'\" onmouseover=\"this.style.backgroundColor='#9cf'\" onmouseout=\"this.style.backgroundColor='#def'\">确定</td>");
		arr.push("</tr>");
		arr.push("</table>");
		arr.push("</td>");
		arr.push("</tr>");
		arr.push("</table>");
		arr.push("</div></body>");
		arr.push("<script>function $Z(ele){return document.getElementById(ele);};function setTime(){if(!TopWindow.TimeSelector.setTime(Control.value)){}TopWindow.TimeSelector.adjustSize();}</script>");
		doc.write(arr.join("\n"));
		doc.close();				
		win.Control = ctrl;
		win.TopWindow = pw;
		win.setTime();
	}else{
		ele = pw.$Z("_TimeSelector");
		ele.show();
		var frame = pw.$Z("_TimeSelector_Frame");
		frame.show();
		frame.contentWindow.Control = ctrl;
		frame.contentWindow.setTime();
	}	
	var pos1 = $ZE.computePositionEx(ctrl,ele);
	ele.style.top = pos1.y+"px";
	ele.style.left = pos1.x+"px";
	pw.DateTime.showingID = ctrl.id;
	pw.SourceWindow = window;
	Misc.lockScroll(window);
}

TimeSelector.onMouseOver = function(cell){
	var win = $Z("_TimeSelector_Frame").contentWindow;
	var id = cell.parentNode.parentNode.parentNode.parentNode.id;
	var arr = [win.$Z("selectorHour").innerHTML,win.$Z("selectorMinute").innerHTML,win.$Z("selectorSecond").innerHTML];
	if(id=="divHour"){
		arr[0] = cell.innerHTML;
	}else if(id=="divMinute"){
		arr[1] = cell.innerHTML;
	}else if(id=="divSecond"){
		arr[2] = cell.innerHTML;
	}
	win.$Z("_TimeSelector_Tip").innerHTML = arr.join(":");
	$ZE.addClassName("over",true,cell);
}
	
TimeSelector.onMouseOut = function(cell){
	$ZE.removeClassName("over",cell);
}

TimeSelector.onClick = function(cell){
	var win = $Z("_TimeSelector_Frame").contentWindow;
	$ZE.addClassName("selected",true,cell);
	var id = cell.parentNode.parentNode.parentNode.parentNode.id;
	if(id=="divHour"){
		win.$Z("divHour").getElementsByTagName("td")[parseInt(win.$Z("selectorHour").innerHTML)].className = "";
		win.$Z("selectorHour").innerHTML = cell.innerHTML;		
		TimeSelector.showType('Minute');
	}else if(id=="divMinute"){
		win.$Z("divMinute").getElementsByTagName("td")[parseInt(win.$Z("selectorMinute").innerHTML)].className = "";
		win.$Z("selectorMinute").innerHTML = cell.innerHTML;		
		TimeSelector.showType('Second');
	}else if(id=="divSecond"){
		win.$Z("divSecond").getElementsByTagName("td")[parseInt(win.$Z("selectorSecond").innerHTML)].className = "";
		win.$Z("selectorSecond").innerHTML = cell.innerHTML;		
		TimeSelector.returnTime();
	}
	var pw = $ZE.getTopLevelWindow();
	Misc.unlockScroll(pw.SourceWindow);
}

TimeSelector.close = function(){
	var pw = $ZE.getTopLevelWindow();
	if(pw.DateTime&&pw.$Z("_TimeSelector")&&pw.$Z("_TimeSelector").visible()){
  	var frame = pw.$Z("_TimeSelector_Frame");
		try{frame.contentWindow.Control.onblur.apply(frame.contentWindow.Control,[]);}catch(ex){}
		$ZE.hide(pw.$Z("_TimeSelector"));
  	Misc.unlockScroll(pw.SourceWindow);			
		pw.SourceWindow = null;
  	pw.DateTime.showingID = false;
  }
}	

Calendar.showYearSelector = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var eleYear = win.$Z("_Calendar_Year"),eleSelector = win.$Z("_Calendar_YearSelector");
	eleYear.style.display = 'none';
	eleSelector.style.display = '';
	var year = eleYear.Year;
	for(var i=year>50?year-50:0;i<=50+parseInt(year);i++){
		eleSelector.options.add(new Option(i, i));
	}
	eleSelector.focus();
	eleSelector.selectedIndex = 50;
	Calendar.adjustSize();
}

Calendar.showMonthSelector = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var eleMonth = win.$Z("_Calendar_Month"),eleSelector = win.$Z("_Calendar_MonthSelector");
	eleMonth.style.display = 'none';
	eleSelector.style.display = '';
	eleSelector.focus();
	eleSelector.selectedIndex = eleMonth.Month;
	Calendar.adjustSize();
}

Calendar.hideYearSelector = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var eleYear = win.$Z("_Calendar_Year"),eleSelector = win.$Z("_Calendar_YearSelector");
	eleYear.style.display = '';
	eleSelector.style.display = 'none';
	for(var i=eleSelector.options.length; i>-1; i--) eleSelector.remove(i);
	Calendar.adjustSize();
}
	
Calendar.hideMonthSelector = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	win.$Z("_Calendar_Month").style.display = '';
	win.$Z("_Calendar_MonthSelector").style.display = 'none';
	Calendar.adjustSize();
}

Calendar.adjustSize = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var dim = $ZE.getDimensions(win.$Z("_Calendar_Table"));
	win.frameElement.height = dim.height+1;
	win.frameElement.width = dim.width+3;	
}

TimeSelector.adjustSize = function(){
	var win = $Z("_TimeSelector_Frame").contentWindow;
	var dim = $ZE.getDimensions(win.$Z("_TimeSelector_Table"));
	win.frameElement.height = dim.height;
	win.frameElement.width = dim.width+3;
}

Calendar.onYearSelectorChange = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var eleYear = win.$Z("_Calendar_Year"),eleSelector = win.$Z("_Calendar_YearSelector");
	eleYear.Year = eleSelector.value;
	var date = eleYear.Year+"-"+_LeftPad(win.$Z("_Calendar_Month").Month+1,"0",2)+"-01";
	Calendar.setDate(date);
	eleYear.style.display = '';
	eleSelector.style.display = 'none';
	Calendar.adjustSize();
}
	
Calendar.onMonthSelectorChange = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var eleMonth = win.$Z("_Calendar_Month"),eleSelector = win.$Z("_Calendar_MonthSelector");
	eleMonth.Month = parseInt(eleSelector.value);
	var date = win.$Z("_Calendar_Year").Year+"-"+_LeftPad(eleMonth.Month+1,"0",2)+"-01";
	Calendar.setDate(date);
	eleMonth.style.display = '';
	eleSelector.style.display = 'none';
	Calendar.adjustSize();
}

Calendar.getDateString = function(cell){
	var win = $Z("_Calendar_Frame").contentWindow;
	var Control = win.Control;
	var format = Control.$A("format");
	if(!format){
		format = "yyyy-MM-dd";
	}
	if(cell.Day){
		var day = cell.Day,month=win.$Z("_Calendar_Month").Month,year=win.$Z("_Calendar_Year").Year;
		if(day<0){
			day = -day;
			if(month==0){month = 11;year--;}else{month--;}
		}else if(day>32){
			day -= 40;
			if(month==11){month = 0;year++;}else{month++;}
		}
		format = format.replace("yyyy",year);
		format = format.replace("MM",_LeftPad(month+1,"0",2));
		format = format.replace("dd",_LeftPad(day,"0",2));
		return format;
	}else if(cell.id=="_Calendar_Today"){
		var d = new Date();
		format = format.replace("yyyy",isGecko?d.getYear()+1900:d.getYear());
		format = format.replace("MM",_LeftPad(d.getMonth()+1,"0",2));
		format = format.replace("dd",_LeftPad(d.getDate(),"0",2));
		return format;
	}else{
		return false;
	}
}
	
Calendar.onMouseOver = function(cell){
	var win = $Z("_Calendar_Frame").contentWindow;
	cell.oldCssText = cell.style.cssText;
	var str = Calendar.getDateString(cell);
	if(str){win.$Z("_Calendar_Tip").innerHTML = str;}
	if(cell.Day){
		cell.style.cssText = "border-top: 1px solid #06c;"
				  +"border-right: 1px solid #06c;"
				  +"border-bottom: 1px solid #06c;"
				  +"border-left: 1px solid #06c;"
				  +"padding: 2px 2px 0px 2px;"
				  +"background: #EDFBD2;";
	}else{
		cell.style.cssText = "background: #9cf;";
	}
	Calendar.isMouseOut = false;
}
	
Calendar.onMouseOut = function(cell){
	cell.style.cssText = cell.oldCssText;
}
	
Calendar.returnDate = function(cell){
	var win = $Z("_Calendar_Frame").contentWindow;
	var Control = win.Control;
	$S(Control,Calendar.getDateString(cell));
	var _evt = Control.$A("onchange");
	if(_evt){
		eval(_evt);
	}
	cell.style.cssText = cell.oldCssText;
	Calendar.close();
}
	
Calendar.previousYear = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var date = (--win.$Z("_Calendar_Year").Year)+"-"+_LeftPad(++win.$Z("_Calendar_Month").Month,"0",2)+"-01";
	Calendar.setDate(date);
	Calendar.adjustSize();
}
	
Calendar.nextYear = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var date = (++win.$Z("_Calendar_Year").Year)+"-"+_LeftPad(++win.$Z("_Calendar_Month").Month,"0",2)+"-01";
	Calendar.setDate(date);
	Calendar.adjustSize();
}
Calendar.previousMonth = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var month = win.$Z("_Calendar_Month").Month,year = win.$Z("_Calendar_Year").Year;
	if(month==0){month=11;year--;}else{month--;}
	var date = ""+year+"-"+_LeftPad(month+1,"0",2)+"-01";
	Calendar.setDate(date);
	Calendar.adjustSize();
}
	
Calendar.nextMonth = function(){
	var win = $Z("_Calendar_Frame").contentWindow;
	var month = win.$Z("_Calendar_Month").Month,year = win.$Z("_Calendar_Year").Year;
	if(month==11){month=0;year++;}else{month++;}
	var date = ""+year+"-"+_LeftPad(month+1,"0",2)+"-01";
	Calendar.setDate(date);
	Calendar.adjustSize();
}
	
Calendar.setDate = function(date){//日期算法在这里
	var win = $Z("_Calendar_Frame").contentWindow;
	var Control = win.Control;
	var array;
	if(!date){
		var d = new Date();
		array = [isGecko?d.getYear()+1900:d.getYear(),d.getMonth()+1,d.getDate()];
	}else{
		var format = Control.$A("format");
		if(!format){
			format = "yyyy-MM-dd";
		}
		array = new Array(3);
		var yIndex = format.indexOf("yyyy");
		if(yIndex>=0){
			array[0] = date.substr(yIndex,4);
		}else{
			alert("日期格式错误，没有年!");	
		}
		var MIndex = format.indexOf("MM");
		if(MIndex>=0){
			array[1] = date.substr(MIndex,2);
		}else{
			alert("日期格式错误，没有月!");	
		}
		var dIndex = format.indexOf("dd");
		if(dIndex>=0){
			array[2] = date.substr(dIndex,2);
		}else{
			alert("日期格式错误，没有日!");	
		}
	}
	var year = array[0];
	var isRightDate = true;
	if(isNaN(year)){
		year = '2000';
		isRightDate = false;
	}
	win.$Z("_Calendar_Year").innerHTML = year;
	win.$Z("_Calendar_Year").Year = year;
	var month =	array.length>1?array[1]-1:0;
	if(array.length==1)isRightDate = false;
	if(isNaN(month)||month<0||month>11){month = 0;isRightDate = false;}
	win.$Z("_Calendar_Month").innerHTML = Calendar.monthNames[month];
	win.$Z("_Calendar_Month").Month = month;
	var day = array.length>2?array[2]:0;
	var d2 = new Date();
	d2.setYear(year);
	d2.setMonth(month);
	d2.setDate(1);
	var week = d2.getDay();
	//计算上个月最后几天
	if(month==0){
		d2.setYear(year-1);
		d2.setMonth(11);
	}else{
		d2.setYear(year);
		d2.setMonth(month-1);
	}
	var days = [],i,j;
	for(i=28;i<33;i++){
		d2.setDate(i);
		if(d2.getMonth()==month){//等于表明是前一月的日期
			for(j=i-week;j<i;j++){
				days.push([0,j]);
			}
			break;
		}
	}
	//计算本月的所有天数
	d2.setYear(year);
	d2.setMonth(month);
	for(i=1;i<32;i++){
		if(i>=28){
			d2.setDate(i);
			if(d2.getMonth()!=month){
				break;//到了该月最后一天了
			}
		}
		if((week+i)%7==0||(week+i)%7==1){
			days.push([1,i]);
		}else{
			days.push([2,i]);
		}
	}
	//计算下月的开头几天
	for(j=0;j<7-((i-1+week)%7==0?7:(i-1+week)%7);j++){
		days.push([3,j+1]);
	}
	var html = [],rows = win.$Z("_Calendar_Table").rows;
	for(i=0;i<days.length;i++){
		var flag = days[i][0];
		var cell = rows[Math.floor(2+i/7)].cells[i%7];
		cell.innerHTML = days[i][1];
		if(flag==0){cell.className = "day disabled";cell.Day = -days[i][1];}
		if(flag==3){cell.className = "day disabled";cell.Day = 40+days[i][1];}
		if(flag==1){cell.className = "day weekend";cell.Day = days[i][1];}
		if(flag==2){cell.className = "day";cell.Day = days[i][1];}
	}
	for(j=4;j<6;j++){
		if(j>days.length/7-1){
			win.$Z("_Calendar_DayRow"+j).style.display = 'none';
		}else{
			win.$Z("_Calendar_DayRow"+j).style.display = '';					
		}
	}
	if(array.length==2)isRightDate = false;
	if(isNaN(day)||day<1||day>31){day = 1;isRightDate = false;}
	win.$Z("_Calendar_Day"+(day-1+week)).className += " selected";
	win.$Z("_Calendar_Tip").innerHTML = year+"-"+_LeftPad(month+1,"0",2)+"-"+_LeftPad(day,"0",2)
	d2 = new Date();
	if((isGecko?d2.getYear()+1900:d2.getYear())==year&&d2.getMonth()==month){
		win.$Z("_Calendar_Day"+(d2.getDate()-1+week)).className += " today";//当前日期样式
	}
	return isRightDate;
}
	
Calendar.show = function(ctrl,date){
	var pw = $ZE.getTopLevelWindow();
	ctrl = $Z(ctrl);	
	try{ctrl.onfocus.apply(ctrl,[]);}catch(ex){}
	date = date?date:$V(ctrl);
	var ele;
	if(!pw.$Z("_Calendar")){
		ele = pw.document.createElement('div');
		ele.id = "_Calendar";
		ele.style.position = "absolute";
		ele.style.zIndex = 999;
		ele.innerHTML = "<iframe id='_Calendar_Frame' frameborder=0 scrolling=no width=194 height=153></iframe>";
		ele.style.width = "194px";
		pw.document.body.appendChild(ele) ; 
		ele.style.display = '';

		var win = pw.$Z("_Calendar_Frame").contentWindow;
		var doc = win.document;
		doc.open();
		var arr = [];
		arr.push("<style>");
		arr.push(".nostyle{}");
		arr.push(".calendar {position: absolute; border-top: 1px solid #777; border-right: 1px solid #555; border-bottom: 0px solid #444; border-left: 1px solid #666; font-size: 11px; cursor: default; background: #ddd;}");
		arr.push(".calendar table { font-size: 11px; color: #06c; cursor: default; background: #def; font-family: tahoma,verdana,sans-serif;}");
		arr.push(".daynames{color:555;}");
		arr.push(".calendar .button {text-align: center;padding: 1px;border-top: 1px solid #fff; border-right: 1px solid #999; border-bottom: 1px solid #999; border-left: 1px solid #fff;}");
		arr.push(".calendar .buttontoday {text-align: center; padding: 1px; border-top: 1px solid #999; border-right: 1px solid #999; border-bottom: 1px solid #666; color:#000;}");
		arr.push(".calendar .buttonclose {text-align: center; padding: 1px; border-top: 1px solid #fff; border-right: 0px solid #999; border-bottom: 1px solid #999; border-left: 1px solid #fff;}");
		arr.push(".calendar thead .title {font-weight: bold; border-right: 1px solid #999; border-bottom: 1px solid #999; background: #B3D4FF; color: #258; text-align: center;}");
		arr.push(".calendar thead .name {border-bottom: 1px solid #ccc; padding: 2px; text-align: right; background: #E8EEF4;}");
		arr.push(".calendar .weekend {color: #e70;}");
		arr.push(".calendar tbody .day {width: 2em; text-align: right; padding: 2px 4px 2px 2px; background: #fff;}");
		arr.push(".calendar tbody td.selected {font-weight: bold; border-top: 1px solid #06c; border-right: 1px solid #06c; border-bottom: 1px solid #06c; border-left: 1px solid #06c; padding: 2px 2px 0px 2px; background: #B3D4FF;}");
		arr.push(".calendar tbody td.weekend {color: #e70;}");
		arr.push(".calendar tbody td.today {font-weight: bold;color: #03f;}");
		arr.push(".calendar tbody .disabled { color: #999; }");
		arr.push(".calendar tfoot .tiptoday {padding: 2px; border-top: 1px solid #999; border-right: 0px solid #999; border-bottom: 1px solid #666; border-left: 0px solid #999; background: #fff6cc; font-weight: bold; color: #258; text-align: center;}");
		arr.push("body {margin: 0px; }");
		arr.push("</style>");
		arr.push("<div class='calendar'>");
		arr.push("<TABLE oncontextmenu='return false' onselectstart='return false;' id=_Calendar_Table cellSpacing=0 cellPadding=0 width=190>");
		arr.push("  <THEAD>");
		arr.push("    <TR><TD colSpan=7>");
		arr.push("      <TABLE class=nostyle cellSpacing=0 cellPadding=0 width='100%'>");
		arr.push("        <TBODY>");
		arr.push("          <TR height=20>");
		arr.push("            <TD class=button onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.previousYear(); onmouseout=TopWindow.Calendar.onMouseOut(this); width=12>&#8249;</TD>");
		arr.push("            <TD class=title><DIV id=_Calendar_Year style='WIDTH: 63px' onclick=TopWindow.Calendar.showYearSelector();>2006</DIV>");
		arr.push("              <SELECT id=_Calendar_YearSelector onBlur='TopWindow.Calendar.hideYearSelector()' style='DISPLAY: none; FONT-SIZE: 11px; WIDTH: 63px' onChange='TopWindow.Calendar.onYearSelectorChange()'>");
		arr.push("              </SELECT></TD>");
		arr.push("            <TD class=button onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.nextYear(); onmouseout=TopWindow.Calendar.onMouseOut(this); width=12>&#8250;</TD>");
		arr.push("            <TD class=button onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.previousMonth(); onmouseout=TopWindow.Calendar.onMouseOut(this); width=12>&#8249;</TD>");
		arr.push("            <TD class=title><DIV id=_Calendar_Month style='WIDTH: 63px' onclick=TopWindow.Calendar.showMonthSelector();>12月</DIV>");
		arr.push("              <SELECT id=_Calendar_MonthSelector onblur=TopWindow.Calendar.hideMonthSelector() style='DISPLAY: none; FONT-SIZE: 11px; WIDTH: 63px' onchange=TopWindow.Calendar.onMonthSelectorChange()>");
		arr.push("                <OPTION value=0 selected>1月</OPTION>");
		for(var i=1;i<12;i++){
			arr.push("                <OPTION value="+i+">"+(i+1)+"月</OPTION>");
		}
		arr.push("              </SELECT></TD>");
		arr.push("        <TD class=button onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.nextMonth(); onmouseout=TopWindow.Calendar.onMouseOut(this); width=12>&#8250;</TD>");
		arr.push("          <TD class=buttonclose onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.close(); onmouseout=TopWindow.Calendar.onMouseOut(this); width=16>×</TD>");
		arr.push("        </TR>");
		arr.push("        </TBODY>");
		arr.push("      </TABLE>");
		arr.push("      </TD>");
		arr.push("    </TR>");
		arr.push("    <TR class=daynames>");
		arr.push("      <TD class='name weekend'>日</TD>");
		arr.push("      <TD class=name>一</TD>");
		arr.push("      <TD class=name>二</TD>");
		arr.push("      <TD class=name>三</TD>");
		arr.push("      <TD class=name>四</TD>");
		arr.push("      <TD class=name>五</TD>");
		arr.push("      <TD class='name weekend'>六</TD>");
		arr.push("    </TR>");
		arr.push("  </THEAD>");
		arr.push("  <TBODY id=_Calendar_Body>");
		for(var i=0;i<6;i++){
			arr.push("    <TR class=daysrow id=_Calendar_DayRow"+i+">");
			for(var j=0;j<7;j++){
				arr.push("      <TD class=day id=_Calendar_Day"+(i*7+j)+" onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.returnDate(this); onmouseout=TopWindow.Calendar.onMouseOut(this);>&nbsp;</TD>");
			}
			arr.push("    </TR>");
		}
		arr.push("  </TBODY>");
		arr.push("  <TFOOT>");
		arr.push("    <TR class=footrow>");
		arr.push("      <TD class=buttontoday id=_Calendar_Today onmouseover=TopWindow.Calendar.onMouseOver(this); onclick=TopWindow.Calendar.returnDate(this); onmouseout=TopWindow.Calendar.onMouseOut(this); colSpan=2>今日</TD>");
		arr.push("      <TD class=tiptoday id=_Calendar_Tip align=middle colSpan=5>&nbsp;</TD>");
		arr.push("    </TR>");
		arr.push("  </TFOOT>");
		arr.push("</TABLE>");
		arr.push("</div>");
		arr.push("<script>function $Z(ele){return document.getElementById(ele);};function setDate(){if(!TopWindow.Calendar.setDate(Control.value)){}TopWindow.Calendar.adjustSize();}</script>");
		doc.write(arr.join("\n"));
		doc.close();
		win.Control = ctrl;
		win.TopWindow = pw;
		win.setDate();
	}else{
		ele = pw.$Z("_Calendar");
		ele.show();
		var frame = pw.$Z("_Calendar_Frame");
		frame.show();
		frame.contentWindow.Control = ctrl;
		frame.contentWindow.setDate();
	}	

	var pos1 = $ZE.computePositionEx(ctrl,ele);
	ele.style.top = pos1.y+"px";
	ele.style.left = pos1.x+"px";
	pw.DateTime.showingID = ctrl.id;
	pw.SourceWindow = window;
	Misc.lockScroll(window);
}
	
Calendar.close = function(){
	var pw = $ZE.getTopLevelWindow();
	if(pw.DateTime&&pw.$Z("_Calendar")&&pw.$Z("_Calendar").visible()){
		var frame = pw.$Z("_Calendar_Frame");
		try{frame.contentWindow.Control.onblur.apply(frame.contentWindow.Control,[]);}catch(ex){}
  	$ZE.hide(pw.$Z("_Calendar"));
  	Misc.unlockScroll(pw.SourceWindow);
  	
		pw.SourceWindow = null;
  	pw.DateTime.showingID = false;
  }
}