var scripts = document.getElementsByTagName("script");

var isIE = navigator.userAgent.toLowerCase().indexOf("msie") != -1;
var isIE8 = !!window.XDomainRequest&&!!document.documentMode;
var isIE7 = navigator.userAgent.toLowerCase().indexOf("msie 7.0") != -1 && !isIE8;
var isIE6 = navigator.userAgent.toLowerCase().indexOf("msie 6.0") != -1;
var isGecko = navigator.userAgent.toLowerCase().indexOf("gecko") != -1;
var isQuirks = document.compatMode == "BackCompat";
var isBorderBox = isIE && isQuirks;

if(isGecko){
    var p = HTMLElement.prototype;
    p.__defineSetter__("innerText",function(txt){this.textContent = txt;});
    p.__defineGetter__("innerText",function(){return this.textContent;});
    p.insertAdjacentElement = function(where,parsedNode){
        switch(where){
            case "beforeBegin":
                this.parentNode.insertBefore(parsedNode,this);
                break;
            case "afterBegin":
                this.insertBefore(parsedNode,this.firstChild);
                break;
            case "beforeEnd":
                this.appendChild(parsedNode);
                break;
            case "afterEnd":
                if(this.nextSibling)
                    this.parentNode.insertBefore(parsedNode,this.nextSibling);
                else
                    this.parentNode.appendChild(parsedNode);
                break;
        }
    };
    p.insertAdjacentHTML = function(where,htmlStr){
        var r=this.ownerDocument.createRange();
        r.setStartBefore(this);
        var parsedHTML=r.createContextualFragment(htmlStr);
        this.insertAdjacentElement(where,parsedHTML);
    };
    p.attachEvent = function(evtName,func){
        evtName = evtName.substring(2);
        this.addEventListener(evtName,func,false);
    };
    p.detachEvent = function(evtName,func){
        evtName = evtName.substring(2);
        this.removeEventListener(evtName,func,false);
    };
    window.attachEvent = p.attachEvent;
    window.detachEvent = p.detachEvent;
    document.attachEvent = p.attachEvent;
    document.detachEvent = p.detachEvent;
    p.__defineGetter__("currentStyle", function(){
        return this.ownerDocument.defaultView.getComputedStyle(this,null);
    });
    p.__defineGetter__("children",function(){
        var tmp=[];
        for(var i=0;i<this.childNodes.length;i++){
            var n=this.childNodes[i];
            if(n.nodeType==1){
                tmp.push(n);
            }
        }
        return tmp;
    });
    p.__defineSetter__("outerHTML",function(sHTML){
        var r=this.ownerDocument.createRange();
        r.setStartBefore(this);
        var df=r.createContextualFragment(sHTML);
        this.parentNode.replaceChild(df,this);
        return sHTML;
    });
    p.__defineGetter__("outerHTML",function(){
        var attr;
        var attrs=this.attributes;
        var str="<"+this.tagName.toLowerCase();
        for(var i=0;i<attrs.length;i++){
            attr=attrs[i];
            if(attr.specified){
                str+=" "+attr.name+'="'+attr.value+'"';
            }
        }
        if(!this.hasChildNodes){
            return str+">";
        }
        return str+">"+this.innerHTML+"</"+this.tagName.toLowerCase()+">";
    });
    p.__defineGetter__("canHaveChildren",function(){
        switch(this.tagName.toLowerCase()){
            case "area":
            case "base":
            case "basefont":
            case "col":
            case "frame":
            case "hr":
            case "img":
            case "br":
            case "input":
            case "isindex":
            case "link":
            case "meta":
            case "param":
                return false;
        }
        return true;
    });
    Event.prototype.__defineGetter__("srcElement",function(){
        var node=this.target;
        while(node&&node.nodeType!=1)node=node.parentNode;
        return node;
    });
    p.__defineGetter__("parentElement",function(){
        if(this.parentNode==this.ownerDocument){
            return null;
        }
        return this.parentNode;
    });
}else{
    try {
        document.documentElement.addBehavior("#default#userdata");
        document.execCommand('BackgroundImageCache', false, true);
    } catch(e) {alert(e);}
}

var _setTimeout = window.setTimeout;
window.setTimeout = function(callback,timeout,param){
    if(typeof callback == 'function'){
        var args = Array.prototype.slice.call(arguments,2);
        var _callback = function(){
            callback.apply(null,args);
        };
        return _setTimeout(_callback,timeout);
    }
    return _setTimeout(callback,timeout);
};

var Core = {};
Core.attachMethod = function(ele){
    if(!ele||ele["$A"]){
        return;
    }
    if(ele.nodeType==9){
        return;
    }
    var win;
    try{
        if(isGecko){
            win = ele.ownerDocument.defaultView;
        }else{
            win = ele.ownerDocument.parentWindow;
        }
        for(var prop in win.$ZE){
            ele[prop] = win.$ZE[prop];
        }
    }catch(ex){
        //alert("Core.attachMethod:"+ele)//有些对象不能附加属性，如flash
    }
};

var Constant = {};

Constant.Null = "_ZVING_NULL";

function $Z(ele) {
    if (typeof(ele) == 'string'){
        ele = document.getElementById(ele);
        if(!ele){
            return null;
        }
    }
    if(ele){
        Core.attachMethod(ele);
    }
    return ele;
}

function $V(ele){
    var eleId = ele;
    ele = $Z(ele);
    if(!ele){
        alert("表单元素不存在:"+eleId);
        return null;
    }
    switch (ele.type.toLowerCase()) {
        case 'submit':
        case 'hidden':
        case 'password':
        case 'textarea':
        case 'file':
        case 'image':
        case 'select-one':
        case 'text':
            return ele.value;
        case 'checkbox':
        case 'radio':
            if (ele.checked){
                return ele.value;
            }else{
                return null;
            }
        default:
            return "";
    }
}

function $S(ele,v){
    var eleId = ele;
    ele = $Z(ele);
    if(!v&&v!=0){
        v = "";
    }
    if(!ele){
        alert("表单元素不存在:"+eleId);
        return;
    }
    switch (ele.type.toLowerCase()) {
        case 'submit':
        case 'hidden':
        case 'password':
        case 'textarea':
        case 'button':
        case 'file':
        case 'image':
        case 'select-one':
        case 'text':
            ele.value = v;
            break;
        case 'checkbox':
        case 'radio':
            if(ele.value==""+v){
                ele.checked = true;
            }else{
                ele.checked=false;
            }
            break;
    }
}
function $T(tagName){
    var ts = document.getElementsByTagName(tagName);//此处返回的不是数组
    var arr = [];
    var len = ts.length;
    for(var i=0;i<len;i++){
        arr.push($Z(ts[i]));
    }
    return arr;
}
function $T(tagName,ele){
    ele = $Z(ele);
    ele = ele || document;
    var ts = ele.getElementsByTagName(tagName);//此处返回的不是数组
    var arr = [];
    var len = ts.length;
    for(var i=0;i<len;i++){
        arr.push($Z(ts[i]));
    }
    return arr;
}

function $N(ele){
    if (typeof(ele) == 'string'){
        ele = document.getElementsByName(ele);
        if(!ele||ele.length==0){
            return null;
        }
        var arr = [];
        for(var i=0;i<ele.length;i++){
            var e = ele[i];
            if(e.getAttribute("ztype")=="select"){
                e = e.parentNode;
            }
            Core.attachMethod(e);
            arr.push(e);
        }
        ele = arr;
    }
    return ele;
}
function $N_rowcheck(ele){
    if (typeof(ele) == 'string'){
        ele =document.getElementsByName(ele);//$Z(ele)
        if(!ele||ele.length==0){
            return null;
        }
        var arr = [];
        for(var i=0;i<ele.length;i++){
            var e = ele[i];
            if(e.getAttribute("ztype")=="select"){
                e = e.parentNode;
            }
            Core.attachMethod(e);
            arr.push(e);
        }
        ele = arr;
    }
    return ele;
}
function $NV(ele){
    ele = $N(ele);
    if(!ele){
        return null;
    }
    var arr = [];
    for(var i=0;i<ele.length;i++){
        var v = $V(ele[i]);
        if(v!=null){
            arr.push(v);
        }
    }
    return arr.length==0? null:arr;
}

