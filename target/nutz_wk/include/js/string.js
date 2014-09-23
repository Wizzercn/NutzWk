
function JTrim(s)
{
    var r1, r2, s1, s2, s3;
    r1 = new RegExp("^ *");
    r2 = new RegExp(" *$");
    s1 = "" + s + "";
    s2 = s1.replace(r1, "");
    s3 = s2.replace(r2, "");
    r1 = null;
    r2 = null;
    return(s3);
}

//验证电子邮件的合法性
function isValidEmail(obj, str)
{
    var s = JTrim(obj.value);
    var n = 0;
    var apos = s.indexOf("@");
    var dpos = s.lastIndexOf(".");
    var spos = s.indexOf(" ");
    var cpos = s.indexOf(",");
    if (cpos >= 0 || spos >= 0 || apos <= 0 || dpos <= 0) n = 0;
    else if (dpos <= apos + 1) n = 0;
    else if (s.charAt(apos + 1) == '.') n = 0;
    else if (s.charAt(s.length - 1) == '.') n = 0;
    else
        return true;
    if (str != "")
    {
        Dialog.alert("无效的" + str + "！");
        obj.focus();
    }
    return false;
}

//验证密码合法性
function isValidPassword(obj, str)
{
    var s = obj.value;
    if (s.length == 0)
    {
        if (str != "")
        {
            Dialog.alert(str + "不可为空！");
            obj.focus();
        }
        return false;
    }
    return true;
}

//验证密码是否相同
function isMatchPassword(obj1, obj2, str)
{
    var s1 = obj1.value;
    var s2 = obj2.value;
    if (s1 != s2)
    {
        Dialog.alert(str + "不匹配！");
        obj1.focus();
        return false;
    }
    return true;
}

//验证obj是否为空
function isValidValue(obj, str)
{
	 
    var s = JTrim(obj.value);
    var c;
    if (s.length == 0)
    {
        if (str != "")
        {
            Dialog.alert(str + "不可为空！");
            obj.focus();
        }
        return false;
    }
    return true;
}

//验证str是否包含/
function noSlash(obj, str)
{
    var s = obj.value;
    var c;
    for (tmp = 0; tmp < s.length; tmp++)
    {
        c = s.charAt(tmp);
        if (c == '/')
        {
            if (str != "")
            {
                Dialog.alert(str + '不可包含字符"/"！');
                obj.focus();
            }
            return false;
        }
    }
    return true;
}
//验证obj是否包含str/
function noStr(obj, str)
{
    var s = obj.value;
    var c;
    for (tmp = 0; tmp < s.length; tmp++)
    {
        c = s.charAt(tmp);
        if (c == str)
        {
            if (str != "")
            {
                Dialog.alert('不可包含字符"' + str + '"！');
                obj.focus();
            }
            return false;
        }
    }
    return true;
}

//验证obj是否包含中文/
function nochinaStr(obj)
{
    var s = obj.value;
    var c;
    var re = /[^\x00-\xff]/g;
    for (tmp = 0; tmp < s.length; tmp++)
    {
        c = s.charAt(tmp);
        if (re.test(c))
        {
            Dialog.alert('不可包含中文字符');
            obj.focus();
            return false;
        }

    }
    return true;
}

//验证str是否为数字
function isDigits(obj, str)
{
    var s = obj.value;
    var c;
    for (tmp = 0; tmp < s.length; tmp++)
    {
        c = s.charAt(tmp);
        if (c > '9' || c < '0')
        {
            if (str != "")
            {
                Dialog.alert(str + '必须是整数！');
                obj.focus();
            }
            return false;
        }
    }
    return true;
}

//验证str是否为 Double型
function isDouble(obj, str)
{
    var s = obj.value;
    var c;
    if (str != null && str != "")
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if (c > '9' || c < '0')
            {
                if (tmp> 0 && c == ".")
                {
                }
                else if (str != "")
                {
                    Dialog.alert(str + '必须是数字！');
                    obj.focus();
                    return false;
                }

            }
        }
    return true;
}
function isDouble1(obj, str)
{
    var s = obj.value;
    if(s.length>1 && s.charAt(0)=="-" ){
    	s=s.substring(1,s.length);
    }
     var c;
    if (str != null && str != "")
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if (c > '9' || c < '0')
            {
                if (tmp> 0 && c == ".")
                {
                }
                else if (str != "")
                {
                    Dialog.alert(str + '必须是数字！');
                    obj.focus();
                    return false;
                }

            }
        }
    return true;   
}

