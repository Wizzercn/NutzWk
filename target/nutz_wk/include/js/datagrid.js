var DataGrid = {};

Constant.SortString = "_WIZ_SORTSTRING";
Constant.PageIndex = "_WIZ_PAGEINDEX";
Constant.PageTotal = "_WIZ_PAGETOTAL";
Constant.TagBody = "_WIZ_TAGBODY";
Constant.ID = "_WIZ_ID";
Constant.Method = "_WIZ_METHOD";
Constant.Page = "_WIZ_PAGE";
Constant.Size = "_WIZ_SIZE";
Constant.Null = "_WIZ_NULL";
Constant.InsertRow = "_WIZ_INSERTROW";
Constant.DataTable = "_WIZ_DataTable";

DataGrid.onAllCheckClick = function(ele){
	ele = $Z(ele);
	var strID = ele.id;
	if($Z(strID+"_AllCheck").checked){
		DataGrid.selectAll(ele);
	}else{
		DataGrid.unselectAll(ele);
	}
};

DataGrid.selectAll = function(ele){
	ele = $Z(ele);
	var strID = ele.id;
	var arr = $N(strID+"_RowCheck");
	for(var i=0;arr&&i<arr.length;i++){
		if(!arr[i].disabled){
			arr[i].checked = true;
			DataGrid.onSelectorClick(arr[i]);
		}
	}
};

DataGrid.unselectAll = function(ele){
	ele = $Z(ele);
	var strID = ele.id;
	var arr = $N(strID+"_RowCheck");
	for(var i=0;arr&&i<arr.length;i++){
		if(!arr[i].disabled){
			arr[i].checked = false;
			DataGrid.onSelectorClick(arr[i]);
		}
	}
};
DataGrid.onAllCheckClickA = function(ele){
	ele = $Z(ele);
	var strID = ele.id;
	if($Z(strID+"_AllCheck").checked){
		DataGrid.selectAllA(ele);
	}else{
		DataGrid.unselectAllA(ele);
	}
};
DataGrid.selectAllA = function(ele){
//	ele = $Z(ele);
//	var strID = ele.id;
	var arr = $N("id");
	for(var i=0;arr&&i<arr.length;i++){
		if(!arr[i].disabled){
			arr[i].checked = true;
			DataGrid.onSelectorClickA(arr[i]);
		}
	}
};

DataGrid.unselectAllA = function(ele){
//	ele = $Z(ele);
//	var strID = ele.id;
//	var arr = $N(strID+"_RowCheck");
    var arr = $N("id");
	for(var i=0;arr&&i<arr.length;i++){
		if(!arr[i].disabled){
			arr[i].checked = false;
			DataGrid.onSelectorClickA(arr[i]);
		}
	}
};
DataGrid.getSelectedValue = function(ele){
	ele = $Z(ele);
	var strID = ele.id;
	return $NV(strID+"_RowCheck");
};
DataGrid.getSelectedValueID = function(ele){
	ele = $Z(ele);
//	var strID = ele.id;
	return $NV("id");
};
DataGrid.getSelectedTreeValue = function(ele){
	ele = $Z(ele);
	return $NV(ele.id+"_TreeRowCheck");
};

DataGrid.getSelectedRows = function(ele){
	ele = $Z(ele);
	var rs = [];
	for(var i=1;i<ele.rows.length;i++){
		if(ele.rows[i].Selected){
			rs.push(ele.rows[i]);
		}
	}
	return rs;
};

DataGrid.getSelectedData = function(ele){
	ele = $Z(ele);
	var ds = ele.DataSource;
	var values = [];
	for(var i=1;i<ele.rows.length;i++){
		if(ele.rows[i].Selected){
			values.push(ds.Values[i-1]);
		}
	}
	var dt = new DataTable();
	var cols = [];
	for(var i=0;i<ds.Columns.length;i++){
		cols.push([ds.Columns[i].Name,ds.Columns[i].Type]);
	}
	dt.init(cols,values);
	return dt;
};

DataGrid.select = function(ele,v){
	ele = $Z(ele);
	var arr = $N(ele.id+"_RowCheck");
	if(!arr){
		return;
	}
	for(var i=0;i<arr.length;i++){
		if(arr[i].value==v){
				arr[i].checked = true;
				DataGrid.onSelectorClick(arr[i]);
				break;
		}
	}
};

DataGrid.SelectedBgColor = "#D8F79D";
DataGrid.MouseOverBgColor = "#EDFBD2";

DataGrid.onSelectorClick = function(ele,evt){
	var tr = $Z(ele).getParent("tr");
	var dg = tr.parentNode.parentNode;
	if(ele.tagName.toLowerCase()=="input"){
		tr.Selected = ele.checked;
	}else{
		tr.Selected = !tr.Selected;
		$Z(dg.id+"_RowCheck"+tr.rowIndex).checked = tr.Selected;
	}
	DataGrid.onRowSelected(tr,evt);
	var multiSelect = dg.$A("multiSelect")!="false";
	if(evt&&multiSelect){//事件调用，evt为空则为其他函数调用
		tr.SelectorFlag = true;
	}
};
DataGrid.onSelectorClickA = function(ele,evt){
	var tr = $Z(ele).getParent("tr");
	var dg = tr.parentNode.parentNode;
	if(ele.tagName.toLowerCase()=="input"){
		tr.Selected = ele.checked;
	}else{
		tr.Selected = !tr.Selected;
		$Z(dg.id+"_RowCheck"+tr.rowIndex).checked = tr.Selected;
	}
	DataGrid.onRowSelected(tr,evt);
    tr.SelectorFlag = true;
//	var multiSelect = dg.$A("multiSelect")!="false";
//	if(evt&&multiSelect){//事件调用，evt为空则为其他函数调用
//		tr.SelectorFlag = true;
//	}
};

DataGrid.onRowSelected = function(tr,evt){
	if(typeof(tr.DefaultBgColor)=="undefined"){
		tr.DefaultBgColor = tr.style.backgroundColor;
	}
	if(tr.Selected){
		tr.style.backgroundColor = DataGrid.SelectedBgColor;
	}else{
		tr.style.backgroundColor = tr.DefaultBgColor;
	}
};