function $NS(ele,value){
    ele = $N(ele);
    if(!ele){
        return;
    }
    if(!ele[0]){
        return $S(ele,value);
    }
    if(ele[0].type=="checkbox"){
        if(value==null){
            value = new Array(4);
        }
        var arr = value;
        if(!isArray(value)){
            arr = value.split(",");
        }
        for(var j=0;j<arr.length;j++){
            for(var i=0;i<ele.length;i++){
                $S(ele[i],arr[j]);
            }
        }
        return;
    }
    for(var i=0;i<ele.length;i++){
        $S(ele[i],value);
    }
}

function $F(ele){
    if(!ele)
        return document.forms[0];
    else{
        ele = $Z(ele);
        if(ele&&ele.tagName.toLowerCase()!="form")
            return null;
        return ele;
    }
}

function encodeURL(str){
    return encodeURI(str).replace(/=/g,"%3D").replace(/\+/g,"%2B").replace(/\?/g,"%3F").replace(/\&/g,"%26");
}

function htmlEncode(str) {
    return str.replace(/&/g,"&amp;").replace(/\"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/ /g,"&nbsp;");
}

function htmlDecode(str) {
    return str.replace(/\&quot;/g,"\"").replace(/\&lt;/g,"<").replace(/\&gt;/g,">").replace(/\&nbsp;/g," ").replace(/\&amp;/g,"&");
}

function isInt(str){
    return /^\-?\d+$/.test(""+str);
}

function isNumber(str){
    //var t = ""+str;
    for(var i=0;i<str.length;i++){
        var chr = str.charAt(i);
        if(chr!="."&&chr!="E"&&isNaN(parseInt(chr))){
            return false;
        }
    }
    return true;
}

function isTime(str){
    if(!str){
        return false;
    }
    var arr = str.split(":");
    if(arr.length!=3){
        return false;
    }
    if(!isNumber(arr[0])||!isNumber(arr[1])||!isNumber(arr[2])){
        return false;
    }
    var date = new Date();
    date.setHours(arr[0]);
    date.setMinutes(arr[1]);
    date.setSeconds(arr[2]);
    return date.toString().indexOf("Invalid")<0;
}

function isDate(str){
    if(!str){
        return false;
    }
    var arr = str.split("-");
    if(arr.length!=3){
        return false;
    }
    if(!isNumber(arr[0])||!isNumber(arr[1])||!isNumber(arr[2])){
        return false;
    }
    var date = new Date();
    date.setFullYear(arr[0]);
    date.setMonth(arr[1]);
    date.setDate(arr[2]);
    return date.toString().indexOf("Invalid")<0;
}

function isDateTime(str){
    if(!str){
        return false;
    }
    if(str.indexOf(" ")<0){
        return isDate(str);
    }
    var arr = str.split(" ");
    if(arr.length<2){
        return false;
    }
    return isDate(arr[0])&&isTime(arr[1]);
}

function isArray(obj) {
    if(!obj){
        return false;
    }
    if (obj.constructor.toString().indexOf("Array") == -1){
        return false;
    } else{
        return true;
    }
}

function isNull(v){
    return v===null||typeof(v)=="undefined";
}

function isNotNull(v){
    return !isNull(v);
}

function getEvent(evt){
    return window.event||evt;
}

function stopEvent(evt){//阻止一切事件执行,包括浏览器默认的事件
    evt = getEvent(evt);
    if(!evt){
        return;
    }
    if(isGecko){
        evt.preventDefault();
        evt.stopPropagation();
    }
    evt.cancelBubble = true;
    evt.returnValue = false;
}

function cancelEvent(evt){//仅阻止用户定义的事件
    evt = getEvent(evt);
    evt.cancelBubble = true;
}

function getEventPosition(evt){//返回相对于最上级窗口的左上角原点的坐标
    evt = getEvent(evt);
    var pos = {x:evt.clientX, y:evt.clientY};
    var win;
    if(isGecko){
        win = evt.srcElement.ownerDocument.defaultView;
    }else{
        win = evt.srcElement.ownerDocument.parentWindow;
    }
    var sw,sh;
    while(win!=win.parent){
        if(win.frameElement&&win.parent.DataCollection){
            pos2 = $ZE.getPosition(win.frameElement);
            pos.x += pos2.x;
            pos.y += pos2.y;
        }
        sw = Math.max(win.document.body.scrollLeft, win.document.documentElement.scrollLeft);
        sh = Math.max(win.document.body.scrollTop, win.document.documentElement.scrollTop);
        pos.x -= sw;
        pos.y -= sh;
        if(!win.parent.DataCollection){
            break;
        }
        win = win.parent;
    }

    return pos;
}

function getEventPositionLocal(evt){//返回事件在当前页面上的坐标
    evt = getEvent(evt);
    var pos = {x:evt.clientX, y:evt.clientY};
    var win;
    if(isGecko){
        win = evt.srcElement.ownerDocument.defaultView;
    }else{
        win = evt.srcElement.ownerDocument.parentWindow;
    }
    pos.x += Math.max(win.document.body.scrollLeft, win.document.documentElement.scrollLeft);
    pos.y += Math.max(win.document.body.scrollTop, win.document.documentElement.scrollTop);
    return pos;
}

function toXMLDOM(xml){
    var doc;
    if(isIE){
        try{
            doc = new ActiveXObject("Microsoft.XMLDOM");
        }catch(ex){
            doc = new ActiveXObject("Msxml2.DOMDocument");
        }
        doc.loadXML(xml);
    }else{
        var p =new DOMParser();
        doc= p.parseFromString(xml,"text/xml");
    }
    return doc;
}

var JSON = {};

JSON.toString=function(O) {
    var string = [];
    var isArray = function(a) {
        var string = [];
        for(var i=0; i< a.length; i++) string.push(JSON.toString(a[i]));
        return string.join(',');
    };
    var isObject = function(obj) {
        var string = [];
        for (var p in obj){
            if(obj.hasOwnProperty(p) && p!='prototype'){
                string.push('"'+p+'":'+JSON.toString(obj[p]));
            }
        };
        return string.join(',');
    };
    if (!O) return false;
    if (O instanceof Function) string.push(O);
    else if (O instanceof Array) string.push('['+isArray(O)+']');
    else if (typeof O == 'object') string.push('{'+isObject(O)+'}');
    else if (typeof O == 'string') string.push('"'+O+'"');
    else if (typeof O == 'number' && isFinite(O)) string.push(O);
    return string.join(',');
};

JSON.evaluate=function(str) {
    return (typeof str=="string")?eval('(' + str + ')'):str;
};

String.prototype.startsWith = String.prototype.startWith = function(str) {
    return this.indexOf(str) == 0;
};

String.prototype.endsWith = String.prototype.endWith = function(str) {
    var i = this.lastIndexOf(str);
    return i>=0 && this.lastIndexOf(str) == this.length-str.length;
};

String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g,"");
};

String.prototype.leftPad = function(c,count){
    if(!isNaN(count)){
        var a = "";
        for(var i=this.length;i<count;i++){
            a = a.concat(c);
        }
        a = a.concat(this);
        return a;
    }
    return null;
};