//是否是负数（浮点型）
function isNegative(obj, str)
{
    var s = obj.value;
    if(s.length>1 && s.charAt(0)=="-" ){
    	s=s.substring(1,s.length);
    }
     var c;
    if (str != null && str != "")
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if (c > '9' || c < '0')
            {
                if (tmp> 0 && c == ".")
                {
                }
                else if (str != "")
                {
                    Dialog.alert(str + '必须是数字！');
                    obj.focus();
                    return false;
                }
            }
        }
    return true;   
}

//验证不能为空，且是double类型
function nullAndDouble(obj,str){
	var s = obj.value;
    if(isValidValue(obj,str)){
    	
    	 var c;
    if (str != null && str != ""){
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if (c > '9' || c < '0')
            {
                if (tmp> 0 && c == ".")
                {
                }
                else if (str != "")
                {
                    Dialog.alert(str + '必须是数字！');
                    obj.focus();
                    return false;
                }

            }
        }
    return true;
    }
    
   }
}


//验证不能为空，且是整数类型
function nullAndNum(obj,str){
	var s = obj.value;
	
    if(!isDigits(obj,str)) return ;    
    var c;
    if (str != null && str != "")
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if (c > '9' || c < '0')
            {
                if (tmp> 0 && c == ".")
                {
                }
                else if (str != "")
                {
                    Dialog.alert(str + '必须是数字！');
                    obj.focus();
                    return false;
                }

            }
        }
    return true;
}


//验证str是否为 Double型
function isSaveStr(obj, str)
{
    var s = obj.value;
    var c;
    if (str != null && str != "")
    {
        for (tmp = 0; tmp < s.length; tmp++)
        {
            c = s.charAt(tmp);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
            {
            }
            else
            {
                Dialog.alert(str + '必须是数字或英文字母！');
                obj.focus();
                return false;
            }
        }
    }
    return true;
}

//提取STR中所有的数字
function parseDigits(str)
{
    var c;
    var i = 0,j = 0;
    var sOK = "";

    for (; tmp < str.length; tmp++)
    {
        c = str.charAt(tmp);
        if (c > '9' || c < '0')
            continue;
        sOK += "" + c;
    }
    return sOK;
}

//验证年月日的合法性
function isValidDate(sYear, sMonth, sDate)
{
    if (sMonth == 2)
    {
        if (sDate > 29)
            return false;
        else if (sDate == 29 && !( (sYear % 4) == 0 && (sYear % 100) != 0 || (sYear % 400) == 0 ))
            return false;
        else
            return true;
    }
    else if ((sMonth == 4 || sMonth == 6 || sMonth == 9 || sMonth == 11 ) && sDate == 31)
        return false;
    else
        return true;
}

//选中 obj中值为 selectedValue
function selected(obj, selectedValue)
{ //默认选择 ，obj为选择菜单 ，selectedValue为默认值
 
    if (selectedValue == "")
        return;
    for (var tmp = 0; tmp < obj.length; tmp++)
    {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
        var value = obj.options[tmp].value;
        
        if (value == "")
            value = obj.options[tmp].text;
        if (value == selectedValue)
        {
            obj.options[tmp].selected = true;
            return;
        }
    }
}


//选中 obj中值为 selectedText ---xukai
function selected2(obj, selectedText)
{ //默认选择 ，obj为选择菜单 ，selectedValue为默认值

    if (selectedText == "")
        return;
    for (var tmp = 0; tmp < obj.length; tmp++)
    {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
        var value = obj.options[tmp].text;
        if (value == "")
            value = obj.options[tmp].text;
        if (value == selectedText)
        {
            obj.options[tmp].selected = true;
            return;
        }
    }
}

//选中单选框中值为 selectedValue
function checked(obj, selectedValue)
{ //默认选择 ，obj为选择菜单 ，selectedValue为默认值

    if (selectedValue == "")
        return;
    for (var tmp = 0; tmp < obj.length; tmp++)
    {     //如果选择菜单某项的值等于selectedValue，则该项为默认选项
        var value = obj[tmp].value;
        if (value == "")
            value = obj[tmp].text;
        if (value == selectedValue)
        {
            obj[tmp].checked = true;
            return;
        }
    }
}