DataGrid.onRowClick = function(ele,evt){
	evt = getEvent(evt);
	var dg = $Z(ele.parentNode.parentNode);
	var multiSelect = dg.$A("multiSelect")!="false";
	for(var j=1;j<dg.rows.length;j++){
		var row = dg.rows[j];
		if(evt){
			if(!evt.ctrlKey||!multiSelect){
				if(row!=ele&&row.Selected){
					if(ele.SelectorFlag){
						continue;
					}
					row.Selected = false;
					var chkbox = $Z(dg.id+"_RowCheck"+row.rowIndex);
					if(chkbox){
						chkbox.checked = false;
					}
					DataGrid.onRowSelected(row);
				}
			}
		}
	}
	if(evt&&!ele.SelectorFlag){
		if(evt.ctrlKey){//按Ctrl点击选中行的其他区域
			if(ele.Selected){
				ele.Selected = false;
				var chkbox = $Z(dg.id+"_RowCheck"+row.rowIndex);
				if(chkbox){
					chkbox.checked = false;
				}
				DataGrid.onRowSelected(ele);
				ele.SelectorFlag = false;
				return;
			}
		}
	}
	var chkbox = $Z(dg.id+"_RowCheck"+ele.rowIndex);
	if(chkbox){
		if(evt&&!ele.SelectorFlag){
			chkbox.checked = true;
		}
		ele.Selected = chkbox.checked;
	}else{
		ele.Selected = true;
	}
	DataGrid.onRowSelected(ele,evt);
	ele.SelectorFlag = false;
};

DataGrid.onSort = function(ele){
	var direction = ele.getAttribute("direction");
	if(!direction){
		direction = "";
	}
	if(direction.toUpperCase()=="ASC"){
		direction = "";
	}else	if(direction==""){
		direction = " DESC";
	}else	if(direction.toUpperCase()=="DESC"){
		direction = " ASC";
	}
	var sortField = ele.getAttribute("sortField");
	var table = ele.parentNode.parentNode.parentNode;
	var sortString = table.getAttribute("SortString");
	var tmp = [];
	if(!sortString){
		sortString = "";
	}
	var arr = sortString.split(",");		
	tmp.push(sortField+direction);

	for(var i=0;i<arr.length;i++){
		if(!arr[i]||arr[i].toLowerCase()==sortField.toLowerCase()||arr[i].indexOf(sortField+" ")>=0){
			continue;
		}
		tmp.push(arr[i]);
	}
	sortString = tmp.join();
	if(!sortString){
		sortString = Constant.Null;
	}
	if(table.id.endsWith("_HeadTable")){
		table = $Z(table.id.substring(0,table.id.lastIndexOf("_")));
	}
	DataGrid.setParam(table,Constant.SortString,sortString);
	DataGrid.loadData(table);
};

DataGrid.sort = function(ele,field,order){
	if(!order){
		order = "DESC";
	}else{
		order = order.toUpperCase();	
	}
	DataGrid.setParam(ele,Constant.SortString,field+" "+order);
	DataGrid.loadData(ele);
};

DataGrid.init = function(ele){
	ele = $Z(ele);
	DataGrid.setParam(ele,Constant.ID, ele.id);
	DataGrid.setParam(ele,Constant.Method, ele.getAttribute("method"));
	DataGrid.setParam(ele,Constant.Page, ele.getAttribute("page"));
	DataGrid.setParam(ele,Constant.TagBody, ele.TagBody);	
	
	/*if(isGecko){//禁止选择,IE下在DataGridAction中实现
		for(var i=0;i<ele.rows.length;i++){
			for(var j=0;j<ele.rows[i].cells.length;j++){
				ele.rows[i].cells[j].style.MozUserSelect = 'none';
			}
		}
	}*/
	//初始化DataGird方法，但要兼容原有写法
	var id = ele.id;
	ele.clear = function(){DataGrid.clear(id);};
	ele.loadData = function(func,dc){DataGrid.loadData(id,func,dc);};
	ele._insertRow = ele.insertRow;
	ele._deleteRow = ele.deleteRow;
	ele.insertRow = function(index,editFlag,func){DataGrid.insertRow(id,index,editFlag,func);};
	ele.deleteRow = function(index){DataGrid.deleteRow(id,index);};
	ele.edit = function(evt){DataGrid.edit(evt,id);};
	ele.filter = function(func){DataGrid.filter(id,func);};
	ele.getParam = function(name){return DataGrid.getParam(id,name);};
	ele.setParam = function(name,value){DataGrid.setParam(id,name,value);};
	ele.toExcel = function(toExcelpageFlag){DataGrid.toExcel(id,toExcelpageFlag);};
	ele.allToExcel = function(){DataGrid.allToExcel(id);};
	ele.save = function(method,func){DataGrid.save(id,method,func);};
	ele.select = function(v){DataGrid.select(id,v);};
	ele.sort = function(field,order){DataGrid.sort(id,field,order);};

	ele.showLoading = function(){DataGrid.showLoading(id);};
	ele.selectAll = function(){DataGrid.selectAll(id);};
	ele.unselectAll = function(){DataGrid.unselectAll(id);};
	ele.getSelectedData = function(){return DataGrid.getSelectedData(id);};
	ele.getSelectedRows = function(){return DataGrid.getSelectedRows(id);};
	ele.getSelectedTreeValue = function(){return DataGrid.getSelectedTreeValue(id);};
	ele.getSelectedValue = function(){return DataGrid.getSelectedValue(id);};
	ele.getSelectors = function(){return DataGrid.getSelectors(id);};
	ele.getColumnInfo = function(){return DataGrid.getColumnInfo(id);};
	ele.selectedRowToExcel = function(){DataGrid.selectedRowToExcel(id);};
	ele.setFixedHeight = function(h){DataGrid.setFixedHeight(id,h);};
	ele.setFixedWidth = function(w){DataGrid.setFixedWidth(id,w);};
	
	ele.getPageSize = function(){return DataGrid.getParam(id,Constant.PageSize);};
	ele.getPageIndex = function(){return DataGrid.getParam(id,Constant.PageIndex);};
	ele.setPageSize = function(size){DataGrid.setParam(id,Constant.PageSize,size);};
	ele.setPageIndex = function(index){DataGrid.setParam(id,Constant.PageIndex,index);};
	
	ele.isPageFlag = function(){return $Z(id).$A("page")=="true";};
	ele.isLazy = function(){return $Z(id).$A("lazy")=="true";};
	ele.isScroll = function(){return $Z(id).$A("scroll")=="true";};
	
	var page = ele.isPageFlag();
	if(page&&ele.rows.length<=3){
		ele.RowHeight = 23;
	}else if(!page&&ele.rows.length==1){
		ele.RowHeight = 23;
	}else if(page&&ele.rows.length>1){
		ele.RowHeight = $ZE.getDimensions(ele.rows[1]).height;
	}else{
		ele.RowHeight = 23;
	}
	if(!ele.RowHeight){
		ele.RowHeight = 23;
	}

	DataGrid.autoFillBlank(ele);

	//初始化滚动
	if(ele.$A("scroll")=="true"){
		DataGrid.initScroll(ele);
	}
};