String.prototype.rightPad = function(c,count){
    if(!isNaN(count)){
        var a = this;
        for(var i=this.length;i<count;i++){
            a = a.concat(c);
        }
        return a;
    }
    return null;
};

Array.prototype.clone = function(){
    var len = this.length;
    var r = [];
    for(var i=0;i<len;i++){
        if(typeof(this[i])=="undefined"||this[i]==null){
            r[i] = this[i];
            continue;
        }
        if(this[i].constructor==Array){
            r[i] = this[i].clone();
        }else{
            r[i] = this[i];
        }
    }
    return r;
};

Array.prototype.insert = function(index,data){
    if(isNaN(index) || index<0 || index>this.length) {
        this.push(data);
    }else{
        var temp = this.slice(index);
        this[index]=data;
        for (var i=0; i<temp.length; i++){
            this[index+1+i]=temp[i];
        }
    }
    return this;
};

Array.prototype.remove = function(s,dust){//如果dust为ture，则返回被删除的元素
    if(dust){
        var dustArr=[];
        for(var i=0;i<this.length;i++){
            if(s == this[i]){
                dustArr.push(this.splice(i, 1)[0]);
            }
        }
        return dustArr;
    }

    for(var i=0;i<this.length;i++){
        if(s == this[i]){
            this.splice(i, 1);
        }
    }
    return this;
};

Array.prototype.indexOf = function(func){
    var len = this.length;
    for(var i=0;i<len;i++){
        if (this[i]==arguments[0])
            return i;
    }
    return -1;
};

Array.prototype.each = function(func){
    var len = this.length;
    for(var i=0;i<len;i++){
        try{
            func(this[i],i);
        }catch(ex){
            //alert("Array.prototype.each:"+ex.message);
        }
    }
};

var Form = {};
Form.setValue = function(dr,ele){
    ele = $F(ele);
    for(var i=0;i<ele.elements.length;i++){
        var c = $Z(ele.elements[i]);
        if(c.$A("ztype")=="select"){
            c = c.parentElement;
        }
        if(c.type=="checkbox"||c.type=="radio"){
            if(c.name){
                $NS(c.name,dr.get(c.name));
                continue;
            }
        }
        var id = c.id.toLowerCase();
        if(dr.get(id)){
            $S(c,dr.get(id));
        }
    }
};

Form.getData = function(ele){
    ele = $F(ele);
    if(!ele){
        alert("查找表单元素失败!"+ele);
        return;
    }
    var dc = new DataCollection();
    var arr = ele.elements;
    for(var i=0;i<arr.length;i++){
        var c = $Z(arr[i]);
        var ID = c.id;
        if(!c.type){
            continue;
        }
        if(c.type=="checkbox"||c.type=="radio"){
            if(c.name){
                dc.add(c.name,$NV(c.name));
                continue;
            }
        }
        if(!ID){
            continue;
        }
        if(c.$A("ztype")=="select"){
            c = c.parentElement;
        }
        dc.add(c.id,$V(c));
    }
    return dc;
};

var $ZE = {};

$ZE.$A = function(attr,ele) {
    ele = ele || this;
    ele = $Z(ele);
    return ele.getAttribute?ele.getAttribute(attr):null;
};

$ZE.$T = function(tagName,ele){
    ele = ele || this;
    ele = window.$Z(ele);
    return window.$T(tagName,ele);
};

$ZE.visible = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    if(ele.style.display=="none"){
        return false;
    }
    return true;
};

$ZE.toggle = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    $ZE[$ZE.visible(ele) ? 'hide' : 'show'](ele);
};

$ZE.toString = function(flag,index,ele) {//flag表示是否显示函数内容
    ele = ele || this;
    var arr = [];
    var i = 0;
//	for(var prop in ele){
//		if(!index||i>=index){
//			var v = null;
//			try{v = ele[prop];}catch(ex){}//gecko下可能会报错
//			if(!flag){
//				if(typeof(v)=="function"){
//					v = "function()";
//				}else if((""+v).length>100){
//					v = (""+v).substring(0,100)+"...";
//				}
//			}
//			arr.push(prop+":"+v);
//		}
//		i++;
//	}
    return arr.join("\n");
};

$ZE.focusEx = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    try{
        ele.focus();
    }catch(ex){}
};

$ZE.getForm = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    if(isIE){
        ele = ele.parentElement;
    }else if(isGecko){
        ele = ele.parentNode;
    }
    if(!ele){
        return null;
    }
    if(ele.tagName.toLowerCase()=="form"){
        return ele;
    }else{
        return $ZE.GetForm(ele);
    }
};

$ZE.hide = function(ele) {
    if(!ele){
        ele = this;
    }
    ele = $Z(ele);
    if(ele.tagName.toLowerCase()=="input"&&ele.type=="button"){
        if(ele.parentElement&&ele.parentElement.getAttribute("ztype")=="zInputBtnWrapper"){
            ele.parentElement.style.display = 'none';
        }
    }
    ele.style.display = 'none';
};

$ZE.show = function(ele) {
    if(!ele){
        ele = this;
    }
    ele = $Z(ele);
    if(ele.tagName.toLowerCase()=="input"&&ele.type=="button"){
        if(ele.parentElement&&ele.parentElement.getAttribute("ztype")=="zInputBtnWrapper"){
            ele.parentElement.style.display = '';
        }
    }
    ele.style.display = '';
};

$ZE.disable = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    if(ele.tagName.toLowerCase()=="form"){
        var elements = ele.elements;
        for (var i = 0; i < elements.length; i++) {
            //var element = elements[i];
            ele.blur();
            if(ele.hasClassName("zPushBtn")){
                ele.addClassName("zPushBtnDisabled");
                if(ele.onclick){
                    ele.onclickbak = ele.onclick;
                }
                ele.onclick=null;
            }else{
                ele.disabled = 'true';
            }
        }
    }else{
        if(ele.$A("ztype")&&ele.$A("ztype").toLowerCase()=="select"){
            Selector.setDisabled(ele,true);
        }else if(ele.hasClassName("zPushBtn")){
            ele.addClassName("zPushBtnDisabled");
            if(ele.onclick){
                ele.onclickbak = ele.onclick;
            }
            ele.onclick=null;
        }else{
            ele.disabled = 'true';
        }
    }
};

$ZE.enable = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    if(ele.tagName.toLowerCase()=="form"){
        var elements = ele.elements;
        for (var i = 0; i < elements.length; i++) {
            //var element = elements[i];
            if(ele.hasClassName("zPushBtnDisabled")){
                ele.className="zPushBtn";
                if(ele.onclickbak){
                    ele.onclick = ele.onclickbak;
                }
            }else{
                ele.disabled = '';
            }
        }
    }else{
        if(ele.$A("ztype")&&ele.$A("ztype").toLowerCase()=="select"){
            Selector.setDisabled(ele,false);
        }else if(ele.hasClassName("zPushBtnDisabled")){
            ele.className="zPushBtn";
            if(ele.onclickbak){
                ele.onclick = ele.onclickbak;
            }
        }else{
            ele.disabled = '';
        }
    }
};

$ZE.scrollTo = function(ele) {
    ele = ele || this;
    ele = $Z(ele);
    var x = ele.x ? ele.x : ele.offsetLeft,
        y = ele.y ? ele.y : ele.offsetTop;
    window.scrollTo(x, y);
};