//选中单选框中值为 selectedValue 徐凯
function checked2(obj, selectedValue)
{ //默认选择 ，obj为选择菜单 ，selectedValue为默认值
	 
    if (selectedValue == "")
        return;
	if(obj.value==selectedValue)
	{
		obj.checked=true;
		return;
	}
}

//选中OBJ中所有的先项
function selectAll(checkobj, obj)
{
	 
    if (checkobj.checked)
    {
        if (obj.length == null)
        {
            obj.checked = true;
        }
        else
        {
            for (tmp = 0; tmp < obj.length; tmp++)
            {
                obj[tmp].checked = true;
            }
        }
    }
    else
    {
        if (obj.length == null)
        {
            obj.checked = false;
        }
        else
        {
            for (tmp = 0; tmp < obj.length; tmp++)
            {
                obj[tmp].checked = false;
            }

        }
    }
}


//获取单先框或多选择框选中的数目,i 不带空格
function setSelectno(selObj, values)
{
    if (selObj.length == null)
    {
        if (values.indexOf("," + selObj.value + ",") != -1)
        {
            selObj.checked = true
        }
        return;
    }
    else
    {
        if (values.length == 0)
        {
            return;
        }
        else
        {
            for (tmp = 0; tmp < selObj.length; tmp++)
            {

                if (values.indexOf("," + selObj[tmp].value + ",") != -1)
                {
                    selObj[tmp].checked = true
                }
            }
        }
    }
    return;
}

//获取单先框或多选择框选中的数目
function setSelect(selObj, values)
{
    if (selObj.length == null)
    {
        if (values.indexOf(", " + selObj.value + ", ") != -1)
        {
            selObj.checked = true
        }
        return;
    }
    else
    {
        if (values.length == 0)
        {
            return;
        }
        else
        {
            for (tmp = 0; tmp < selObj.length; tmp++)
            {

                if (values.indexOf(", " + selObj[tmp].value + ", ") != -1)
                {
                    selObj[tmp].checked = true
                }
            }
        }
    }
    return;
}
function checkboxed(obj,values){
	if(values==""){
		return;
	}else{
	
	values=values.substring(0,values.length-1);//去掉最后一个";"符号
	
	var strs= new Array(); //定义一数组
	strs=values.split(";");
	  for(var tmp=0;tmp<strs.length;tmp++){
			var t=strs[tmp];
			
			if(t==obj[t].id){
					obj[t].checked=true;
				}
			
	}
	}
}


//获取单先框或多选择框选中的数目
function getSelectCount(selObj)
{
    var count = 0;
    if (selObj.length == null)
    {
        if (selObj.checked == true) count = 1;
    }
    else
    {
        for (tmp = 0; tmp < selObj.length; tmp++)
        {
            if (selObj[tmp].checked == true)
                count++;
        }
    }
    return count;
}

//获取第一个选择项的值
function getfirstSelect(selObj)
{
    var value = '';
    if (selObj.length == null)
    {
        if (selObj.checked == true) value = selObj.value;
    }
    else
    {
        for (tmp = 0; tmp < selObj.length; tmp++)
        {
            if (selObj[tmp].checked == true)
            {
                value = selObj[tmp].value
                break;
            }
        }
    }
    return value;
}

//转到指定页面
function itemGo(gourl)
{
    window.location = gourl;
}

//转到自定义页面，并定义其动作。
function itemToAction(gourl, action)
{
    document.form1.action = gourl;
    document.form1.doAction.value = action;
    document.form1.submit();
}

//转到新增页面
function itemToAdd(gourl)
{       
    document.form1.action = gourl;
    document.form1.doAction.value = "toadd";
    document.form1.submit();
}
//转到修改页面增加列表判断
function itemToAddList(gourl)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "toadd";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}

//转到修改页面
function itemTodoAction(gourl,doAction)
{

    document.form1.action = gourl;
    document.form1.doAction.value = doAction;
    document.form1.submit();

}

//转到修改页面
function itemToDelete(gourl)
{
     if (confirm("确定要删除吗?"))
    {
	    document.form1.action = gourl;
	    document.form1.doAction.value = "delete";
	    document.form1.submit();

    }
}
function itemToDelete1(gourl)
{
     if (confirm("确定要删除吗?"))
    {
	    document.form1.action = gourl;
	    document.form1.doAction.value = "delete1";
	    document.form1.submit();

    }
}