DataGrid.getParam = function(ele,name){
	ele = $Z(ele);
	return ele.Params.get(name);
};

DataGrid.setParam = function(ele,name,value){
	ele = $Z(ele);
	if(!ele.Params){
		ele.Params = new DataCollection();
	}
	ele.Params.add(name,value);
};

DataGrid.getColumnInfo = function(ele){
	ele = $Z(ele);
	var headTR = ele.rows[0];
	var arr = [];
	for(var i=0;i<headTR.cells.length;i++){
		var td = $Z(headTR.cells[i]);
		var ztype = td.$A("ztype");
		ztype = ztype?ztype.toLowerCase():null;
		if(ztype=="rowno"){
			continue;
		}
		if(ztype=="selector"){
			continue;
		}
		if(ztype=="edit"){
			continue;
		}
		if(td.innerText){//没有标题的列不要
			var w = isIE?td.currentStyle.width:td.offsetWidth;//$ZE.getDimensions()在单元格宽度为百分比值时有误
			w = w+"";
			if(w.indexOf("%")>0){
				w = $Z(ele).getDimensions().width*parseInt(w.replace(/\D/gi,''))/100;
			}else if(w.indexOf("px") != -1){
				w = w.replace("px", "");
			}else if(w == "auto"){
				w = 0;
			}
			arr.push([td.innerText,i,w]);
		}
	}
	return arr;
};

DataGrid.selectedRowToExcel = function(ele){
	ele = $Z(ele);
	var rows = DataGrid.getSelectedRows(ele);
	var arr = [];
	for(var i=0;i<rows.length;i++){
		arr.push(rows[i].rowIndex);
	}
	DataGrid.toExcel(ele,ele.getParam(Constant.Page)=="true",arr);
};

DataGrid.toExcel = function(ele,toExcelPageFlag,rows){
	ele = $Z(ele);
	var diag = new Dialog("选择要导出的列","Framework/Controls/DataGridToExcelDialog.html",500,150);
	diag.OKEvent = function(){
		var columns = $DW.$N("Column");
		var columnIndexes = [],columnWidths = [];
		for(var i=0;i<columns.length;i++){
			if(columns[i].checked){
				columnIndexes.push(columns[i].value);
				columnWidths.push($Z(columns[i]).$A("columnWidth"));
			}
		}
		$D.close();
		DataGrid.toExcelSubmit(ele,toExcelPageFlag,columnIndexes,columnWidths,rows);
	};
	diag.addParam("DataGridID",ele.id);
	diag.show();
};

DataGrid.allToExcel = function(ele,toExcelPageFlag){
	ele = $Z(ele);
	var columnIndexes = [],columnWidths = [];
	var arr = ele.getColumnInfo();
	for(var i=0;i<arr.length;i++){
		columnIndexes.push(arr[i][1]);
		columnWidths.push(arr[i][2]);
	}
	DataGrid.toExcelSubmit(ele,toExcelPageFlag,columnIndexes,columnWidths);
};

DataGrid.toExcelSubmit = function(ele,toExcelPageFlag,columnIndexes,columnWidths,rows){
	ele = $Z(ele);
	var xls = "_Excel_";
	var doc = window.document.body;
	var f = $Z(xls+"_Form");
	if(f){
		f.outerHTML = "";//要清空上次导出设置
	}
	f  = document.createElement("form");
	doc.appendChild(f);
	f.id = xls+"_Form";
	f.method="post";
	f.action = Server.ContextPath+"/include/controls/DataGridToExcel.jsp";
	var inputs = ele.Params.keys;
	for(var i = 0;i<inputs.length;i++){
		var input = $Z(xls+inputs[i]);
		if(!input){
			input  = document.createElement("input");
			
		}
		input.type="hidden";
		input.id =xls+inputs[i];
		input.name = xls+inputs[i];
		input.value=DataGrid.getParam(ele,inputs[i]);
		f.appendChild(input);
	}
	
	var input = $Z(xls+"_WIZ_ToExcelPageFlag");
	if(!input){
		input  = document.createElement("input");
		
	}
	input.type = "hidden";
	input.id =xls+"_WIZ_ToExcelPageFlag";
	input.name = xls+"_WIZ_ToExcelPageFlag";
	input.value= toExcelPageFlag? "1":"0";
	f.appendChild(input);

	input = $Z(xls+"_WIZ_Widths");
	if(!input){
		input  = document.createElement("input");
	}
	input.type="hidden";
	input.id =xls+"_WIZ_Widths";
	input.name = xls+"_WIZ_Widths";
	input.value = columnWidths.join();
	f.appendChild(input);
	
	input = $Z(xls+"_WIZ_Indexes");
	if(!input){
		input  = document.createElement("input");
	}
	input.type="hidden";
	input.id =xls+"_WIZ_Indexes";
	input.name = xls+"_WIZ_Indexes";
	input.value = columnIndexes.join();
	f.appendChild(input);
	
	if(rows){
		input = $Z(xls+"_WIZ_Rows");
		if(!input){
			input  = document.createElement("input");
		}
		input.type="hidden";
		input.id =xls+"_WIZ_Rows";
		input.name = xls+"_WIZ_Rows";
		input.value = rows.join();
	}
	f.appendChild(input);
	f.submit();
};

DataGrid.showLoading = function(ele){
	ele = $Z(ele);
	var bgdiv = $Z("_LoadingBGDiv");
	var icondiv = $Z("_LoadingIconDiv");
	if(!bgdiv){
		bgdiv = document.createElement("div");	
		bgdiv.id = "_LoadingBGDiv";
		$ZE.hide(bgdiv);
		bgdiv.style.cssText = "background-color:#333;position:absolute;z-index:800;opacity:0.01;filter:alpha(opacity=1);";
	 	document.body.appendChild(bgdiv);
	 	
		icondiv = document.createElement("div");	
		icondiv.id = "_LoadingIconDiv";
		$ZE.hide(icondiv);
		icondiv.innerHTML = "　<img src='"+Server.ContextPath+"/images/default/loadingGreen15px.gif'><font color=green> 正在加载......　</font>";
		icondiv.style.cssText = "padding-top:5px;background-color:#ffc;position:absolute;z-index:801;height:20px;width:120px";
	 	document.body.appendChild(icondiv);
	}
	var pos = ele.getPosition();
	var dim = ele.getDimensions();
	bgdiv.style.top = pos.y+"px";
	bgdiv.style.left = pos.x+"px";
	bgdiv.style.width = dim.width+"px";
	bgdiv.style.height = dim.height+"px";
	if(isGecko){
		icondiv.style.top = (pos.y)+"px";
		icondiv.style.left = (pos.x)+"px";
	}else{
		icondiv.style.top = (pos.y+2)+"px";
		icondiv.style.left = (pos.x+2)+"px";
	}
	$ZE.show(bgdiv);
	$ZE.show(icondiv);
};