$ZE.getDimensions = function(ele){
    ele = ele || this;
    ele = $Z(ele);
    var dim;
    if(ele.tagName.toLowerCase()=="script"){
        dim = {width:0,height:0};
    }else if ($ZE.visible(ele)){
        if(isIE && ele.offsetWidth ==0 && ele.offsetHeight ==0){
            if(isBorderBox){
                dim = {width: ele.currentStyle.pixelWidth, height: ele.currentStyle.pixelHeight};
            }else{
                dim = {width: +ele.currentStyle.pixelWidth+ele.currentStyle.borderLeftWidth.replace(/\D/g,'')+ele.currentStyle.borderRightWidth.replace(/\D/g,'')+ele.currentStyle.paddingLeft.replace(/\D/g,'')+ele.currentStyle.paddingRight.replace(/\D/g,''),
                    height: +ele.currentStyle.pixelHeight+ele.currentStyle.borderTopWidth.replace(/\D/g,'')+ele.currentStyle.borderBottomWidth.replace(/\D/g,'')+ele.currentStyle.paddingTop.replace(/\D/g,'')+ele.currentStyle.paddingBottom.replace(/\D/g,'')
                };
            }
        }else{
            dim = {width: ele.offsetWidth, height: ele.offsetHeight};
        }
    }else{
        var style = ele.style;
        var vis = style.visibility;
        var pos = style.position;
        var dis = style.display;
        style.visibility = 'hidden';
        style.position = 'absolute';
        style.display = 'block';
        var w = ele.offsetWidth;
        var h = ele.offsetHeight;
        style.display = dis;
        style.position = pos;
        style.visibility = vis;
        dim = {width: w, height: h};
    }
    return dim;
};

$ZE.getPosition = function(ele){
    ele = ele || this;
    ele = $Z(ele);
    var doc = ele.ownerDocument;
    if(ele.parentNode===null||ele.style.display=='none'){
        return false;
    }
    var parent = null;
    var pos = [];
    var box;
    if(ele.getBoundingClientRect){//IE,FF3,己很精确，但还没有非常确定无误的定位
        box = ele.getBoundingClientRect();
        var scrollTop = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);
        var scrollLeft = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
        var X = box.left + scrollLeft - doc.documentElement.clientLeft;
        var Y = box.top + scrollTop - doc.documentElement.clientTop;
        if(isIE){
            X--;
            Y--;
        }
        return {x:X, y:Y};
    }else if(doc.getBoxObjectFor){ // FF2
        box = doc.getBoxObjectFor(ele);
        var borderLeft = (ele.style.borderLeftWidth)?parseInt(ele.style.borderLeftWidth):0;
        var borderTop = (ele.style.borderTopWidth)?parseInt(ele.style.borderTopWidth):0;
        pos = [box.x - borderLeft, box.y - borderTop];
    }
    if (ele.parentNode) {
        parent = ele.parentNode;
    }else {
        parent = null;
    }
    while (parent && parent.tagName != 'BODY' && parent.tagName != 'HTML'){
        pos[0] -= parent.scrollLeft;
        pos[1] -= parent.scrollTop;
        if (parent.parentNode){
            parent = parent.parentNode;
        }else{
            parent = null;
        }
    }
    return {x:pos[0],y:pos[1]};
};

$ZE.getPositionEx = function(ele){
    ele = ele || this;
    ele = $Z(ele);
    var pos = $ZE.getPosition(ele);
    var win = window;
    var sw,sh;
    while(win!=win.parent){
        if(win.frameElement&&win.parent.DataCollection){
            pos2 = $ZE.getPosition(win.frameElement);
            pos.x += pos2.x;
            pos.y += pos2.y;
        }
        sw = Math.max(win.document.body.scrollLeft, win.document.documentElement.scrollLeft);
        sh = Math.max(win.document.body.scrollTop, win.document.documentElement.scrollTop);
        pos.x -= sw;
        pos.y -= sh;
        if(!win.parent.DataCollection){
            break;
        }
        win = win.parent;
    }
    return pos;
};

$ZE.getParent = function(tagName,ele){
    ele = ele || this;
    ele = $Z(ele);
    while(ele){
        if(ele.tagName.toLowerCase()==tagName.toLowerCase()){
            return $Z(ele);
        }
        ele = ele.parentElement;
    }
    return null;
};

$ZE.getParentByAttr = function(attrName,attrValue,ele){
    ele = ele || this;
    ele = $Z(ele);
    while(ele){
        if(ele.getAttribute(attrName)==attrValue){
            return $Z(ele);
        }
        ele = ele.parentElement;
    }
    return null;
};

$ZE.nextElement = function(ele){
    ele = ele || this;
    ele = $Z(ele);
    var x = ele.nextSibling;
    while (x&&x.nodeType!=1){
        x = x.nextSibling;
    }
    return $Z(x);
};

$ZE.previousElement = function(ele){
    ele = ele || this;
    ele = $Z(ele);
    var x = ele.previousSibling;
    while (x&&x.nodeType!=1){
        x = x.previousSibling;
    }
    return $Z(x);
};

$ZE.getTopLevelWindow = function(){
    var pw = window;
    while(pw!=pw.parent){
        if(!pw.parent.DataCollection){
            return pw.parent.parent;
        }
        pw = pw.parent;
    }
    return pw;
};

$ZE.hasClassName = function(className,ele){
    ele = ele || this;
    ele = $Z(ele);
    return (new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i").test(ele.className));
};

$ZE.addClassName = function(className,ele,before){
    ele = ele || this;
    ele = $Z(ele);
    var currentClass = ele.className;
    currentClass = currentClass?currentClass:"";
    if(!new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i").test(currentClass)){
        if(before){
            ele.className = className + ((currentClass.length > 0)? " " : "") + currentClass;
        }else{
            ele.className = currentClass + ((currentClass.length > 0)? " " : "") + className;
        }
    }
    return ele.className;
};

$ZE.removeClassName = function(className,ele){
    ele = ele || this;
    ele = $Z(ele);
    var classToRemove = new RegExp(("(^|\\s)" + className + "(?=\\s|$)"), "i");
    ele.className = ele.className.replace(classToRemove, "").replace(/^\s+|\s+$/g, "");
    return ele.className;
};

/*
 给定p1(x1/y1)和p2(x2/y2)，p1在p2的左上方(也可重合)，计算一个起始坐标，
 使得元素ele(宽为w,高为h)能够全部在p1之上，或者p2之下，并且尽可能显示ele的全部
 flag="all"表示ele能够显示在x1的两边或者x2的两边
 flag="left"表示ele能够显示在x1的左边或者x2的左边
 flag="right"表示ele能够显示在x1的右边或者x2的右边
 右键菜单、日期控件、下拉框控件需要这个函数
 */
$ZE.computePosition = function(x1,y1,x2,y2,flag,w,h,win){
    var doc = win?win.document:document;
    var cw = isQuirks?doc.body.clientWidth:doc.documentElement.clientWidth;
    var ch = isQuirks?doc.body.clientHeight:doc.documentElement.clientHeight;
    var sw = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
    var sh = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);
    if(!flag||flag.toLowerCase()=="all"){
        //先考虑p2
        if(y2-sh+h-ch<0){
            if(x2-sw+w-cw<0){//从P2往右展开可行
                return {x:x2,y:y2};
            }else{//往左展开
                return {x:x2-w,y:y2};
            }
        }
        //考虑p1
        if(x1-sw+w-cw<0){//从P1往右展开可行
            return {x:x1,y:y1-h};
        }else{//往左展开
            return {x:x1-w,y:y1-h};
        }
    }else	if(flag.toLowerCase()=="right"){
        //先考虑p2
        if(y2-sh+h-ch<0){
            if(x2-sw+w-cw<0){//从P2往右展开可行

                return {x:x2,y:y2};
            }
        }
        //考虑p1
        return {x:x1,y:y1-h};
    }else if(flag.toLowerCase()=="left"){
        //先考虑p2
        if(y2-sh+h-ch<0){
            if(x2-sw-w>0){//从P2往左展开可行
                return {x:x2,y:y2};
            }
        }
        //考虑p1
        return {x:x1-w,y:y1-h};
    }
};