//提交新增
function itemAdd(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "add";
    document.form1.submit();
}

//转到修改页面
function itemToUpdate(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "toupdate";
    document.form1.submit();
}

//转到修改页面
function itemToUpdate1(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "toupdate1";
    document.form1.submit();
}
//转到浏览页面
function itemToDetail(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "detail";
    document.form1.submit();
}

//提交修改
function itemUpdate(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "update";
    document.form1.submit();
}

//转到修改页面增加列表判断
function itemToUpdateList(gourl)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "toupdate";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}
function itemToUpdateListyd(gourl)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "listpage";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}

//转到修改页面增加列表判断
function itemToUpdateList1(gourl)
{
    if (getSelectCount(document.form1.indexNO) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "toupdate";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}
//转到修改页面增加列表判断
function itemToUpdateList2(gourl)
{
    if (getSelectCount(document.form1.indexNO) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "toupdate1";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}
//转到修改页面增加列表判断
function itemToUpdateList3(gourl)
{
    if (getSelectCount(document.form1.indexNO) >= 1)
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "toupdate2";
        document.form1.submit();
    }
    else
    {
        Dialog.alert('请选择一条记录！');
    }
}
//提交删除
function itemDel(gourl)
{
    if (confirm("确定要删除吗"))
    {
        document.form1.action = gourl;
        document.form1.doAction.value = "delete";
        document.form1.deltype.value = "0";
        document.form1.submit();
    }
}

//提交删除增加列表判断
function itemDelList(gourl,type)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
        if (confirm("确定要删除吗?"))
        {
            document.form1.action = gourl;
            try
            {
                getIds();
                document.form1.deltype.value = type;
            }
            catch(err)
            {

            }
            document.form1.doAction.value = "delete";
            document.form1.submit();
        }

    }
    else
    {
        Dialog.alert('至少选择一条记录！');
    }
}


//提交批量更新操作
function itemDelList2(msg, mathod)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
        if (confirm(msg))
        {
            try
            {
                getIds();
            }
            catch(err)
            {

            }
            document.form1.doAction.value = mathod;
            document.form1.submit();
        }

    }
    else
    {
        Dialog.alert('至少选择一条记录！');
    }
}
//提交删除增加列表判断
function itemDelList1(gourl, type)
{
    if (getSelectCount(document.form1.indexNO) >= 1)
    {
        if (confirm("确定要删除吗?"))
        {
            document.form1.action = gourl;
            try
            {
                getIds1();
                document.form1.deltype.value = type;
            }
            catch(err)
            {

            }
            document.form1.doAction.value = "delete";
            document.form1.submit();
        }

    }
    else
    {
        Dialog.alert('至少选择一条记录！');
    }
}

//判断输入字符长度
function valueLength(obj, name, len)
{
    if (obj.value.length > len)
    {
        Dialog.alert(name + '长度不可以超过' + len);
        return false;
    }
    return true;
}
//得到选择的字符串
function getIds1()
{
    var ids = "";
    var obj = document.form1.indexNO;
    if (document.form1.indexNO.length == null)
    {
        if (document.form1.indexNO.checked)
        {
            ids = document.form1.indexNO.value;
        }
    }
    else
    {
        for (var tmp = 0; tmp < obj.length; tmp++)
        {
            if (obj[tmp].checked)
            {
                ids += obj[tmp].value + ",";
            }
        }
    }
    document.form1.checkids.value = ids;
}

//得到选择的字符串
function getIds()
{
    var ids = "";
    var obj = document.form1.id;
    if (document.form1.id.length == null)
    {
        if (document.form1.id.checked)
        {
            ids = document.form1.id.value;
        }
    }
    else
    {
        for (var tmp = 0; tmp < obj.length; tmp++)
        {
            if (obj[tmp].checked)
            {
                ids += obj[tmp].value + ",";
            }
        }
    }
    document.form1.checkids.value = ids;
}

//得到选择的字符串
function getValues(obj)
{
    var ids = "";
    if (obj.length == null)
    {
        if (obj.checked)
        {
            ids = obj.value;
        }
    }
    else
    {
        for (var tmp = 0; tmp < obj.length; tmp++)
        {
            if (obj[tmp].checked)
            {
                ids += obj[tmp].value + ",";
            }
        }
    }
    document.form1.checkids.value = ids;
}