DataGrid.closeLoading = function(){
	var bgdiv = $Z("_LoadingBGDiv");
	if(bgdiv){
		$ZE.hide(bgdiv);
		$Z("_LoadingIconDiv").hide();
	}
};

DataGrid.loadData = function(ele,func,dc){
	if(DataGrid.isLoading){
		return;
	}
	DataGrid.isLoading = true;
	try{
		ele = $Z(ele);
		var id  = ele.id;
		if(!DataGrid.getParam(ele,Constant.TagBody)){//尚未载入完全
			return;
		}
		if(!dc){
			dc = new DataCollection();
		}
		for(var i=0;i<ele.Params.size();i++){
			var key = ele.Params.getKey(i);
			dc.add(key,ele.Params.get(key));
		}
		DataGrid.showLoading(ele);
		Server.sendRequest("com.zving.framework.controls.DataGridPage.doWork",dc,function(response){
			var dg = ele.getParentByAttr("ztype","_DataGrid");
			if(dg){
				ele = dg;
			}
			if(!ele.parentNode){
				return;
			}
			if(!response.get("HTML")||response.get("HTML").length<10){
				return;
			}
			var newEle = null;
			var table = ele.tagName.toLowerCase()=="table"?ele:$Z(id);
			var afterEdit = table.afterEdit;
			var cancelEdit = table.cancelEdit;
			var beforeEdit = table.beforeEdit;
			var onContextMenu = table.onContextMenu;
			if(DataGrid.getParam(id,"_WIZ_SCROLL")=="true"){
				ele = ele.getParentByAttr("ztype","_DataGridWrapper");
				ele.outerHTML =  response.get("HTML");
				newEle = $Z(id);
			}else{
				newEle = document.createElement("div");
				newEle.setAttribute("ztype","_DataGrid");
				newEle.innerHTML = response.get("HTML");
				ele.parentNode.replaceChild(newEle,ele);
			}
			ele = null;
			if(isIE){
				execScript($Z(newEle).$T("script")[0].text);
			}
			eval("DataGrid_"+id+"_Init();");

			table = $Z(id);
			table.afterEdit = afterEdit;
			table.cancelEdit = cancelEdit;
			table.beforeEdit = beforeEdit;
			table.onContextMenu = onContextMenu;
			Effect.initChildren(table);
			if(func){
				try{
					func(response);
				}catch(ex){alert(ex.message);}
			}
			//setTimeout(DataGrid.closeLoading,200);
			table = null;
			newEle = null;
			ele = null;
			func = null;
		});
	}finally{
		DataGrid.isLoading = false;
	}
};

DataGrid.filter = function(ele,func){
	ele = $Z(ele);
	var ds = ele.DataSource;
	for(var i=0;i<ds.Rows.length;i++){
		try{
			if(func(ds.Rows[i])){
				$Z(ele.rows[i+1]).show();
			}else{
				$Z(ele.rows[i+1]).hide();
			}
		}catch(ex){alert(ex.message)}
	}
};

DataGrid.clear = function(ele){
	ele = $Z(ele);

	for(var i=ele.rows.length-2;i>0;i--){
		ele._deleteRow(i);
	}
	if(!ele.isPageFlag()){
		if(ele.rows.length>1){
			ele._deleteRow(1);
		}
	}else{
		if(ele.$A("scroll")=="true"){
			if(ele.rows.length>1){
				ele._deleteRow(1);
			}
		}
		if($Z(ele.id+"_PageBar")){
			$Z(ele.id+"_PageBar").innerHTML = "<div style='float:right;font-family:Tahoma'>第一页&nbsp;|&nbsp;上一页&nbsp;|&nbsp;下一页&nbsp;|&nbsp;最末页&nbsp;&nbsp;转到第&nbsp;<input id='_PageBar_Index' type='text' class='inputText' style='width:40px'>&nbsp;页&nbsp;&nbsp;<input type='button' class='inputButton' value='跳转'></div><div style='float:left;font-family:Tahoma'>共 0 条记录，每页 10 条，当前第 0 / 0 页</div>";	
		}
	}
	DataGrid.autoFillBlank(ele);
	ele.DataSource.Rows = [];
	ele.DataSource.Values = [];
};

DataGrid.autoFillBlank = function(ele){//自动用空白行撑开DataGrid
	ele = $Z(ele);
	if(ele.$A("autoFill")!="false"){
		var size = 15;
		if(ele.$A("page")=="true"){
			size = parseInt(ele.getParam(Constant.Size));
		}
		size = size-ele.DataSource.Rows.length;
		for(var i=ele.rows.length-1;i>0;i--){
			if(ele.rows[i].getAttribute("ztype")=="blank"){
				if(size<=0){
					$Z(ele.rows[i]).hide();
				}else{
					$Z(ele.rows[i]).show();
					ele.rows[i].style.height = ele.RowHeight*(size)+"px";
				}
				return;
			}
		}
		if(size<=0){
			return;
		}
		var colcount = ele.rows[0].cells.length;
		var row = ele._insertRow(ele.rows.length-1);
		for(var j=0;j<colcount;j++){
			var cell = row.insertCell(-1);				
			cell.innerHTML = "&nbsp;";
		}
		alert(ele.RowHeight);
		row.style.height = ele.RowHeight*(size)+"px";
		row.setAttribute("ztype","blank");
	}
};

DataGrid.firstPage = function(ele){
	ele = $Z(ele);
	DataGrid.setParam(ele,Constant.PageIndex,0);
	DataGrid.loadData(ele);
};

DataGrid.lastPage = function(ele){
	ele = $Z(ele);
	var total = DataGrid.getParam(ele,Constant.PageTotal);
	var size = DataGrid.getParam(ele,Constant.Size);
	var max = Math.ceil(parseInt(total)*1.0/parseInt(size));
	DataGrid.setParam(ele,Constant.PageIndex,max-1);
	DataGrid.loadData(ele);
};

DataGrid.previousPage = function(ele){
	ele = $Z(ele);
	var index = DataGrid.getParam(ele,Constant.PageIndex);
	DataGrid.setParam(ele,Constant.PageIndex,parseInt(index)-1);
	DataGrid.loadData(ele);
};