/*---------------------------Server,Page-------------------------*/
var Server = {};
Server.RequestMap = {};
Server.MainServletURL = "MainServlet.jsp";
Server.ContextPath = CONTEXTPATH;
Server.Pool = [];

Server.getXMLHttpRequest = function(){
    for(var i=0;i<Server.Pool.length;i++){
        if(Server.Pool[i][1]=="0"){
            Server.Pool[i][1] = "1";
            return Server.Pool[i][0];
        }
    }
    var request=null;
    if (window.XMLHttpRequest){
        request = new XMLHttpRequest();
    }else if(window.ActiveXObject){
        for(var i =5;i>1;i--){
            try{
                if(i==2){
                    request = new ActiveXObject( "Microsoft.XMLHTTP" );
                }else{
                    request = new ActiveXObject( "Msxml2.XMLHTTP." + i + ".0" );
                }
            }catch(ex){}
        }
    }
    Server.Pool.push([request,"1"]);
    return request;
};

Server.loadURL = function(url,func){
    var Request = Server.getXMLHttpRequest();
    Request.open("GET", Server.ContextPath+url, true);
    Request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    Request.onreadystatechange = function(){
        if (Request.readyState==4&&Request.status==200) {
            try{
                if(func){
                    func(Request.responseText);
                };
            }finally{
                for(var i=0;i<Server.Pool.length;i++){
                    if(Server.Pool[i][0]==Request){
                        Server.Pool[i][1] = "0";
                        break;
                    }
                }
                Request = null;
                func = null;
            }
        }
    };
    Request.send(null);
};

Server.loadScript = function(url){
    document.write('<script type="text/javascript" src="' +url + '"><\/script>') ;
};

Server.loadCSS = function(url){
    if(isGecko){
        var e = document.createElement('LINK') ;
        e.rel	= 'stylesheet' ;
        e.type	= 'text/css' ;
        e.href	= url ;
        document.getElementsByTagName("HEAD")[0].appendChild(e) ;
    }else{
        document.createStyleSheet(url);
    }
};

Server.getOneValue = function(methodName,dc,func){//dc既可是一个DataCollection，也可以是一个单值
    if(dc&&dc.prototype==DataCollection.prototype){
        Server.sendRequest(methodName,dc,func);
    }else{
        var dc1 = new DataCollection();
        dc1.add("_Param0",dc);
        Server.sendRequest(methodName,dc1,func);
    }
};

Server.sendRequest = function(methodName,dataCollection,func,id,waitMsg){//参数id用来限定id相同的请求同一时间只能有一个
    if(!Server.executeRegisteredEvent(methodName,dataCollection)){
        Console.log(methodName+"的调用被注册事件阻止!");
        return;
    }
    var Request;
    if(id!=null && Server.RequestMap[id]){
        Request = Server.RequestMap[id];
        Request.abort();
    }else{
        Request = Server.getXMLHttpRequest();
    }
    Request.open("POST", Server.ContextPath+Server.MainServletURL, true);
    Request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    var url = "_ZVING_METHOD="+methodName+"&_ZVING_DATA=";
    if(dataCollection){
        url += encodeURL(htmlEncode(dataCollection.toXML()));
    }
    url += "&_ZVING_URL="+encodeURL(window.location.pathname);
    alert(url);
    Server._ResponseDC = null;
    Request.onreadystatechange = function(){Server.onRequestComplete(Request,func);};
    Request.send(url);
};

Server.onRequestComplete = function(Request,func){
    if (Request.readyState==4&&Request.status==200) {
        try{
            var xmlDoc = Request.responseXML;
            var dc = new DataCollection();
            if(xmlDoc){
                if(dc.parseXML(xmlDoc)){
                    dc["Status"] = dc.get("_ZVING_STATUS");
                    dc["Message"] = dc.get("_ZVING_MESSAGE");
                    if(dc.get("_ZVING_SCRIPT")){
                        eval(dc.get("_ZVING_SCRIPT"));
                    }
                }
                Server._ResponseDC = dc;
                xmlDoc = null;
            }else{
                dc["Status"] = 0;
                dc["Message"] = "服务器发生异常,未获取到数据!";
            }
            if(func){
                func(dc);
            }
            //}catch(ex){
            //	alert("Server.onRequestComplete:"+ex.message+"\t"+ex.lineNumber);
        }finally{
            for(var i=0;i<Server.Pool.length;i++){
                if(Server.Pool[i][0]==Request){
                    Server.Pool[i][1] = "0";
                    break;
                }
            }
            Request = null;
            func = null;
        }
    }
};

Server.getResponse = function(){
    return Server._ResponseDC;
};

Server.Events = {};
Server.registerEvent = function(methodName,func){//当调用method时执行相应的函数，当该函数值为false退出调用，不向后台发送指令
    var arr = Server.Events[methodName];
    if(!arr){
        arr = [];
    }
    arr.push(func);
};

Server.executeRegisteredEvent = function(methodName,dc){
    var arr = Server.Events[methodName];
    if(!arr){
        return true;
    }
    for(var i=0;i<arr.length;i++){
        if(!arr[i].apply(null,[dc,methodName])){
            return false;
        }
    }
    return true;
};
String.format = function(str) {
    var args = arguments,re = new RegExp("%([1-" + args.length + "])", "g");
    return String(str).replace(re, function($1, $2) {
        return args[$2];
    });
};
String.formatmodel = function(str, model) {
    for (var k in model) {
        var re = new RegExp("{" + k + "}", "g");
        str = str.replace(re, model[k]);
    }
    return str;
};
var Util = {};
Util.Layer = (function() {
    var _cache = {},Return = {};
    Return.Min = function(box, mBox, callback) {
        if (!_cache.MinBorder) {
            _cache.Minborder = $('<div style="z-index:10000000;background: none repeat scroll 0 0 #fff;filter:alpha(opacity=30);-moz-opacity:0.3;opacity:0.3;border:1px soild #ccc;position:absolute;top:0;left0;display:none;"></div>');
            $(document.body).append(_cache.Minborder)
        }
        var w = box.width(),h = box.height(),t = box.offset().top,l = box.offset().left,eT = mBox.offset().top,eL = mBox.offset().left,eW = mBox.width(),eH = mBox.height();
        _cache.Minborder.width(w).height(h).css({left:l,top:t}).show();
        _cache.Minborder.animate({width:eW,height:eH,top:eT,left:eL}, 300, function() {
            _cache.Minborder.hide()
        });
        if (callback) {
            callback()
        }
    };
    Return.Center = function(box, setting) {
        var mainBox;
        var cut = 0,t = 0,l = 0;
        if (setting) {
            if (setting.Main) {
                mainBox = setting.Main;
                t = mainBox.offset().top;
                l = mainBox.offset().left;
                if (setting && setting.NoAddScrollTop) {
                    t -= $(window).scrollTop()
                }
            } else {
                mainBox = $(window)
            }
            if (setting.Cut != undefined) {
                cut = setting.Cut
            }
        } else {
            mainBox = $(window)
        }
        var cssT = (mainBox.height() - box.height()) / 3 + cut + t;
        var cssL = (mainBox.width() - box.width()) / 2 + cut + l;
        if (cssT < 0) {
            cssT = 0
        }
        if (cssL < 0) {
            cssL = 0
        }
        var st = 0;
        if (!setting || !setting.NoAddScrollTop) {
            st = mainBox.scrollTop()
        }
        if (st) {
            cssT += st
        }
        box.css({top:cssT,left:cssL})
    };
    return Return
})();
var Page = {};
Page.ACTIVE = {AcDesk:0,GetMain:function() {
    return $(document.body);
}};
Page.MinMessage = (function() {
    var _dom,_timer;
    var _temp = '<div class="popup-hint" style="z-index:99999999;"><i class="" rel="type"></i><em class="sl"><b></b></em><span rel="con"></span><em class="sr"><b></b></em></div>';
    var _cache = {Type:{suc:"hint-icon hint-suc-m",war:"hint-icon hint-war-m",err:"hint-icon hint-err-m",load:"hint-loader",inf:"hint-icon hint-inf-m"}};
    var create = function(text, type) {
        if (!_dom) {
            _dom = $(String.format(_temp, text));
            $(document.body).append(_dom);
            if (isIE6) {
            } else {
                _dom.css({position:"fixed"});
            }
        }
        _dom.find("[rel='con']").html(text);
        var icon = _dom.find("[rel='type']");
        for (var k in _cache.Type) {
            icon.removeClass(_cache.Type[k]);
        }
        icon.addClass(_cache.Type[type]);
    };
    var hide = function() {
        if (_timer) {
            window.clearTimeout(_timer);
        }
        if (_dom) {
            _dom.hide();
        }
    };
    return{Show:function(obj) {
        if (!obj.type) {
            obj.type = "war";
        }
        create(obj.text, obj.type);
        Util.Layer.Center(_dom, {NoAddScrollTop:!(isIE6)});
        _dom.show();
        if (_timer) {
            window.clearTimeout(_timer);
        }
        if (obj.timeout) {
            _timer = window.setTimeout(hide, obj.timeout);
        }
    },Hide:function() {
        hide();
    }}
})();