//得到选择的字符串
function getSelectValues(obj, obj2)
{
    var ids = "";
    if (obj.length == null)
    {
        if (obj.checked)
        {
            ids = obj.value;
        }
    }
    else
    {
        for (var tmp = 0; tmp < obj.length; tmp++)
        {
            if (obj[tmp].checked)
            {
                ids += obj[tmp].value + "、";
            }
        }
    }
    if (ids.length > 0)
    {
        ids = ids.substr(0, ids.length - 1);
    }
    obj2.value = ids;
}

//判断其值是否被选中
function isSelect(value)
{
    var obj = document.form1.id;
    for (var tmp = 0; tmp < obj.length; tmp++)
    {
        if (obj[tmp].checked && obj[tmp].value == value)
        {
            return true;
        }
    }
    return false;
}

//打开一个弹出窗口
function openWin(endtarget, WINname, WINwidth, WINheight)
{
    var showw = window.open(endtarget, WINname, 'status=yes,toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,width=' + (WINwidth + 22) + ',height=' + WINheight);
    showw.focus();
}


//得到下拉框的级别
function gettree(id)
{
    var line=" ";
    var node=" ├";
    var restr="";
    var count= id.length / 4;
    for(tmp=1;tmp<count;tmp++)
    {
        restr+=line;
    }
    return restr+=node;
}

//得到下拉框的级别html代码方式
function gettreecode(id)
{

    var line="&nbsp;";
    var node="&nbsp;├";
    var restr="";
    var count= id.length / 4;
    for(var tmp=1;tmp<count;tmp++)
    {
        restr+=line;
    }
    return restr+=node;
}
function gettreecode1(id)
{

    var line=" ";
    var node=" ├";
    var restr="";
    var count= id.length / 4;
    for(tmp=1;tmp<count;tmp++)
    {
        restr+=line;
    }
    return restr+=node;
}
//得到资源的级别
function gettreemap1(id)
{

    var line="&nbsp;";

    var restr="";
    var count= id.length / 4;
    for(tmp=1;tmp<count;tmp++)
    {
        restr+=line;
    }
    return restr;
}
//得到资源的级别
function gettreemap(id, domain)
{
    var line="<img src='" + domain + "/images/private/sys/line.png' border=0>";
    var node="<img src='" + domain + "/images/private/sys/mfc.gif' border=0><img src='" + domain + "/images/private/sys/node.png' border=0>";
    var restr="";
    var count= id.length / 4;
    for(tmp=1;tmp<count;tmp++)
    {
        restr+=line;
    }
    return restr+=node;
}

//得到资源状态
function getstate(state, online)
{
    if (state == online)
    {
        return "√";
    }
    else
    {
        return "×";
    }
}


//
 function getnum(f, c)
{
    var t = Math.pow(10, c);
    return Math.round(f * t) / t;
}


function ForDight(Dight, How)
{
    Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);

    return  Dight;

}

//转到审核页面 xukai
function itemToAudit(gourl)
{
    document.form1.action = gourl;
    document.form1.doAction.value = "toaudit";
    document.form1.submit();
}

//转到审核页面 xukai
function itemToDel(gourl)
{
	if (confirm("确定要删除吗"))
    {
	    document.form1.action = gourl;
	    document.form1.deltype.value="0";
	    document.form1.doAction.value = "delete";
	    document.form1.submit();
    }
}

function cancle(obja, action)
{
   var obj=obja;
   obj.doAction.value=action;
   obj.submit();

}

 //提交删除增加列表判断
function itemJyList(gourl, type)
{
    if (getSelectCount(document.form1.id) >= 1)
    {
       document.form1.action = gourl;
       try
       {
           getIds();
           document.form1.enabled.value = type;
       }
       catch(err)
       {

       }
       document.form1.doAction.value = "jinyong";
       document.form1.submit();
    }
    else
    {
        Dialog.alert('至少选择一条记录！');
    }
}
   //关闭子窗口，并更新父窗口
     function closeWin(location){
        this.location=location;
        hasClosed = true;
        window.opener.location="javascript:reloadPage();";
        window.opener=null;
        window.close();
    }
     function reloadPage() {
        history.go(0);
        document.execCommand("refresh")
        document.location = document.location;
        document.location.reload();
    }
  