DataGrid.nextPage = function(ele){
	ele = $Z(ele);
	var index = DataGrid.getParam(ele,Constant.PageIndex);
	DataGrid.setParam(ele,Constant.PageIndex,parseInt(index)+1);
	DataGrid.loadData(ele);
};

DataGrid._onContextMenu = function(tr,evt){
	if(!tr.Selected){
		DataGrid.onRowClick(tr,evt);
	}
	evt = getEvent(evt);
	var dg = tr.parentNode.parentNode;
	Menu.close();
	if(dg.onContextMenu){
		dg.onContextMenu(tr,evt);
	}else{//默认菜单为复制文本、导出成Excel、导出全部成Excel
//		evt = getEvent(evt);
//		var id = dg.id;
//		var menu = new Menu();
//		menu.Width = 150;
//		menu.setEvent(evt);
//		var text = evt.srcElement.innerText;
//		menu.addItem("复制文本",function(){
//			Misc.copyToClipboard(text);
//		},"Framework/Icons/icon003a2.gif");
//		menu.addItem("导出选中行成Excel",function(){
//			DataGrid.selectedRowToExcel(id);
//		},"Icons/icon003a2.gif");
//		menu.addItem("导出本页成Excel",function(){
//			DataGrid.toExcel(id);
//		},"Icons/icon003a4.gif");
//		menu.addItem("导出全部成Excel",function(){
//			DataGrid.toExcel(id,true);
//		},"Icons/icon003a3.gif");
//		menu.show();
	}		
	stopEvent(evt);
};

DataGrid.treeClick = function(ele){
	var p = ele.parentNode;
	var tr,table;
	try{
		while(p){
			var tagName = p.tagName.toLowerCase();
			if(tagName=="tr"){
				tr = p;
			}
			if(tagName=="table"){
				table = p;
				break;
			}
			p = p.parentNode;
		}
	}catch(ex){alert(ex.message);}

	var hideFlag = false;
	if(ele.src.indexOf("butExpand")>0){
		ele.src = ""+Server.ContextPath+"Framework/Images/butCollapse.gif";
		hideFlag = true;
	}else{
		ele.src = ""+Server.ContextPath+"Framework/Images/butExpand.gif";
	}
	var rows = table.rows;
	var level = parseInt(tr.getAttribute("level"));
	for(var i=0;i<rows.length;i++){
		if(rows[i]===tr){
			for(var j=i+1;j<rows.length;j++){
				var r = $Z(rows[j]);
				if(parseInt(r.$A("level"))>level){
					if(hideFlag){
						if($ZE.visible(r)){
							r.setAttribute("_TreeHideLevel",level);
							$ZE.hide(r);
						}
					}else{
						if(r.$A("_TreeHideLevel")==level){
							$ZE.show(r);
							r.setAttribute("_TreeHideLevel",null);
						}
					}
				}else{
					break;
				}
			}
			break;
		}
	}
};

DataGrid.getSelectors = function(ele){
	ele = $Z(ele);
	return $N(ele.id+"_RowCheck");
};

DataGrid.deleteRow = function(ele,index){
	ele = $Z(ele);
	var rowNo;
	if(index<1){
		index = 1;
	}
	if(ele.isPageFlag()){
		if(ele.rows.length<=3||index>=ele.rows.length-2){
			return;
		}
	}
	if(!ele.isPageFlag()){
		if(ele.rows.length<=2||index>=ele.rows.length-1){
			return;
		}
	}
	if(index==1){
		var cellIndex = null;
		var tr = ele.rows[0];
		for(var i=0;i<tr.cells.length;i++){
			var ztype = tr.cells[i].getAttribute("ztype");
			if(ztype&&ztype.toLowerCase()=="rowno"){
				cellIndex = i;
				break;
			}
		}
		rowNo = ele.rows[1].cells[cellIndex].innerText;
	}
	ele._deleteRow(index);
	ele.DataSource.deleteRow(index-1);
	DataGrid.resetRowEx(ele,rowNo);
	DataGrid.autoFillBlank(ele);
};

DataGrid.insertRow = function(ele,index,editFlag,func){
	if(DataGrid.EditingRow!=null){
		if(!DataGrid.changeStatus(DataGrid.EditingRow)){
			return;
		}
	}
	if(DataGrid.isLoading){
		return;
	}
	DataGrid.isLoading = true;
	try{
		ele = $Z(ele);
		if((!(index===0)&&!index)||index>ele.DataSource.Rows.length){
			index = ele.DataSource.Rows.length;
		}
		
		var dt = new DataTable();
		dt.Columns = ele.DataSource.Columns;
		if(ele.DataSource.Rows.length==0){
			dt.Rows = [];
			dt.Values = [];
			dt.insertRow(0,new Array(dt.Columns.length));
			for(var i=0;i<dt.getColCount();i++){
				dt.Rows[0].set2(i,"");
			}
		}else{
			dt.Rows = [ele.DataSource.Rows[0]];
			dt.Values = [ele.DataSource.Values[0]];
			for(var i=0;i<dt.getColCount();i++){
				dt.Rows[0].set2(i,"");
			}
		}
		var dc = new DataCollection();
		for(var i=0;i<ele.Params.size();i++){
			var key = ele.Params.getKey(i);
			dc.add(key,ele.Params.get(key));
		}
		dc.add(Constant.DataTable,dt);
		dc.add(Constant.InsertRow,1);
	
		DataGrid.showLoading(ele);
		Server.sendRequest("com.zving.framework.controls.DataGridPage.doWork",dc,function(response){
			var tr = ele._insertRow(index+1);
			var TRAttr = response.get("TRAttr");
			DataGrid.setAttr(tr,TRAttr);
			for(var i=0;i<ele.rows[0].cells.length;i++){
				var td = tr.insertCell(-1);
				var TDAttr = response.get("TDAttr"+i);
				DataGrid.setAttr(td,TDAttr);
				td.innerHTML = response.get("TDHtml"+i);
			}
			ele.DataSource.insertRow(index,new Array(ele.DataSource.getColCount()));
			for(var i=0;i<ele.DataSource.getColCount();i++){
				ele.DataSource.Rows[index].set2(i,"");
			}
			DataGrid.resetRowEx(ele);

			if(editFlag){
				DataGrid.changeStatus($Z(id).rows[index+1]);
			}else{
				Effect.initChildren(tr);
			}
			if(func){
				try{
					func(response);
				}catch(ex){alert(ex.message);}
			}
			DataGrid.autoFillBlank(ele);
			setTimeout(DataGrid.closeLoading,200);
		});
	}finally{
		DataGrid.isLoading = false;
	}
};