Page.clickFunctions = [];
Page.click = function(event){
    for(var i=0;i<Page.clickFunctions.length;i++){
        Page.clickFunctions[i](event);
    }
    if(window!=window.parent&&window.parent.Page){
        window.parent.Page.click();
    }
};
Page.onClick = function(f){
    Page.clickFunctions.push(f);
};

Page._Sort = function(a1,a2){
    var i1 = a1[1];
    var i2 = a2[1];
    if(typeof(i1)=="number"){
        if(typeof(i2)=="number"){
            if(i1>i2){
                return 1;
            }else if(i1==i2){
                return 0;
            }else{
                return -1;
            }
        }
        return -1;
    }else{
        if(typeof(i2)=="number"){
            return 1;
        }else{
            return 0;
        }
    }
};

Page.loadFunctions = [];
Page.load = function(){
    //var t = new Date().getTime();
    if(window._OnLoad){//Select控件会用到
        try{window._OnLoad();}catch(ex){}
    }
    Page.loadFunctions.sort(Page._Sort);
    for(var i=0;i<Page.loadFunctions.length;i++){
        try{Page.loadFunctions[i][0]();}catch(ex){}
    }
    //t = new Date().getTime()-t;
    //Console.log(window.location+"初始化耗时: "+t+" 秒.");
};
Page.onLoad = function(f,index){
    Page.loadFunctions.push([f,index]);
};
Page.readyFunctions = [];
Page.ready=function(){
    if(window._OnReady){
        try{window._OnReady();}catch(ex){}
    }
    Page.readyFunctions.sort(Page._Sort);
    for(var i=0;i<Page.readyFunctions.length;i++){
        try{Page.readyFunctions[i][0]();}catch(ex){}
    }
};
Page.onReady= function(f,index){
    Page.readyFunctions.push([f,index]);
};

Page.mouseDownFunctions = [];
Page.mousedown = function(event){
    for(var i=0;i<Page.mouseDownFunctions.length;i++){
        Page.mouseDownFunctions[i](event);
    }
};

Page.onMouseDown = function(f){
    Page.mouseDownFunctions.push(f);
};

Page.mouseUpFunctions = [];
Page.mouseup = function(event){
    for(var i=0;i<Page.mouseUpFunctions.length;i++){
        Page.mouseUpFunctions[i](event);
    }
};

Page.onMouseUp = function(f){
    Page.mouseUpFunctions.push(f);
};

Page.mouseMoveFunctions = [];
Page.mousemove = function(event){
    for(var i=0;i<Page.mouseMoveFunctions.length;i++){
        Page.mouseMoveFunctions[i](event);
    }
};

Page.onMouseMove = function(f){
    Page.mouseMoveFunctions.push(f);
};

if(document.attachEvent){
    document.attachEvent('onclick',Page.click);
    document.attachEvent('onmousedown',Page.mousedown);
    window.attachEvent('onload',Page.load);
    document.attachEvent('onmouseup',Page.mouseup);
    document.attachEvent('onmousemove',Page.mousemove);
}else{
    document.addEventListener('click',Page.click,false);
    document.addEventListener('mousedown',Page.mousedown,false);
    window.addEventListener('load',Page.load,false);
    document.addEventListener('mouseup',Page.mouseup,false);
    document.addEventListener('mousemove',Page.mousemove,false);
}
/**begin onDOMReady**/
isReady=false;
var timer;
var bindReady = function(evt){
    if(isReady) return;
    isReady=true;
    Page.ready.call(window);
    if(document.removeEventListener){
        document.removeEventListener("DOMContentLoaded", bindReady, false);
    }else{
        if(window == window.top){
            clearInterval(timer);
            timer = null;
        }else{
            document.detachEvent("onreadystatechange", bindReady);
        }
    }
};
if(document.addEventListener){
    document.addEventListener("DOMContentLoaded", bindReady, false);
}else{
    if(window == window.top){
        timer = setInterval(function(){
            try{
                isReady||document.documentElement.doScroll('left');//在IE下用能否执行doScroll判断dom是否加载完毕
            }catch(e){
                return;
            }
            bindReady();
        },5);
    }else{
        document.attachEvent("onreadystatechange", function(){
            if((/loaded|complete/).test(document.readyState))
                bindReady();
        });
    }
}
/**end onDOMReady**/

function DataTable(){
    this.Columns = null;
    this.Values = null;
    this.Rows = null;
    this.ColMap = {};

    DataTable.prototype.getRowCount = function(){
        return this.Rows.length;
    };

    DataTable.prototype.getColCount = function(){
        return this.Columns.length;
    };

    DataTable.prototype.getColName = function(i){
        return this.Columns[i];
    };

    DataTable.prototype.get2 = function(i,j){
        return this.Rows[i].get2(j);
    };

    DataTable.prototype.get = function(i,str){
        return this.Rows[i].get(str);
    };

    DataTable.prototype.getDataRow = function(i){
        return this.Rows[i];
    };

    DataTable.prototype.deleteRow = function(i){
        this.Values.splice(i,1);
        this.Rows.splice(i,1);
        for(var k=i;k<this.Rows.length;k++){
            this.Rows[k].Index = k;
        }
    };

    DataTable.prototype.insertRow = function(i,values){
        this.Values.splice(i,0,values);
        this.Rows.splice(i,0,new DataRow(this,i));
        for(var k=i;k<this.Rows.length;k++){
            this.Rows[k].Index = k;
        }
    };

    DataTable.prototype.init = function(cols,values){
        this.Values = values;
        this.Columns = [];
        this.Rows = [];
        for(var i=0;i<cols.length;i++){
            var col = {};
            col.Name = cols[i][0].toLowerCase();
            col.Type = cols[i][1];
            this.Columns[i] = col;
            this.ColMap[col.Name] = i;
        }
        for(var i=0;i<values.length;i++){
            var row = new DataRow(this,i);
            this.Rows[i] = row;
        }
    };

    DataTable.prototype.toString = function(){
        var arr = [];
        arr.push("<columns><![CDATA[[");
        for(var i=0;i<this.Columns.length;i++){
            if(i!=0){
                arr.push(",");
            }
            arr.push("[");
            arr.push("\""+this.Columns[i].Name+"\",");
            arr.push(this.Columns[i].Type);
            arr.push("]");
        }
        arr.push("]]]></columns>");
        arr.push("<values><![CDATA[[");
        for(var i=0;i<this.Values.length;i++){
            if(i!=0){
                arr.push(",");
            }
            arr.push("[");
            for(var j=0;j<this.Columns.length;j++){
                if(j!=0){
                    arr.push(",");
                }
                if(this.Values[i][j]==null||typeof(this.Values[i][j])=="undefined"){
                    arr.push("\"_ZVING_NULL\"");
                }else{
                    arr.push("\""+this.Values[i][j]+"\"");
                }
            }
            arr.push("]");
        }
        arr.push("]]]></values>");
        return arr.join('');
    };
}

