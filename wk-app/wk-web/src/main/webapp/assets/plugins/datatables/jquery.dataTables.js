(function(window,document,undefined){(function(factory){"use strict";if(typeof define==='function'&&define.amd){define('datatables',['jquery'],factory);}
else if(typeof exports==='object'){factory(require('jquery'));}
else if(jQuery&&!jQuery.fn.dataTable){factory(jQuery);}}
(function($){"use strict";var DataTable;var _ext;var _Api;var _api_register;var _api_registerPlural;var _re_dic={};var _re_new_lines=/[\r\n]/g;var _re_html=/<.*?>/g;var _re_date_start=/^[\w\+\-]/;var _re_date_end=/[\w\+\-]$/;var _re_escape_regex=new RegExp('(\\'+['/','.','*','+','?','|','(',')','[',']','{','}','\\','$','^','-'].join('|\\')+')','g');var _re_formatted_numeric=/[',$£€¥%\u2009\u202F]/g;var _empty=function(d){return!d||d===true||d==='-'?true:false;};var _intVal=function(s){var integer=parseInt(s,10);return!isNaN(integer)&&isFinite(s)?integer:null;};var _numToDecimal=function(num,decimalPoint){if(!_re_dic[decimalPoint]){_re_dic[decimalPoint]=new RegExp(_fnEscapeRegex(decimalPoint),'g');}
return typeof num==='string'&&decimalPoint!=='.'?num.replace(/\./g,'').replace(_re_dic[decimalPoint],'.'):num;};var _isNumber=function(d,decimalPoint,formatted){var strType=typeof d==='string';if(decimalPoint&&strType){d=_numToDecimal(d,decimalPoint);}
if(formatted&&strType){d=d.replace(_re_formatted_numeric,'');}
return _empty(d)||(!isNaN(parseFloat(d))&&isFinite(d));};var _isHtml=function(d){return _empty(d)||typeof d==='string';};var _htmlNumeric=function(d,decimalPoint,formatted){if(_empty(d)){return true;}
var html=_isHtml(d);return!html?null:_isNumber(_stripHtml(d),decimalPoint,formatted)?true:null;};var _pluck=function(a,prop,prop2){var out=[];var i=0,ien=a.length;if(prop2!==undefined){for(;i<ien;i++){if(a[i]&&a[i][prop]){out.push(a[i][prop][prop2]);}}}
else{for(;i<ien;i++){if(a[i]){out.push(a[i][prop]);}}}
return out;};var _pluck_order=function(a,order,prop,prop2)
{var out=[];var i=0,ien=order.length;if(prop2!==undefined){for(;i<ien;i++){out.push(a[order[i]][prop][prop2]);}}
else{for(;i<ien;i++){out.push(a[order[i]][prop]);}}
return out;};var _range=function(len,start)
{var out=[];var end;if(start===undefined){start=0;end=len;}
else{end=start;start=len;}
for(var i=start;i<end;i++){out.push(i);}
return out;};var _stripHtml=function(d){return d.replace(_re_html,'');};var _unique=function(src)
{var
out=[],val,i,ien=src.length,j,k=0;again:for(i=0;i<ien;i++){val=src[i];for(j=0;j<k;j++){if(out[j]===val){continue again;}}
out.push(val);k++;}
return out;};function _fnHungarianMap(o)
{var
hungarian='a aa ai ao as b fn i m o s ',match,newKey,map={};$.each(o,function(key,val){match=key.match(/^([^A-Z]+?)([A-Z])/);if(match&&hungarian.indexOf(match[1]+' ')!==-1)
{newKey=key.replace(match[0],match[2].toLowerCase());map[newKey]=key;if(match[1]==='o')
{_fnHungarianMap(o[key]);}}});o._hungarianMap=map;}
function _fnCamelToHungarian(src,user,force)
{if(!src._hungarianMap){_fnHungarianMap(src);}
var hungarianKey;$.each(user,function(key,val){hungarianKey=src._hungarianMap[key];if(hungarianKey!==undefined&&(force||user[hungarianKey]===undefined))
{if(hungarianKey.charAt(0)==='o')
{if(!user[hungarianKey]){user[hungarianKey]={};}
$.extend(true,user[hungarianKey],user[key]);_fnCamelToHungarian(src[hungarianKey],user[hungarianKey],force);}
else{user[hungarianKey]=user[key];}}});}
function _fnLanguageCompat(lang)
{var defaults=DataTable.defaults.oLanguage;var zeroRecords=lang.sZeroRecords;if(!lang.sEmptyTable&&zeroRecords&&defaults.sEmptyTable==="No data available in table")
{_fnMap(lang,lang,'sZeroRecords','sEmptyTable');}
if(!lang.sLoadingRecords&&zeroRecords&&defaults.sLoadingRecords==="Loading...")
{_fnMap(lang,lang,'sZeroRecords','sLoadingRecords');}
if(lang.sInfoThousands){lang.sThousands=lang.sInfoThousands;}
var decimal=lang.sDecimal;if(decimal){_addNumericSort(decimal);}}
var _fnCompatMap=function(o,knew,old){if(o[knew]!==undefined){o[old]=o[knew];}};function _fnCompatOpts(init)
{_fnCompatMap(init,'ordering','bSort');_fnCompatMap(init,'orderMulti','bSortMulti');_fnCompatMap(init,'orderClasses','bSortClasses');_fnCompatMap(init,'orderCellsTop','bSortCellsTop');_fnCompatMap(init,'order','aaSorting');_fnCompatMap(init,'orderFixed','aaSortingFixed');_fnCompatMap(init,'paging','bPaginate');_fnCompatMap(init,'pagingType','sPaginationType');_fnCompatMap(init,'pageLength','iDisplayLength');_fnCompatMap(init,'searching','bFilter');var searchCols=init.aoSearchCols;if(searchCols){for(var i=0,ien=searchCols.length;i<ien;i++){if(searchCols[i]){_fnCamelToHungarian(DataTable.models.oSearch,searchCols[i]);}}}}
function _fnCompatCols(init)
{_fnCompatMap(init,'orderable','bSortable');_fnCompatMap(init,'orderData','aDataSort');_fnCompatMap(init,'orderSequence','asSorting');_fnCompatMap(init,'orderDataType','sortDataType');}
function _fnBrowserDetect(settings)
{var browser=settings.oBrowser;var n=$('<div/>').css({position:'absolute',top:0,left:0,height:1,width:1,overflow:'hidden'}).append($('<div/>').css({position:'absolute',top:1,left:1,width:100,overflow:'scroll'}).append($('<div class="test"/>').css({width:'100%',height:10}))).appendTo('body');var test=n.find('.test');browser.bScrollOversize=test[0].offsetWidth===100;browser.bScrollbarLeft=test.offset().left!==1;n.remove();}
function _fnReduce(that,fn,init,start,end,inc)
{var
i=start,value,isSet=false;if(init!==undefined){value=init;isSet=true;}
while(i!==end){if(!that.hasOwnProperty(i)){continue;}
value=isSet?fn(value,that[i],i,that):that[i];isSet=true;i+=inc;}
return value;}
function _fnAddColumn(oSettings,nTh)
{var oDefaults=DataTable.defaults.column;var iCol=oSettings.aoColumns.length;var oCol=$.extend({},DataTable.models.oColumn,oDefaults,{"nTh":nTh?nTh:document.createElement('th'),"sTitle":oDefaults.sTitle?oDefaults.sTitle:nTh?nTh.innerHTML:'',"aDataSort":oDefaults.aDataSort?oDefaults.aDataSort:[iCol],"mData":oDefaults.mData?oDefaults.mData:iCol,idx:iCol});oSettings.aoColumns.push(oCol);var searchCols=oSettings.aoPreSearchCols;searchCols[iCol]=$.extend({},DataTable.models.oSearch,searchCols[iCol]);_fnColumnOptions(oSettings,iCol,null);}
function _fnColumnOptions(oSettings,iCol,oOptions)
{var oCol=oSettings.aoColumns[iCol];var oClasses=oSettings.oClasses;var th=$(oCol.nTh);if(!oCol.sWidthOrig){oCol.sWidthOrig=th.attr('width')||null;var t=(th.attr('style')||'').match(/width:\s*(\d+[pxem%]+)/);if(t){oCol.sWidthOrig=t[1];}}
if(oOptions!==undefined&&oOptions!==null)
{_fnCompatCols(oOptions);_fnCamelToHungarian(DataTable.defaults.column,oOptions);if(oOptions.mDataProp!==undefined&&!oOptions.mData)
{oOptions.mData=oOptions.mDataProp;}
if(oOptions.sType)
{oCol._sManualType=oOptions.sType;}
if(oOptions.className&&!oOptions.sClass)
{oOptions.sClass=oOptions.className;}
$.extend(oCol,oOptions);_fnMap(oCol,oOptions,"sWidth","sWidthOrig");if(typeof oOptions.iDataSort==='number')
{oCol.aDataSort=[oOptions.iDataSort];}
_fnMap(oCol,oOptions,"aDataSort");}
var mDataSrc=oCol.mData;var mData=_fnGetObjectDataFn(mDataSrc);var mRender=oCol.mRender?_fnGetObjectDataFn(oCol.mRender):null;var attrTest=function(src){return typeof src==='string'&&src.indexOf('@')!==-1;};oCol._bAttrSrc=$.isPlainObject(mDataSrc)&&(attrTest(mDataSrc.sort)||attrTest(mDataSrc.type)||attrTest(mDataSrc.filter));oCol.fnGetData=function(rowData,type,meta){var innerData=mData(rowData,type,undefined,meta);return mRender&&type?mRender(innerData,type,rowData,meta):innerData;};oCol.fnSetData=function(rowData,val,meta){return _fnSetObjectDataFn(mDataSrc)(rowData,val,meta);};if(typeof mDataSrc!=='number'){oSettings._rowReadObject=true;}
if(!oSettings.oFeatures.bSort)
{oCol.bSortable=false;th.addClass(oClasses.sSortableNone);}
var bAsc=$.inArray('asc',oCol.asSorting)!==-1;var bDesc=$.inArray('desc',oCol.asSorting)!==-1;if(!oCol.bSortable||(!bAsc&&!bDesc))
{oCol.sSortingClass=oClasses.sSortableNone;oCol.sSortingClassJUI="";}
else if(bAsc&&!bDesc)
{oCol.sSortingClass=oClasses.sSortableAsc;oCol.sSortingClassJUI=oClasses.sSortJUIAscAllowed;}
else if(!bAsc&&bDesc)
{oCol.sSortingClass=oClasses.sSortableDesc;oCol.sSortingClassJUI=oClasses.sSortJUIDescAllowed;}
else
{oCol.sSortingClass=oClasses.sSortable;oCol.sSortingClassJUI=oClasses.sSortJUI;}}
function _fnAdjustColumnSizing(settings)
{if(settings.oFeatures.bAutoWidth!==false)
{var columns=settings.aoColumns;_fnCalculateColumnWidths(settings);for(var i=0,iLen=columns.length;i<iLen;i++)
{columns[i].nTh.style.width=columns[i].sWidth;}}
var scroll=settings.oScroll;if(scroll.sY!==''||scroll.sX!=='')
{_fnScrollDraw(settings);}
_fnCallbackFire(settings,null,'column-sizing',[settings]);}
function _fnVisibleToColumnIndex(oSettings,iMatch)
{var aiVis=_fnGetColumns(oSettings,'bVisible');return typeof aiVis[iMatch]==='number'?aiVis[iMatch]:null;}
function _fnColumnIndexToVisible(oSettings,iMatch)
{var aiVis=_fnGetColumns(oSettings,'bVisible');var iPos=$.inArray(iMatch,aiVis);return iPos!==-1?iPos:null;}
function _fnVisbleColumns(oSettings)
{return _fnGetColumns(oSettings,'bVisible').length;}
function _fnGetColumns(oSettings,sParam)
{var a=[];$.map(oSettings.aoColumns,function(val,i){if(val[sParam]){a.push(i);}});return a;}
function _fnColumnTypes(settings)
{var columns=settings.aoColumns;var data=settings.aoData;var types=DataTable.ext.type.detect;var i,ien,j,jen,k,ken;var col,cell,detectedType,cache;for(i=0,ien=columns.length;i<ien;i++){col=columns[i];cache=[];if(!col.sType&&col._sManualType){col.sType=col._sManualType;}
else if(!col.sType){for(j=0,jen=types.length;j<jen;j++){for(k=0,ken=data.length;k<ken;k++){if(cache[k]===undefined){cache[k]=_fnGetCellData(settings,k,i,'type');}
detectedType=types[j](cache[k],settings);if(!detectedType||detectedType==='html'){break;}}
if(detectedType){col.sType=detectedType;break;}}
if(!col.sType){col.sType='string';}}}}
function _fnApplyColumnDefs(oSettings,aoColDefs,aoCols,fn)
{var i,iLen,j,jLen,k,kLen,def;var columns=oSettings.aoColumns;if(aoColDefs)
{for(i=aoColDefs.length-1;i>=0;i--)
{def=aoColDefs[i];var aTargets=def.targets!==undefined?def.targets:def.aTargets;if(!$.isArray(aTargets))
{aTargets=[aTargets];}
for(j=0,jLen=aTargets.length;j<jLen;j++)
{if(typeof aTargets[j]==='number'&&aTargets[j]>=0)
{while(columns.length<=aTargets[j])
{_fnAddColumn(oSettings);}
fn(aTargets[j],def);}
else if(typeof aTargets[j]==='number'&&aTargets[j]<0)
{fn(columns.length+aTargets[j],def);}
else if(typeof aTargets[j]==='string')
{for(k=0,kLen=columns.length;k<kLen;k++)
{if(aTargets[j]=="_all"||$(columns[k].nTh).hasClass(aTargets[j]))
{fn(k,def);}}}}}}
if(aoCols)
{for(i=0,iLen=aoCols.length;i<iLen;i++)
{fn(i,aoCols[i]);}}}
function _fnAddData(oSettings,aDataIn,nTr,anTds)
{var iRow=oSettings.aoData.length;var oData=$.extend(true,{},DataTable.models.oRow,{src:nTr?'dom':'data'});oData._aData=aDataIn;oSettings.aoData.push(oData);var nTd,sThisType;var columns=oSettings.aoColumns;for(var i=0,iLen=columns.length;i<iLen;i++)
{if(nTr){_fnSetCellData(oSettings,iRow,i,_fnGetCellData(oSettings,iRow,i));}
columns[i].sType=null;}
oSettings.aiDisplayMaster.push(iRow);if(nTr||!oSettings.oFeatures.bDeferRender)
{_fnCreateTr(oSettings,iRow,nTr,anTds);}
return iRow;}
function _fnAddTr(settings,trs)
{var row;if(!(trs instanceof $)){trs=$(trs);}
return trs.map(function(i,el){row=_fnGetRowElements(settings,el);return _fnAddData(settings,row.data,el,row.cells);});}
function _fnNodeToDataIndex(oSettings,n)
{return(n._DT_RowIndex!==undefined)?n._DT_RowIndex:null;}
function _fnNodeToColumnIndex(oSettings,iRow,n)
{return $.inArray(n,oSettings.aoData[iRow].anCells);}
function _fnGetCellData(settings,rowIdx,colIdx,type)
{var draw=settings.iDraw;var col=settings.aoColumns[colIdx];var rowData=settings.aoData[rowIdx]._aData;var defaultContent=col.sDefaultContent;var cellData=col.fnGetData(rowData,type,{settings:settings,row:rowIdx,col:colIdx});if(cellData===undefined){if(settings.iDrawError!=draw&&defaultContent===null){_fnLog(settings,0,"Requested unknown parameter "+
(typeof col.mData=='function'?'{function}':"'"+col.mData+"'")+" for row "+rowIdx,4);settings.iDrawError=draw;}
return defaultContent;}
if((cellData===rowData||cellData===null)&&defaultContent!==null){cellData=defaultContent;}
else if(typeof cellData==='function'){return cellData.call(rowData);}
if(cellData===null&&type=='display'){return'';}
return cellData;}
function _fnSetCellData(settings,rowIdx,colIdx,val)
{var col=settings.aoColumns[colIdx];var rowData=settings.aoData[rowIdx]._aData;col.fnSetData(rowData,val,{settings:settings,row:rowIdx,col:colIdx});}
var __reArray=/\[.*?\]$/;var __reFn=/\(\)$/;function _fnSplitObjNotation(str)
{return $.map(str.match(/(\\.|[^\.])+/g),function(s){return s.replace(/\\./g,'.');});}
function _fnGetObjectDataFn(mSource)
{if($.isPlainObject(mSource))
{var o={};$.each(mSource,function(key,val){if(val){o[key]=_fnGetObjectDataFn(val);}});return function(data,type,row,meta){var t=o[type]||o._;return t!==undefined?t(data,type,row,meta):data;};}
else if(mSource===null)
{return function(data){return data;};}
else if(typeof mSource==='function')
{return function(data,type,row,meta){return mSource(data,type,row,meta);};}
else if(typeof mSource==='string'&&(mSource.indexOf('.')!==-1||mSource.indexOf('[')!==-1||mSource.indexOf('(')!==-1))
{var fetchData=function(data,type,src){var arrayNotation,funcNotation,out,innerSrc;if(src!=="")
{var a=_fnSplitObjNotation(src);for(var i=0,iLen=a.length;i<iLen;i++)
{arrayNotation=a[i].match(__reArray);funcNotation=a[i].match(__reFn);if(arrayNotation)
{a[i]=a[i].replace(__reArray,'');if(a[i]!==""){data=data[a[i]];}
out=[];a.splice(0,i+1);innerSrc=a.join('.');for(var j=0,jLen=data.length;j<jLen;j++){out.push(fetchData(data[j],type,innerSrc));}
var join=arrayNotation[0].substring(1,arrayNotation[0].length-1);data=(join==="")?out:out.join(join);break;}
else if(funcNotation)
{a[i]=a[i].replace(__reFn,'');data=data[a[i]]();continue;}
if(data===null||data[a[i]]===undefined)
{return undefined;}
data=data[a[i]];}}
return data;};return function(data,type){return fetchData(data,type,mSource);};}
else
{return function(data,type){return data[mSource];};}}
function _fnSetObjectDataFn(mSource)
{if($.isPlainObject(mSource))
{return _fnSetObjectDataFn(mSource._);}
else if(mSource===null)
{return function(){};}
else if(typeof mSource==='function')
{return function(data,val,meta){mSource(data,'set',val,meta);};}
else if(typeof mSource==='string'&&(mSource.indexOf('.')!==-1||mSource.indexOf('[')!==-1||mSource.indexOf('(')!==-1))
{var setData=function(data,val,src){var a=_fnSplitObjNotation(src),b;var aLast=a[a.length-1];var arrayNotation,funcNotation,o,innerSrc;for(var i=0,iLen=a.length-1;i<iLen;i++)
{arrayNotation=a[i].match(__reArray);funcNotation=a[i].match(__reFn);if(arrayNotation)
{a[i]=a[i].replace(__reArray,'');data[a[i]]=[];b=a.slice();b.splice(0,i+1);innerSrc=b.join('.');for(var j=0,jLen=val.length;j<jLen;j++)
{o={};setData(o,val[j],innerSrc);data[a[i]].push(o);}
return;}
else if(funcNotation)
{a[i]=a[i].replace(__reFn,'');data=data[a[i]](val);}
if(data[a[i]]===null||data[a[i]]===undefined)
{data[a[i]]={};}
data=data[a[i]];}
if(aLast.match(__reFn))
{data=data[aLast.replace(__reFn,'')](val);}
else
{data[aLast.replace(__reArray,'')]=val;}};return function(data,val){return setData(data,val,mSource);};}
else
{return function(data,val){data[mSource]=val;};}}
function _fnGetDataMaster(settings)
{return _pluck(settings.aoData,'_aData');}
function _fnClearTable(settings)
{settings.aoData.length=0;settings.aiDisplayMaster.length=0;settings.aiDisplay.length=0;}
function _fnDeleteIndex(a,iTarget,splice)
{var iTargetIndex=-1;for(var i=0,iLen=a.length;i<iLen;i++)
{if(a[i]==iTarget)
{iTargetIndex=i;}
else if(a[i]>iTarget)
{a[i]--;}}
if(iTargetIndex!=-1&&splice===undefined)
{a.splice(iTargetIndex,1);}}
function _fnInvalidateRow(settings,rowIdx,src,column)
{var row=settings.aoData[rowIdx];var i,ien;if(src==='dom'||((!src||src==='auto')&&row.src==='dom')){row._aData=_fnGetRowElements(settings,row).data;}
else{var cells=row.anCells;var cell;if(cells){for(i=0,ien=cells.length;i<ien;i++){cell=cells[i];while(cell.childNodes.length){cell.removeChild(cell.firstChild);}
cells[i].innerHTML=_fnGetCellData(settings,rowIdx,i,'display');}}}
row._aSortData=null;row._aFilterData=null;var cols=settings.aoColumns;if(column!==undefined){cols[column].sType=null;}
else{for(i=0,ien=cols.length;i<ien;i++){cols[i].sType=null;}}
_fnRowAttributes(row);}
function _fnGetRowElements(settings,row)
{var
tds=[],td=row.firstChild,name,col,o,i=0,contents,columns=settings.aoColumns,objectRead=settings._rowReadObject;var d=objectRead?{}:[];var attr=function(str,td){if(typeof str==='string'){var idx=str.indexOf('@');if(idx!==-1){var attr=str.substring(idx+1);var setter=_fnSetObjectDataFn(str);setter(d,td.getAttribute(attr));}}};var cellProcess=function(cell){col=columns[i];contents=$.trim(cell.innerHTML);if(col&&col._bAttrSrc){var setter=_fnSetObjectDataFn(col.mData._);setter(d,contents);attr(col.mData.sort,cell);attr(col.mData.type,cell);attr(col.mData.filter,cell);}
else{if(objectRead){if(!col._setter){col._setter=_fnSetObjectDataFn(col.mData);}
col._setter(d,contents);}
else{d.push(contents);}}
i++;};if(td){while(td){name=td.nodeName.toUpperCase();if(name=="TD"||name=="TH"){cellProcess(td);tds.push(td);}
td=td.nextSibling;}}
else{tds=row.anCells;for(var j=0,jen=tds.length;j<jen;j++){cellProcess(tds[j]);}}
return{data:d,cells:tds};}
function _fnCreateTr(oSettings,iRow,nTrIn,anTds)
{var
row=oSettings.aoData[iRow],rowData=row._aData,cells=[],nTr,nTd,oCol,i,iLen;if(row.nTr===null)
{nTr=nTrIn||document.createElement('tr');row.nTr=nTr;row.anCells=cells;nTr._DT_RowIndex=iRow;_fnRowAttributes(row);for(i=0,iLen=oSettings.aoColumns.length;i<iLen;i++)
{oCol=oSettings.aoColumns[i];nTd=nTrIn?anTds[i]:document.createElement(oCol.sCellType);cells.push(nTd);if(!nTrIn||oCol.mRender||oCol.mData!==i)
{nTd.innerHTML=_fnGetCellData(oSettings,iRow,i,'display');}
if(oCol.sClass)
{nTd.className+=' '+oCol.sClass;}
if(oCol.bVisible&&!nTrIn)
{nTr.appendChild(nTd);}
else if(!oCol.bVisible&&nTrIn)
{nTd.parentNode.removeChild(nTd);}
if(oCol.fnCreatedCell)
{oCol.fnCreatedCell.call(oSettings.oInstance,nTd,_fnGetCellData(oSettings,iRow,i),rowData,iRow,i);}}
_fnCallbackFire(oSettings,'aoRowCreatedCallback',null,[nTr,rowData,iRow]);}
row.nTr.setAttribute('role','row');}
function _fnRowAttributes(row)
{var tr=row.nTr;var data=row._aData;if(tr){if(data.DT_RowId){tr.id=data.DT_RowId;}
if(data.DT_RowClass){var a=data.DT_RowClass.split(' ');row.__rowc=row.__rowc?_unique(row.__rowc.concat(a)):a;$(tr).removeClass(row.__rowc.join(' ')).addClass(data.DT_RowClass);}
if(data.DT_RowData){$(tr).data(data.DT_RowData);}}}
function _fnBuildHead(oSettings)
{var i,ien,cell,row,column;var thead=oSettings.nTHead;var tfoot=oSettings.nTFoot;var createHeader=$('th, td',thead).length===0;var classes=oSettings.oClasses;var columns=oSettings.aoColumns;if(createHeader){row=$('<tr/>').appendTo(thead);}
for(i=0,ien=columns.length;i<ien;i++){column=columns[i];cell=$(column.nTh).addClass(column.sClass);if(createHeader){cell.appendTo(row);}
if(oSettings.oFeatures.bSort){cell.addClass(column.sSortingClass);if(column.bSortable!==false){cell.attr('tabindex',oSettings.iTabIndex).attr('aria-controls',oSettings.sTableId);_fnSortAttachListener(oSettings,column.nTh,i);}}
if(column.sTitle!=cell.html()){cell.html(column.sTitle);}
_fnRenderer(oSettings,'header')(oSettings,cell,column,classes);}
if(createHeader){_fnDetectHeader(oSettings.aoHeader,thead);}
$(thead).find('>tr').attr('role','row');$(thead).find('>tr>th, >tr>td').addClass(classes.sHeaderTH);$(tfoot).find('>tr>th, >tr>td').addClass(classes.sFooterTH);if(tfoot!==null){var cells=oSettings.aoFooter[0];for(i=0,ien=cells.length;i<ien;i++){column=columns[i];column.nTf=cells[i].cell;if(column.sClass){$(column.nTf).addClass(column.sClass);}}}}
function _fnDrawHead(oSettings,aoSource,bIncludeHidden)
{var i,iLen,j,jLen,k,kLen,n,nLocalTr;var aoLocal=[];var aApplied=[];var iColumns=oSettings.aoColumns.length;var iRowspan,iColspan;if(!aoSource)
{return;}
if(bIncludeHidden===undefined)
{bIncludeHidden=false;}
for(i=0,iLen=aoSource.length;i<iLen;i++)
{aoLocal[i]=aoSource[i].slice();aoLocal[i].nTr=aoSource[i].nTr;for(j=iColumns-1;j>=0;j--)
{if(!oSettings.aoColumns[j].bVisible&&!bIncludeHidden)
{aoLocal[i].splice(j,1);}}
aApplied.push([]);}
for(i=0,iLen=aoLocal.length;i<iLen;i++)
{nLocalTr=aoLocal[i].nTr;if(nLocalTr)
{while((n=nLocalTr.firstChild))
{nLocalTr.removeChild(n);}}
for(j=0,jLen=aoLocal[i].length;j<jLen;j++)
{iRowspan=1;iColspan=1;if(aApplied[i][j]===undefined)
{nLocalTr.appendChild(aoLocal[i][j].cell);aApplied[i][j]=1;while(aoLocal[i+iRowspan]!==undefined&&aoLocal[i][j].cell==aoLocal[i+iRowspan][j].cell)
{aApplied[i+iRowspan][j]=1;iRowspan++;}
while(aoLocal[i][j+iColspan]!==undefined&&aoLocal[i][j].cell==aoLocal[i][j+iColspan].cell)
{for(k=0;k<iRowspan;k++)
{aApplied[i+k][j+iColspan]=1;}
iColspan++;}
$(aoLocal[i][j].cell).attr('rowspan',iRowspan).attr('colspan',iColspan);}}}}
function _fnDraw(oSettings)
{var aPreDraw=_fnCallbackFire(oSettings,'aoPreDrawCallback','preDraw',[oSettings]);if($.inArray(false,aPreDraw)!==-1)
{_fnProcessingDisplay(oSettings,false);return;}
var i,iLen,n;var anRows=[];var iRowCount=0;var asStripeClasses=oSettings.asStripeClasses;var iStripes=asStripeClasses.length;var iOpenRows=oSettings.aoOpenRows.length;var oLang=oSettings.oLanguage;var iInitDisplayStart=oSettings.iInitDisplayStart;var bServerSide=_fnDataSource(oSettings)=='ssp';var aiDisplay=oSettings.aiDisplay;oSettings.bDrawing=true;if(iInitDisplayStart!==undefined&&iInitDisplayStart!==-1)
{oSettings._iDisplayStart=bServerSide?iInitDisplayStart:iInitDisplayStart>=oSettings.fnRecordsDisplay()?0:iInitDisplayStart;oSettings.iInitDisplayStart=-1;}
var iDisplayStart=oSettings._iDisplayStart;var iDisplayEnd=oSettings.fnDisplayEnd();if(oSettings.bDeferLoading)
{oSettings.bDeferLoading=false;oSettings.iDraw++;_fnProcessingDisplay(oSettings,false);}
else if(!bServerSide)
{oSettings.iDraw++;}
else if(!oSettings.bDestroying&&!_fnAjaxUpdate(oSettings))
{return;}
if(aiDisplay.length!==0)
{var iStart=bServerSide?0:iDisplayStart;var iEnd=bServerSide?oSettings.aoData.length:iDisplayEnd;for(var j=iStart;j<iEnd;j++)
{var iDataIndex=aiDisplay[j];var aoData=oSettings.aoData[iDataIndex];if(aoData.nTr===null)
{_fnCreateTr(oSettings,iDataIndex);}
var nRow=aoData.nTr;if(iStripes!==0)
{var sStripe=asStripeClasses[iRowCount%iStripes];if(aoData._sRowStripe!=sStripe)
{$(nRow).removeClass(aoData._sRowStripe).addClass(sStripe);aoData._sRowStripe=sStripe;}}
_fnCallbackFire(oSettings,'aoRowCallback',null,[nRow,aoData._aData,iRowCount,j]);anRows.push(nRow);iRowCount++;}}
else
{var sZero=oLang.sZeroRecords;if(oSettings.iDraw==1&&_fnDataSource(oSettings)=='ajax')
{sZero=oLang.sLoadingRecords;}
else if(oLang.sEmptyTable&&oSettings.fnRecordsTotal()===0)
{sZero=oLang.sEmptyTable;}
anRows[0]=$('<tr/>',{'class':iStripes?asStripeClasses[0]:''}).append($('<td />',{'valign':'top','colSpan':_fnVisbleColumns(oSettings),'class':oSettings.oClasses.sRowEmpty}).html(sZero))[0];}
_fnCallbackFire(oSettings,'aoHeaderCallback','header',[$(oSettings.nTHead).children('tr')[0],_fnGetDataMaster(oSettings),iDisplayStart,iDisplayEnd,aiDisplay]);_fnCallbackFire(oSettings,'aoFooterCallback','footer',[$(oSettings.nTFoot).children('tr')[0],_fnGetDataMaster(oSettings),iDisplayStart,iDisplayEnd,aiDisplay]);var body=$(oSettings.nTBody);body.children().detach();body.append($(anRows));_fnCallbackFire(oSettings,'aoDrawCallback','draw',[oSettings]);oSettings.bSorted=false;oSettings.bFiltered=false;oSettings.bDrawing=false;}
function _fnReDraw(settings,holdPosition)
{var
features=settings.oFeatures,sort=features.bSort,filter=features.bFilter;if(sort){_fnSort(settings);}
if(filter){_fnFilterComplete(settings,settings.oPreviousSearch);}
else{settings.aiDisplay=settings.aiDisplayMaster.slice();}
if(holdPosition!==true){settings._iDisplayStart=0;}
settings._drawHold=holdPosition;_fnDraw(settings);settings._drawHold=false;}
function _fnAddOptionsHtml(oSettings)
{var classes=oSettings.oClasses;var table=$(oSettings.nTable);var holding=$('<div/>').insertBefore(table);var features=oSettings.oFeatures;var insert=$('<div/>',{id:oSettings.sTableId+'_wrapper','class':classes.sWrapper+(oSettings.nTFoot?'':' '+classes.sNoFooter)});oSettings.nHolding=holding[0];oSettings.nTableWrapper=insert[0];oSettings.nTableReinsertBefore=oSettings.nTable.nextSibling;var aDom=oSettings.sDom.split('');var featureNode,cOption,nNewNode,cNext,sAttr,j;for(var i=0;i<aDom.length;i++)
{featureNode=null;cOption=aDom[i];if(cOption=='<')
{nNewNode=$('<div/>')[0];cNext=aDom[i+1];if(cNext=="'"||cNext=='"')
{sAttr="";j=2;while(aDom[i+j]!=cNext)
{sAttr+=aDom[i+j];j++;}
if(sAttr=="H")
{sAttr=classes.sJUIHeader;}
else if(sAttr=="F")
{sAttr=classes.sJUIFooter;}
if(sAttr.indexOf('.')!=-1)
{var aSplit=sAttr.split('.');nNewNode.id=aSplit[0].substr(1,aSplit[0].length-1);nNewNode.className=aSplit[1];}
else if(sAttr.charAt(0)=="#")
{nNewNode.id=sAttr.substr(1,sAttr.length-1);}
else
{nNewNode.className=sAttr;}
i+=j;}
insert.append(nNewNode);insert=$(nNewNode);}
else if(cOption=='>')
{insert=insert.parent();}
else if(cOption=='l'&&features.bPaginate&&features.bLengthChange)
{featureNode=_fnFeatureHtmlLength(oSettings);}
else if(cOption=='f'&&features.bFilter)
{featureNode=_fnFeatureHtmlFilter(oSettings);}
else if(cOption=='r'&&features.bProcessing)
{featureNode=_fnFeatureHtmlProcessing(oSettings);}
else if(cOption=='t')
{featureNode=_fnFeatureHtmlTable(oSettings);}
else if(cOption=='i'&&features.bInfo)
{featureNode=_fnFeatureHtmlInfo(oSettings);}
else if(cOption=='p'&&features.bPaginate)
{featureNode=_fnFeatureHtmlPaginate(oSettings);}
else if(DataTable.ext.feature.length!==0)
{var aoFeatures=DataTable.ext.feature;for(var k=0,kLen=aoFeatures.length;k<kLen;k++)
{if(cOption==aoFeatures[k].cFeature)
{featureNode=aoFeatures[k].fnInit(oSettings);break;}}}
if(featureNode)
{var aanFeatures=oSettings.aanFeatures;if(!aanFeatures[cOption])
{aanFeatures[cOption]=[];}
aanFeatures[cOption].push(featureNode);insert.append(featureNode);}}
holding.replaceWith(insert);}
function _fnDetectHeader(aLayout,nThead)
{var nTrs=$(nThead).children('tr');var nTr,nCell;var i,k,l,iLen,jLen,iColShifted,iColumn,iColspan,iRowspan;var bUnique;var fnShiftCol=function(a,i,j){var k=a[i];while(k[j]){j++;}
return j;};aLayout.splice(0,aLayout.length);for(i=0,iLen=nTrs.length;i<iLen;i++)
{aLayout.push([]);}
for(i=0,iLen=nTrs.length;i<iLen;i++)
{nTr=nTrs[i];iColumn=0;nCell=nTr.firstChild;while(nCell){if(nCell.nodeName.toUpperCase()=="TD"||nCell.nodeName.toUpperCase()=="TH")
{iColspan=nCell.getAttribute('colspan')*1;iRowspan=nCell.getAttribute('rowspan')*1;iColspan=(!iColspan||iColspan===0||iColspan===1)?1:iColspan;iRowspan=(!iRowspan||iRowspan===0||iRowspan===1)?1:iRowspan;iColShifted=fnShiftCol(aLayout,i,iColumn);bUnique=iColspan===1?true:false;for(l=0;l<iColspan;l++)
{for(k=0;k<iRowspan;k++)
{aLayout[i+k][iColShifted+l]={"cell":nCell,"unique":bUnique};aLayout[i+k].nTr=nTr;}}}
nCell=nCell.nextSibling;}}}
function _fnGetUniqueThs(oSettings,nHeader,aLayout)
{var aReturn=[];if(!aLayout)
{aLayout=oSettings.aoHeader;if(nHeader)
{aLayout=[];_fnDetectHeader(aLayout,nHeader);}}
for(var i=0,iLen=aLayout.length;i<iLen;i++)
{for(var j=0,jLen=aLayout[i].length;j<jLen;j++)
{if(aLayout[i][j].unique&&(!aReturn[j]||!oSettings.bSortCellsTop))
{aReturn[j]=aLayout[i][j].cell;}}}
return aReturn;}
function _fnBuildAjax(oSettings,data,fn)
{_fnCallbackFire(oSettings,'aoServerParams','serverParams',[data]);if(data&&$.isArray(data)){var tmp={};var rbracket=/(.*?)\[\]$/;$.each(data,function(key,val){var match=val.name.match(rbracket);if(match){var name=match[0];if(!tmp[name]){tmp[name]=[];}
tmp[name].push(val.value);}
else{tmp[val.name]=val.value;}});data=tmp;}
var ajaxData;var ajax=oSettings.ajax;var instance=oSettings.oInstance;if($.isPlainObject(ajax)&&ajax.data)
{ajaxData=ajax.data;var newData=$.isFunction(ajaxData)?ajaxData(data):ajaxData;data=$.isFunction(ajaxData)&&newData?newData:$.extend(true,data,newData);delete ajax.data;}
var baseAjax={"data":data,"success":function(json){var error=json.error||json.sError;if(error){oSettings.oApi._fnLog(oSettings,0,error);}
oSettings.json=json;_fnCallbackFire(oSettings,null,'xhr',[oSettings,json]);fn(json);},"dataType":"json","cache":false,"type":oSettings.sServerMethod,"error":function(xhr,error,thrown){var log=oSettings.oApi._fnLog;if(error=="parsererror"){log(oSettings,0,'Invalid JSON response',1);}
else if(xhr.readyState===4){log(oSettings,0,'Ajax error',7);}
_fnProcessingDisplay(oSettings,false);}};oSettings.oAjaxData=data;_fnCallbackFire(oSettings,null,'preXhr',[oSettings,data]);if(oSettings.fnServerData)
{oSettings.fnServerData.call(instance,oSettings.sAjaxSource,$.map(data,function(val,key){return{name:key,value:val};}),fn,oSettings);}
else if(oSettings.sAjaxSource||typeof ajax==='string')
{oSettings.jqXHR=$.ajax($.extend(baseAjax,{url:ajax||oSettings.sAjaxSource}));}
else if($.isFunction(ajax))
{oSettings.jqXHR=ajax.call(instance,data,fn,oSettings);}
else
{oSettings.jqXHR=$.ajax($.extend(baseAjax,ajax));ajax.data=ajaxData;}}
function _fnAjaxUpdate(settings)
{if(settings.bAjaxDataGet){settings.iDraw++;_fnProcessingDisplay(settings,true);_fnBuildAjax(settings,_fnAjaxParameters(settings),function(json){_fnAjaxUpdateDraw(settings,json);});return false;}
return true;}
function _fnAjaxParameters(settings)
{var
columns=settings.aoColumns,columnCount=columns.length,features=settings.oFeatures,preSearch=settings.oPreviousSearch,preColSearch=settings.aoPreSearchCols,i,data=[],dataProp,column,columnSearch,sort=_fnSortFlatten(settings),displayStart=settings._iDisplayStart,displayLength=features.bPaginate!==false?settings._iDisplayLength:-1;var param=function(name,value){data.push({'name':name,'value':value});};param('sEcho',settings.iDraw);param('iColumns',columnCount);param('sColumns',_pluck(columns,'sName').join(','));param('iDisplayStart',displayStart);param('iDisplayLength',displayLength);var d={draw:settings.iDraw,columns:[],order:[],start:displayStart,length:displayLength,search:{value:preSearch.sSearch,regex:preSearch.bRegex}};for(i=0;i<columnCount;i++){column=columns[i];columnSearch=preColSearch[i];dataProp=typeof column.mData=="function"?'function':column.mData;d.columns.push({data:dataProp,name:column.sName,searchable:column.bSearchable,orderable:column.bSortable,search:{value:columnSearch.sSearch,regex:columnSearch.bRegex}});param("mDataProp_"+i,dataProp);if(features.bFilter){param('sSearch_'+i,columnSearch.sSearch);param('bRegex_'+i,columnSearch.bRegex);param('bSearchable_'+i,column.bSearchable);}
if(features.bSort){param('bSortable_'+i,column.bSortable);}}
if(features.bFilter){param('sSearch',preSearch.sSearch);param('bRegex',preSearch.bRegex);}
if(features.bSort){$.each(sort,function(i,val){d.order.push({column:val.col,dir:val.dir});param('iSortCol_'+i,val.col);param('sSortDir_'+i,val.dir);});param('iSortingCols',sort.length);}
var legacy=DataTable.ext.legacy.ajax;if(legacy===null){return settings.sAjaxSource?data:d;}
return legacy?data:d;}
function _fnAjaxUpdateDraw(settings,json)
{var compat=function(old,modern){return json[old]!==undefined?json[old]:json[modern];};var draw=compat('sEcho','draw');var recordsTotal=compat('iTotalRecords','recordsTotal');var rocordsFiltered=compat('iTotalDisplayRecords','recordsFiltered');if(draw){if(draw*1<settings.iDraw){return;}
settings.iDraw=draw*1;}
_fnClearTable(settings);settings._iRecordsTotal=parseInt(recordsTotal,10);settings._iRecordsDisplay=parseInt(rocordsFiltered,10);var data=_fnAjaxDataSrc(settings,json);for(var i=0,ien=data.length;i<ien;i++){_fnAddData(settings,data[i]);}
settings.aiDisplay=settings.aiDisplayMaster.slice();settings.bAjaxDataGet=false;_fnDraw(settings);if(!settings._bInitComplete){_fnInitComplete(settings,json);}
settings.bAjaxDataGet=true;_fnProcessingDisplay(settings,false);}
function _fnAjaxDataSrc(oSettings,json)
{var dataSrc=$.isPlainObject(oSettings.ajax)&&oSettings.ajax.dataSrc!==undefined?oSettings.ajax.dataSrc:oSettings.sAjaxDataProp;if(dataSrc==='data'){return json.aaData||json[dataSrc];}
return dataSrc!==""?_fnGetObjectDataFn(dataSrc)(json):json;}
function _fnFeatureHtmlFilter(settings)
{var classes=settings.oClasses;var tableId=settings.sTableId;var language=settings.oLanguage;var previousSearch=settings.oPreviousSearch;var features=settings.aanFeatures;var input='<input type="search" class="'+classes.sFilterInput+'"/>';var str=language.sSearch;str=str.match(/_INPUT_/)?str.replace('_INPUT_',input):str+input;var filter=$('<div/>',{'id':!features.f?tableId+'_filter':null,'class':classes.sFilter}).append($('<label/>').append(str));var searchFn=function(){var n=features.f;var val=!this.value?"":this.value;if(val!=previousSearch.sSearch){_fnFilterComplete(settings,{"sSearch":val,"bRegex":previousSearch.bRegex,"bSmart":previousSearch.bSmart,"bCaseInsensitive":previousSearch.bCaseInsensitive});settings._iDisplayStart=0;_fnDraw(settings);}};var searchDelay=settings.searchDelay!==null?settings.searchDelay:_fnDataSource(settings)==='ssp'?400:0;var jqFilter=$('input',filter).val(previousSearch.sSearch).attr('placeholder',language.sSearchPlaceholder).bind('keyup.DT search.DT input.DT paste.DT cut.DT',searchDelay?_fnThrottle(searchFn,searchDelay):searchFn).bind('keypress.DT',function(e){if(e.keyCode==13){return false;}}).attr('aria-controls',tableId);$(settings.nTable).on('search.dt.DT',function(ev,s){if(settings===s){try{if(jqFilter[0]!==document.activeElement){jqFilter.val(previousSearch.sSearch);}}
catch(e){}}});return filter[0];}
function _fnFilterComplete(oSettings,oInput,iForce)
{var oPrevSearch=oSettings.oPreviousSearch;var aoPrevSearch=oSettings.aoPreSearchCols;var fnSaveFilter=function(oFilter){oPrevSearch.sSearch=oFilter.sSearch;oPrevSearch.bRegex=oFilter.bRegex;oPrevSearch.bSmart=oFilter.bSmart;oPrevSearch.bCaseInsensitive=oFilter.bCaseInsensitive;};var fnRegex=function(o){return o.bEscapeRegex!==undefined?!o.bEscapeRegex:o.bRegex;};_fnColumnTypes(oSettings);if(_fnDataSource(oSettings)!='ssp')
{_fnFilter(oSettings,oInput.sSearch,iForce,fnRegex(oInput),oInput.bSmart,oInput.bCaseInsensitive);fnSaveFilter(oInput);for(var i=0;i<aoPrevSearch.length;i++)
{_fnFilterColumn(oSettings,aoPrevSearch[i].sSearch,i,fnRegex(aoPrevSearch[i]),aoPrevSearch[i].bSmart,aoPrevSearch[i].bCaseInsensitive);}
_fnFilterCustom(oSettings);}
else
{fnSaveFilter(oInput);}
oSettings.bFiltered=true;_fnCallbackFire(oSettings,null,'search',[oSettings]);}
function _fnFilterCustom(settings)
{var filters=DataTable.ext.search;var displayRows=settings.aiDisplay;var row,rowIdx;for(var i=0,ien=filters.length;i<ien;i++){var rows=[];for(var j=0,jen=displayRows.length;j<jen;j++){rowIdx=displayRows[j];row=settings.aoData[rowIdx];if(filters[i](settings,row._aFilterData,rowIdx,row._aData,j)){rows.push(rowIdx);}}
displayRows.length=0;displayRows.push.apply(displayRows,rows);}}
function _fnFilterColumn(settings,searchStr,colIdx,regex,smart,caseInsensitive)
{if(searchStr===''){return;}
var data;var display=settings.aiDisplay;var rpSearch=_fnFilterCreateSearch(searchStr,regex,smart,caseInsensitive);for(var i=display.length-1;i>=0;i--){data=settings.aoData[display[i]]._aFilterData[colIdx];if(!rpSearch.test(data)){display.splice(i,1);}}}
function _fnFilter(settings,input,force,regex,smart,caseInsensitive)
{var rpSearch=_fnFilterCreateSearch(input,regex,smart,caseInsensitive);var prevSearch=settings.oPreviousSearch.sSearch;var displayMaster=settings.aiDisplayMaster;var display,invalidated,i;if(DataTable.ext.search.length!==0){force=true;}
invalidated=_fnFilterData(settings);if(input.length<=0){settings.aiDisplay=displayMaster.slice();}
else{if(invalidated||force||prevSearch.length>input.length||input.indexOf(prevSearch)!==0||settings.bSorted){settings.aiDisplay=displayMaster.slice();}
display=settings.aiDisplay;for(i=display.length-1;i>=0;i--){if(!rpSearch.test(settings.aoData[display[i]]._sFilterRow)){display.splice(i,1);}}}}
function _fnFilterCreateSearch(search,regex,smart,caseInsensitive)
{search=regex?search:_fnEscapeRegex(search);if(smart){var a=$.map(search.match(/"[^"]+"|[^ ]+/g)||'',function(word){if(word.charAt(0)==='"'){var m=word.match(/^"(.*)"$/);word=m?m[1]:word;}
return word.replace('"','');});search='^(?=.*?'+a.join(')(?=.*?')+').*$';}
return new RegExp(search,caseInsensitive?'i':'');}
function _fnEscapeRegex(sVal)
{return sVal.replace(_re_escape_regex,'\\$1');}
var __filter_div=$('<div>')[0];var __filter_div_textContent=__filter_div.textContent!==undefined;function _fnFilterData(settings)
{var columns=settings.aoColumns;var column;var i,j,ien,jen,filterData,cellData,row;var fomatters=DataTable.ext.type.search;var wasInvalidated=false;for(i=0,ien=settings.aoData.length;i<ien;i++){row=settings.aoData[i];if(!row._aFilterData){filterData=[];for(j=0,jen=columns.length;j<jen;j++){column=columns[j];if(column.bSearchable){cellData=_fnGetCellData(settings,i,j,'filter');if(fomatters[column.sType]){cellData=fomatters[column.sType](cellData);}
if(cellData===null){cellData='';}
if(typeof cellData!=='string'&&cellData.toString){cellData=cellData.toString();}}
else{cellData='';}
if(cellData.indexOf&&cellData.indexOf('&')!==-1){__filter_div.innerHTML=cellData;cellData=__filter_div_textContent?__filter_div.textContent:__filter_div.innerText;}
if(cellData.replace){cellData=cellData.replace(/[\r\n]/g,'');}
filterData.push(cellData);}
row._aFilterData=filterData;row._sFilterRow=filterData.join('  ');wasInvalidated=true;}}
return wasInvalidated;}
function _fnSearchToCamel(obj)
{return{search:obj.sSearch,smart:obj.bSmart,regex:obj.bRegex,caseInsensitive:obj.bCaseInsensitive};}
function _fnSearchToHung(obj)
{return{sSearch:obj.search,bSmart:obj.smart,bRegex:obj.regex,bCaseInsensitive:obj.caseInsensitive};}
function _fnFeatureHtmlInfo(settings)
{var
tid=settings.sTableId,nodes=settings.aanFeatures.i,n=$('<div/>',{'class':settings.oClasses.sInfo,'id':!nodes?tid+'_info':null});if(!nodes){settings.aoDrawCallback.push({"fn":_fnUpdateInfo,"sName":"information"});n.attr('role','status').attr('aria-live','polite');$(settings.nTable).attr('aria-describedby',tid+'_info');}
return n[0];}
function _fnUpdateInfo(settings)
{var nodes=settings.aanFeatures.i;if(nodes.length===0){return;}
var
lang=settings.oLanguage,start=settings._iDisplayStart+1,end=settings.fnDisplayEnd(),max=settings.fnRecordsTotal(),total=settings.fnRecordsDisplay(),out=total?lang.sInfo:lang.sInfoEmpty;if(total!==max){out+=' '+ lang.sInfoFiltered;}
out+=lang.sInfoPostFix;out=_fnInfoMacros(settings,out);var callback=lang.fnInfoCallback;if(callback!==null){out=callback.call(settings.oInstance,settings,start,end,max,total,out);}
$(nodes).html(out);}
function _fnInfoMacros(settings,str)
{var
formatter=settings.fnFormatNumber,start=settings._iDisplayStart+1,len=settings._iDisplayLength,vis=settings.fnRecordsDisplay(),all=len===-1;return str.replace(/_START_/g,formatter.call(settings,start)).replace(/_END_/g,formatter.call(settings,settings.fnDisplayEnd())).replace(/_MAX_/g,formatter.call(settings,settings.fnRecordsTotal())).replace(/_TOTAL_/g,formatter.call(settings,vis)).replace(/_PAGE_/g,formatter.call(settings,all?1:Math.ceil(start/len))).replace(/_PAGES_/g,formatter.call(settings,all?1:Math.ceil(vis/len)));}
function _fnInitialise(settings)
{var i,iLen,iAjaxStart=settings.iInitDisplayStart;var columns=settings.aoColumns,column;var features=settings.oFeatures;if(!settings.bInitialised){setTimeout(function(){_fnInitialise(settings);},200);return;}
_fnAddOptionsHtml(settings);_fnBuildHead(settings);_fnDrawHead(settings,settings.aoHeader);_fnDrawHead(settings,settings.aoFooter);_fnProcessingDisplay(settings,true);if(features.bAutoWidth){_fnCalculateColumnWidths(settings);}
for(i=0,iLen=columns.length;i<iLen;i++){column=columns[i];if(column.sWidth){column.nTh.style.width=_fnStringToCss(column.sWidth);}}
_fnReDraw(settings);var dataSrc=_fnDataSource(settings);if(dataSrc!='ssp'){if(dataSrc=='ajax'){_fnBuildAjax(settings,[],function(json){var aData=_fnAjaxDataSrc(settings,json);for(i=0;i<aData.length;i++){_fnAddData(settings,aData[i]);}
settings.iInitDisplayStart=iAjaxStart;_fnReDraw(settings);_fnProcessingDisplay(settings,false);_fnInitComplete(settings,json);},settings);}
else{_fnProcessingDisplay(settings,false);_fnInitComplete(settings);}}}
function _fnInitComplete(settings,json)
{settings._bInitComplete=true;if(json){_fnAdjustColumnSizing(settings);}
_fnCallbackFire(settings,'aoInitComplete','init',[settings,json]);}
function _fnLengthChange(settings,val)
{var len=parseInt(val,10);settings._iDisplayLength=len;_fnLengthOverflow(settings);_fnCallbackFire(settings,null,'length',[settings,len]);}
function _fnFeatureHtmlLength(settings)
{var
classes=settings.oClasses,tableId=settings.sTableId,menu=settings.aLengthMenu,d2=$.isArray(menu[0]),lengths=d2?menu[0]:menu,language=d2?menu[1]:menu;var select=$('<select/>',{'name':tableId+'_length','aria-controls':tableId,'class':classes.sLengthSelect});for(var i=0,ien=lengths.length;i<ien;i++){select[0][i]=new Option(language[i],lengths[i]);}
var div=$('<div><label/></div>').addClass(classes.sLength);if(!settings.aanFeatures.l){div[0].id=tableId+'_length';}
div.children().append(settings.oLanguage.sLengthMenu.replace('_MENU_',select[0].outerHTML));$('select',div).val(settings._iDisplayLength).bind('change.DT',function(e){_fnLengthChange(settings,$(this).val());_fnDraw(settings);});$(settings.nTable).bind('length.dt.DT',function(e,s,len){if(settings===s){$('select',div).val(len);}});return div[0];}
function _fnFeatureHtmlPaginate(settings)
{var
type=settings.sPaginationType,plugin=DataTable.ext.pager[type],modern=typeof plugin==='function',redraw=function(settings){_fnDraw(settings);},node=$('<div/>').addClass(settings.oClasses.sPaging+ type)[0],features=settings.aanFeatures;if(!modern){plugin.fnInit(settings,node,redraw);}
if(!features.p)
{node.id=settings.sTableId+'_paginate';settings.aoDrawCallback.push({"fn":function(settings){if(modern){var
start=settings._iDisplayStart,len=settings._iDisplayLength,visRecords=settings.fnRecordsDisplay(),all=len===-1,page=all?0:Math.ceil(start/len),pages=all?1:Math.ceil(visRecords/len),buttons=plugin(page,pages),i,ien;for(i=0,ien=features.p.length;i<ien;i++){_fnRenderer(settings,'pageButton')(settings,features.p[i],i,buttons,page,pages);}}
else{plugin.fnUpdate(settings,redraw);}},"sName":"pagination"});}
return node;}
function _fnPageChange(settings,action,redraw)
{var
start=settings._iDisplayStart,len=settings._iDisplayLength,records=settings.fnRecordsDisplay();if(records===0||len===-1)
{start=0;}
else if(typeof action==="number")
{start=action*len;if(start>records)
{start=0;}}
else if(action=="first")
{start=0;}
else if(action=="previous")
{start=len>=0?start- len:0;if(start<0)
{start=0;}}
else if(action=="next")
{if(start+ len<records)
{start+=len;}}
else if(action=="last")
{start=Math.floor((records-1)/len)*len;}
else
{_fnLog(settings,0,"Unknown paging action: "+action,5);}
var changed=settings._iDisplayStart!==start;settings._iDisplayStart=start;if(changed){_fnCallbackFire(settings,null,'page',[settings]);if(redraw){_fnDraw(settings);}}
return changed;}
function _fnFeatureHtmlProcessing(settings)
{return $('<div/>',{'id':!settings.aanFeatures.r?settings.sTableId+'_processing':null,'class':settings.oClasses.sProcessing}).html(settings.oLanguage.sProcessing).insertBefore(settings.nTable)[0];}
function _fnProcessingDisplay(settings,show)
{if(settings.oFeatures.bProcessing){$(settings.aanFeatures.r).css('display',show?'block':'none');}
_fnCallbackFire(settings,null,'processing',[settings,show]);}
function _fnFeatureHtmlTable(settings)
{var table=$(settings.nTable);table.attr('role','grid');var scroll=settings.oScroll;if(scroll.sX===''&&scroll.sY===''){return settings.nTable;}
var scrollX=scroll.sX;var scrollY=scroll.sY;var classes=settings.oClasses;var caption=table.children('caption');var captionSide=caption.length?caption[0]._captionSide:null;var headerClone=$(table[0].cloneNode(false));var footerClone=$(table[0].cloneNode(false));var footer=table.children('tfoot');var _div='<div/>';var size=function(s){return!s?null:_fnStringToCss(s);};if(scroll.sX&&table.attr('width')==='100%'){table.removeAttr('width');}
if(!footer.length){footer=null;}
var scroller=$(_div,{'class':classes.sScrollWrapper}).append($(_div,{'class':classes.sScrollHead}).css({overflow:'hidden',position:'relative',border:0,width:scrollX?size(scrollX):'100%'}).append($(_div,{'class':classes.sScrollHeadInner}).css({'box-sizing':'content-box',width:scroll.sXInner||'100%'}).append(headerClone.removeAttr('id').css('margin-left',0).append(table.children('thead')))).append(captionSide==='top'?caption:null)).append($(_div,{'class':classes.sScrollBody}).css({overflow:'auto',height:size(scrollY),width:size(scrollX)}).append(table));if(footer){scroller.append($(_div,{'class':classes.sScrollFoot}).css({overflow:'hidden',border:0,width:scrollX?size(scrollX):'100%'}).append($(_div,{'class':classes.sScrollFootInner}).append(footerClone.removeAttr('id').css('margin-left',0).append(table.children('tfoot')))).append(captionSide==='bottom'?caption:null));}
var children=scroller.children();var scrollHead=children[0];var scrollBody=children[1];var scrollFoot=footer?children[2]:null;if(scrollX){$(scrollBody).scroll(function(e){var scrollLeft=this.scrollLeft;scrollHead.scrollLeft=scrollLeft;if(footer){scrollFoot.scrollLeft=scrollLeft;}});}
settings.nScrollHead=scrollHead;settings.nScrollBody=scrollBody;settings.nScrollFoot=scrollFoot;settings.aoDrawCallback.push({"fn":_fnScrollDraw,"sName":"scrolling"});return scroller[0];}
function _fnScrollDraw(settings)
{var
scroll=settings.oScroll,scrollX=scroll.sX,scrollXInner=scroll.sXInner,scrollY=scroll.sY,barWidth=scroll.iBarWidth,divHeader=$(settings.nScrollHead),divHeaderStyle=divHeader[0].style,divHeaderInner=divHeader.children('div'),divHeaderInnerStyle=divHeaderInner[0].style,divHeaderTable=divHeaderInner.children('table'),divBodyEl=settings.nScrollBody,divBody=$(divBodyEl),divBodyStyle=divBodyEl.style,divFooter=$(settings.nScrollFoot),divFooterInner=divFooter.children('div'),divFooterTable=divFooterInner.children('table'),header=$(settings.nTHead),table=$(settings.nTable),tableEl=table[0],tableStyle=tableEl.style,footer=settings.nTFoot?$(settings.nTFoot):null,browser=settings.oBrowser,ie67=browser.bScrollOversize,headerTrgEls,footerTrgEls,headerSrcEls,footerSrcEls,headerCopy,footerCopy,headerWidths=[],footerWidths=[],headerContent=[],idx,correction,sanityWidth,zeroOut=function(nSizer){var style=nSizer.style;style.paddingTop="0";style.paddingBottom="0";style.borderTopWidth="0";style.borderBottomWidth="0";style.height=0;};table.children('thead, tfoot').remove();headerCopy=header.clone().prependTo(table);headerTrgEls=header.find('tr');headerSrcEls=headerCopy.find('tr');headerCopy.find('th, td').removeAttr('tabindex');if(footer){footerCopy=footer.clone().prependTo(table);footerTrgEls=footer.find('tr');footerSrcEls=footerCopy.find('tr');}
if(!scrollX)
{divBodyStyle.width='100%';divHeader[0].style.width='100%';}
$.each(_fnGetUniqueThs(settings,headerCopy),function(i,el){idx=_fnVisibleToColumnIndex(settings,i);el.style.width=settings.aoColumns[idx].sWidth;});if(footer){_fnApplyToChildren(function(n){n.style.width="";},footerSrcEls);}
if(scroll.bCollapse&&scrollY!==""){divBodyStyle.height=(divBody[0].offsetHeight+ header[0].offsetHeight)+"px";}
sanityWidth=table.outerWidth();if(scrollX===""){tableStyle.width="100%";if(ie67&&(table.find('tbody').height()>divBodyEl.offsetHeight||divBody.css('overflow-y')=="scroll")){tableStyle.width=_fnStringToCss(table.outerWidth()- barWidth);}}
else
{if(scrollXInner!==""){tableStyle.width=_fnStringToCss(scrollXInner);}
else if(sanityWidth==divBody.width()&&divBody.height()<table.height()){tableStyle.width=_fnStringToCss(sanityWidth-barWidth);if(table.outerWidth()>sanityWidth-barWidth){tableStyle.width=_fnStringToCss(sanityWidth);}}
else{tableStyle.width=_fnStringToCss(sanityWidth);}}
sanityWidth=table.outerWidth();_fnApplyToChildren(zeroOut,headerSrcEls);_fnApplyToChildren(function(nSizer){headerContent.push(nSizer.innerHTML);headerWidths.push(_fnStringToCss($(nSizer).css('width')));},headerSrcEls);_fnApplyToChildren(function(nToSize,i){nToSize.style.width=headerWidths[i];},headerTrgEls);$(headerSrcEls).height(0);if(footer)
{_fnApplyToChildren(zeroOut,footerSrcEls);_fnApplyToChildren(function(nSizer){footerWidths.push(_fnStringToCss($(nSizer).css('width')));},footerSrcEls);_fnApplyToChildren(function(nToSize,i){nToSize.style.width=footerWidths[i];},footerTrgEls);$(footerSrcEls).height(0);}
_fnApplyToChildren(function(nSizer,i){nSizer.innerHTML='<div class="dataTables_sizing" style="height:0;overflow:hidden;">'+headerContent[i]+'</div>';nSizer.style.width=headerWidths[i];},headerSrcEls);if(footer)
{_fnApplyToChildren(function(nSizer,i){nSizer.innerHTML="";nSizer.style.width=footerWidths[i];},footerSrcEls);}
if(table.outerWidth()<sanityWidth)
{correction=((divBodyEl.scrollHeight>divBodyEl.offsetHeight||divBody.css('overflow-y')=="scroll"))?sanityWidth+barWidth:sanityWidth;if(ie67&&(divBodyEl.scrollHeight>divBodyEl.offsetHeight||divBody.css('overflow-y')=="scroll")){tableStyle.width=_fnStringToCss(correction-barWidth);}
if(scrollX===""||scrollXInner!==""){_fnLog(settings,1,'Possible column misalignment',6);}}
else
{correction='100%';}
divBodyStyle.width=_fnStringToCss(correction);divHeaderStyle.width=_fnStringToCss(correction);if(footer){settings.nScrollFoot.style.width=_fnStringToCss(correction);}
if(!scrollY){if(ie67){divBodyStyle.height=_fnStringToCss(tableEl.offsetHeight+barWidth);}}
if(scrollY&&scroll.bCollapse){divBodyStyle.height=_fnStringToCss(scrollY);var iExtra=(scrollX&&tableEl.offsetWidth>divBodyEl.offsetWidth)?barWidth:0;if(tableEl.offsetHeight<divBodyEl.offsetHeight){divBodyStyle.height=_fnStringToCss(tableEl.offsetHeight+iExtra);}}
var iOuterWidth=table.outerWidth();divHeaderTable[0].style.width=_fnStringToCss(iOuterWidth);divHeaderInnerStyle.width=_fnStringToCss(iOuterWidth);var bScrolling=table.height()>divBodyEl.clientHeight||divBody.css('overflow-y')=="scroll";var padding='padding'+(browser.bScrollbarLeft?'Left':'Right');divHeaderInnerStyle[padding]=bScrolling?barWidth+"px":"0px";if(footer){divFooterTable[0].style.width=_fnStringToCss(iOuterWidth);divFooterInner[0].style.width=_fnStringToCss(iOuterWidth);divFooterInner[0].style[padding]=bScrolling?barWidth+"px":"0px";}
divBody.scroll();if((settings.bSorted||settings.bFiltered)&&!settings._drawHold){divBodyEl.scrollTop=0;}}
function _fnApplyToChildren(fn,an1,an2)
{var index=0,i=0,iLen=an1.length;var nNode1,nNode2;while(i<iLen){nNode1=an1[i].firstChild;nNode2=an2?an2[i].firstChild:null;while(nNode1){if(nNode1.nodeType===1){if(an2){fn(nNode1,nNode2,index);}
else{fn(nNode1,index);}
index++;}
nNode1=nNode1.nextSibling;nNode2=an2?nNode2.nextSibling:null;}
i++;}}
var __re_html_remove=/<.*?>/g;function _fnCalculateColumnWidths(oSettings)
{var
table=oSettings.nTable,columns=oSettings.aoColumns,scroll=oSettings.oScroll,scrollY=scroll.sY,scrollX=scroll.sX,scrollXInner=scroll.sXInner,columnCount=columns.length,visibleColumns=_fnGetColumns(oSettings,'bVisible'),headerCells=$('th',oSettings.nTHead),tableWidthAttr=table.getAttribute('width'),tableContainer=table.parentNode,userInputs=false,i,column,columnIdx,width,outerWidth;for(i=0;i<visibleColumns.length;i++){column=columns[visibleColumns[i]];if(column.sWidth!==null){column.sWidth=_fnConvertToWidth(column.sWidthOrig,tableContainer);userInputs=true;}}
if(!userInputs&&!scrollX&&!scrollY&&columnCount==_fnVisbleColumns(oSettings)&&columnCount==headerCells.length){for(i=0;i<columnCount;i++){columns[i].sWidth=_fnStringToCss(headerCells.eq(i).width());}}
else
{var tmpTable=$(table).clone().empty().css('visibility','hidden').removeAttr('id').append($(oSettings.nTHead).clone(false)).append($(oSettings.nTFoot).clone(false)).append($('<tbody><tr/></tbody>'));tmpTable.find('tfoot th, tfoot td').css('width','');var tr=tmpTable.find('tbody tr');headerCells=_fnGetUniqueThs(oSettings,tmpTable.find('thead')[0]);for(i=0;i<visibleColumns.length;i++){column=columns[visibleColumns[i]];headerCells[i].style.width=column.sWidthOrig!==null&&column.sWidthOrig!==''?_fnStringToCss(column.sWidthOrig):'';}
if(oSettings.aoData.length){for(i=0;i<visibleColumns.length;i++){columnIdx=visibleColumns[i];column=columns[columnIdx];$(_fnGetWidestNode(oSettings,columnIdx)).clone(false).append(column.sContentPadding).appendTo(tr);}}
tmpTable.appendTo(tableContainer);if(scrollX&&scrollXInner){tmpTable.width(scrollXInner);}
else if(scrollX){tmpTable.css('width','auto');if(tmpTable.width()<tableContainer.offsetWidth){tmpTable.width(tableContainer.offsetWidth);}}
else if(scrollY){tmpTable.width(tableContainer.offsetWidth);}
else if(tableWidthAttr){tmpTable.width(tableWidthAttr);}
_fnScrollingWidthAdjust(oSettings,tmpTable[0]);if(scrollX)
{var total=0;for(i=0;i<visibleColumns.length;i++){column=columns[visibleColumns[i]];outerWidth=$(headerCells[i]).outerWidth();total+=column.sWidthOrig===null?outerWidth:parseInt(column.sWidth,10)+ outerWidth- $(headerCells[i]).width();}
tmpTable.width(_fnStringToCss(total));table.style.width=_fnStringToCss(total);}
for(i=0;i<visibleColumns.length;i++){column=columns[visibleColumns[i]];width=$(headerCells[i]).width();if(width){column.sWidth=_fnStringToCss(width);}}
table.style.width=_fnStringToCss(tmpTable.css('width'));tmpTable.remove();}
if(tableWidthAttr){table.style.width=_fnStringToCss(tableWidthAttr);}
if((tableWidthAttr||scrollX)&&!oSettings._reszEvt){$(window).bind('resize.DT-'+oSettings.sInstance,_fnThrottle(function(){_fnAdjustColumnSizing(oSettings);}));oSettings._reszEvt=true;}}
function _fnThrottle(fn,freq){var
frequency=freq!==undefined?freq:200,last,timer;return function(){var
that=this,now=+new Date(),args=arguments;if(last&&now<last+ frequency){clearTimeout(timer);timer=setTimeout(function(){last=undefined;fn.apply(that,args);},frequency);}
else if(last){last=now;fn.apply(that,args);}
else{last=now;}};}
function _fnConvertToWidth(width,parent)
{if(!width){return 0;}
var n=$('<div/>').css('width',_fnStringToCss(width)).appendTo(parent||document.body);var val=n[0].offsetWidth;n.remove();return val;}
function _fnScrollingWidthAdjust(settings,n)
{var scroll=settings.oScroll;if(scroll.sX||scroll.sY){var correction=!scroll.sX?scroll.iBarWidth:0;n.style.width=_fnStringToCss($(n).outerWidth()- correction);}}
function _fnGetWidestNode(settings,colIdx)
{var idx=_fnGetMaxLenString(settings,colIdx);if(idx<0){return null;}
var data=settings.aoData[idx];return!data.nTr?$('<td/>').html(_fnGetCellData(settings,idx,colIdx,'display'))[0]:data.anCells[colIdx];}
function _fnGetMaxLenString(settings,colIdx)
{var s,max=-1,maxIdx=-1;for(var i=0,ien=settings.aoData.length;i<ien;i++){s=_fnGetCellData(settings,i,colIdx,'display')+'';s=s.replace(__re_html_remove,'');if(s.length>max){max=s.length;maxIdx=i;}}
return maxIdx;}
function _fnStringToCss(s)
{if(s===null){return'0px';}
if(typeof s=='number'){return s<0?'0px':s+'px';}
return s.match(/\d$/)?s+'px':s;}
function _fnScrollBarWidth()
{if(!DataTable.__scrollbarWidth){var inner=$('<p/>').css({width:'100%',height:200,padding:0})[0];var outer=$('<div/>').css({position:'absolute',top:0,left:0,width:200,height:150,padding:0,overflow:'hidden',visibility:'hidden'}).append(inner).appendTo('body');var w1=inner.offsetWidth;outer.css('overflow','scroll');var w2=inner.offsetWidth;if(w1===w2){w2=outer[0].clientWidth;}
outer.remove();DataTable.__scrollbarWidth=w1- w2;}
return DataTable.__scrollbarWidth;}
function _fnSortFlatten(settings)
{var
i,iLen,k,kLen,aSort=[],aiOrig=[],aoColumns=settings.aoColumns,aDataSort,iCol,sType,srcCol,fixed=settings.aaSortingFixed,fixedObj=$.isPlainObject(fixed),nestedSort=[],add=function(a){if(a.length&&!$.isArray(a[0])){nestedSort.push(a);}
else{nestedSort.push.apply(nestedSort,a);}};if($.isArray(fixed)){add(fixed);}
if(fixedObj&&fixed.pre){add(fixed.pre);}
add(settings.aaSorting);if(fixedObj&&fixed.post){add(fixed.post);}
for(i=0;i<nestedSort.length;i++)
{srcCol=nestedSort[i][0];aDataSort=aoColumns[srcCol].aDataSort;for(k=0,kLen=aDataSort.length;k<kLen;k++)
{iCol=aDataSort[k];sType=aoColumns[iCol].sType||'string';if(nestedSort[i]._idx===undefined){nestedSort[i]._idx=$.inArray(nestedSort[i][1],aoColumns[iCol].asSorting);}
aSort.push({src:srcCol,col:iCol,dir:nestedSort[i][1],index:nestedSort[i]._idx,type:sType,formatter:DataTable.ext.type.order[sType+"-pre"]});}}
return aSort;}
function _fnSort(oSettings)
{var
i,ien,iLen,j,jLen,k,kLen,sDataType,nTh,aiOrig=[],oExtSort=DataTable.ext.type.order,aoData=oSettings.aoData,aoColumns=oSettings.aoColumns,aDataSort,data,iCol,sType,oSort,formatters=0,sortCol,displayMaster=oSettings.aiDisplayMaster,aSort;_fnColumnTypes(oSettings);aSort=_fnSortFlatten(oSettings);for(i=0,ien=aSort.length;i<ien;i++){sortCol=aSort[i];if(sortCol.formatter){formatters++;}
_fnSortData(oSettings,sortCol.col);}
if(_fnDataSource(oSettings)!='ssp'&&aSort.length!==0)
{for(i=0,iLen=displayMaster.length;i<iLen;i++){aiOrig[displayMaster[i]]=i;}
if(formatters===aSort.length){displayMaster.sort(function(a,b){var
x,y,k,test,sort,len=aSort.length,dataA=aoData[a]._aSortData,dataB=aoData[b]._aSortData;for(k=0;k<len;k++){sort=aSort[k];x=dataA[sort.col];y=dataB[sort.col];test=x<y?-1:x>y?1:0;if(test!==0){return sort.dir==='asc'?test:-test;}}
x=aiOrig[a];y=aiOrig[b];return x<y?-1:x>y?1:0;});}
else{displayMaster.sort(function(a,b){var
x,y,k,l,test,sort,fn,len=aSort.length,dataA=aoData[a]._aSortData,dataB=aoData[b]._aSortData;for(k=0;k<len;k++){sort=aSort[k];x=dataA[sort.col];y=dataB[sort.col];fn=oExtSort[sort.type+"-"+sort.dir]||oExtSort["string-"+sort.dir];test=fn(x,y);if(test!==0){return test;}}
x=aiOrig[a];y=aiOrig[b];return x<y?-1:x>y?1:0;});}}
oSettings.bSorted=true;}
function _fnSortAria(settings)
{var label;var nextSort;var columns=settings.aoColumns;var aSort=_fnSortFlatten(settings);var oAria=settings.oLanguage.oAria;for(var i=0,iLen=columns.length;i<iLen;i++)
{var col=columns[i];var asSorting=col.asSorting;var sTitle=col.sTitle.replace(/<.*?>/g,"");var th=col.nTh;th.removeAttribute('aria-sort');if(col.bSortable){if(aSort.length>0&&aSort[0].col==i){th.setAttribute('aria-sort',aSort[0].dir=="asc"?"ascending":"descending");nextSort=asSorting[aSort[0].index+1]||asSorting[0];}
else{nextSort=asSorting[0];}
label=sTitle+(nextSort==="asc"?oAria.sSortAscending:oAria.sSortDescending);}
else{label=sTitle;}
th.setAttribute('aria-label',label);}}
function _fnSortListener(settings,colIdx,append,callback)
{var col=settings.aoColumns[colIdx];var sorting=settings.aaSorting;var asSorting=col.asSorting;var nextSortIdx;var next=function(a,overflow){var idx=a._idx;if(idx===undefined){idx=$.inArray(a[1],asSorting);}
return idx+1<asSorting.length?idx+1:overflow?null:0;};if(typeof sorting[0]==='number'){sorting=settings.aaSorting=[sorting];}
if(append&&settings.oFeatures.bSortMulti){var sortIdx=$.inArray(colIdx,_pluck(sorting,'0'));if(sortIdx!==-1){nextSortIdx=next(sorting[sortIdx],true);if(nextSortIdx===null){sorting.splice(sortIdx,1);}
else{sorting[sortIdx][1]=asSorting[nextSortIdx];sorting[sortIdx]._idx=nextSortIdx;}}
else{sorting.push([colIdx,asSorting[0],0]);sorting[sorting.length-1]._idx=0;}}
else if(sorting.length&&sorting[0][0]==colIdx){nextSortIdx=next(sorting[0]);sorting.length=1;sorting[0][1]=asSorting[nextSortIdx];sorting[0]._idx=nextSortIdx;}
else{sorting.length=0;sorting.push([colIdx,asSorting[0]]);sorting[0]._idx=0;}
_fnReDraw(settings);if(typeof callback=='function'){callback(settings);}}
function _fnSortAttachListener(settings,attachTo,colIdx,callback)
{var col=settings.aoColumns[colIdx];_fnBindAction(attachTo,{},function(e){if(col.bSortable===false){return;}
if(settings.oFeatures.bProcessing){_fnProcessingDisplay(settings,true);setTimeout(function(){_fnSortListener(settings,colIdx,e.shiftKey,callback);if(_fnDataSource(settings)!=='ssp'){_fnProcessingDisplay(settings,false);}},0);}
else{_fnSortListener(settings,colIdx,e.shiftKey,callback);}});}
function _fnSortingClasses(settings)
{var oldSort=settings.aLastSort;var sortClass=settings.oClasses.sSortColumn;var sort=_fnSortFlatten(settings);var features=settings.oFeatures;var i,ien,colIdx;if(features.bSort&&features.bSortClasses){for(i=0,ien=oldSort.length;i<ien;i++){colIdx=oldSort[i].src;$(_pluck(settings.aoData,'anCells',colIdx)).removeClass(sortClass+(i<2?i+1:3));}
for(i=0,ien=sort.length;i<ien;i++){colIdx=sort[i].src;$(_pluck(settings.aoData,'anCells',colIdx)).addClass(sortClass+(i<2?i+1:3));}}
settings.aLastSort=sort;}
function _fnSortData(settings,idx)
{var column=settings.aoColumns[idx];var customSort=DataTable.ext.order[column.sSortDataType];var customData;if(customSort){customData=customSort.call(settings.oInstance,settings,idx,_fnColumnIndexToVisible(settings,idx));}
var row,cellData;var formatter=DataTable.ext.type.order[column.sType+"-pre"];for(var i=0,ien=settings.aoData.length;i<ien;i++){row=settings.aoData[i];if(!row._aSortData){row._aSortData=[];}
if(!row._aSortData[idx]||customSort){cellData=customSort?customData[i]:_fnGetCellData(settings,i,idx,'sort');row._aSortData[idx]=formatter?formatter(cellData):cellData;}}}
function _fnSaveState(settings)
{if(!settings.oFeatures.bStateSave||settings.bDestroying)
{return;}
var state={time:+new Date(),start:settings._iDisplayStart,length:settings._iDisplayLength,order:$.extend(true,[],settings.aaSorting),search:_fnSearchToCamel(settings.oPreviousSearch),columns:$.map(settings.aoColumns,function(col,i){return{visible:col.bVisible,search:_fnSearchToCamel(settings.aoPreSearchCols[i])};})};_fnCallbackFire(settings,"aoStateSaveParams",'stateSaveParams',[settings,state]);settings.oSavedState=state;settings.fnStateSaveCallback.call(settings.oInstance,settings,state);}
function _fnLoadState(settings,oInit)
{var i,ien;var columns=settings.aoColumns;if(!settings.oFeatures.bStateSave){return;}
var state=settings.fnStateLoadCallback.call(settings.oInstance,settings);if(!state||!state.time){return;}
var abStateLoad=_fnCallbackFire(settings,'aoStateLoadParams','stateLoadParams',[settings,state]);if($.inArray(false,abStateLoad)!==-1){return;}
var duration=settings.iStateDuration;if(duration>0&&state.time<+new Date()-(duration*1000)){return;}
if(columns.length!==state.columns.length){return;}
settings.oLoadedState=$.extend(true,{},state);settings._iDisplayStart=state.start;settings.iInitDisplayStart=state.start;settings._iDisplayLength=state.length;settings.aaSorting=[];$.each(state.order,function(i,col){settings.aaSorting.push(col[0]>=columns.length?[0,col[1]]:col);});$.extend(settings.oPreviousSearch,_fnSearchToHung(state.search));for(i=0,ien=state.columns.length;i<ien;i++){var col=state.columns[i];columns[i].bVisible=col.visible;$.extend(settings.aoPreSearchCols[i],_fnSearchToHung(col.search));}
_fnCallbackFire(settings,'aoStateLoaded','stateLoaded',[settings,state]);}
function _fnSettingsFromNode(table)
{var settings=DataTable.settings;var idx=$.inArray(table,_pluck(settings,'nTable'));return idx!==-1?settings[idx]:null;}
function _fnLog(settings,level,msg,tn)
{msg='DataTables warning: '+
(settings!==null?'table id='+settings.sTableId+' - ':'')+msg;if(tn){msg+='. For more information about this error, please see '+'http://datatables.net/tn/'+tn;}
if(!level){var ext=DataTable.ext;var type=ext.sErrMode||ext.errMode;if(type=='alert'){alert(msg);}
else{throw new Error(msg);}}
else if(window.console&&console.log){console.log(msg);}}
function _fnMap(ret,src,name,mappedName)
{if($.isArray(name)){$.each(name,function(i,val){if($.isArray(val)){_fnMap(ret,src,val[0],val[1]);}
else{_fnMap(ret,src,val);}});return;}
if(mappedName===undefined){mappedName=name;}
if(src[name]!==undefined){ret[mappedName]=src[name];}}
function _fnExtend(out,extender,breakRefs)
{var val;for(var prop in extender){if(extender.hasOwnProperty(prop)){val=extender[prop];if($.isPlainObject(val)){if(!$.isPlainObject(out[prop])){out[prop]={};}
$.extend(true,out[prop],val);}
else if(breakRefs&&prop!=='data'&&prop!=='aaData'&&$.isArray(val)){out[prop]=val.slice();}
else{out[prop]=val;}}}
return out;}
function _fnBindAction(n,oData,fn)
{$(n).bind('click.DT',oData,function(e){n.blur();fn(e);}).bind('keypress.DT',oData,function(e){if(e.which===13){e.preventDefault();fn(e);}}).bind('selectstart.DT',function(){return false;});}
function _fnCallbackReg(oSettings,sStore,fn,sName)
{if(fn)
{oSettings[sStore].push({"fn":fn,"sName":sName});}}
function _fnCallbackFire(settings,callbackArr,e,args)
{var ret=[];if(callbackArr){ret=$.map(settings[callbackArr].slice().reverse(),function(val,i){return val.fn.apply(settings.oInstance,args);});}
if(e!==null){$(settings.nTable).trigger(e+'.dt',args);}
return ret;}
function _fnLengthOverflow(settings)
{var
start=settings._iDisplayStart,end=settings.fnDisplayEnd(),len=settings._iDisplayLength;if(start>=end)
{start=end- len;}
if(len===-1||start<0)
{start=0;}
settings._iDisplayStart=start;}
function _fnRenderer(settings,type)
{var renderer=settings.renderer;var host=DataTable.ext.renderer[type];if($.isPlainObject(renderer)&&renderer[type]){return host[renderer[type]]||host._;}
else if(typeof renderer==='string'){return host[renderer]||host._;}
return host._;}
function _fnDataSource(settings)
{if(settings.oFeatures.bServerSide){return'ssp';}
else if(settings.ajax||settings.sAjaxSource){return'ajax';}
return'dom';}
DataTable=function(options)
{this.$=function(sSelector,oOpts)
{return this.api(true).$(sSelector,oOpts);};this._=function(sSelector,oOpts)
{return this.api(true).rows(sSelector,oOpts).data();};this.api=function(traditional)
{return traditional?new _Api(_fnSettingsFromNode(this[_ext.iApiIndex])):new _Api(this);};this.fnAddData=function(data,redraw)
{var api=this.api(true);var rows=$.isArray(data)&&($.isArray(data[0])||$.isPlainObject(data[0]))?api.rows.add(data):api.row.add(data);if(redraw===undefined||redraw){api.draw();}
return rows.flatten().toArray();};this.fnAdjustColumnSizing=function(bRedraw)
{var api=this.api(true).columns.adjust();var settings=api.settings()[0];var scroll=settings.oScroll;if(bRedraw===undefined||bRedraw){api.draw(false);}
else if(scroll.sX!==""||scroll.sY!==""){_fnScrollDraw(settings);}};this.fnClearTable=function(bRedraw)
{var api=this.api(true).clear();if(bRedraw===undefined||bRedraw){api.draw();}};this.fnClose=function(nTr)
{this.api(true).row(nTr).child.hide();};this.fnDeleteRow=function(target,callback,redraw)
{var api=this.api(true);var rows=api.rows(target);var settings=rows.settings()[0];var data=settings.aoData[rows[0][0]];rows.remove();if(callback){callback.call(this,settings,data);}
if(redraw===undefined||redraw){api.draw();}
return data;};this.fnDestroy=function(remove)
{this.api(true).destroy(remove);};this.fnDraw=function(complete)
{this.api(true).draw(!complete);};this.fnFilter=function(sInput,iColumn,bRegex,bSmart,bShowGlobal,bCaseInsensitive)
{var api=this.api(true);if(iColumn===null||iColumn===undefined){api.search(sInput,bRegex,bSmart,bCaseInsensitive);}
else{api.column(iColumn).search(sInput,bRegex,bSmart,bCaseInsensitive);}
api.draw();};this.fnGetData=function(src,col)
{var api=this.api(true);if(src!==undefined){var type=src.nodeName?src.nodeName.toLowerCase():'';return col!==undefined||type=='td'||type=='th'?api.cell(src,col).data():api.row(src).data()||null;}
return api.data().toArray();};this.fnGetNodes=function(iRow)
{var api=this.api(true);return iRow!==undefined?api.row(iRow).node():api.rows().nodes().flatten().toArray();};this.fnGetPosition=function(node)
{var api=this.api(true);var nodeName=node.nodeName.toUpperCase();if(nodeName=='TR'){return api.row(node).index();}
else if(nodeName=='TD'||nodeName=='TH'){var cell=api.cell(node).index();return[cell.row,cell.columnVisible,cell.column];}
return null;};this.fnIsOpen=function(nTr)
{return this.api(true).row(nTr).child.isShown();};this.fnOpen=function(nTr,mHtml,sClass)
{return this.api(true).row(nTr).child(mHtml,sClass).show().child()[0];};this.fnPageChange=function(mAction,bRedraw)
{var api=this.api(true).page(mAction);if(bRedraw===undefined||bRedraw){api.draw(false);}};this.fnSetColumnVis=function(iCol,bShow,bRedraw)
{var api=this.api(true).column(iCol).visible(bShow);if(bRedraw===undefined||bRedraw){api.columns.adjust().draw();}};this.fnSettings=function()
{return _fnSettingsFromNode(this[_ext.iApiIndex]);};this.fnSort=function(aaSort)
{this.api(true).order(aaSort).draw();};this.fnSortListener=function(nNode,iColumn,fnCallback)
{this.api(true).order.listener(nNode,iColumn,fnCallback);};this.fnUpdate=function(mData,mRow,iColumn,bRedraw,bAction)
{var api=this.api(true);if(iColumn===undefined||iColumn===null){api.row(mRow).data(mData);}
else{api.cell(mRow,iColumn).data(mData);}
if(bAction===undefined||bAction){api.columns.adjust();}
if(bRedraw===undefined||bRedraw){api.draw();}
return 0;};this.fnVersionCheck=_ext.fnVersionCheck;var _that=this;var emptyInit=options===undefined;var len=this.length;if(emptyInit){options={};}
this.oApi=this.internal=_ext.internal;for(var fn in DataTable.ext.internal){if(fn){this[fn]=_fnExternApiFunc(fn);}}
this.each(function(){var o={};var oInit=len>1?_fnExtend(o,options,true):options;var i=0,iLen,j,jLen,k,kLen;var sId=this.getAttribute('id');var bInitHandedOff=false;var defaults=DataTable.defaults;if(this.nodeName.toLowerCase()!='table')
{_fnLog(null,0,'Non-table node initialisation ('+this.nodeName+')',2);return;}
_fnCompatOpts(defaults);_fnCompatCols(defaults.column);_fnCamelToHungarian(defaults,defaults,true);_fnCamelToHungarian(defaults.column,defaults.column,true);_fnCamelToHungarian(defaults,oInit);var allSettings=DataTable.settings;for(i=0,iLen=allSettings.length;i<iLen;i++)
{if(allSettings[i].nTable==this)
{var bRetrieve=oInit.bRetrieve!==undefined?oInit.bRetrieve:defaults.bRetrieve;var bDestroy=oInit.bDestroy!==undefined?oInit.bDestroy:defaults.bDestroy;if(emptyInit||bRetrieve)
{return allSettings[i].oInstance;}
else if(bDestroy)
{allSettings[i].oInstance.fnDestroy();break;}
else
{_fnLog(allSettings[i],0,'Cannot reinitialise DataTable',3);return;}}
if(allSettings[i].sTableId==this.id)
{allSettings.splice(i,1);break;}}
if(sId===null||sId==="")
{sId="DataTables_Table_"+(DataTable.ext._unique++);this.id=sId;}
var oSettings=$.extend(true,{},DataTable.models.oSettings,{"nTable":this,"oApi":_that.internal,"oInit":oInit,"sDestroyWidth":$(this)[0].style.width,"sInstance":sId,"sTableId":sId});allSettings.push(oSettings);oSettings.oInstance=(_that.length===1)?_that:$(this).dataTable();_fnCompatOpts(oInit);if(oInit.oLanguage)
{_fnLanguageCompat(oInit.oLanguage);}
if(oInit.aLengthMenu&&!oInit.iDisplayLength)
{oInit.iDisplayLength=$.isArray(oInit.aLengthMenu[0])?oInit.aLengthMenu[0][0]:oInit.aLengthMenu[0];}
oInit=_fnExtend($.extend(true,{},defaults),oInit);_fnMap(oSettings.oFeatures,oInit,["bPaginate","bLengthChange","bFilter","bSort","bSortMulti","bInfo","bProcessing","bAutoWidth","bSortClasses","bServerSide","bDeferRender"]);_fnMap(oSettings,oInit,["asStripeClasses","ajax","fnServerData","fnFormatNumber","sServerMethod","aaSorting","aaSortingFixed","aLengthMenu","sPaginationType","sAjaxSource","sAjaxDataProp","iStateDuration","sDom","bSortCellsTop","iTabIndex","fnStateLoadCallback","fnStateSaveCallback","renderer","searchDelay",["iCookieDuration","iStateDuration"],["oSearch","oPreviousSearch"],["aoSearchCols","aoPreSearchCols"],["iDisplayLength","_iDisplayLength"],["bJQueryUI","bJUI"]]);_fnMap(oSettings.oScroll,oInit,[["sScrollX","sX"],["sScrollXInner","sXInner"],["sScrollY","sY"],["bScrollCollapse","bCollapse"]]);_fnMap(oSettings.oLanguage,oInit,"fnInfoCallback");_fnCallbackReg(oSettings,'aoDrawCallback',oInit.fnDrawCallback,'user');_fnCallbackReg(oSettings,'aoServerParams',oInit.fnServerParams,'user');_fnCallbackReg(oSettings,'aoStateSaveParams',oInit.fnStateSaveParams,'user');_fnCallbackReg(oSettings,'aoStateLoadParams',oInit.fnStateLoadParams,'user');_fnCallbackReg(oSettings,'aoStateLoaded',oInit.fnStateLoaded,'user');_fnCallbackReg(oSettings,'aoRowCallback',oInit.fnRowCallback,'user');_fnCallbackReg(oSettings,'aoRowCreatedCallback',oInit.fnCreatedRow,'user');_fnCallbackReg(oSettings,'aoHeaderCallback',oInit.fnHeaderCallback,'user');_fnCallbackReg(oSettings,'aoFooterCallback',oInit.fnFooterCallback,'user');_fnCallbackReg(oSettings,'aoInitComplete',oInit.fnInitComplete,'user');_fnCallbackReg(oSettings,'aoPreDrawCallback',oInit.fnPreDrawCallback,'user');var oClasses=oSettings.oClasses;if(oInit.bJQueryUI)
{$.extend(oClasses,DataTable.ext.oJUIClasses,oInit.oClasses);if(oInit.sDom===defaults.sDom&&defaults.sDom==="lfrtip")
{oSettings.sDom='<"H"lfr>t<"F"ip>';}
if(!oSettings.renderer){oSettings.renderer='jqueryui';}
else if($.isPlainObject(oSettings.renderer)&&!oSettings.renderer.header){oSettings.renderer.header='jqueryui';}}
else
{$.extend(oClasses,DataTable.ext.classes,oInit.oClasses);}
$(this).addClass(oClasses.sTable);if(oSettings.oScroll.sX!==""||oSettings.oScroll.sY!=="")
{oSettings.oScroll.iBarWidth=_fnScrollBarWidth();}
if(oSettings.oScroll.sX===true){oSettings.oScroll.sX='100%';}
if(oSettings.iInitDisplayStart===undefined)
{oSettings.iInitDisplayStart=oInit.iDisplayStart;oSettings._iDisplayStart=oInit.iDisplayStart;}
if(oInit.iDeferLoading!==null)
{oSettings.bDeferLoading=true;var tmp=$.isArray(oInit.iDeferLoading);oSettings._iRecordsDisplay=tmp?oInit.iDeferLoading[0]:oInit.iDeferLoading;oSettings._iRecordsTotal=tmp?oInit.iDeferLoading[1]:oInit.iDeferLoading;}
if(oInit.oLanguage.sUrl!=="")
{oSettings.oLanguage.sUrl=oInit.oLanguage.sUrl;$.getJSON(oSettings.oLanguage.sUrl,null,function(json){_fnLanguageCompat(json);_fnCamelToHungarian(defaults.oLanguage,json);$.extend(true,oSettings.oLanguage,oInit.oLanguage,json);_fnInitialise(oSettings);});bInitHandedOff=true;}
else
{$.extend(true,oSettings.oLanguage,oInit.oLanguage);}
if(oInit.asStripeClasses===null)
{oSettings.asStripeClasses=[oClasses.sStripeOdd,oClasses.sStripeEven];}
var stripeClasses=oSettings.asStripeClasses;var rowOne=$('tbody tr:eq(0)',this);if($.inArray(true,$.map(stripeClasses,function(el,i){return rowOne.hasClass(el);}))!==-1){$('tbody tr',this).removeClass(stripeClasses.join(' '));oSettings.asDestroyStripes=stripeClasses.slice();}
var anThs=[];var aoColumnsInit;var nThead=this.getElementsByTagName('thead');if(nThead.length!==0)
{_fnDetectHeader(oSettings.aoHeader,nThead[0]);anThs=_fnGetUniqueThs(oSettings);}
if(oInit.aoColumns===null)
{aoColumnsInit=[];for(i=0,iLen=anThs.length;i<iLen;i++)
{aoColumnsInit.push(null);}}
else
{aoColumnsInit=oInit.aoColumns;}
for(i=0,iLen=aoColumnsInit.length;i<iLen;i++)
{_fnAddColumn(oSettings,anThs?anThs[i]:null);}
_fnApplyColumnDefs(oSettings,oInit.aoColumnDefs,aoColumnsInit,function(iCol,oDef){_fnColumnOptions(oSettings,iCol,oDef);});if(rowOne.length){var a=function(cell,name){return cell.getAttribute('data-'+name)?name:null;};$.each(_fnGetRowElements(oSettings,rowOne[0]).cells,function(i,cell){var col=oSettings.aoColumns[i];if(col.mData===i){var sort=a(cell,'sort')||a(cell,'order');var filter=a(cell,'filter')||a(cell,'search');if(sort!==null||filter!==null){col.mData={_:i+'.display',sort:sort!==null?i+'.@data-'+sort:undefined,type:sort!==null?i+'.@data-'+sort:undefined,filter:filter!==null?i+'.@data-'+filter:undefined};_fnColumnOptions(oSettings,i);}}});}
var features=oSettings.oFeatures;if(oInit.bStateSave)
{features.bStateSave=true;_fnLoadState(oSettings,oInit);_fnCallbackReg(oSettings,'aoDrawCallback',_fnSaveState,'state_save');}
if(oInit.aaSorting===undefined)
{var sorting=oSettings.aaSorting;for(i=0,iLen=sorting.length;i<iLen;i++)
{sorting[i][1]=oSettings.aoColumns[i].asSorting[0];}}
_fnSortingClasses(oSettings);if(features.bSort)
{_fnCallbackReg(oSettings,'aoDrawCallback',function(){if(oSettings.bSorted){var aSort=_fnSortFlatten(oSettings);var sortedColumns={};$.each(aSort,function(i,val){sortedColumns[val.src]=val.dir;});_fnCallbackFire(oSettings,null,'order',[oSettings,aSort,sortedColumns]);_fnSortAria(oSettings);}});}
_fnCallbackReg(oSettings,'aoDrawCallback',function(){if(oSettings.bSorted||_fnDataSource(oSettings)==='ssp'||features.bDeferRender){_fnSortingClasses(oSettings);}},'sc');_fnBrowserDetect(oSettings);var captions=$(this).children('caption').each(function(){this._captionSide=$(this).css('caption-side');});var thead=$(this).children('thead');if(thead.length===0)
{thead=$('<thead/>').appendTo(this);}
oSettings.nTHead=thead[0];var tbody=$(this).children('tbody');if(tbody.length===0)
{tbody=$('<tbody/>').appendTo(this);}
oSettings.nTBody=tbody[0];var tfoot=$(this).children('tfoot');if(tfoot.length===0&&captions.length>0&&(oSettings.oScroll.sX!==""||oSettings.oScroll.sY!==""))
{tfoot=$('<tfoot/>').appendTo(this);}
if(tfoot.length===0||tfoot.children().length===0){$(this).addClass(oClasses.sNoFooter);}
else if(tfoot.length>0){oSettings.nTFoot=tfoot[0];_fnDetectHeader(oSettings.aoFooter,oSettings.nTFoot);}
if(oInit.aaData)
{for(i=0;i<oInit.aaData.length;i++)
{_fnAddData(oSettings,oInit.aaData[i]);}}
else if(oSettings.bDeferLoading||_fnDataSource(oSettings)=='dom')
{_fnAddTr(oSettings,$(oSettings.nTBody).children('tr'));}
oSettings.aiDisplay=oSettings.aiDisplayMaster.slice();oSettings.bInitialised=true;if(bInitHandedOff===false)
{_fnInitialise(oSettings);}});_that=null;return this;};var __apiStruct=[];var __arrayProto=Array.prototype;var _toSettings=function(mixed)
{var idx,jq;var settings=DataTable.settings;var tables=$.map(settings,function(el,i){return el.nTable;});if(!mixed){return[];}
else if(mixed.nTable&&mixed.oApi){return[mixed];}
else if(mixed.nodeName&&mixed.nodeName.toLowerCase()==='table'){idx=$.inArray(mixed,tables);return idx!==-1?[settings[idx]]:null;}
else if(mixed&&typeof mixed.settings==='function'){return mixed.settings().toArray();}
else if(typeof mixed==='string'){jq=$(mixed);}
else if(mixed instanceof $){jq=mixed;}
if(jq){return jq.map(function(i){idx=$.inArray(this,tables);return idx!==-1?settings[idx]:null;}).toArray();}};_Api=function(context,data)
{if(!this instanceof _Api){throw'DT API must be constructed as a new object';}
var settings=[];var ctxSettings=function(o){var a=_toSettings(o);if(a){settings.push.apply(settings,a);}};if($.isArray(context)){for(var i=0,ien=context.length;i<ien;i++){ctxSettings(context[i]);}}
else{ctxSettings(context);}
this.context=_unique(settings);if(data){this.push.apply(this,data.toArray?data.toArray():data);}
this.selector={rows:null,cols:null,opts:null};_Api.extend(this,this,__apiStruct);};DataTable.Api=_Api;_Api.prototype={concat:__arrayProto.concat,context:[],each:function(fn)
{for(var i=0,ien=this.length;i<ien;i++){fn.call(this,this[i],i,this);}
return this;},eq:function(idx)
{var ctx=this.context;return ctx.length>idx?new _Api(ctx[idx],this[idx]):null;},filter:function(fn)
{var a=[];if(__arrayProto.filter){a=__arrayProto.filter.call(this,fn,this);}
else{for(var i=0,ien=this.length;i<ien;i++){if(fn.call(this,this[i],i,this)){a.push(this[i]);}}}
return new _Api(this.context,a);},flatten:function()
{var a=[];return new _Api(this.context,a.concat.apply(a,this.toArray()));},join:__arrayProto.join,indexOf:__arrayProto.indexOf||function(obj,start)
{for(var i=(start||0),ien=this.length;i<ien;i++){if(this[i]===obj){return i;}}
return-1;},iterator:function(flatten,type,fn){var
a=[],ret,i,ien,j,jen,context=this.context,rows,items,item,selector=this.selector;if(typeof flatten==='string'){fn=type;type=flatten;flatten=false;}
for(i=0,ien=context.length;i<ien;i++){var apiInst=new _Api(context[i]);if(type==='table'){ret=fn.call(apiInst,context[i],i);if(ret!==undefined){a.push(ret);}}
else if(type==='columns'||type==='rows'){ret=fn.call(apiInst,context[i],this[i],i);if(ret!==undefined){a.push(ret);}}
else if(type==='column'||type==='column-rows'||type==='row'||type==='cell'){items=this[i];if(type==='column-rows'){rows=_selector_row_indexes(context[i],selector.opts);}
for(j=0,jen=items.length;j<jen;j++){item=items[j];if(type==='cell'){ret=fn.call(apiInst,context[i],item.row,item.column,i,j);}
else{ret=fn.call(apiInst,context[i],item,i,j,rows);}
if(ret!==undefined){a.push(ret);}}}}
if(a.length){var api=new _Api(context,flatten?a.concat.apply([],a):a);var apiSelector=api.selector;apiSelector.rows=selector.rows;apiSelector.cols=selector.cols;apiSelector.opts=selector.opts;return api;}
return this;},lastIndexOf:__arrayProto.lastIndexOf||function(obj,start)
{return this.indexOf.apply(this.toArray.reverse(),arguments);},length:0,map:function(fn)
{var a=[];if(__arrayProto.map){a=__arrayProto.map.call(this,fn,this);}
else{for(var i=0,ien=this.length;i<ien;i++){a.push(fn.call(this,this[i],i));}}
return new _Api(this.context,a);},pluck:function(prop)
{return this.map(function(el){return el[prop];});},pop:__arrayProto.pop,push:__arrayProto.push,reduce:__arrayProto.reduce||function(fn,init)
{return _fnReduce(this,fn,init,0,this.length,1);},reduceRight:__arrayProto.reduceRight||function(fn,init)
{return _fnReduce(this,fn,init,this.length-1,-1,-1);},reverse:__arrayProto.reverse,selector:null,shift:__arrayProto.shift,sort:__arrayProto.sort,splice:__arrayProto.splice,toArray:function()
{return __arrayProto.slice.call(this);},to$:function()
{return $(this);},toJQuery:function()
{return $(this);},unique:function()
{return new _Api(this.context,_unique(this));},unshift:__arrayProto.unshift};_Api.extend=function(scope,obj,ext)
{if(!obj||(!(obj instanceof _Api)&&!obj.__dt_wrapper)){return;}
var
i,ien,j,jen,struct,inner,methodScoping=function(scope,fn,struc){return function(){var ret=fn.apply(scope,arguments);_Api.extend(ret,ret,struc.methodExt);return ret;};};for(i=0,ien=ext.length;i<ien;i++){struct=ext[i];obj[struct.name]=typeof struct.val==='function'?methodScoping(scope,struct.val,struct):$.isPlainObject(struct.val)?{}:struct.val;obj[struct.name].__dt_wrapper=true;_Api.extend(scope,obj[struct.name],struct.propExt);}};_Api.register=_api_register=function(name,val)
{if($.isArray(name)){for(var j=0,jen=name.length;j<jen;j++){_Api.register(name[j],val);}
return;}
var
i,ien,heir=name.split('.'),struct=__apiStruct,key,method;var find=function(src,name){for(var i=0,ien=src.length;i<ien;i++){if(src[i].name===name){return src[i];}}
return null;};for(i=0,ien=heir.length;i<ien;i++){method=heir[i].indexOf('()')!==-1;key=method?heir[i].replace('()',''):heir[i];var src=find(struct,key);if(!src){src={name:key,val:{},methodExt:[],propExt:[]};struct.push(src);}
if(i===ien-1){src.val=val;}
else{struct=method?src.methodExt:src.propExt;}}};_Api.registerPlural=_api_registerPlural=function(pluralName,singularName,val){_Api.register(pluralName,val);_Api.register(singularName,function(){var ret=val.apply(this,arguments);if(ret===this){return this;}
else if(ret instanceof _Api){return ret.length?$.isArray(ret[0])?new _Api(ret.context,ret[0]):ret[0]:undefined;}
return ret;});};var __table_selector=function(selector,a)
{if(typeof selector==='number'){return[a[selector]];}
var nodes=$.map(a,function(el,i){return el.nTable;});return $(nodes).filter(selector).map(function(i){var idx=$.inArray(this,nodes);return a[idx];}).toArray();};_api_register('tables()',function(selector){return selector?new _Api(__table_selector(selector,this.context)):this;});_api_register('table()',function(selector){var tables=this.tables(selector);var ctx=tables.context;return ctx.length?new _Api(ctx[0]):tables;});_api_registerPlural('tables().nodes()','table().node()',function(){return this.iterator('table',function(ctx){return ctx.nTable;});});_api_registerPlural('tables().body()','table().body()',function(){return this.iterator('table',function(ctx){return ctx.nTBody;});});_api_registerPlural('tables().header()','table().header()',function(){return this.iterator('table',function(ctx){return ctx.nTHead;});});_api_registerPlural('tables().footer()','table().footer()',function(){return this.iterator('table',function(ctx){return ctx.nTFoot;});});_api_registerPlural('tables().containers()','table().container()',function(){return this.iterator('table',function(ctx){return ctx.nTableWrapper;});});_api_register('draw()',function(resetPaging){return this.iterator('table',function(settings){_fnReDraw(settings,resetPaging===false);});});_api_register('page()',function(action){if(action===undefined){return this.page.info().page;}
return this.iterator('table',function(settings){_fnPageChange(settings,action);});});_api_register('page.info()',function(action){if(this.context.length===0){return undefined;}
var
settings=this.context[0],start=settings._iDisplayStart,len=settings._iDisplayLength,visRecords=settings.fnRecordsDisplay(),all=len===-1;return{"page":all?0:Math.floor(start/len),"pages":all?1:Math.ceil(visRecords/len),"start":start,"end":settings.fnDisplayEnd(),"length":len,"recordsTotal":settings.fnRecordsTotal(),"recordsDisplay":visRecords};});_api_register('page.len()',function(len){if(len===undefined){return this.context.length!==0?this.context[0]._iDisplayLength:undefined;}
return this.iterator('table',function(settings){_fnLengthChange(settings,len);});});var __reload=function(settings,holdPosition,callback){if(_fnDataSource(settings)=='ssp'){_fnReDraw(settings,holdPosition);}
else{_fnProcessingDisplay(settings,true);_fnBuildAjax(settings,[],function(json){_fnClearTable(settings);var data=_fnAjaxDataSrc(settings,json);for(var i=0,ien=data.length;i<ien;i++){_fnAddData(settings,data[i]);}
_fnReDraw(settings,holdPosition);_fnProcessingDisplay(settings,false);});}
if(callback){var api=new _Api(settings);api.one('draw',function(){callback(api.ajax.json());});}};_api_register('ajax.json()',function(){var ctx=this.context;if(ctx.length>0){return ctx[0].json;}});_api_register('ajax.params()',function(){var ctx=this.context;if(ctx.length>0){return ctx[0].oAjaxData;}});_api_register('ajax.reload()',function(callback,resetPaging){return this.iterator('table',function(settings){__reload(settings,resetPaging===false,callback);});});_api_register('ajax.url()',function(url){var ctx=this.context;if(url===undefined){if(ctx.length===0){return undefined;}
ctx=ctx[0];return ctx.ajax?$.isPlainObject(ctx.ajax)?ctx.ajax.url:ctx.ajax:ctx.sAjaxSource;}
return this.iterator('table',function(settings){if($.isPlainObject(settings.ajax)){settings.ajax.url=url;}
else{settings.ajax=url;}});});_api_register('ajax.url().load()',function(callback,resetPaging){return this.iterator('table',function(ctx){__reload(ctx,resetPaging===false,callback);});});var _selector_run=function(selector,select)
{var
out=[],res,a,i,ien,j,jen,selectorType=typeof selector;if(!selector||selectorType==='string'||selectorType==='function'||selector.length===undefined){selector=[selector];}
for(i=0,ien=selector.length;i<ien;i++){a=selector[i]&&selector[i].split?selector[i].split(','):[selector[i]];for(j=0,jen=a.length;j<jen;j++){res=select(typeof a[j]==='string'?$.trim(a[j]):a[j]);if(res&&res.length){out.push.apply(out,res);}}}
return out;};var _selector_opts=function(opts)
{if(!opts){opts={};}
if(opts.filter&&!opts.search){opts.search=opts.filter;}
return{search:opts.search||'none',order:opts.order||'current',page:opts.page||'all'};};var _selector_first=function(inst)
{for(var i=0,ien=inst.length;i<ien;i++){if(inst[i].length>0){inst[0]=inst[i];inst.length=1;inst.context=[inst.context[i]];return inst;}}
inst.length=0;return inst;};var _selector_row_indexes=function(settings,opts)
{var
i,ien,tmp,a=[],displayFiltered=settings.aiDisplay,displayMaster=settings.aiDisplayMaster;var
search=opts.search,order=opts.order,page=opts.page;if(_fnDataSource(settings)=='ssp'){return search==='removed'?[]:_range(0,displayMaster.length);}
else if(page=='current'){for(i=settings._iDisplayStart,ien=settings.fnDisplayEnd();i<ien;i++){a.push(displayFiltered[i]);}}
else if(order=='current'||order=='applied'){a=search=='none'?displayMaster.slice():search=='applied'?displayFiltered.slice():$.map(displayMaster,function(el,i){return $.inArray(el,displayFiltered)===-1?el:null;});}
else if(order=='index'||order=='original'){for(i=0,ien=settings.aoData.length;i<ien;i++){if(search=='none'){a.push(i);}
else{tmp=$.inArray(i,displayFiltered);if((tmp===-1&&search=='removed')||(tmp>=0&&search=='applied'))
{a.push(i);}}}}
return a;};var __row_selector=function(settings,selector,opts)
{return _selector_run(selector,function(sel){var selInt=_intVal(sel);var i,ien;if(selInt!==null&&!opts){return[selInt];}
var rows=_selector_row_indexes(settings,opts);if(selInt!==null&&$.inArray(selInt,rows)!==-1){return[selInt];}
else if(!sel){return rows;}
var nodes=_pluck_order(settings.aoData,rows,'nTr');if(typeof sel==='function'){return $.map(rows,function(idx){var row=settings.aoData[idx];return sel(idx,row._aData,row.nTr)?idx:null;});}
if(sel.nodeName){if($.inArray(sel,nodes)!==-1){return[sel._DT_RowIndex];}}
return $(nodes).filter(sel).map(function(){return this._DT_RowIndex;}).toArray();});};_api_register('rows()',function(selector,opts){if(selector===undefined){selector='';}
else if($.isPlainObject(selector)){opts=selector;selector='';}
opts=_selector_opts(opts);var inst=this.iterator('table',function(settings){return __row_selector(settings,selector,opts);});inst.selector.rows=selector;inst.selector.opts=opts;return inst;});_api_register('rows().nodes()',function(){return this.iterator('row',function(settings,row){return settings.aoData[row].nTr||undefined;});});_api_register('rows().data()',function(){return this.iterator(true,'rows',function(settings,rows){return _pluck_order(settings.aoData,rows,'_aData');});});_api_registerPlural('rows().cache()','row().cache()',function(type){return this.iterator('row',function(settings,row){var r=settings.aoData[row];return type==='search'?r._aFilterData:r._aSortData;});});_api_registerPlural('rows().invalidate()','row().invalidate()',function(src){return this.iterator('row',function(settings,row){_fnInvalidateRow(settings,row,src);});});_api_registerPlural('rows().indexes()','row().index()',function(){return this.iterator('row',function(settings,row){return row;});});_api_registerPlural('rows().remove()','row().remove()',function(){var that=this;return this.iterator('row',function(settings,row,thatIdx){var data=settings.aoData;data.splice(row,1);for(var i=0,ien=data.length;i<ien;i++){if(data[i].nTr!==null){data[i].nTr._DT_RowIndex=i;}}
var displayIndex=$.inArray(row,settings.aiDisplay);_fnDeleteIndex(settings.aiDisplayMaster,row);_fnDeleteIndex(settings.aiDisplay,row);_fnDeleteIndex(that[thatIdx],row,false);_fnLengthOverflow(settings);});});_api_register('rows.add()',function(rows){var newRows=this.iterator('table',function(settings){var row,i,ien;var out=[];for(i=0,ien=rows.length;i<ien;i++){row=rows[i];if(row.nodeName&&row.nodeName.toUpperCase()==='TR'){out.push(_fnAddTr(settings,row)[0]);}
else{out.push(_fnAddData(settings,row));}}
return out;});var modRows=this.rows(-1);modRows.pop();modRows.push.apply(modRows,newRows.toArray());return modRows;});_api_register('row()',function(selector,opts){return _selector_first(this.rows(selector,opts));});_api_register('row().data()',function(data){var ctx=this.context;if(data===undefined){return ctx.length&&this.length?ctx[0].aoData[this[0]]._aData:undefined;}
ctx[0].aoData[this[0]]._aData=data;_fnInvalidateRow(ctx[0],this[0],'data');return this;});_api_register('row().node()',function(){var ctx=this.context;return ctx.length&&this.length?ctx[0].aoData[this[0]].nTr||null:null;});_api_register('row.add()',function(row){if(row instanceof $&&row.length){row=row[0];}
var rows=this.iterator('table',function(settings){if(row.nodeName&&row.nodeName.toUpperCase()==='TR'){return _fnAddTr(settings,row)[0];}
return _fnAddData(settings,row);});return this.row(rows[0]);});var __details_add=function(ctx,row,data,klass)
{var rows=[];var addRow=function(r,k){if(r.nodeName&&r.nodeName.toLowerCase()==='tr'){rows.push(r);}
else{var created=$('<tr><td/></tr>').addClass(k);$('td',created).addClass(k).html(r)
[0].colSpan=_fnVisbleColumns(ctx);rows.push(created[0]);}};if($.isArray(data)||data instanceof $){for(var i=0,ien=data.length;i<ien;i++){addRow(data[i],klass);}}
else{addRow(data,klass);}
if(row._details){row._details.remove();}
row._details=$(rows);if(row._detailsShow){row._details.insertAfter(row.nTr);}};var __details_remove=function(api,idx)
{var ctx=api.context;if(ctx.length){var row=ctx[0].aoData[idx!==undefined?idx:api[0]];if(row._details){row._details.remove();row._detailsShow=undefined;row._details=undefined;}}};var __details_display=function(api,show){var ctx=api.context;if(ctx.length&&api.length){var row=ctx[0].aoData[api[0]];if(row._details){row._detailsShow=show;if(show){row._details.insertAfter(row.nTr);}
else{row._details.detach();}
__details_events(ctx[0]);}}};var __details_events=function(settings)
{var api=new _Api(settings);var namespace='.dt.DT_details';var drawEvent='draw'+namespace;var colvisEvent='column-visibility'+namespace;var destroyEvent='destroy'+namespace;var data=settings.aoData;api.off(drawEvent+' '+ colvisEvent+' '+ destroyEvent);if(_pluck(data,'_details').length>0){api.on(drawEvent,function(e,ctx){if(settings!==ctx){return;}
api.rows({page:'current'}).eq(0).each(function(idx){var row=data[idx];if(row._detailsShow){row._details.insertAfter(row.nTr);}});});api.on(colvisEvent,function(e,ctx,idx,vis){if(settings!==ctx){return;}
var row,visible=_fnVisbleColumns(ctx);for(var i=0,ien=data.length;i<ien;i++){row=data[i];if(row._details){row._details.children('td[colspan]').attr('colspan',visible);}}});api.on(destroyEvent,function(e,ctx){if(settings!==ctx){return;}
for(var i=0,ien=data.length;i<ien;i++){if(data[i]._details){__details_remove(api,i);}}});}};var _emp='';var _child_obj=_emp+'row().child';var _child_mth=_child_obj+'()';_api_register(_child_mth,function(data,klass){var ctx=this.context;if(data===undefined){return ctx.length&&this.length?ctx[0].aoData[this[0]]._details:undefined;}
else if(data===true){this.child.show();}
else if(data===false){__details_remove(this);}
else if(ctx.length&&this.length){__details_add(ctx[0],ctx[0].aoData[this[0]],data,klass);}
return this;});_api_register([_child_obj+'.show()',_child_mth+'.show()'],function(show){__details_display(this,true);return this;});_api_register([_child_obj+'.hide()',_child_mth+'.hide()'],function(){__details_display(this,false);return this;});_api_register([_child_obj+'.remove()',_child_mth+'.remove()'],function(){__details_remove(this);return this;});_api_register(_child_obj+'.isShown()',function(){var ctx=this.context;if(ctx.length&&this.length){return ctx[0].aoData[this[0]]._detailsShow||false;}
return false;});var __re_column_selector=/^(.+):(name|visIdx|visible)$/;var __columnData=function(settings,column,r1,r2,rows){var a=[];for(var row=0,ien=rows.length;row<ien;row++){a.push(_fnGetCellData(settings,rows[row],column));}
return a;};var __column_selector=function(settings,selector,opts)
{var
columns=settings.aoColumns,names=_pluck(columns,'sName'),nodes=_pluck(columns,'nTh');return _selector_run(selector,function(s){var selInt=_intVal(s);if(s===''){return _range(columns.length);}
if(selInt!==null){return[selInt>=0?selInt:columns.length+ selInt];}
if(typeof s==='function'){var rows=_selector_row_indexes(settings,opts);return $.map(columns,function(col,idx){return s(idx,__columnData(settings,idx,0,0,rows),nodes[idx])?idx:null;});}
var match=typeof s==='string'?s.match(__re_column_selector):'';if(match){switch(match[2]){case'visIdx':case'visible':var idx=parseInt(match[1],10);if(idx<0){var visColumns=$.map(columns,function(col,i){return col.bVisible?i:null;});return[visColumns[visColumns.length+ idx]];}
return[_fnVisibleToColumnIndex(settings,idx)];case'name':return $.map(names,function(name,i){return name===match[1]?i:null;});}}
else{return $(nodes).filter(s).map(function(){return $.inArray(this,nodes);}).toArray();}});};var __setColumnVis=function(settings,column,vis,recalc){var
cols=settings.aoColumns,col=cols[column],data=settings.aoData,row,cells,i,ien,tr;if(vis===undefined){return col.bVisible;}
if(col.bVisible===vis){return;}
if(vis){var insertBefore=$.inArray(true,_pluck(cols,'bVisible'),column+1);for(i=0,ien=data.length;i<ien;i++){tr=data[i].nTr;cells=data[i].anCells;if(tr){tr.insertBefore(cells[column],cells[insertBefore]||null);}}}
else{$(_pluck(settings.aoData,'anCells',column)).detach();}
col.bVisible=vis;_fnDrawHead(settings,settings.aoHeader);_fnDrawHead(settings,settings.aoFooter);if(recalc===undefined||recalc){_fnAdjustColumnSizing(settings);if(settings.oScroll.sX||settings.oScroll.sY){_fnScrollDraw(settings);}}
_fnCallbackFire(settings,null,'column-visibility',[settings,column,vis]);_fnSaveState(settings);};_api_register('columns()',function(selector,opts){if(selector===undefined){selector='';}
else if($.isPlainObject(selector)){opts=selector;selector='';}
opts=_selector_opts(opts);var inst=this.iterator('table',function(settings){return __column_selector(settings,selector,opts);});inst.selector.cols=selector;inst.selector.opts=opts;return inst;});_api_registerPlural('columns().header()','column().header()',function(selector,opts){return this.iterator('column',function(settings,column){return settings.aoColumns[column].nTh;});});_api_registerPlural('columns().footer()','column().footer()',function(selector,opts){return this.iterator('column',function(settings,column){return settings.aoColumns[column].nTf;});});_api_registerPlural('columns().data()','column().data()',function(){return this.iterator('column-rows',__columnData);});_api_registerPlural('columns().dataSrc()','column().dataSrc()',function(){return this.iterator('column',function(settings,column){return settings.aoColumns[column].mData;});});_api_registerPlural('columns().cache()','column().cache()',function(type){return this.iterator('column-rows',function(settings,column,i,j,rows){return _pluck_order(settings.aoData,rows,type==='search'?'_aFilterData':'_aSortData',column);});});_api_registerPlural('columns().nodes()','column().nodes()',function(){return this.iterator('column-rows',function(settings,column,i,j,rows){return _pluck_order(settings.aoData,rows,'anCells',column);});});_api_registerPlural('columns().visible()','column().visible()',function(vis,calc){return this.iterator('column',function(settings,column){return vis===undefined?settings.aoColumns[column].bVisible:__setColumnVis(settings,column,vis,calc);});});_api_registerPlural('columns().indexes()','column().index()',function(type){return this.iterator('column',function(settings,column){return type==='visible'?_fnColumnIndexToVisible(settings,column):column;});});_api_register('columns.adjust()',function(){return this.iterator('table',function(settings){_fnAdjustColumnSizing(settings);});});_api_register('column.index()',function(type,idx){if(this.context.length!==0){var ctx=this.context[0];if(type==='fromVisible'||type==='toData'){return _fnVisibleToColumnIndex(ctx,idx);}
else if(type==='fromData'||type==='toVisible'){return _fnColumnIndexToVisible(ctx,idx);}}});_api_register('column()',function(selector,opts){return _selector_first(this.columns(selector,opts));});var __cell_selector=function(settings,selector,opts)
{var data=settings.aoData;var rows=_selector_row_indexes(settings,opts);var cells=_pluck_order(data,rows,'anCells');var allCells=$([].concat.apply([],cells));var row;var columns=settings.aoColumns.length;var a,i,ien,j,o,host;return _selector_run(selector,function(s){var fnSelector=typeof s==='function';if(s===null||s===undefined||fnSelector){a=[];for(i=0,ien=rows.length;i<ien;i++){row=rows[i];for(j=0;j<columns;j++){o={row:row,column:j};if(fnSelector){host=settings.aoData[row];if(s(o,_fnGetCellData(settings,row,j),host.anCells[j])){a.push(o);}}
else{a.push(o);}}}
return a;}
if($.isPlainObject(s)){return[s];}
return allCells.filter(s).map(function(i,el){row=el.parentNode._DT_RowIndex;return{row:row,column:$.inArray(el,data[row].anCells)};}).toArray();});};_api_register('cells()',function(rowSelector,columnSelector,opts){if($.isPlainObject(rowSelector)){if(typeof rowSelector.row!==undefined){opts=columnSelector;columnSelector=null;}
else{opts=rowSelector;rowSelector=null;}}
if($.isPlainObject(columnSelector)){opts=columnSelector;columnSelector=null;}
if(columnSelector===null||columnSelector===undefined){return this.iterator('table',function(settings){return __cell_selector(settings,rowSelector,_selector_opts(opts));});}
var columns=this.columns(columnSelector,opts);var rows=this.rows(rowSelector,opts);var a,i,ien,j,jen;var cells=this.iterator('table',function(settings,idx){a=[];for(i=0,ien=rows[idx].length;i<ien;i++){for(j=0,jen=columns[idx].length;j<jen;j++){a.push({row:rows[idx][i],column:columns[idx][j]});}}
return a;});$.extend(cells.selector,{cols:columnSelector,rows:rowSelector,opts:opts});return cells;});_api_registerPlural('cells().nodes()','cell().node()',function(){return this.iterator('cell',function(settings,row,column){return settings.aoData[row].anCells[column];});});_api_register('cells().data()',function(){return this.iterator('cell',function(settings,row,column){return _fnGetCellData(settings,row,column);});});_api_registerPlural('cells().cache()','cell().cache()',function(type){type=type==='search'?'_aFilterData':'_aSortData';return this.iterator('cell',function(settings,row,column){return settings.aoData[row][type][column];});});_api_registerPlural('cells().render()','cell().render()',function(type){return this.iterator('cell',function(settings,row,column){return _fnGetCellData(settings,row,column,type);});});_api_registerPlural('cells().indexes()','cell().index()',function(){return this.iterator('cell',function(settings,row,column){return{row:row,column:column,columnVisible:_fnColumnIndexToVisible(settings,column)};});});_api_register(['cells().invalidate()','cell().invalidate()'],function(src){var selector=this.selector;this.rows(selector.rows,selector.opts).invalidate(src);return this;});_api_register('cell()',function(rowSelector,columnSelector,opts){return _selector_first(this.cells(rowSelector,columnSelector,opts));});_api_register('cell().data()',function(data){var ctx=this.context;var cell=this[0];if(data===undefined){return ctx.length&&cell.length?_fnGetCellData(ctx[0],cell[0].row,cell[0].column):undefined;}
_fnSetCellData(ctx[0],cell[0].row,cell[0].column,data);_fnInvalidateRow(ctx[0],cell[0].row,'data',cell[0].column);return this;});_api_register('order()',function(order,dir){var ctx=this.context;if(order===undefined){return ctx.length!==0?ctx[0].aaSorting:undefined;}
if(typeof order==='number'){order=[[order,dir]];}
else if(!$.isArray(order[0])){order=Array.prototype.slice.call(arguments);}
return this.iterator('table',function(settings){settings.aaSorting=order.slice();});});_api_register('order.listener()',function(node,column,callback){return this.iterator('table',function(settings){_fnSortAttachListener(settings,node,column,callback);});});_api_register(['columns().order()','column().order()'],function(dir){var that=this;return this.iterator('table',function(settings,i){var sort=[];$.each(that[i],function(j,col){sort.push([col,dir]);});settings.aaSorting=sort;});});_api_register('search()',function(input,regex,smart,caseInsen){var ctx=this.context;if(input===undefined){return ctx.length!==0?ctx[0].oPreviousSearch.sSearch:undefined;}
return this.iterator('table',function(settings){if(!settings.oFeatures.bFilter){return;}
_fnFilterComplete(settings,$.extend({},settings.oPreviousSearch,{"sSearch":input+"","bRegex":regex===null?false:regex,"bSmart":smart===null?true:smart,"bCaseInsensitive":caseInsen===null?true:caseInsen}),1);});});_api_registerPlural('columns().search()','column().search()',function(input,regex,smart,caseInsen){return this.iterator('column',function(settings,column){var preSearch=settings.aoPreSearchCols;if(input===undefined){return preSearch[column].sSearch;}
if(!settings.oFeatures.bFilter){return;}
$.extend(preSearch[column],{"sSearch":input+"","bRegex":regex===null?false:regex,"bSmart":smart===null?true:smart,"bCaseInsensitive":caseInsen===null?true:caseInsen});_fnFilterComplete(settings,settings.oPreviousSearch,1);});});_api_register('state()',function(){return this.context.length?this.context[0].oSavedState:null;});_api_register('state.clear()',function(){return this.iterator('table',function(settings){settings.fnStateSaveCallback.call(settings.oInstance,settings,{});});});_api_register('state.loaded()',function(){return this.context.length?this.context[0].oLoadedState:null;});_api_register('state.save()',function(){return this.iterator('table',function(settings){_fnSaveState(settings);});});DataTable.versionCheck=DataTable.fnVersionCheck=function(version)
{var aThis=DataTable.version.split('.');var aThat=version.split('.');var iThis,iThat;for(var i=0,iLen=aThat.length;i<iLen;i++){iThis=parseInt(aThis[i],10)||0;iThat=parseInt(aThat[i],10)||0;if(iThis===iThat){continue;}
return iThis>iThat;}
return true;};DataTable.isDataTable=DataTable.fnIsDataTable=function(table)
{var t=$(table).get(0);var is=false;$.each(DataTable.settings,function(i,o){if(o.nTable===t||o.nScrollHead===t||o.nScrollFoot===t){is=true;}});return is;};DataTable.tables=DataTable.fnTables=function(visible)
{return jQuery.map(DataTable.settings,function(o){if(!visible||(visible&&$(o.nTable).is(':visible'))){return o.nTable;}});};DataTable.util={throttle:_fnThrottle};DataTable.camelToHungarian=_fnCamelToHungarian;_api_register('$()',function(selector,opts){var
rows=this.rows(opts).nodes(),jqRows=$(rows);return $([].concat(jqRows.filter(selector).toArray(),jqRows.find(selector).toArray()));});$.each(['on','one','off'],function(i,key){_api_register(key+'()',function(){var args=Array.prototype.slice.call(arguments);if(!args[0].match(/\.dt\b/)){args[0]+='.dt';}
var inst=$(this.tables().nodes());inst[key].apply(inst,args);return this;});});_api_register('clear()',function(){return this.iterator('table',function(settings){_fnClearTable(settings);});});_api_register('settings()',function(){return new _Api(this.context,this.context);});_api_register('data()',function(){return this.iterator('table',function(settings){return _pluck(settings.aoData,'_aData');}).flatten();});_api_register('destroy()',function(remove){remove=remove||false;return this.iterator('table',function(settings){var orig=settings.nTableWrapper.parentNode;var classes=settings.oClasses;var table=settings.nTable;var tbody=settings.nTBody;var thead=settings.nTHead;var tfoot=settings.nTFoot;var jqTable=$(table);var jqTbody=$(tbody);var jqWrapper=$(settings.nTableWrapper);var rows=$.map(settings.aoData,function(r){return r.nTr;});var i,ien;settings.bDestroying=true;_fnCallbackFire(settings,"aoDestroyCallback","destroy",[settings]);if(!remove){new _Api(settings).columns().visible(true);}
jqWrapper.unbind('.DT').find(':not(tbody *)').unbind('.DT');$(window).unbind('.DT-'+settings.sInstance);if(table!=thead.parentNode){jqTable.children('thead').detach();jqTable.append(thead);}
if(tfoot&&table!=tfoot.parentNode){jqTable.children('tfoot').detach();jqTable.append(tfoot);}
jqTable.detach();jqWrapper.detach();settings.aaSorting=[];settings.aaSortingFixed=[];_fnSortingClasses(settings);$(rows).removeClass(settings.asStripeClasses.join(' '));$('th, td',thead).removeClass(classes.sSortable+' '+
classes.sSortableAsc+' '+classes.sSortableDesc+' '+classes.sSortableNone);if(settings.bJUI){$('th span.'+classes.sSortIcon+', td span.'+classes.sSortIcon,thead).detach();$('th, td',thead).each(function(){var wrapper=$('div.'+classes.sSortJUIWrapper,this);$(this).append(wrapper.contents());wrapper.detach();});}
if(!remove&&orig){orig.insertBefore(table,settings.nTableReinsertBefore);}
jqTbody.children().detach();jqTbody.append(rows);jqTable.css('width',settings.sDestroyWidth).removeClass(classes.sTable);ien=settings.asDestroyStripes.length;if(ien){jqTbody.children().each(function(i){$(this).addClass(settings.asDestroyStripes[i%ien]);});}
var idx=$.inArray(settings,DataTable.settings);if(idx!==-1){DataTable.settings.splice(idx,1);}});});DataTable.version="1.10.3";DataTable.settings=[];DataTable.models={};DataTable.models.oSearch={"bCaseInsensitive":true,"sSearch":"","bRegex":false,"bSmart":true};DataTable.models.oRow={"nTr":null,"anCells":null,"_aData":[],"_aSortData":null,"_aFilterData":null,"_sFilterRow":null,"_sRowStripe":"","src":null};DataTable.models.oColumn={"idx":null,"aDataSort":null,"asSorting":null,"bSearchable":null,"bSortable":null,"bVisible":null,"_sManualType":null,"_bAttrSrc":false,"fnCreatedCell":null,"fnGetData":null,"fnSetData":null,"mData":null,"mRender":null,"nTh":null,"nTf":null,"sClass":null,"sContentPadding":null,"sDefaultContent":null,"sName":null,"sSortDataType":'std',"sSortingClass":null,"sSortingClassJUI":null,"sTitle":null,"sType":null,"sWidth":null,"sWidthOrig":null};DataTable.defaults={"aaData":null,"aaSorting":[[0,'asc']],"aaSortingFixed":[],"ajax":null,"aLengthMenu":[10,25,50,100],"aoColumns":null,"aoColumnDefs":null,"aoSearchCols":[],"asStripeClasses":null,"bAutoWidth":true,"bDeferRender":false,"bDestroy":false,"bFilter":true,"bInfo":true,"bJQueryUI":false,"bLengthChange":true,"bPaginate":true,"bProcessing":false,"bRetrieve":false,"bScrollCollapse":false,"bServerSide":false,"bSort":true,"bSortMulti":true,"bSortCellsTop":false,"bSortClasses":true,"bStateSave":false,"fnCreatedRow":null,"fnDrawCallback":null,"fnFooterCallback":null,"fnFormatNumber":function(toFormat){return toFormat.toString().replace(/\B(?=(\d{3})+(?!\d))/g,this.oLanguage.sThousands);},"fnHeaderCallback":null,"fnInfoCallback":null,"fnInitComplete":null,"fnPreDrawCallback":null,"fnRowCallback":null,"fnServerData":null,"fnServerParams":null,"fnStateLoadCallback":function(settings){try{return JSON.parse((settings.iStateDuration===-1?sessionStorage:localStorage).getItem('DataTables_'+settings.sInstance+'_'+location.pathname));}catch(e){}},"fnStateLoadParams":null,"fnStateLoaded":null,"fnStateSaveCallback":function(settings,data){try{(settings.iStateDuration===-1?sessionStorage:localStorage).setItem('DataTables_'+settings.sInstance+'_'+location.pathname,JSON.stringify(data));}catch(e){}},"fnStateSaveParams":null,"iStateDuration":7200,"iDeferLoading":null,"iDisplayLength":10,"iDisplayStart":0,"iTabIndex":0,"oClasses":{},"oLanguage":{"oAria":{"sSortAscending":": activate to sort column ascending","sSortDescending":": activate to sort column descending"},"oPaginate":{"sFirst":"First","sLast":"Last","sNext":"Next","sPrevious":"Previous"},"sEmptyTable":"No data available in table","sInfo":"Showing _START_ to _END_ of _TOTAL_ entries","sInfoEmpty":"Showing 0 to 0 of 0 entries","sInfoFiltered":"(filtered from _MAX_ total entries)","sInfoPostFix":"","sDecimal":"","sThousands":",","sLengthMenu":"Show _MENU_ entries","sLoadingRecords":"Loading...","sProcessing":"Processing...","sSearch":"Search:","sSearchPlaceholder":"","sUrl":"","sZeroRecords":"No matching records found"},"oSearch":$.extend({},DataTable.models.oSearch),"sAjaxDataProp":"data","sAjaxSource":null,"sDom":"lfrtip","searchDelay":null,"sPaginationType":"simple_numbers","sScrollX":"","sScrollXInner":"","sScrollY":"","sServerMethod":"GET","renderer":null};_fnHungarianMap(DataTable.defaults);DataTable.defaults.column={"aDataSort":null,"iDataSort":-1,"asSorting":['asc','desc'],"bSearchable":true,"bSortable":true,"bVisible":true,"fnCreatedCell":null,"mData":null,/**
		 * This property is the rendering partner to `data` and it is suggested that
		 * when you want to manipulate data for display (including filtering,
		 * sorting etc) without altering the underlying data for the table, use this
		 * property. `render` can be considered to be the the read only companion to
		 * `data` which is read / write (then as such more complex). Like `data`
		 * this option can be given in a number of different ways to effect its
		 * behaviour:
		 *
		 * * `integer` - treated as an array index for the data source. This is the
		 *   default that DataTables uses (incrementally increased for each column).
		 * * `string` - read an object property from the data source. There are
		 *   three 'special' options that can be used in the string to alter how
		 *   DataTables reads the data from the source object:
		 *    * `.` - Dotted Javascript notation. Just as you use a `.` in
		 *      Javascript to read from nested objects, so to can the options
		 *      specified in `data`. For example: `browser.version` or
		 *      `browser.name`. If your object parameter name contains a period, use
		 *      `\\` to escape it - i.e. `first\\.name`.
		 *    * `[]` - Array notation. DataTables can automatically combine data
		 *      from and array source, joining the data with the characters provided
		 *      between the two brackets. For example: `name[, ]` would provide a
		 *      comma-space separated list from the source array. If no characters
		 *      are provided between the brackets, the original array source is
		 *      returned.
		 *    * `()` - Function notation. Adding `()` to the end of a parameter will
		 *      execute a function of the name given. For example: `browser()` for a
		 *      simple function on the data source, `browser.version()` for a
		 *      function in a nested property or even `browser().version` to get an
		 *      object property if the function called returns an object.
		 * * `object` - use different data for the different data types requested by
		 *   DataTables ('filter', 'display', 'type' or 'sort'). The property names
		 *   of the object is the data type the property refers to and the value can
		 *   defined using an integer, string or function using the same rules as
		 *   `render` normally does. Note that an `_` option _must_ be specified.
		 *   This is the default value to use if you haven't specified a value for
		 *   the data type requested by DataTables.
		 * * `function` - the function given will be executed whenever DataTables
		 *   needs to set or get the data for a cell in the column. The function
		 *   takes three parameters:
		 *    * Parameters:
		 *      * {array|object} The data source for the row (based on `data`)
		 *      * {string} The type call data requested - this will be 'filter',
		 *        'display', 'type' or 'sort'.
		 *      * {array|object} The full data source for the row (not based on
		 *        `data`)
		 *    * Return:
		 *      * The return value from the function is what will be used for the
		 *        data requested.
		 *
		 *  @type string|int|function|object|null
		 *  @default null Use the data source value.
		 *
		 *  @name DataTable.defaults.column.render
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Create a comma separated list from an array of objects
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "ajaxSource": "sources/deep.txt",
		 *        "columns": [
		 *          { "data": "engine" },
		 *          { "data": "browser" },
		 *          {
		 *            "data": "platform",
		 *            "render": "[, ].name"
		 *          }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Execute a function to obtain data
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [ {
		 *          "targets": [ 0 ],
		 *          "data": null, // Use the full data source object for the renderer's source
		 *          "render": "browserName()"
		 *        } ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // As an object, extracting different data for the different types
		 *    // This would be used with a data source such as:
		 *    //   { "phone": 5552368, "phone_filter": "5552368 555-2368", "phone_display": "555-2368" }
		 *    // Here the `phone` integer is used for sorting and type detection, while `phone_filter`
		 *    // (which has both forms) is used for filtering for if a user inputs either format, while
		 *    // the formatted phone number is the one that is shown in the table.
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [ {
		 *          "targets": [ 0 ],
		 *          "data": null, // Use the full data source object for the renderer's source
		 *          "render": {
		 *            "_": "phone",
		 *            "filter": "phone_filter",
		 *            "display": "phone_display"
		 *          }
		 *        } ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Use as a function to create a link from the data source
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [ {
		 *          "targets": [ 0 ],
		 *          "data": "download_link",
		 *          "render": function ( data, type, full ) {
		 *            return '<a href="'+data+'">Download</a>';
		 *          }
		 *        } ]
		 *      } );
		 *    } );
		 */
		"mRender": null,
	
	
		/**
		 * Change the cell type created for the column - either TD cells or TH cells. This
		 * can be useful as TH cells have semantic meaning in the table body, allowing them
		 * to act as a header for a row (you may wish to add scope='row' to the TH elements).
		 *  @type string
		 *  @default td
		 *
		 *  @name DataTable.defaults.column.cellType
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Make the first column use TH cells
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [ {
		 *          "targets": [ 0 ],
		 *          "cellType": "th"
		 *        } ]
		 *      } );
		 *    } );
		 */
		"sCellType": "td",
	
	
		/**
		 * Class to give to each cell in this column.
		 *  @type string
		 *  @default <i>Empty string</i>
		 *
		 *  @name DataTable.defaults.column.class
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "class": "my_class", "targets": [ 0 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          { "class": "my_class" },
		 *          null,
		 *          null,
		 *          null,
		 *          null
		 *        ]
		 *      } );
		 *    } );
		 */
		"sClass": "",
	
		/**
		 * When DataTables calculates the column widths to assign to each column,
		 * it finds the longest string in each column and then constructs a
		 * temporary table and reads the widths from that. The problem with this
		 * is that "mmm" is much wider then "iiii", but the latter is a longer
		 * string - thus the calculation can go wrong (doing it properly and putting
		 * it into an DOM object and measuring that is horribly(!) slow). Thus as
		 * a "work around" we provide this option. It will append its value to the
		 * text that is found to be the longest string for the column - i.e. padding.
		 * Generally you shouldn't need this!
		 *  @type string
		 *  @default <i>Empty string<i>
		 *
		 *  @name DataTable.defaults.column.contentPadding
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          null,
		 *          null,
		 *          null,
		 *          {
		 *            "contentPadding": "mmm"
		 *          }
		 *        ]
		 *      } );
		 *    } );
		 */
		"sContentPadding": "",
	
	
		/**
		 * Allows a default value to be given for a column's data, and will be used
		 * whenever a null data source is encountered (this can be because `data`
		 * is set to null, or because the data source itself is null).
		 *  @type string
		 *  @default null
		 *
		 *  @name DataTable.defaults.column.defaultContent
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          {
		 *            "data": null,
		 *            "defaultContent": "Edit",
		 *            "targets": [ -1 ]
		 *          }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          null,
		 *          null,
		 *          null,
		 *          {
		 *            "data": null,
		 *            "defaultContent": "Edit"
		 *          }
		 *        ]
		 *      } );
		 *    } );
		 */
		"sDefaultContent": null,
	
	
		/**
		 * This parameter is only used in DataTables' server-side processing. It can
		 * be exceptionally useful to know what columns are being displayed on the
		 * client side, and to map these to database fields. When defined, the names
		 * also allow DataTables to reorder information from the server if it comes
		 * back in an unexpected order (i.e. if you switch your columns around on the
		 * client-side, your server-side code does not also need updating).
		 *  @type string
		 *  @default <i>Empty string</i>
		 *
		 *  @name DataTable.defaults.column.name
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "name": "engine", "targets": [ 0 ] },
		 *          { "name": "browser", "targets": [ 1 ] },
		 *          { "name": "platform", "targets": [ 2 ] },
		 *          { "name": "version", "targets": [ 3 ] },
		 *          { "name": "grade", "targets": [ 4 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          { "name": "engine" },
		 *          { "name": "browser" },
		 *          { "name": "platform" },
		 *          { "name": "version" },
		 *          { "name": "grade" }
		 *        ]
		 *      } );
		 *    } );
		 */
		"sName": "",
	
	
		/**
		 * Defines a data source type for the ordering which can be used to read
		 * real-time information from the table (updating the internally cached
		 * version) prior to ordering. This allows ordering to occur on user
		 * editable elements such as form inputs.
		 *  @type string
		 *  @default std
		 *
		 *  @name DataTable.defaults.column.orderDataType
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "orderDataType": "dom-text", "targets": [ 2, 3 ] },
		 *          { "type": "numeric", "targets": [ 3 ] },
		 *          { "orderDataType": "dom-select", "targets": [ 4 ] },
		 *          { "orderDataType": "dom-checkbox", "targets": [ 5 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          null,
		 *          null,
		 *          { "orderDataType": "dom-text" },
		 *          { "orderDataType": "dom-text", "type": "numeric" },
		 *          { "orderDataType": "dom-select" },
		 *          { "orderDataType": "dom-checkbox" }
		 *        ]
		 *      } );
		 *    } );
		 */
		"sSortDataType": "std",
	
	
		/**
		 * The title of this column.
		 *  @type string
		 *  @default null <i>Derived from the 'TH' value for this column in the
		 *    original HTML table.</i>
		 *
		 *  @name DataTable.defaults.column.title
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "title": "My column title", "targets": [ 0 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          { "title": "My column title" },
		 *          null,
		 *          null,
		 *          null,
		 *          null
		 *        ]
		 *      } );
		 *    } );
		 */
		"sTitle": null,
	
	
		/**
		 * The type allows you to specify how the data for this column will be
		 * ordered. Four types (string, numeric, date and html (which will strip
		 * HTML tags before ordering)) are currently available. Note that only date
		 * formats understood by Javascript's Date() object will be accepted as type
		 * date. For example: "Mar 26, 2008 5:03 PM". May take the values: 'string',
		 * 'numeric', 'date' or 'html' (by default). Further types can be adding
		 * through plug-ins.
		 *  @type string
		 *  @default null <i>Auto-detected from raw data</i>
		 *
		 *  @name DataTable.defaults.column.type
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "type": "html", "targets": [ 0 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          { "type": "html" },
		 *          null,
		 *          null,
		 *          null,
		 *          null
		 *        ]
		 *      } );
		 *    } );
		 */
		"sType": null,
	
	
		/**
		 * Defining the width of the column, this parameter may take any CSS value
		 * (3em, 20px etc). DataTables applies 'smart' widths to columns which have not
		 * been given a specific width through this interface ensuring that the table
		 * remains readable.
		 *  @type string
		 *  @default null <i>Automatic</i>
		 *
		 *  @name DataTable.defaults.column.width
		 *  @dtopt Columns
		 *
		 *  @example
		 *    // Using `columnDefs`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columnDefs": [
		 *          { "width": "20%", "targets": [ 0 ] }
		 *        ]
		 *      } );
		 *    } );
		 *
		 *  @example
		 *    // Using `columns`
		 *    $(document).ready( function() {
		 *      $('#example').dataTable( {
		 *        "columns": [
		 *          { "width": "20%" },
		 *          null,
		 *          null,
		 *          null,
		 *          null
		 *        ]
		 *      } );
		 *    } );
		 */
		"sWidth": null
	};
	
	_fnHungarianMap( DataTable.defaults.column );
	
	
	
	/**
	 * DataTables settings object - this holds all the information needed for a
	 * given table, including configuration, data and current application of the
	 * table options. DataTables does not have a single instance for each DataTable
	 * with the settings attached to that instance, but rather instances of the
	 * DataTable "class" are created on-the-fly as needed (typically by a
	 * $().dataTable() call) and the settings object is then applied to that
	 * instance.
	 *
	 * Note that this object is related to {@link DataTable.defaults} but this
	 * one is the internal data store for DataTables's cache of columns. It should
	 * NOT be manipulated outside of DataTables. Any configuration should be done
	 * through the initialisation options.
	 *  @namespace
	 *  @todo Really should attach the settings object to individual instances so we
	 *    don't need to create new instances on each $().dataTable() call (if the
	 *    table already exists). It would also save passing oSettings around and
	 *    into every single function. However, this is a very significant
	 *    architecture change for DataTables and will almost certainly break
	 *    backwards compatibility with older installations. This is something that
	 *    will be done in 2.0.
	 */
	DataTable.models.oSettings = {
		/**
		 * Primary features of DataTables and their enablement state.
		 *  @namespace
		 */
		"oFeatures": {
	
			/**
			 * Flag to say if DataTables should automatically try to calculate the
			 * optimum table and columns widths (true) or not (false).
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bAutoWidth": null,
	
			/**
			 * Delay the creation of TR and TD elements until they are actually
			 * needed by a driven page draw. This can give a significant speed
			 * increase for Ajax source and Javascript source data, but makes no
			 * difference at all fro DOM and server-side processing tables.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bDeferRender": null,
	
			/**
			 * Enable filtering on the table or not. Note that if this is disabled
			 * then there is no filtering at all on the table, including fnFilter.
			 * To just remove the filtering input use sDom and remove the 'f' option.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bFilter": null,
	
			/**
			 * Table information element (the 'Showing x of y records' div) enable
			 * flag.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bInfo": null,
	
			/**
			 * Present a user control allowing the end user to change the page size
			 * when pagination is enabled.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bLengthChange": null,
	
			/**
			 * Pagination enabled or not. Note that if this is disabled then length
			 * changing must also be disabled.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bPaginate": null,
	
			/**
			 * Processing indicator enable flag whenever DataTables is enacting a
			 * user request - typically an Ajax request for server-side processing.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bProcessing": null,
	
			/**
			 * Server-side processing enabled flag - when enabled DataTables will
			 * get all data from the server for every draw - there is no filtering,
			 * sorting or paging done on the client-side.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bServerSide": null,
	
			/**
			 * Sorting enablement flag.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bSort": null,
	
			/**
			 * Multi-column sorting
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bSortMulti": null,
	
			/**
			 * Apply a class to the columns which are being sorted to provide a
			 * visual highlight or not. This can slow things down when enabled since
			 * there is a lot of DOM interaction.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bSortClasses": null,
	
			/**
			 * State saving enablement flag.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bStateSave": null
		},
	
	
		/**
		 * Scrolling settings for a table.
		 *  @namespace
		 */
		"oScroll": {
			/**
			 * When the table is shorter in height than sScrollY, collapse the
			 * table container down to the height of the table (when true).
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type boolean
			 */
			"bCollapse": null,
	
			/**
			 * Width of the scrollbar for the web-browser's platform. Calculated
			 * during table initialisation.
			 *  @type int
			 *  @default 0
			 */
			"iBarWidth": 0,
	
			/**
			 * Viewport width for horizontal scrolling. Horizontal scrolling is
			 * disabled if an empty string.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type string
			 */
			"sX": null,
	
			/**
			 * Width to expand the table to when using x-scrolling. Typically you
			 * should not need to use this.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type string
			 *  @deprecated
			 */
			"sXInner": null,
	
			/**
			 * Viewport height for vertical scrolling. Vertical scrolling is disabled
			 * if an empty string.
			 * Note that this parameter will be set by the initialisation routine. To
			 * set a default use {@link DataTable.defaults}.
			 *  @type string
			 */
			"sY": null
		},
	
		/**
		 * Language information for the table.
		 *  @namespace
		 *  @extends DataTable.defaults.oLanguage
		 */
		"oLanguage": {
			/**
			 * Information callback function. See
			 * {@link DataTable.defaults.fnInfoCallback}
			 *  @type function
			 *  @default null
			 */
			"fnInfoCallback": null
		},
	
		/**
		 * Browser support parameters
		 *  @namespace
		 */
		"oBrowser": {
			/**
			 * Indicate if the browser incorrectly calculates width:100% inside a
			 * scrolling element (IE6/7)
			 *  @type boolean
			 *  @default false
			 */
			"bScrollOversize": false,
	
			/**
			 * Determine if the vertical scrollbar is on the right or left of the
			 * scrolling container - needed for rtl language layout, although not
			 * all browsers move the scrollbar (Safari).
			 *  @type boolean
			 *  @default false
			 */
			"bScrollbarLeft": false
		},
	
	
		"ajax": null,
	
	
		/**
		 * Array referencing the nodes which are used for the features. The
		 * parameters of this object match what is allowed by sDom - i.e.
		 *   <ul>
		 *     <li>'l' - Length changing</li>
		 *     <li>'f' - Filtering input</li>
		 *     <li>'t' - The table!</li>
		 *     <li>'i' - Information</li>
		 *     <li>'p' - Pagination</li>
		 *     <li>'r' - pRocessing</li>
		 *   </ul>
		 *  @type array
		 *  @default []
		 */
		"aanFeatures": [],
	
		/**
		 * Store data information - see {@link DataTable.models.oRow} for detailed
		 * information.
		 *  @type array
		 *  @default []
		 */
		"aoData": [],
	
		/**
		 * Array of indexes which are in the current display (after filtering etc)
		 *  @type array
		 *  @default []
		 */
		"aiDisplay": [],
	
		/**
		 * Array of indexes for display - no filtering
		 *  @type array
		 *  @default []
		 */
		"aiDisplayMaster": [],
	
		/**
		 * Store information about each column that is in use
		 *  @type array
		 *  @default []
		 */
		"aoColumns": [],
	
		/**
		 * Store information about the table's header
		 *  @type array
		 *  @default []
		 */
		"aoHeader": [],
	
		/**
		 * Store information about the table's footer
		 *  @type array
		 *  @default []
		 */
		"aoFooter": [],
	
		/**
		 * Store the applied global search information in case we want to force a
		 * research or compare the old search to a new one.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @namespace
		 *  @extends DataTable.models.oSearch
		 */
		"oPreviousSearch": {},
	
		/**
		 * Store the applied search for each column - see
		 * {@link DataTable.models.oSearch} for the format that is used for the
		 * filtering information for each column.
		 *  @type array
		 *  @default []
		 */
		"aoPreSearchCols": [],
	
		/**
		 * Sorting that is applied to the table. Note that the inner arrays are
		 * used in the following manner:
		 * <ul>
		 *   <li>Index 0 - column number</li>
		 *   <li>Index 1 - current sorting direction</li>
		 * </ul>
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type array
		 *  @todo These inner arrays should really be objects
		 */
		"aaSorting": null,
	
		/**
		 * Sorting that is always applied to the table (i.e. prefixed in front of
		 * aaSorting).
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type array
		 *  @default []
		 */
		"aaSortingFixed": [],
	
		/**
		 * Classes to use for the striping of a table.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type array
		 *  @default []
		 */
		"asStripeClasses": null,
	
		/**
		 * If restoring a table - we should restore its striping classes as well
		 *  @type array
		 *  @default []
		 */
		"asDestroyStripes": [],
	
		/**
		 * If restoring a table - we should restore its width
		 *  @type int
		 *  @default 0
		 */
		"sDestroyWidth": 0,
	
		/**
		 * Callback functions array for every time a row is inserted (i.e. on a draw).
		 *  @type array
		 *  @default []
		 */
		"aoRowCallback": [],
	
		/**
		 * Callback functions for the header on each draw.
		 *  @type array
		 *  @default []
		 */
		"aoHeaderCallback": [],
	
		/**
		 * Callback function for the footer on each draw.
		 *  @type array
		 *  @default []
		 */
		"aoFooterCallback": [],
	
		/**
		 * Array of callback functions for draw callback functions
		 *  @type array
		 *  @default []
		 */
		"aoDrawCallback": [],
	
		/**
		 * Array of callback functions for row created function
		 *  @type array
		 *  @default []
		 */
		"aoRowCreatedCallback": [],
	
		/**
		 * Callback functions for just before the table is redrawn. A return of
		 * false will be used to cancel the draw.
		 *  @type array
		 *  @default []
		 */
		"aoPreDrawCallback": [],
	
		/**
		 * Callback functions for when the table has been initialised.
		 *  @type array
		 *  @default []
		 */
		"aoInitComplete": [],
	
	
		/**
		 * Callbacks for modifying the settings to be stored for state saving, prior to
		 * saving state.
		 *  @type array
		 *  @default []
		 */
		"aoStateSaveParams": [],
	
		/**
		 * Callbacks for modifying the settings that have been stored for state saving
		 * prior to using the stored values to restore the state.
		 *  @type array
		 *  @default []
		 */
		"aoStateLoadParams": [],
	
		/**
		 * Callbacks for operating on the settings object once the saved state has been
		 * loaded
		 *  @type array
		 *  @default []
		 */
		"aoStateLoaded": [],
	
		/**
		 * Cache the table ID for quick access
		 *  @type string
		 *  @default <i>Empty string</i>
		 */
		"sTableId": "",
	
		/**
		 * The TABLE node for the main table
		 *  @type node
		 *  @default null
		 */
		"nTable": null,
	
		/**
		 * Permanent ref to the thead element
		 *  @type node
		 *  @default null
		 */
		"nTHead": null,
	
		/**
		 * Permanent ref to the tfoot element - if it exists
		 *  @type node
		 *  @default null
		 */
		"nTFoot": null,
	
		/**
		 * Permanent ref to the tbody element
		 *  @type node
		 *  @default null
		 */
		"nTBody": null,
	
		/**
		 * Cache the wrapper node (contains all DataTables controlled elements)
		 *  @type node
		 *  @default null
		 */
		"nTableWrapper": null,
	
		/**
		 * Indicate if when using server-side processing the loading of data
		 * should be deferred until the second draw.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type boolean
		 *  @default false
		 */
		"bDeferLoading": false,
	
		/**
		 * Indicate if all required information has been read in
		 *  @type boolean
		 *  @default false
		 */
		"bInitialised": false,
	
		/**
		 * Information about open rows. Each object in the array has the parameters
		 * 'nTr' and 'nParent'
		 *  @type array
		 *  @default []
		 */
		"aoOpenRows": [],
	
		/**
		 * Dictate the positioning of DataTables' control elements - see
		 * {@link DataTable.model.oInit.sDom}.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type string
		 *  @default null
		 */
		"sDom": null,
	
		/**
		 * Search delay (in mS)
		 *  @type integer
		 *  @default null
		 */
		"searchDelay": null,
	
		/**
		 * Which type of pagination should be used.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type string
		 *  @default two_button
		 */
		"sPaginationType": "two_button",
	
		/**
		 * The state duration (for `stateSave`) in seconds.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type int
		 *  @default 0
		 */
		"iStateDuration": 0,
	
		/**
		 * Array of callback functions for state saving. Each array element is an
		 * object with the following parameters:
		 *   <ul>
		 *     <li>function:fn - function to call. Takes two parameters, oSettings
		 *       and the JSON string to save that has been thus far created. Returns
		 *       a JSON string to be inserted into a json object
		 *       (i.e. '"param": [ 0, 1, 2]')</li>
		 *     <li>string:sName - name of callback</li>
		 *   </ul>
		 *  @type array
		 *  @default []
		 */
		"aoStateSave": [],
	
		/**
		 * Array of callback functions for state loading. Each array element is an
		 * object with the following parameters:
		 *   <ul>
		 *     <li>function:fn - function to call. Takes two parameters, oSettings
		 *       and the object stored. May return false to cancel state loading</li>
		 *     <li>string:sName - name of callback</li>
		 *   </ul>
		 *  @type array
		 *  @default []
		 */
		"aoStateLoad": [],
	
		/**
		 * State that was saved. Useful for back reference
		 *  @type object
		 *  @default null
		 */
		"oSavedState": null,
	
		/**
		 * State that was loaded. Useful for back reference
		 *  @type object
		 *  @default null
		 */
		"oLoadedState": null,
	
		/**
		 * Source url for AJAX data for the table.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type string
		 *  @default null
		 */
		"sAjaxSource": null,
	
		/**
		 * Property from a given object from which to read the table data from. This
		 * can be an empty string (when not server-side processing), in which case
		 * it is  assumed an an array is given directly.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type string
		 */
		"sAjaxDataProp": null,
	
		/**
		 * Note if draw should be blocked while getting data
		 *  @type boolean
		 *  @default true
		 */
		"bAjaxDataGet": true,
	
		/**
		 * The last jQuery XHR object that was used for server-side data gathering.
		 * This can be used for working with the XHR information in one of the
		 * callbacks
		 *  @type object
		 *  @default null
		 */
		"jqXHR": null,
	
		/**
		 * JSON returned from the server in the last Ajax request
		 *  @type object
		 *  @default undefined
		 */
		"json": undefined,
	
		/**
		 * Data submitted as part of the last Ajax request
		 *  @type object
		 *  @default undefined
		 */
		"oAjaxData": undefined,
	
		/**
		 * Function to get the server-side data.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type function
		 */
		"fnServerData": null,
	
		/**
		 * Functions which are called prior to sending an Ajax request so extra
		 * parameters can easily be sent to the server
		 *  @type array
		 *  @default []
		 */
		"aoServerParams": [],
	
		/**
		 * Send the XHR HTTP method - GET or POST (could be PUT or DELETE if
		 * required).
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type string
		 */
		"sServerMethod": null,
	
		/**
		 * Format numbers for display.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type function
		 */
		"fnFormatNumber": null,
	
		/**
		 * List of options that can be used for the user selectable length menu.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type array
		 *  @default []
		 */
		"aLengthMenu": null,
	
		/**
		 * Counter for the draws that the table does. Also used as a tracker for
		 * server-side processing
		 *  @type int
		 *  @default 0
		 */
		"iDraw": 0,
	
		/**
		 * Indicate if a redraw is being done - useful for Ajax
		 *  @type boolean
		 *  @default false
		 */
		"bDrawing": false,
	
		/**
		 * Draw index (iDraw) of the last error when parsing the returned data
		 *  @type int
		 *  @default -1
		 */
		"iDrawError": -1,
	
		/**
		 * Paging display length
		 *  @type int
		 *  @default 10
		 */
		"_iDisplayLength": 10,
	
		/**
		 * Paging start point - aiDisplay index
		 *  @type int
		 *  @default 0
		 */
		"_iDisplayStart": 0,
	
		/**
		 * Server-side processing - number of records in the result set
		 * (i.e. before filtering), Use fnRecordsTotal rather than
		 * this property to get the value of the number of records, regardless of
		 * the server-side processing setting.
		 *  @type int
		 *  @default 0
		 *  @private
		 */
		"_iRecordsTotal": 0,
	
		/**
		 * Server-side processing - number of records in the current display set
		 * (i.e. after filtering). Use fnRecordsDisplay rather than
		 * this property to get the value of the number of records, regardless of
		 * the server-side processing setting.
		 *  @type boolean
		 *  @default 0
		 *  @private
		 */
		"_iRecordsDisplay": 0,
	
		/**
		 * Flag to indicate if jQuery UI marking and classes should be used.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type boolean
		 */
		"bJUI": null,
	
		/**
		 * The classes to use for the table
		 *  @type object
		 *  @default {}
		 */
		"oClasses": {},
	
		/**
		 * Flag attached to the settings object so you can check in the draw
		 * callback if filtering has been done in the draw. Deprecated in favour of
		 * events.
		 *  @type boolean
		 *  @default false
		 *  @deprecated
		 */
		"bFiltered": false,
	
		/**
		 * Flag attached to the settings object so you can check in the draw
		 * callback if sorting has been done in the draw. Deprecated in favour of
		 * events.
		 *  @type boolean
		 *  @default false
		 *  @deprecated
		 */
		"bSorted": false,
	
		/**
		 * Indicate that if multiple rows are in the header and there is more than
		 * one unique cell per column, if the top one (true) or bottom one (false)
		 * should be used for sorting / title by DataTables.
		 * Note that this parameter will be set by the initialisation routine. To
		 * set a default use {@link DataTable.defaults}.
		 *  @type boolean
		 */
		"bSortCellsTop": null,
	
		/**
		 * Initialisation object that is used for the table
		 *  @type object
		 *  @default null
		 */
		"oInit": null,
	
		/**
		 * Destroy callback functions - for plug-ins to attach themselves to the
		 * destroy so they can clean up markup and events.
		 *  @type array
		 *  @default []
		 */
		"aoDestroyCallback": [],
	
	
		/**
		 * Get the number of records in the current record set, before filtering
		 *  @type function
		 */
		"fnRecordsTotal": function ()
		{
			return _fnDataSource( this ) == 'ssp' ?
				this._iRecordsTotal * 1 :
				this.aiDisplayMaster.length;
		},
	
		/**
		 * Get the number of records in the current record set, after filtering
		 *  @type function
		 */
		"fnRecordsDisplay": function ()
		{
			return _fnDataSource( this ) == 'ssp' ?
				this._iRecordsDisplay * 1 :
				this.aiDisplay.length;
		},
	
		/**
		 * Get the display end point - aiDisplay index
		 *  @type function
		 */
		"fnDisplayEnd": function ()
		{
			var
				len      = this._iDisplayLength,
				start    = this._iDisplayStart,
				calc     = start + len,
				records  = this.aiDisplay.length,
				features = this.oFeatures,
				paginate = features.bPaginate;
	
			if ( features.bServerSide ) {
				return paginate === false || len === -1 ?
					start + records :
					Math.min( start+len, this._iRecordsDisplay );
			}
			else {
				return ! paginate || calc>records || len===-1 ?
					records :
					calc;
			}
		},
	
		/**
		 * The DataTables object for this table
		 *  @type object
		 *  @default null
		 */
		"oInstance": null,
	
		/**
		 * Unique identifier for each instance of the DataTables object. If there
		 * is an ID on the table node, then it takes that value, otherwise an
		 * incrementing internal counter is used.
		 *  @type string
		 *  @default null
		 */
		"sInstance": null,
	
		/**
		 * tabindex attribute value that is added to DataTables control elements, allowing
		 * keyboard navigation of the table and its controls.
		 */
		"iTabIndex": 0,
	
		/**
		 * DIV container for the footer scrolling table if scrolling
		 */
		"nScrollHead": null,
	
		/**
		 * DIV container for the footer scrolling table if scrolling
		 */
		"nScrollFoot": null,
	
		/**
		 * Last applied sort
		 *  @type array
		 *  @default []
		 */
		"aLastSort": [],
	
		/**
		 * Stored plug-in instances
		 *  @type object
		 *  @default {}
		 */
		"oPlugins": {}
	};

	/**
	 * Extension object for DataTables that is used to provide all extension
	 * options.
	 *
	 * Note that the `DataTable.ext` object is available through
	 * `jQuery.fn.dataTable.ext` where it may be accessed and manipulated. It is
	 * also aliased to `jQuery.fn.dataTableExt` for historic reasons.
	 *  @namespace
	 *  @extends DataTable.models.ext
	 */
	
	
	/**
	 * DataTables extensions
	 * 
	 * This namespace acts as a collection area for plug-ins that can be used to
	 * extend DataTables capabilities. Indeed many of the build in methods
	 * use this method to provide their own capabilities (sorting methods for
	 * example).
	 *
	 * Note that this namespace is aliased to `jQuery.fn.dataTableExt` for legacy
	 * reasons
	 *
	 *  @namespace
	 */
	DataTable.ext = _ext = {
		/**
		 * Element class names
		 *
		 *  @type object
		 *  @default {}
		 */
		classes: {},
	
	
		/**
		 * Error reporting.
		 * 
		 * How should DataTables report an error. Can take the value 'alert' or
		 * 'throw'
		 *
		 *  @type string
		 *  @default alert
		 */
		errMode: "alert",
	
	
		/**
		 * Feature plug-ins.
		 * 
		 * This is an array of objects which describe the feature plug-ins that are
		 * available to DataTables. These feature plug-ins are then available for
		 * use through the `dom` initialisation option.
		 * 
		 * Each feature plug-in is described by an object which must have the
		 * following properties:
		 * 
		 * * `fnInit` - function that is used to initialise the plug-in,
		 * * `cFeature` - a character so the feature can be enabled by the `dom`
		 *   instillation option. This is case sensitive.
		 *
		 * The `fnInit` function has the following input parameters:
		 *
		 * 1. `{object}` DataTables settings object: see
		 *    {@link DataTable.models.oSettings}
		 *
		 * And the following return is expected:
		 * 
		 * * {node|null} The element which contains your feature. Note that the
		 *   return may also be void if your plug-in does not require to inject any
		 *   DOM elements into DataTables control (`dom`) - for example this might
		 *   be useful when developing a plug-in which allows table control via
		 *   keyboard entry
		 *
		 *  @type array
		 *
		 *  @example
		 *    $.fn.dataTable.ext.features.push( {
		 *      "fnInit": function( oSettings ) {
		 *        return new TableTools( { "oDTSettings": oSettings } );
		 *      },
		 *      "cFeature": "T"
		 *    } );
		 */
		feature: [],
	
	
		/**
		 * Row searching.
		 * 
		 * This method of searching is complimentary to the default type based
		 * searching, and a lot more comprehensive as it allows you complete control
		 * over the searching logic. Each element in this array is a function
		 * (parameters described below) that is called for every row in the table,
		 * and your logic decides if it should be included in the searching data set
		 * or not.
		 *
		 * Searching functions have the following input parameters:
		 *
		 * 1. `{object}` DataTables settings object: see
		 *    {@link DataTable.models.oSettings}
		 * 2. `{array|object}` Data for the row to be processed (same as the
		 *    original format that was passed in as the data source, or an array
		 *    from a DOM data source
		 * 3. `{int}` Row index ({@link DataTable.models.oSettings.aoData}), which
		 *    can be useful to retrieve the `TR` element if you need DOM interaction.
		 *
		 * And the following return is expected:
		 *
		 * * {boolean} Include the row in the searched result set (true) or not
		 *   (false)
		 *
		 * Note that as with the main search ability in DataTables, technically this
		 * is "filtering", since it is subtractive. However, for consistency in
		 * naming we call it searching here.
		 *
		 *  @type array
		 *  @default []
		 *
		 *  @example
		 *    // The following example shows custom search being applied to the
		 *    // fourth column (i.e. the data[3] index) based on two input values
		 *    // from the end-user, matching the data in a certain range.
		 *    $.fn.dataTable.ext.search.push(
		 *      function( settings, data, dataIndex ) {
		 *        var min = document.getElementById('min').value * 1;
		 *        var max = document.getElementById('max').value * 1;
		 *        var version = data[3] == "-" ? 0 : data[3]*1;
		 *
		 *        if ( min == "" && max == "" ) {
		 *          return true;
		 *        }
		 *        else if ( min == "" && version < max ) {
		 *          return true;
		 *        }
		 *        else if ( min < version && "" == max ) {
		 *          return true;
		 *        }
		 *        else if ( min < version && version < max ) {
		 *          return true;
		 *        }
		 *        return false;
		 *      }
		 *    );
		 */
		search: [],
	
	
		/**
		 * Internal functions, exposed for used in plug-ins.
		 * 
		 * Please note that you should not need to use the internal methods for
		 * anything other than a plug-in (and even then, try to avoid if possible).
		 * The internal function may change between releases.
		 *
		 *  @type object
		 *  @default {}
		 */
		internal: {},
	
	
		/**
		 * Legacy configuration options. Enable and disable legacy options that
		 * are available in DataTables.
		 *
		 *  @type object
		 */
		legacy: {
			/**
			 * Enable / disable DataTables 1.9 compatible server-side processing
			 * requests
			 *
			 *  @type boolean
			 *  @default null
			 */
			ajax: null
		},
	
	
		/**
		 * Pagination plug-in methods.
		 * 
		 * Each entry in this object is a function and defines which buttons should
		 * be shown by the pagination rendering method that is used for the table:
		 * {@link DataTable.ext.renderer.pageButton}. The renderer addresses how the
		 * buttons are displayed in the document, while the functions here tell it
		 * what buttons to display. This is done by returning an array of button
		 * descriptions (what each button will do).
		 *
		 * Pagination types (the four built in options and any additional plug-in
		 * options defined here) can be used through the `paginationType`
		 * initialisation parameter.
		 *
		 * The functions defined take two parameters:
		 *
		 * 1. `{int} page` The current page index
		 * 2. `{int} pages` The number of pages in the table
		 *
		 * Each function is expected to return an array where each element of the
		 * array can be one of:
		 *
		 * * `first` - Jump to first page when activated
		 * * `last` - Jump to last page when activated
		 * * `previous` - Show previous page when activated
		 * * `next` - Show next page when activated
		 * * `{int}` - Show page of the index given
		 * * `{array}` - A nested array containing the above elements to add a
		 *   containing 'DIV' element (might be useful for styling).
		 *
		 * Note that DataTables v1.9- used this object slightly differently whereby
		 * an object with two functions would be defined for each plug-in. That
		 * ability is still supported by DataTables 1.10+ to provide backwards
		 * compatibility, but this option of use is now decremented and no longer
		 * documented in DataTables 1.10+.
		 *
		 *  @type object
		 *  @default {}
		 *
		 *  @example
		 *    // Show previous, next and current page buttons only
		 *    $.fn.dataTableExt.oPagination.current = function ( page, pages ) {
		 *      return [ 'previous', page, 'next' ];
		 *    };
		 */
		pager: {},
	
	
		renderer: {
			pageButton: {},
			header: {}
		},
	
	
		/**
		 * Ordering plug-ins - custom data source
		 * 
		 * The extension options for ordering of data available here is complimentary
		 * to the default type based ordering that DataTables typically uses. It
		 * allows much greater control over the the data that is being used to
		 * order a column, but is necessarily therefore more complex.
		 * 
		 * This type of ordering is useful if you want to do ordering based on data
		 * live from the DOM (for example the contents of an 'input' element) rather
		 * than just the static string that DataTables knows of.
		 * 
		 * The way these plug-ins work is that you create an array of the values you
		 * wish to be ordering for the column in question and then return that
		 * array. The data in the array much be in the index order of the rows in
		 * the table (not the currently ordering order!). Which order data gathering
		 * function is run here depends on the `dt-init columns.orderDataType`
		 * parameter that is used for the column (if any).
		 *
		 * The functions defined take two parameters:
		 *
		 * 1. `{object}` DataTables settings object: see
		 *    {@link DataTable.models.oSettings}
		 * 2. `{int}` Target column index
		 *
		 * Each function is expected to return an array:
		 *
		 * * `{array}` Data for the column to be ordering upon
		 *
		 *  @type array
		 *
		 *  @example
		 *    // Ordering using `input` node values
		 *    $.fn.dataTable.ext.order['dom-text'] = function  ( settings, col )
		 *    {
		 *      return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
		 *        return $('input', td).val();
		 *      } );
		 *    }
		 */
		order: {},
	
	
		/**
		 * Type based plug-ins.
		 *
		 * Each column in DataTables has a type assigned to it, either by automatic
		 * detection or by direct assignment using the `type` option for the column.
		 * The type of a column will effect how it is ordering and search (plug-ins
		 * can also make use of the column type if required).
		 *
		 * @namespace
		 */
		type: {
			/**
			 * Type detection functions.
			 *
			 * The functions defined in this object are used to automatically detect
			 * a column's type, making initialisation of DataTables super easy, even
			 * when complex data is in the table.
			 *
			 * The functions defined take two parameters:
			 *
		     *  1. `{*}` Data from the column cell to be analysed
		     *  2. `{settings}` DataTables settings object. This can be used to
		     *     perform context specific type detection - for example detection
		     *     based on language settings such as using a comma for a decimal
		     *     place. Generally speaking the options from the settings will not
		     *     be required
			 *
			 * Each function is expected to return:
			 *
			 * * `{string|null}` Data type detected, or null if unknown (and thus
			 *   pass it on to the other type detection functions.
			 *
			 *  @type array
			 *
			 *  @example
			 *    // Currency type detection plug-in:
			 *    $.fn.dataTable.ext.type.detect.push(
			 *      function ( data, settings ) {
			 *        // Check the numeric part
			 *        if ( ! $.isNumeric( data.substring(1) ) ) {
			 *          return null;
			 *        }
			 *
			 *        // Check prefixed by currency
			 *        if ( data.charAt(0) == '$' || data.charAt(0) == '&pound;' ) {
			 *          return 'currency';
			 *        }
			 *        return null;
			 *      }
			 *    );
			 */
			detect: [],
	
	
			/**
			 * Type based search formatting.
			 *
			 * The type based searching functions can be used to pre-format the
			 * data to be search on. For example, it can be used to strip HTML
			 * tags or to de-format telephone numbers for numeric only searching.
			 *
			 * Note that is a search is not defined for a column of a given type,
			 * no search formatting will be performed.
			 * 
			 * Pre-processing of searching data plug-ins - When you assign the sType
			 * for a column (or have it automatically detected for you by DataTables
			 * or a type detection plug-in), you will typically be using this for
			 * custom sorting, but it can also be used to provide custom searching
			 * by allowing you to pre-processing the data and returning the data in
			 * the format that should be searched upon. This is done by adding
			 * functions this object with a parameter name which matches the sType
			 * for that target column. This is the corollary of <i>afnSortData</i>
			 * for searching data.
			 *
			 * The functions defined take a single parameter:
			 *
		     *  1. `{*}` Data from the column cell to be prepared for searching
			 *
			 * Each function is expected to return:
			 *
			 * * `{string|null}` Formatted string that will be used for the searching.
			 *
			 *  @type object
			 *  @default {}
			 *
			 *  @example
			 *    $.fn.dataTable.ext.type.search['title-numeric'] = function ( d ) {
			 *      return d.replace(/\n/g," ").replace( /<.*?>/g, "" );
			 *    }
			 */
			search: {},
	
	
			/**
			 * Type based ordering.
			 *
			 * The column type tells DataTables what ordering to apply to the table
			 * when a column is sorted upon. The order for each type that is defined,
			 * is defined by the functions available in this object.
			 *
			 * Each ordering option can be described by three properties added to
			 * this object:
			 *
			 * * `{type}-pre` - Pre-formatting function
			 * * `{type}-asc` - Ascending order function
			 * * `{type}-desc` - Descending order function
			 *
			 * All three can be used together, only `{type}-pre` or only
			 * `{type}-asc` and `{type}-desc` together. It is generally recommended
			 * that only `{type}-pre` is used, as this provides the optimal
			 * implementation in terms of speed, although the others are provided
			 * for compatibility with existing Javascript sort functions.
			 *
			 * `{type}-pre`: Functions defined take a single parameter:
			 *
		     *  1. `{*}` Data from the column cell to be prepared for ordering
			 *
			 * And return:
			 *
			 * * `{*}` Data to be sorted upon
			 *
			 * `{type}-asc` and `{type}-desc`: Functions are typical Javascript sort
			 * functions, taking two parameters:
			 *
		     *  1. `{*}` Data to compare to the second parameter
		     *  2. `{*}` Data to compare to the first parameter
			 *
			 * And returning:
			 *
			 * * `{*}` Ordering match: <0 if first parameter should be sorted lower
			 *   than the second parameter, ===0 if the two parameters are equal and
			 *   >0 if the first parameter should be sorted height than the second
			 *   parameter.
			 * 
			 *  @type object
			 *  @default {}
			 *
			 *  @example
			 *    // Numeric ordering of formatted numbers with a pre-formatter
			 *    $.extend( $.fn.dataTable.ext.type.order, {
			 *      "string-pre": function(x) {
			 *        a = (a === "-" || a === "") ? 0 : a.replace( /[^\d\-\.]/g, "" );
			 *        return parseFloat( a );
			 *      }
			 *    } );
			 *
			 *  @example
			 *    // Case-sensitive string ordering, with no pre-formatting method
			 *    $.extend( $.fn.dataTable.ext.order, {
			 *      "string-case-asc": function(x,y) {
			 *        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
			 *      },
			 *      "string-case-desc": function(x,y) {
			 *        return ((x < y) ? 1 : ((x > y) ? -1 : 0));
			 *      }
			 *    } );
			 */
			order: {}
		},
	
		/**
		 * Unique DataTables instance counter
		 *
		 * @type int
		 * @private
		 */
		_unique: 0,
	
	
		//
		// Depreciated
		// The following properties are retained for backwards compatiblity only.
		// The should not be used in new projects and will be removed in a future
		// version
		//
	
		/**
		 * Version check function.
		 *  @type function
		 *  @depreciated Since 1.10
		 */
		fnVersionCheck: DataTable.fnVersionCheck,
	
	
		/**
		 * Index for what 'this' index API functions should use
		 *  @type int
		 *  @deprecated Since v1.10
		 */
		iApiIndex: 0,
	
	
		/**
		 * jQuery UI class container
		 *  @type object
		 *  @deprecated Since v1.10
		 */
		oJUIClasses: {},
	
	
		/**
		 * Software version
		 *  @type string
		 *  @deprecated Since v1.10
		 */
		sVersion: DataTable.version
	};
	
	
	//
	// Backwards compatibility. Alias to pre 1.10 Hungarian notation counter parts
	//
	$.extend( _ext, {
		afnFiltering: _ext.search,
		aTypes:       _ext.type.detect,
		ofnSearch:    _ext.type.search,
		oSort:        _ext.type.order,
		afnSortData:  _ext.order,
		aoFeatures:   _ext.feature,
		oApi:         _ext.internal,
		oStdClasses:  _ext.classes,
		oPagination:  _ext.pager
	} );
	
	
	$.extend( DataTable.ext.classes, {
		"sTable": "dataTable",
		"sNoFooter": "no-footer",
	
		/* Paging buttons */
		"sPageButton": "paginate_button",
		"sPageButtonActive": "current",
		"sPageButtonDisabled": "disabled",
	
		/* Striping classes */
		"sStripeOdd": "odd",
		"sStripeEven": "even",
	
		/* Empty row */
		"sRowEmpty": "dataTables_empty",
	
		/* Features */
		"sWrapper": "dataTables_wrapper",
		"sFilter": "dataTables_filter",
		"sInfo": "dataTables_info",
		"sPaging": "dataTables_paginate paging_", /* Note that the type is postfixed */
		"sLength": "dataTables_length",
		"sProcessing": "dataTables_processing",
	
		/* Sorting */
		"sSortAsc": "sorting_asc",
		"sSortDesc": "sorting_desc",
		"sSortable": "sorting", /* Sortable in both directions */
		"sSortableAsc": "sorting_asc_disabled",
		"sSortableDesc": "sorting_desc_disabled",
		"sSortableNone": "sorting_disabled",
		"sSortColumn": "sorting_", /* Note that an int is postfixed for the sorting order */
	
		/* Filtering */
		"sFilterInput": "",
	
		/* Page length */
		"sLengthSelect": "",
	
		/* Scrolling */
		"sScrollWrapper": "dataTables_scroll",
		"sScrollHead": "dataTables_scrollHead",
		"sScrollHeadInner": "dataTables_scrollHeadInner",
		"sScrollBody": "dataTables_scrollBody",
		"sScrollFoot": "dataTables_scrollFoot",
		"sScrollFootInner": "dataTables_scrollFootInner",
	
		/* Misc */
		"sHeaderTH": "",
		"sFooterTH": "",
	
		// Deprecated
		"sSortJUIAsc": "",
		"sSortJUIDesc": "",
		"sSortJUI": "",
		"sSortJUIAscAllowed": "",
		"sSortJUIDescAllowed": "",
		"sSortJUIWrapper": "",
		"sSortIcon": "",
		"sJUIHeader": "",
		"sJUIFooter": ""
	} );
	
	
	(function() {
	
	// Reused strings for better compression. Closure compiler appears to have a
	// weird edge case where it is trying to expand strings rather than use the
	// variable version. This results in about 200 bytes being added, for very
	// little preference benefit since it this run on script load only.
	var _empty = '';
	_empty = '';
	
	var _stateDefault = _empty + 'ui-state-default';
	var _sortIcon     = _empty + 'css_right ui-icon ui-icon-';
	var _headerFooter = _empty + 'fg-toolbar ui-toolbar ui-widget-header ui-helper-clearfix';
	
	$.extend( DataTable.ext.oJUIClasses, DataTable.ext.classes, {
		/* Full numbers paging buttons */
		"sPageButton":         "fg-button ui-button "+_stateDefault,
		"sPageButtonActive":   "ui-state-disabled",
		"sPageButtonDisabled": "ui-state-disabled",
	
		/* Features */
		"sPaging": "dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi "+
			"ui-buttonset-multi paging_", /* Note that the type is postfixed */
	
		/* Sorting */
		"sSortAsc":            _stateDefault+" sorting_asc",
		"sSortDesc":           _stateDefault+" sorting_desc",
		"sSortable":           _stateDefault+" sorting",
		"sSortableAsc":        _stateDefault+" sorting_asc_disabled",
		"sSortableDesc":       _stateDefault+" sorting_desc_disabled",
		"sSortableNone":       _stateDefault+" sorting_disabled",
		"sSortJUIAsc":         _sortIcon+"triangle-1-n",
		"sSortJUIDesc":        _sortIcon+"triangle-1-s",
		"sSortJUI":            _sortIcon+"carat-2-n-s",
		"sSortJUIAscAllowed":  _sortIcon+"carat-1-n",
		"sSortJUIDescAllowed": _sortIcon+"carat-1-s",
		"sSortJUIWrapper":     "DataTables_sort_wrapper",
		"sSortIcon":           "DataTables_sort_icon",
	
		/* Scrolling */
		"sScrollHead": "dataTables_scrollHead "+_stateDefault,
		"sScrollFoot": "dataTables_scrollFoot "+_stateDefault,
	
		/* Misc */
		"sHeaderTH":  _stateDefault,
		"sFooterTH":  _stateDefault,
		"sJUIHeader": _headerFooter+" ui-corner-tl ui-corner-tr",
		"sJUIFooter": _headerFooter+" ui-corner-bl ui-corner-br"
	} );
	
	}());
	
	
	
	var extPagination = DataTable.ext.pager;
	
	function _numbers ( page, pages ) {
		var
			numbers = [],
			buttons = extPagination.numbers_length,
			half = Math.floor( buttons / 2 ),
			i = 1;
	
		if ( pages <= buttons ) {
			numbers = _range( 0, pages );
		}
		else if ( page <= half ) {
			numbers = _range( 0, buttons-2 );
			numbers.push( 'ellipsis' );
			numbers.push( pages-1 );
		}
		else if ( page >= pages - 1 - half ) {
			numbers = _range( pages-(buttons-2), pages );
			numbers.splice( 0, 0, 'ellipsis' ); // no unshift in ie6
			numbers.splice( 0, 0, 0 );
		}
		else {
			numbers = _range( page-1, page+2 );
			numbers.push( 'ellipsis' );
			numbers.push( pages-1 );
			numbers.splice( 0, 0, 'ellipsis' );
			numbers.splice( 0, 0, 0 );
		}
	
		numbers.DT_el = 'span';
		return numbers;
	}
	
	
	$.extend( extPagination, {
		simple: function ( page, pages ) {
			return [ 'previous', 'next' ];
		},
	
		full: function ( page, pages ) {
			return [  'first', 'previous', 'next', 'last' ];
		},
	
		simple_numbers: function ( page, pages ) {
			return [ 'previous', _numbers(page, pages), 'next' ];
		},
	
		full_numbers: function ( page, pages ) {
			return [ 'first', 'previous', _numbers(page, pages), 'next', 'last' ];
		},
	
		// For testing and plug-ins to use
		_numbers: _numbers,
		numbers_length: 7
	} );
	
	
	$.extend( true, DataTable.ext.renderer, {
		pageButton: {
			_: function ( settings, host, idx, buttons, page, pages ) {
				var classes = settings.oClasses;
				var lang = settings.oLanguage.oPaginate;
				var btnDisplay, btnClass, counter=0;
	
				var attach = function( container, buttons ) {
					var i, ien, node, button;
					var clickHandler = function ( e ) {
						_fnPageChange( settings, e.data.action, true );
					};
	
					for ( i=0, ien=buttons.length ; i<ien ; i++ ) {
						button = buttons[i];
	
						if ( $.isArray( button ) ) {
							var inner = $( '<'+(button.DT_el || 'div')+'/>' )
								.appendTo( container );
							attach( inner, button );
						}
						else {
							btnDisplay = '';
							btnClass = '';
	
							switch ( button ) {
								case 'ellipsis':
									container.append('<span>&hellip;</span>');
									break;
	
								case 'first':
									btnDisplay = lang.sFirst;
									btnClass = button + (page > 0 ?
										'' : ' '+classes.sPageButtonDisabled);
									break;
	
								case 'previous':
									btnDisplay = lang.sPrevious;
									btnClass = button + (page > 0 ?
										'' : ' '+classes.sPageButtonDisabled);
									break;
	
								case 'next':
									btnDisplay = lang.sNext;
									btnClass = button + (page < pages-1 ?
										'' : ' '+classes.sPageButtonDisabled);
									break;
	
								case 'last':
									btnDisplay = lang.sLast;
									btnClass = button + (page < pages-1 ?
										'' : ' '+classes.sPageButtonDisabled);
									break;
	
								default:
									btnDisplay = button + 1;
									btnClass = page === button ?
										classes.sPageButtonActive : '';
									break;
							}
	
							if ( btnDisplay ) {
								node = $('<a>', {
										'class': classes.sPageButton+' '+btnClass,
										'aria-controls': settings.sTableId,
										'data-dt-idx': counter,
										'tabindex': settings.iTabIndex,
										'id': idx === 0 && typeof button === 'string' ?
											settings.sTableId +'_'+ button :
											null
									} )
									.html( btnDisplay )
									.appendTo( container );
	
								_fnBindAction(
									node, {action: button}, clickHandler
								);
	
								counter++;
							}
						}
					}
				};
	
				// IE9 throws an 'unknown error' if document.activeElement is used
				// inside an iframe or frame. Try / catch the error. Not good for
				// accessibility, but neither are frames.
				try {
					// Because this approach is destroying and recreating the paging
					// elements, focus is lost on the select button which is bad for
					// accessibility. So we want to restore focus once the draw has
					// completed
					var activeEl = $(document.activeElement).data('dt-idx');
	
					attach( $(host).empty(), buttons );
	
					if ( activeEl !== null ) {
						$(host).find( '[data-dt-idx='+activeEl+']' ).focus();
					}
				}
				catch (e) {}
			}
		}
	} );
	
	
	
	var __numericReplace = function ( d, decimalPlace, re1, re2 ) {
		if ( d !== 0 && (!d || d === '-') ) {
			return -Infinity;
		}
	
		// If a decimal place other than `.` is used, it needs to be given to the
		// function so we can detect it and replace with a `.` which is the only
		// decimal place Javascript recognises - it is not locale aware.
		if ( decimalPlace ) {
			d = _numToDecimal( d, decimalPlace );
		}
	
		if ( d.replace ) {
			if ( re1 ) {
				d = d.replace( re1, '' );
			}
	
			if ( re2 ) {
				d = d.replace( re2, '' );
			}
		}
	
		return d * 1;
	};
	
	
	// Add the numeric 'deformatting' functions for sorting. This is done in a
	// function to provide an easy ability for the language options to add
	// additional methods if a non-period decimal place is used.
	function _addNumericSort ( decimalPlace ) {
		$.each(
			{
				// Plain numbers
				"num": function ( d ) {
					return __numericReplace( d, decimalPlace );
				},
	
				// Formatted numbers
				"num-fmt": function ( d ) {
					return __numericReplace( d, decimalPlace, _re_formatted_numeric );
				},
	
				// HTML numeric
				"html-num": function ( d ) {
					return __numericReplace( d, decimalPlace, _re_html );
				},
	
				// HTML numeric, formatted
				"html-num-fmt": function ( d ) {
					return __numericReplace( d, decimalPlace, _re_html, _re_formatted_numeric );
				}
			},
			function ( key, fn ) {
				_ext.type.order[ key+decimalPlace+'-pre' ] = fn;
			}
		);
	}
	
	
	// Default sort methods
	$.extend( _ext.type.order, {
		// Dates
		"date-pre": function ( d ) {
			return Date.parse( d ) || 0;
		},
	
		// html
		"html-pre": function ( a ) {
			return _empty(a) ?
				'' :
				a.replace ?
					a.replace( /<.*?>/g, "" ).toLowerCase() :
					a+'';
		},
	
		// string
		"string-pre": function ( a ) {
			// This is a little complex, but faster than always calling toString,
			// http://jsperf.com/tostring-v-check
			return _empty(a) ?
				'' :
				typeof a === 'string' ?
					a.toLowerCase() :
					! a.toString ?
						'' :
						a.toString();
		},
	
		// string-asc and -desc are retained only for compatibility with the old
		// sort methods
		"string-asc": function ( x, y ) {
			return ((x < y) ? -1 : ((x > y) ? 1 : 0));
		},
	
		"string-desc": function ( x, y ) {
			return ((x < y) ? 1 : ((x > y) ? -1 : 0));
		}
	} );
	
	
	// Numeric sorting types - order doesn't matter here
	_addNumericSort( '' );
	
	
	// Built in type detection. See model.ext.aTypes for information about
	// what is required from this methods.
	$.extend( DataTable.ext.type.detect, [
		// Plain numbers - first since V8 detects some plain numbers as dates
		// e.g. Date.parse('55') (but not all, e.g. Date.parse('22')...).
		function ( d, settings )
		{
			var decimal = settings.oLanguage.sDecimal;
			return _isNumber( d, decimal ) ? 'num'+decimal : null;
		},
	
		// Dates (only those recognised by the browser's Date.parse)
		function ( d, settings )
		{
			// V8 will remove any unknown characters at the start and end of the
			// expression, leading to false matches such as `$245.12` or `10%` being
			// a valid date. See forum thread 18941 for detail.
			if ( d && !(d instanceof Date) && ( ! _re_date_start.test(d) || ! _re_date_end.test(d) ) ) {
				return null;
			}
			var parsed = Date.parse(d);
			return (parsed !== null && !isNaN(parsed)) || _empty(d) ? 'date' : null;
		},
	
		// Formatted numbers
		function ( d, settings )
		{
			var decimal = settings.oLanguage.sDecimal;
			return _isNumber( d, decimal, true ) ? 'num-fmt'+decimal : null;
		},
	
		// HTML numeric
		function ( d, settings )
		{
			var decimal = settings.oLanguage.sDecimal;
			return _htmlNumeric( d, decimal ) ? 'html-num'+decimal : null;
		},
	
		// HTML numeric, formatted
		function ( d, settings )
		{
			var decimal = settings.oLanguage.sDecimal;
			return _htmlNumeric( d, decimal, true ) ? 'html-num-fmt'+decimal : null;
		},
	
		// HTML (this is strict checking - there must be html)
		function ( d, settings )
		{
			return _empty( d ) || (typeof d === 'string' && d.indexOf('<') !== -1) ?
				'html' : null;
		}
	] );
	
	
	
	// Filter formatting functions. See model.ext.ofnSearch for information about
	// what is required from these methods.
	
	
	$.extend( DataTable.ext.type.search, {
		html: function ( data ) {
			return _empty(data) ?
				data :
				typeof data === 'string' ?
					data
						.replace( _re_new_lines, " " )
						.replace( _re_html, "" ) :
					'';
		},
	
		string: function ( data ) {
			return _empty(data) ?
				data :
				typeof data === 'string' ?
					data.replace( _re_new_lines, " " ) :
					data;
		}
	} );
	
	
	
	$.extend( true, DataTable.ext.renderer, {
		header: {
			_: function ( settings, cell, column, classes ) {
				// No additional mark-up required
				// Attach a sort listener to update on sort - note that using the
				// `DT` namespace will allow the event to be removed automatically
				// on destroy, while the `dt` namespaced event is the one we are
				// listening for
				$(settings.nTable).on( 'order.dt.DT', function ( e, ctx, sorting, columns ) {
					if ( settings !== ctx ) { // need to check this this is the host
						return;               // table, not a nested one
					}
	
					var colIdx = column.idx;
	
					cell
						.removeClass(
							column.sSortingClass +' '+
							classes.sSortAsc +' '+
							classes.sSortDesc
						)
						.addClass( columns[ colIdx ] == 'asc' ?
							classes.sSortAsc : columns[ colIdx ] == 'desc' ?
								classes.sSortDesc :
								column.sSortingClass
						);
				} );
			},
	
			jqueryui: function ( settings, cell, column, classes ) {
				$('<div/>')
					.addClass( classes.sSortJUIWrapper )
					.append( cell.contents() )
					.append( $('<span/>')
						.addClass( classes.sSortIcon+' '+column.sSortingClassJUI )
					)
					.appendTo( cell );
	
				// Attach a sort listener to update on sort
				$(settings.nTable).on( 'order.dt.DT', function ( e, ctx, sorting, columns ) {
					if ( settings !== ctx ) {
						return;
					}
	
					var colIdx = column.idx;
	
					cell
						.removeClass( classes.sSortAsc +" "+classes.sSortDesc )
						.addClass( columns[ colIdx ] == 'asc' ?
							classes.sSortAsc : columns[ colIdx ] == 'desc' ?
								classes.sSortDesc :
								column.sSortingClass
						);
	
					cell
						.find( 'span.'+classes.sSortIcon )
						.removeClass(
							classes.sSortJUIAsc +" "+
							classes.sSortJUIDesc +" "+
							classes.sSortJUI +" "+
							classes.sSortJUIAscAllowed +" "+
							classes.sSortJUIDescAllowed
						)
						.addClass( columns[ colIdx ] == 'asc' ?
							classes.sSortJUIAsc : columns[ colIdx ] == 'desc' ?
								classes.sSortJUIDesc :
								column.sSortingClassJUI
						);
				} );
			}
		}
	} );
	
	/*
	 * Public helper functions. These aren't used internally by DataTables, or
	 * called by any of the options passed into DataTables, but they can be used
	 * externally by developers working with DataTables. They are helper functions
	 * to make working with DataTables a little bit easier.
	 */
	
	/**
	 * Helpers for `columns.render`.
	 *
	 * The options defined here can be used with the `columns.render` initialisation
	 * option to provide a display renderer. The following functions are defined:
	 *
	 * * `number` - Will format numeric data (defined by `columns.data`) for
	 *   display, retaining the original unformatted data for sorting and filtering.
	 *   It takes 4 parameters:
	 *   * `string` - Thousands grouping separator
	 *   * `string` - Decimal point indicator
	 *   * `integer` - Number of decimal points to show
	 *   * `string` (optional) - Prefix.
	 *
	 * @example
	 *   // Column definition using the number renderer
	 *   {
	 *     data: "salary",
	 *     render: $.fn.dataTable.render.number( '\'', '.', 0, '$' )
	 *   }
	 *
	 * @namespace
	 */
	DataTable.render = {
		number: function ( thousands, decimal, precision, prefix ) {
			return {
				display: function ( d ) {
					var negative = d < 0 ? '-' : '';
					d = Math.abs( parseFloat( d ) );
	
					var intPart = parseInt( d, 10 );
					var floatPart = precision ?
						decimal+(d - intPart).toFixed( precision ).substring( 2 ):
						'';
	
					return negative + (prefix||'') +
						intPart.toString().replace(
							/\B(?=(\d{3})+(?!\d))/g, thousands
						) +
						floatPart;
				}
			};
		}
	};
	
	
	/*
	 * This is really a good bit rubbish this method of exposing the internal methods
	 * publicly... - To be fixed in 2.0 using methods on the prototype
	 */
	
	
	/**
	 * Create a wrapper function for exporting an internal functions to an external API.
	 *  @param {string} fn API function name
	 *  @returns {function} wrapped function
	 *  @memberof DataTable#internal
	 */
	function _fnExternApiFunc (fn)
	{
		return function() {
			var args = [_fnSettingsFromNode( this[DataTable.ext.iApiIndex] )].concat(
				Array.prototype.slice.call(arguments)
			);
			return DataTable.ext.internal[fn].apply( this, args );
		};
	}
	
	
	/**
	 * Reference to internal functions for use by plug-in developers. Note that
	 * these methods are references to internal functions and are considered to be
	 * private. If you use these methods, be aware that they are liable to change
	 * between versions.
	 *  @namespace
	 */
	$.extend( DataTable.ext.internal, {
		_fnExternApiFunc: _fnExternApiFunc,
		_fnBuildAjax: _fnBuildAjax,
		_fnAjaxUpdate: _fnAjaxUpdate,
		_fnAjaxParameters: _fnAjaxParameters,
		_fnAjaxUpdateDraw: _fnAjaxUpdateDraw,
		_fnAjaxDataSrc: _fnAjaxDataSrc,
		_fnAddColumn: _fnAddColumn,
		_fnColumnOptions: _fnColumnOptions,
		_fnAdjustColumnSizing: _fnAdjustColumnSizing,
		_fnVisibleToColumnIndex: _fnVisibleToColumnIndex,
		_fnColumnIndexToVisible: _fnColumnIndexToVisible,
		_fnVisbleColumns: _fnVisbleColumns,
		_fnGetColumns: _fnGetColumns,
		_fnColumnTypes: _fnColumnTypes,
		_fnApplyColumnDefs: _fnApplyColumnDefs,
		_fnHungarianMap: _fnHungarianMap,
		_fnCamelToHungarian: _fnCamelToHungarian,
		_fnLanguageCompat: _fnLanguageCompat,
		_fnBrowserDetect: _fnBrowserDetect,
		_fnAddData: _fnAddData,
		_fnAddTr: _fnAddTr,
		_fnNodeToDataIndex: _fnNodeToDataIndex,
		_fnNodeToColumnIndex: _fnNodeToColumnIndex,
		_fnGetCellData: _fnGetCellData,
		_fnSetCellData: _fnSetCellData,
		_fnSplitObjNotation: _fnSplitObjNotation,
		_fnGetObjectDataFn: _fnGetObjectDataFn,
		_fnSetObjectDataFn: _fnSetObjectDataFn,
		_fnGetDataMaster: _fnGetDataMaster,
		_fnClearTable: _fnClearTable,
		_fnDeleteIndex: _fnDeleteIndex,
		_fnInvalidateRow: _fnInvalidateRow,
		_fnGetRowElements: _fnGetRowElements,
		_fnCreateTr: _fnCreateTr,
		_fnBuildHead: _fnBuildHead,
		_fnDrawHead: _fnDrawHead,
		_fnDraw: _fnDraw,
		_fnReDraw: _fnReDraw,
		_fnAddOptionsHtml: _fnAddOptionsHtml,
		_fnDetectHeader: _fnDetectHeader,
		_fnGetUniqueThs: _fnGetUniqueThs,
		_fnFeatureHtmlFilter: _fnFeatureHtmlFilter,
		_fnFilterComplete: _fnFilterComplete,
		_fnFilterCustom: _fnFilterCustom,
		_fnFilterColumn: _fnFilterColumn,
		_fnFilter: _fnFilter,
		_fnFilterCreateSearch: _fnFilterCreateSearch,
		_fnEscapeRegex: _fnEscapeRegex,
		_fnFilterData: _fnFilterData,
		_fnFeatureHtmlInfo: _fnFeatureHtmlInfo,
		_fnUpdateInfo: _fnUpdateInfo,
		_fnInfoMacros: _fnInfoMacros,
		_fnInitialise: _fnInitialise,
		_fnInitComplete: _fnInitComplete,
		_fnLengthChange: _fnLengthChange,
		_fnFeatureHtmlLength: _fnFeatureHtmlLength,
		_fnFeatureHtmlPaginate: _fnFeatureHtmlPaginate,
		_fnPageChange: _fnPageChange,
		_fnFeatureHtmlProcessing: _fnFeatureHtmlProcessing,
		_fnProcessingDisplay: _fnProcessingDisplay,
		_fnFeatureHtmlTable: _fnFeatureHtmlTable,
		_fnScrollDraw: _fnScrollDraw,
		_fnApplyToChildren: _fnApplyToChildren,
		_fnCalculateColumnWidths: _fnCalculateColumnWidths,
		_fnThrottle: _fnThrottle,
		_fnConvertToWidth: _fnConvertToWidth,
		_fnScrollingWidthAdjust: _fnScrollingWidthAdjust,
		_fnGetWidestNode: _fnGetWidestNode,
		_fnGetMaxLenString: _fnGetMaxLenString,
		_fnStringToCss: _fnStringToCss,
		_fnScrollBarWidth: _fnScrollBarWidth,
		_fnSortFlatten: _fnSortFlatten,
		_fnSort: _fnSort,
		_fnSortAria: _fnSortAria,
		_fnSortListener: _fnSortListener,
		_fnSortAttachListener: _fnSortAttachListener,
		_fnSortingClasses: _fnSortingClasses,
		_fnSortData: _fnSortData,
		_fnSaveState: _fnSaveState,
		_fnLoadState: _fnLoadState,
		_fnSettingsFromNode: _fnSettingsFromNode,
		_fnLog: _fnLog,
		_fnMap: _fnMap,
		_fnBindAction: _fnBindAction,
		_fnCallbackReg: _fnCallbackReg,
		_fnCallbackFire: _fnCallbackFire,
		_fnLengthOverflow: _fnLengthOverflow,
		_fnRenderer: _fnRenderer,
		_fnDataSource: _fnDataSource,
		_fnRowAttributes: _fnRowAttributes,
		_fnCalculateEnd: function () {} // Used by a lot of plug-ins, but redundant
		                                // in 1.10, so this dead-end function is
		                                // added to prevent errors
	} );
	

	// jQuery access
	$.fn.dataTable = DataTable;

	// Legacy aliases
	$.fn.dataTableSettings = DataTable.settings;
	$.fn.dataTableExt = DataTable.ext;

	// With a capital `D` we return a DataTables API instance rather than a
	// jQuery object
	$.fn.DataTable = function ( opts ) {
		return $(this).dataTable( opts ).api();
	};

	// All properties that are available to $.fn.dataTable should also be
	// available on $.fn.DataTable
	$.each( DataTable, function ( prop, val ) {
		$.fn.DataTable[ prop ] = val;
	} );


	// Information about events fired by DataTables - for documentation.
	/**
	 * Draw event, fired whenever the table is redrawn on the page, at the same
	 * point as fnDrawCallback. This may be useful for binding events or
	 * performing calculations when the table is altered at all.
	 *  @name DataTable#draw.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * Search event, fired when the searching applied to the table (using the
	 * built-in global search, or column filters) is altered.
	 *  @name DataTable#search.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * Page change event, fired when the paging of the table is altered.
	 *  @name DataTable#page.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * Order event, fired when the ordering applied to the table is altered.
	 *  @name DataTable#order.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * DataTables initialisation complete event, fired when the table is fully
	 * drawn, including Ajax data loaded, if Ajax data is required.
	 *  @name DataTable#init.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} oSettings DataTables settings object
	 *  @param {object} json The JSON object request from the server - only
	 *    present if client-side Ajax sourced data is used</li></ol>
	 */

	/**
	 * State save event, fired when the table has changed state a new state save
	 * is required. This event allows modification of the state saving object
	 * prior to actually doing the save, including addition or other state
	 * properties (for plug-ins) or modification of a DataTables core property.
	 *  @name DataTable#stateSaveParams.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} oSettings DataTables settings object
	 *  @param {object} json The state information to be saved
	 */

	/**
	 * State load event, fired when the table is loading state from the stored
	 * data, but prior to the settings object being modified by the saved state
	 * - allowing modification of the saved state is required or loading of
	 * state for a plug-in.
	 *  @name DataTable#stateLoadParams.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} oSettings DataTables settings object
	 *  @param {object} json The saved state information
	 */

	/**
	 * State loaded event, fired when state has been loaded from stored data and
	 * the settings object has been modified by the loaded data.
	 *  @name DataTable#stateLoaded.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} oSettings DataTables settings object
	 *  @param {object} json The saved state information
	 */

	/**
	 * Processing event, fired when DataTables is doing some kind of processing
	 * (be it, order, searcg or anything else). It can be used to indicate to
	 * the end user that there is something happening, or that something has
	 * finished.
	 *  @name DataTable#processing.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} oSettings DataTables settings object
	 *  @param {boolean} bShow Flag for if DataTables is doing processing or not
	 */

	/**
	 * Ajax (XHR) event, fired whenever an Ajax request is completed from a
	 * request to made to the server for new data. This event is called before
	 * DataTables processed the returned data, so it can also be used to pre-
	 * process the data returned from the server, if needed.
	 *
	 * Note that this trigger is called in `fnServerData`, if you override
	 * `fnServerData` and which to use this event, you need to trigger it in you
	 * success function.
	 *  @name DataTable#xhr.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 *  @param {object} json JSON returned from the server
	 *
	 *  @example
	 *     // Use a custom property returned from the server in another DOM element
	 *     $('#table').dataTable().on('xhr.dt', function (e, settings, json) {
	 *       $('#status').html( json.status );
	 *     } );
	 *
	 *  @example
	 *     // Pre-process the data returned from the server
	 *     $('#table').dataTable().on('xhr.dt', function (e, settings, json) {
	 *       for ( var i=0, ien=json.aaData.length ; i<ien ; i++ ) {
	 *         json.aaData[i].sum = json.aaData[i].one + json.aaData[i].two;
	 *       }
	 *       // Note no return - manipulate the data directly in the JSON object.
	 *     } );
	 */

	/**
	 * Destroy event, fired when the DataTable is destroyed by calling fnDestroy
	 * or passing the bDestroy:true parameter in the initialisation object. This
	 * can be used to remove bound events, added DOM nodes, etc.
	 *  @name DataTable#destroy.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * Page length change event, fired when number of records to show on each
	 * page (the length) is changed.
	 *  @name DataTable#length.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 *  @param {integer} len New length
	 */

	/**
	 * Column sizing has changed.
	 *  @name DataTable#column-sizing.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 */

	/**
	 * Column visibility has changed.
	 *  @name DataTable#column-visibility.dt
	 *  @event
	 *  @param {event} e jQuery event object
	 *  @param {object} o DataTables settings object {@link DataTable.models.oSettings}
	 *  @param {int} column Column index
	 *  @param {bool} vis `false` if column now hidden, or `true` if visible
	 */

	return $.fn.dataTable;
}));

}(window, document));