DataGrid.resetRowEx = function(ele,rowNo){//在增、删行后重设行号和选择器顺序
		ele = $Z(ele);
		var cellIndex = null;
		var tr = ele.rows[0];
		for(var i=0;i<tr.cells.length;i++){
			var ztype = tr.cells[i].getAttribute("ztype");
			if(ztype&&ztype.toLowerCase()=="rowno"){
				cellIndex = i;
				break;
			}
		}
		if(cellIndex!=null){
			if(!rowNo){
				rowNo = ele.rows[1].cells[cellIndex].innerText;
				if(!rowNo){
					rowNo = ele.rows[2].cells[cellIndex].innerText;
				}
			}
			rowNo = parseInt(rowNo);
			var len = ele.DataSource.Rows.length;
			for(var i=1;i<=len;i++){
				ele.rows[i].cells[cellIndex].innerText = ""+rowNo++;
			}
		}
		var selectors = ele.getSelectors();//需要重新设置选择器的ID
		if(selectors){
			for(var i=0;i<selectors.length;i++){
				selectors[i].id = ele.id+"_RowCheck"+(i+1);
			}
		}
};

DataGrid.setAttr = function(ele,attr){
	for (var p in attr){					
		if(attr.hasOwnProperty(p) && p!='prototype'){
			p = p.toLowerCase();
			var v = attr[p];
			if(p=="style"){
				ele.style.cssText = v;
				continue;
			}
			if(p.startsWith("on")){
				eval("ele."+p+" = function(event){eval(\""+v+"\")}");
				continue;
			}
			ele[p] = v;
		}
	};
};

DataGrid.changeStatus = function(ele,type){//改变编辑状态
	ele = $Z(ele);
	var dg = ele.parentNode.parentNode;
	var ds = dg.DataSource;
	var ri = ele.rowIndex-1;
	if(DataGrid.EditingRow&&ele!=DataGrid.EditingRow){
		DataGrid.changeStatus(DataGrid.EditingRow);
	}
	var editTR,templateTR,headTR;
	try{
		for(var i=0;i<dg.rows.length;i++){
			var row = dg.rows[i];
			if(row.getAttribute("ztype")=="edit"){
				editTR = row;
			}
			if(row.getAttribute("ztype")=="template"){
				templateTR = row;
			}
			if(row.getAttribute("ztype")=="head"){
				headTR = row;
			}
		}
		if(!headTR){
			headTR = table.rows[0];
		}
	}catch(ex){alert(ex.message);}
	var dr = new DataRow(ds,ri);
	if(!dg.OldValues){
		dg.OldValues = [];
	}
	if(!ele.EditStatus){
		ele.EditStatus = true;
		DataGrid.EditingRow = ele;
	}else{
		ele.EditStatus = false;
		if(!dg.OldValues[ri]){
			var values = ds.Values[ri].clone();
			dg.OldValues[ri] = values;
		}
		if(type!="Cancel"&&dg.afterEdit){
			if(!dg.afterEdit(ele,dr)){
				return false;
			}
		}
		DataGrid.EditingRow = null;
	}
	var v1 = dg.OldValues[ri];
	for(var i=0;i<headTR.cells.length;i++){
		var cell = headTR.cells[i];
		var ztype = cell.getAttribute("ztype");
		var field = cell.getAttribute("field");
		var modifyFlag = false;
		if(ztype&&ztype.toLowerCase()=="selector"){
			$Z(dg.id+"_RowCheck"+ele.rowIndex).disabled = ele.EditStatus?true:false;
		}else if(ztype&&ztype.toLowerCase()=="rowno"){
			ele.cells[i].innerHTML = ele.cells[i].getAttribute("rowno");
		}else if(ztype&&ztype.toLowerCase()=="checkbox"){
			var checkedvalue = ele.cells[i].getAttribute("checkedvalue");
			if (checkedvalue == null) {
				checkedvalue = "Y";
			}
			if(v1){
				modifyFlag = v1[ds.ColMap[field.toLowerCase()]]!=dr.get(field);
			}
			var checked = checkedvalue==dr.get(field) ? "checked" : "";
			$Z(dg.id+"_"+field+"_Checkbox"+ele.rowIndex).disabled = ele.EditStatus?false:true;
		}else if(ztype&&ztype.toLowerCase()=="dropdownlist"){
			if(v1){
				modifyFlag = v1[ds.ColMap[field.toLowerCase()]]!=dr.get(field);
			}
			$Z(dg.id+"_"+field+"_DropDownList"+ele.rowIndex).disabled = ele.EditStatus?false:true;
		}else{
			var arr = [];
			if(ztype&&ztype.toLowerCase()=="tree"){
				var level = parseInt(ele.getAttribute("level"));
				var nextLevel = 0;
				var nextVisiable = true;
				if(ele.rowIndex!=dg.rows.length-1){
					nextLevel = parseInt(dg.rows[ele.rowIndex+1].getAttribute("level"));
					nextVisiable = dg.rows[ele.rowIndex+1].style.display!="none";
				}
				for (var k = 0; k < level; k++) {
					arr.push("<q style='padding:0 10px'></q>");
				}
				if (level < nextLevel) {
					if(nextVisiable){
						arr.push("<img src='"+Server.ContextPath+"/images/butExpand.gif' onclick='DataGrid.treeClick(this)'/>&nbsp;");
					}else{
						arr.push("<img src='"+Server.ContextPath+"/images/butCollapse.gif' onclick='DataGrid.treeClick(this)'/>&nbsp;");
					}
				} else {
					arr.push("<img src='"+Server.ContextPath+"/images/butNoChild.gif'/>&nbsp;");
				}
			}
			var html = unescape(ele.EditStatus?dg.EditArray[i]:dg.TemplateArray[i]);
			var reg = /\$\{(\w+?)\}/gi;
			var last = 0;
			arr.push(html.replace(reg,function($0,$1){
				if(v1){
					if(v1[ds.ColMap[$1.toLowerCase()]]!=dr.get($1)){
						modifyFlag = true;
					}
				}
				var v2 = dr.get($1);
				
				if((v2===""||v2===null)&&!ele.EditStatus){
					v2 = "&nbsp;"
				}
				return v2;
			}));
			ele.cells[i].innerHTML = arr.join('');
		}
		if(modifyFlag){
			ele.cells[i].style.backgroundColor = "#FEB34E";
		}else{
			ele.cells[i].style.backgroundColor = "";
		}
		if(!ele.ModifyFlag&&modifyFlag){
			ele.ModifyFlag = true;
		}
	}

	var arr = ele.$T("div");
	var len = arr.length;
	for(var i=len;i>0;i--){
		var selectEle = $Z(arr[i-1]);
		var type = selectEle.$A("ztype");
		if(type&&type.toLowerCase()=="select"){
			Selector.initCtrl(selectEle);
		}
	}
	
	//编辑时允许选择,不编辑时禁止选择
	if(isIE){
		ele.onselectstart = ele.EditStatus?null:stopEvent;
	}else{
		for(var j=0;j<ele.cells.length;j++){
			ele.cells[j].style.MozUserSelect = ele.EditStatus?'':'none';
		}
	}
	//编辑时单击本行以外的其他行取消编辑状态
	if(ele.EditStatus){
		ele.oldClickEvent = ele.onclick;
		ele.onclick = function(evt){
			evt = getEvent(evt);
			cancelEvent(evt);
		};
	}else{
		ele.onclick = ele.oldClickEvent;
	}

	var inputs = ele.$T("input");
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].type=="text"&&(inputs[i].value==unescape("%A0")||inputs[i].value==" ")){
			inputs[i].value = "";			
		}
		inputs[i].ondblclick = stopEvent;
	}
	Effect.initChildren(ele);
	if(!ele.EditStatus&&dg.cancelEdit){
		dg.cancelEdit(ele,dr);
	}
	if(ele.EditStatus&&dg.beforeEdit){
			dg.beforeEdit(ele,dr);
	}
	return true;
};