function DataRow(dt,index){
    this.DT = dt;
    this.Index = index;

    DataRow.prototype.get2 = function(i){
        return this.DT.Values[this.Index][i];
    };

    DataRow.prototype.getColCount = function(){
        return this.DT.Columns.length;
    };

    DataTable.prototype.getColName = function(i){
        return this.DT.Columns[i];
    };

    DataRow.prototype.get = function(str){
        str = str.toLowerCase();
        var c = this.DT.ColMap[str];
        if(typeof(c)=="undefined"){
            return null;
        }
        return this.DT.Values[this.Index][c];
    };

    DataRow.prototype.set = function(str,value){
        str = str.toLowerCase();
        var c = this.DT.ColMap[str];
        if(typeof(c)=="undefined"){
            return;
        }
        this.DT.Values[this.Index][c] = value;
    };

    DataRow.prototype.set2 = function(i,value){
        this.DT.Values[this.Index][i] = value;
    };
}

function DataCollection(){
    this.map = {};
    this.valuetype = {};
    this.keys = [];

    DataCollection.prototype.get = function(ID){
        if(typeof(ID)=="number"){
            return this.map[this.keys[ID]];
        }
        return this.map[ID];
    };

    DataCollection.prototype.getKey = function(index){
        return this.keys[index];
    };

    DataCollection.prototype.size = function(){
        return this.keys.length;
    };

    DataCollection.prototype.remove = function(ID){
        if(typeof(ID)=="number"){
            if(ID<this.keys.length){
                var obj = this.map[this.keys[ID]];
                this.map[this.keys[ID]] = null;
                this.keys.splice(ID);
                return obj;
            }
        }else{
            for(var i=0;i<this.keys.length;i++){
                if(this.keys[i]==ID){
                    var obj = this.map[ID];
                    this.map[ID] = null;
                    this.keys.splice(i);
                    return obj;
                    break;
                }
            }
        }
        return null;
    };

    DataCollection.prototype.toQueryString = function(){
        var arr = [];
        for(var i=0;i<this.keys.length;i++){
            if(this.map[this.keys[i]]==null||this.map[this.keys[i]]==""){continue;}
            if(i!=0){
                arr.push("&");
            }
            arr.push(this.keys[i]+"="+this.map[this.keys[i]]);
        }
        return arr.join('');
    };

    DataCollection.prototype.parseXML = function(xmlDoc){
        var coll = xmlDoc.documentElement;
        if(!coll){
            return false;
        }
        var nodes = coll.childNodes;
        var len = nodes.length;
        for(var i=0;i<len;i++){
            var node = nodes[i];
            var Type = node.getAttribute("Type");
            var ID = node.getAttribute("ID");
            this.valuetype[ID] = Type;
            if(Type=="String"){
                var v = node.firstChild.nodeValue;
                if(v==Constant.Null){
                    v = null;
                }
                this.map[ID] = v;
            }else if(Type=="StringArray"){
                this.map[ID] = eval("["+node.firstChild.nodeValue+"]");
            }else if(Type=="Map"){
                this.map[ID] = eval("("+node.firstChild.nodeValue+")");
            }else if(Type=="DataTable"||Type=="Schema"||Type=="SchemaSet"){
                this.parseDataTable(node,"DataTable");
            }else{
                this.map[ID] = node.getAttribute("Value");
            }
            this.keys.push(ID);
        }
        return true;
    };

    DataCollection.prototype.parseDataTable = function(node,strType){
        var cols = node.childNodes[0].childNodes[0].nodeValue;
        cols = "var _TMP1 = "+cols+"";
        eval(cols);
        cols = _TMP1;
        var values = node.childNodes[1].childNodes[0].nodeValue;
        values = "var _TMP2 = "+values+"";
        eval(values);
        values = _TMP2;
        var obj;
        obj = new DataTable();
        obj.init(cols,values);
        this.add(node.getAttribute("ID"),obj);
    };

    DataCollection.prototype.toXML = function(){
        var arr = [];
        arr.push("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        arr.push("<collection>");
        for(var i=0;i<this.keys.length;i++){
            var ID = this.keys[i];
            try{
                var v = this.map[ID];
                arr.push("<element ID=\""+ID+"\" Type=\""+this.valuetype[ID]+"\">");
                if(this.valuetype[ID]=="DataTable"){
                    arr.push(v.toString());
                }else if(this.valuetype[ID]=="String"){
                    if(v==null||typeof(v)=="undefined"){
                        arr.push("<![CDATA["+Constant.Null+"]]>");
                    }else{
                        arr.push("<![CDATA["+v+"]]>");
                    }
                }else if(this.valuetype[ID]=="Map"){
                    if(v==null||typeof(v)=="undefined"){
                        arr.push("<![CDATA["+Constant.Null+"]]>");
                    }else{
                        arr.push("<![CDATA["+JSON.toString(v)+"]]>");
                    }
                }else{
                    arr.push(v);
                }
                arr.push("</element>");
            }catch(ex){alert("DataCollection.toXML():"+ID+","+ex.message);}
        }
        arr.push("</collection>");
        return arr.join('');
    };

    DataCollection.prototype.add = function(ID,Value,Type){
        this.map[ID] = Value;
        this.keys.push(ID);
        if(Type){
            this.valuetype[ID] = Type;
        }else	if( Value && Value.getDataRow){//DataTable可能不是本页面中的
            this.valuetype[ID] = "DataTable";
        }else{
            this.valuetype[ID] = "String";
        }
    };

    DataCollection.prototype.addAll = function(dc){
        if(!dc){
            return;
        }
        if(!dc.valuetype){
            alert("DataCollection.addAll()要求参数必须是一个DataCollection!");
        }
        var size = dc.size();
        for(var i=0;i<size;i++){
            var k = dc.keys[i];
            var v = dc.map[k];
            var t = dc.valuetype[k];
            this.add(k,v,t);
        }
    };
}

var Cookie = {};//Cookie操作类，支持大于4K的Cookie

Cookie.Spliter = "_WIZ_SPLITER_";

Cookie.get = function(name){
    var cs = document.cookie.split("; ");
    for(var i=0; i<cs.length; i++){
        var arr = cs[i].split("=");
        var n = arr[0].trim();
        var v = arr[1]?arr[1].trim():"";
        if(n==name){
            return decodeURI(v);
        }
    }
    return null;
};

Cookie.getAll = function(){
    var cs = document.cookie.split("; ");
    var r = [];
    for(var i=0; i<cs.length; i++){
        var arr = cs[i].split("=");
        var n = arr[0].trim();
        var v = arr[1]?arr[1].trim():"";
        if(n.indexOf(Cookie.Spliter)>=0){
            continue;
        }
        if(v.indexOf("^"+Cookie.Spliter)==0){
            var max = v.substring(Cookie.Spliter.length+1,v.indexOf("$"));
            var vs = [v];
            for(var j=1;j<max;j++){
                vs.push(Cookie.get(n+Cookie.Spliter+j));
            }
            v = vs.join('');
            v = v.substring(v.indexOf("$")+1);
        }
        r.push([n,decodeURI(v)]);
    }
    return r;
};

Cookie.set = function(name, value, expires, path, domain, secure, isPart){
    if(!isPart){
        value = encodeURI(value);
        //var value = encodeURI(value);
    }
    if(!name || !value){
        return false;
    }
    if(!path){
        path = Server.ContextPath.replace(/^\w+:\/\/[.\w]+:?\d*/g, '');//特别注意，此处是为了实现不管当前页面在哪个路径下，Cookie中同名名值对只有一份
    }
    if(expires!=null){
        if(/^[0-9]+$/.test(expires)){
            expires = new Date(new Date().getTime()+expires*1000).toGMTString();
        }else{
            var date = DateTime.parseDate(expires);
            if(date){
                expires = date.toGMTString();
            }else{
                expires = undefined;
            }
        }
    }
    if(!isPart){
        Cookie.remove(name, path, domain);
    }
    var cv = name+"="+value+";"
        + ((expires) ? " expires="+expires+";" : "")
        + ((path) ? "path="+path+";" : "")
        + ((domain) ? "domain="+domain+";" : "")
        + ((secure && secure != 0) ? "secure" : "");
    if(cv.length < 4096){
        document.cookie = cv;
    }else{
        var max = Math.ceil(value.length*1.0/3800);
        for(var i=0; i<max; i++){
            if(i==0){
                Cookie.set(name, '^'+Cookie.Spliter+'|'+max+'$'+value.substr(0,3800), expires, path, domain, secure, true);
            }else{
                Cookie.set(name+Cookie.Spliter+i, value.substr(i*3800,3800), expires, path, domain, secure, true);
            }
        }
    }
    return true;
};

Cookie.remove = function(name, path, domain){
    var v = Cookie.get(name);
    if(!name||v==null){
        return false;
    }
    if(encodeURI(v).length > 3800){
        var max = Math.ceil(encodeURI(v).length*1.0/3800);
        for(var i=1; i<max; i++){
            document.cookie = name+Cookie.Spliter+i+"=;"
                + ((path)?"path="+path+";":"")
                + ((domain)?"domain="+domain+";":"")
                + "expires=Thu, 01-Jan-1970 00:00:01 GMT;";
        }
    }
    document.cookie = name+"=;"
        + ((path)?"path="+path+";":"")
        + ((domain)?"domain="+domain+";":"")
        + "expires=Thu, 01-Jan-1970 00:00:01 GMT;";
    return true;
};

var Console = {};
Console.info = [];

Console.log = function(str){
    Console.info.push(str);
    if(Console.info.length>1200){
        Console.info.splice(1000,Console.info.length-1000);
    }
    if(Console.isShowing){
        //立即显示信息
    }
};

Console.show = function(){
    var html = [];
    html.push("<textarea class='input_textarea' style='width:600px;height:200px'>");
    for(var i=0;i<Console.info.length;i++){
        html.push(htmlEncode(Console.info[i]));
        html.push("<br>");
    }
    html.push("</textarea>");
    Dialog.alert(html.join('\n'),700,250);
};

var Misc = {};
Misc.setButtonText = function(ele,text){//为z:button设置文本
    $Z(ele).childNodes[1].innerHTML = text+"&nbsp;";
};

Misc.withinElement = function(event, ele) {//仅适用于Gecko,判断onmouseover,onmouseout是否是一次元素内部重复触发
    return false;
    var parent = event.relatedTarget;
    while(parent&&parent!=ele&&parent!=document.body){
        try{
            parent = parent.parentNode;
        }catch(ex){
            alert("Misc.withinElement:"+ex.message);
            return false;
        }
    }
    return parent == ele;
};

Misc.copyToClipboard = function(text){
    if(text==null){
        return;
    }
    if (window.clipboardData){
        window.clipboardData.setData("Text", text);
    }else if (window.netscape){
        try{
            netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
        }catch(ex){
            Dialog.alert("Firefox自动复制功能未启用！<br>请<a href='about:config' target='_blank'>点击此处</a> 将’signed.applets.codebase_principal_support’设置为’true’");
        }
        var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
        if(!clip){return;}
        var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
        if(!trans){return;}
        trans.addDataFlavor('text/unicode');
        //var str = new Object();
        //var len = new Object();
        var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
        var copytext=text;
        str.data=copytext;
        trans.setTransferData("text/unicode",str,copytext.length*2);
        var clipid=Components.interfaces.nsIClipboard;
        if(!clip){return;}
        clip.setData(trans,null,clipid.kGlobalClipboard);
    }else{
        alert("该浏览器不支持自动复制功能！");
        return;
    }
    Dialog.alert("己复制文字：<br><br><font color='red'>"+text+"</font><br><br>到剪贴板",null,320,150);
};

Misc.lockSelect = function(ele){
    if(!ele){
        ele = document.body;
    }
    if(isGecko){
        ele.style.MozUserSelect = "none";
        ele.style.MozUserInput = "none";
    }else{
        document.selection.empty();
        ele.onselectstart = stopEvent;
    }
};

Misc.unlockSelect = function(ele){
    if(!ele){
        ele = document.body;
    }
    if(isGecko){
        ele.style.MozUserSelect = "";
        ele.style.MozUserInput = "";
    }else{
        ele.onselectstart = null;
    }
};

Misc.lockScroll = function(win){
    if(!win){
        win = window;
    }
    if(isIE){
        win.document.body.attachEvent("onmousewheel",win.stopEvent);
    }else{
        win.addEventListener("DOMMouseScroll", win.stopEvent, false);
    }
};

Misc.unlockScroll = function(win){
    if(!win){
        win = window;
    }
    if(isIE){
        win.document.body.detachEvent("onmousewheel",win.stopEvent);
        win.document.body.detachEvent("onmousewheel",win.stopEvent);//Select.js中可能会连续attch两次,所以必须detach两次
    }else{
        win.removeEventListener("DOMMouseScroll", win.stopEvent, false);
    }
};
var Hidebutton = {};
Hidebutton.hide = function(ele) {
    if(!ele){
        ele = this;
    }
    ele = $Z(ele);
    if(ele.tagName.toLowerCase()=="input"&&ele.type=="button"){
        if(ele.parentElement&&ele.parentElement.getAttribute("ztype")=="zInputBtnWrapper"){
            ele.parentElement.style.display = 'none';
        }
    }
    ele.style.display = 'none';
};
/*START_LOADSCRIPT*/
//Server.loadScript(CONTEXTPATH + "/include/js/jquery.min.js");
Server.loadScript(CONTEXTPATH + "/include/js/jquery-1.10.2.min.js");
Server.loadScript(CONTEXTPATH + "/include/js/jquery.hotkeys.js");
Server.loadScript(CONTEXTPATH + "/include/js/page.js");
Server.loadScript(CONTEXTPATH+"/include/js/Application.js");
Server.loadScript(CONTEXTPATH+"/include/js/datagrid.js");
Server.loadScript(CONTEXTPATH+"/include/js/drag.js");
Server.loadScript(CONTEXTPATH+"/include/js/dialog.js");
Server.loadScript(CONTEXTPATH+"/include/js/datetime.js");
Server.loadScript(CONTEXTPATH+"/include/js/tip.js");
Server.loadScript(CONTEXTPATH+"/include/js/verify.js");
Server.loadScript(CONTEXTPATH+"/include/js/style.js");


/*END_LOADSCRIPT*/