Page.onClick(function(){
	if(DataGrid.EditingRow!=null){
		DataGrid.changeStatus(DataGrid.EditingRow);
	}
});

DataGrid.edit = function(event,ele){//供DataGrid之外的按钮调用
	ele = $Z(ele);
	if(!ele){
		alert("DataGrid.edit的参数必须是一个DataGrid对象");
	}
	var rs = DataGrid.getSelectedRows(ele);
	if(rs.length<1){
		Dialog.alert("请先选择一条记录!");
		return;
	}
	var row = rs[0];
	row.ondblclick.apply(row,[]);
	stopEvent(event);
};

DataGrid.editRow = function(row,func){
	if(func){
		var dg = row.parentNode.parentNode;
		var ds = dg.DataSource;
		var ri = row.rowIndex-1;
	  var dr = new DataRow(ds,ri);
	  func(dr)
	}else{
		DataGrid.changeStatus(row);
	}
};

DataGrid.cancel = function(ele){
	var row = $ZE.getParent("tr",ele);
	DataGrid.changeStatus(row,"Cancel");
};

DataGrid.save = function(strID,method,func){
	if(DataGrid.EditingRow!=null){
		if(!DataGrid.changeStatus(DataGrid.EditingRow)){
			return;
		}
	}
	var dg = $Z(strID);
	var ds =  dg.DataSource;
	var values = [];
	for(var i=1;i<dg.rows.length;i++){
		if(dg.rows[i].ModifyFlag){
			values.push(ds.Values[i-1]);
		}
	}
	if(values.length==0){
		Dialog.alert("数据未被修改过!");
		return;
	}
	var dt = new DataTable();
	dt.Columns = ds.Columns;
	dt.Values = values;
	var dc = new DataCollection();
	dc.add("DT",dt,"DataTable");
	Server.sendRequest(method,dc,function(response){
		if(response&&response.Status==0){
			Dialog.alert(response.Message,func);
		}else{
			Dialog.alert("修改成功!",function(){
				for(var i=1;i<dg.rows.length;i++){
					if(dg.rows[i].ModifyFlag){
						dg.rows[i].ModifyFlag = false;
						for(var j=0;j<dg.rows[i].cells.length;j++){
							dg.rows[i].cells[j].style.backgroundColor =	"";
						}
					}
				}
				dg.OldValues = [];
				if(func){
					func();
				}
			});
		}
	});
};

DataGrid.discard = function(ele,func){
	DataGrid.loadData(ele,func);
};

DataGrid.getRowDragProxy = function(row){
	var table = row.parentNode.parentNode;
	var arr = [];
	var dim = $ZE.getDimensions(table);
	var tableStart = table.outerHTML.split(">")[0];
	tableStart = tableStart.replace(/width\=[\'\"].*?[\"\']/gi,"width='"+dim.width+"'");
	tableStart = tableStart.replace(/align\=[\'\"].*?[\"\']/gi,"");
	arr.push(tableStart);
	arr.push(row.outerHTML.split(">")[0]+" style='background-color:"+DataGrid.SelectedBgColor+"' >");
	for(var i=0;i<row.cells.length;i++){
		arr.push(row.cells[i].outerHTML.split(">")[0]);
		arr.push(" width='"+table.rows[0].cells[i].width+"' style='background-color:"+DataGrid.SelectedBgColor+"'");
		arr.push(">");
		arr.push(row.cells[i].innerHTML);
		arr.push("</td>");
	}
	arr.push("</tr></table>");
	return arr.join('');
};

DataGrid.moveRow = function(row,index){
	var table = row.parentNode.parentNode;
	if(isIE){
		table.moveRow(row.rowIndex,index);
	}else{
		var html = row.outerHTML;
		table._deleteRow(row.rowIndex);
		var newRow = table._insertRow(index);
		newRow.outerHTML = html;
	}
};

DataGrid.dragStart = function(evt){
	var row = this.parentNode;
	var table = row.parentNode.parentNode;
	var rows = table.rows;
	DataGrid.onRowClick(row,evt);
	DragManager.doDrag(evt,DataGrid.getRowDragProxy(row));
};

DataGrid.dragEnd = function(evt){
	var row = $Z(this);
	if(DragManager.DragSource.tagName!="TD"){
		return;
	}
	var rowSource = $Z(DragManager.DragSource.parentNode);
	if(row.getParent("table")!=rowSource.getParent("table")){//不接受其他拖拽
		return;
	}
	var table = row.parentNode.parentNode;
	var si = rowSource.rowIndex;
	var ni = row.rowIndex;
	if(ni>table.DataSource.Rows.length){
		ni = table.DataSource.Rows.length;
	}
	DataGrid.moveRow(rowSource,ni);
	
	var ds = table.DataSource;
	var vs = ds.Values;
	var arr = vs[si-1];
	vs.splice(si-1,1);
	vs.insert(ni-1,arr);	
	for(var i=0;i<vs.length;i++){
		ds.Rows[i] = new DataRow(ds,i);
	}	
	
	var arr = table.rows;	
	for(var i=1;i<arr.length;i++){
		$Z(arr[i]).$T("input").each(function(ele){
			if(ele.id&&ele.id.toString().indexOf("_RowCheck")>0){
				ele.id = table.id+"_RowCheck"+i;//顺序变化后必须改变ID
			}
		});
	}
	row = table.rows[ni];
	DataGrid.onRowClick(row,evt);
	row.DefaultBgColor = "#fff";
	
	var afterDrag = table.getAttribute("afterDrag");
	if(ni!=si&&afterDrag){
		var func = eval("window."+afterDrag);
		var type = "After";
		var targetDr;
		if(ni==1){
			type = "Before";
			targetDr = ds.Rows[1];
		}else{
			targetDr = ds.Rows[ni-2];	
		}		
		func.apply(row,[type,targetDr,ds.Rows[ni-1],ni,si]);
	}
};

DataGrid.dragOver = function(evt,ele){
	if(isGecko){
		return;//Gecko下暂未处理好鼠标移动太快时的情况
	}
	var row = ele||this;
	if(DragManager.DragSource.tagName!="TD"){
		return;
	}
	for(var i=0;i<row.cells.length;i++){
		var cell = row.cells[i];
		var style = isGecko?document.defaultView.getComputedStyle(cell, null):cell.currentStyle;
		if(row.rowIndex>DragManager.DragSource.parentNode.rowIndex){
			if(!cell.borderBottomStyle){
				cell.borderBottomStyle = style.borderBottomStyle;
				cell.borderBottomColor = style.borderBottomColor;
				cell.borderBottomWidth = style.borderBottomWidth;
				cell.style.borderBottom = "dashed 2px #f90";
			}
		}else{
			if(!cell.borderTopStyle){
				cell.borderTopStyle = style.borderTopStyle;
				cell.borderTopColor = style.borderTopColor;
				cell.borderTopWidth = style.borderTopWidth;
				cell.style.borderTop = "dashed 2px #f90";
			}
		}
	}
};

DataGrid.dragOut = function(evt){
	if(isGecko){
		return;//Gecko下暂未处理好鼠标移动太快时的情况
	}
	var row = this;
	if(DragManager.DragSource.tagName!="TD"){
		return;
	}
	for(var i=0;i<row.cells.length;i++){
		var cell = row.cells[i];
		if(row.rowIndex>DragManager.DragSource.parentNode.rowIndex){
			if(cell.borderBottomStyle){//拖出当前行时没有值
				cell.style.borderBottomStyle = cell.borderBottomStyle;
				cell.style.borderBottomColor = cell.borderBottomColor;
				cell.style.borderBottomWidth = cell.borderBottomWidth;
				cell.borderBottomStyle = "";
			}
		}else{
			if(cell.borderTopStyle){
				cell.style.borderTopStyle = cell.borderTopStyle;
				cell.style.borderTopColor = cell.borderTopColor;
				cell.style.borderTopWidth = cell.borderTopWidth;
				cell.borderTopStyle = "";
			}
		}
	}
};

DataGrid.onSortHeadMouseOver = function(ele){
	ele.className = 'thOver';
};

DataGrid.onSortHeadMouseOut = function(ele){
	ele.className = '';
};

DataGrid.setFixedHeight = function(ele,h){
	if(isNull(h)){
		return;
	}
	if(!/\D/.test(""+h)){
		h = h+"px";
	}
	ele = $Z(ele);
	if(ele.$A("scroll")!="true"){
		return;
	}
	$Z(ele.id+"_Wrap_body").style.height = h;
	var tagBody = ele.getParam(Constant.TagBody).replace(/fixedheight\s*=\s*(\&.*?;).*?\1/gi,"fixedheight=$1"+h+"$1");
	ele.setParam(Constant.TagBody,tagBody);
	ele.setAttribute("fixedHeight",h);
};

DataGrid.setFixedWidth = function(ele,w){
	if(isNull(w)){
		return;
	}
	if(!/\D/.test(""+w)){
		w = w+"px";
	}
	ele = $Z(ele);
	if(ele.$A("scroll")!="true"){
		return;
	}
	$Z(ele.id+"_Wrap").style.width = w;
	var tagBody = ele.getParam(Constant.TagBody).replace(/fixedwidth\s*=\s*(\&.*?;).*?\1/gi,"fixedwidth=$1"+w+"$1");
	ele.setParam(Constant.TagBody,tagBody);
	ele.setAttribute("fixedWidth",w);
	DataGrid.resetWidth(ele);
};

DataGrid.initScroll = function(ele){
	ele = $Z(ele);
	var id = ele.id;
	ele.Wrapper = $Z(id+"_Wrap");
	ele.Wrapper.head = $Z(id+"_Wrap_head");
	ele.Wrapper.head.table = ele.Wrapper.head.children[0];
	ele.Wrapper.body = $Z(id+"_Wrap_body");
	ele.Wrapper.body.table = ele.Wrapper.body.children[0];
	ele.Wrapper.onfocus = function(){
		this.addClassName("dt_focus")
	};
	
	ele.Wrapper.onblur = function(){
		this.removeClassName("dt_focus")
	};

	ele.Wrapper.body.onscroll = function(){
		var t  = parseInt(this.scrollLeft*this.parentNode.head.table.scrollWidth/this.parentNode.head.table.scrollWidth);
		if(this.parentNode.head.scrollLeft!=t){
			this.parentNode.head.scrollLeft = t;
		}
	};
		
	var cells = ele.Wrapper.head.table.rows[0].cells;
	if(isIE){
		cells[cells.length-1].style.paddingRight="23px";
	}
	
};

DataGrid.resetWidth = function(ele){
	ele = $Z(ele);
	if(ele.Wrapper.scrollWidth>ele.Wrapper.body.table.offsetWidth){
		var bodyTHs = ele.Wrapper.body.table.rows[0].cells;headTHs = ele.Wrapper.head.table.rows[0].cells;
		var tableWidth = ele.Wrapper.body.table.width;
		if(!tableWidth){
			var tableWidth=0;
			for(var j=0,len=bodyTHs.length;j<len;j++){
				tableWidth += bodyTHs[j].width;
			};
		}
		for(var i=0,len=bodyTHs.length;i<len;i++){
			bodyTHs[i].width = Math.round(bodyTHs[i].width*(this.element.scrollWidth-13*len-23)/(tableWidth));
			headTHs[i].width = bodyTHs[i].width;
			if(isIE&&i==len-1){
				headTHs[i].style.paddingRight="24px";
			}
		}
	}	
};