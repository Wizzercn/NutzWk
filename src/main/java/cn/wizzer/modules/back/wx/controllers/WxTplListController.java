package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_tpl_list;
import cn.wizzer.modules.back.wx.services.WxTplListService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/private/wx/tpl/list")
@Filters({@By(type = PrivateFilter.class)})
public class WxTplListController {
	private static final Log log = Logs.get();
	@Inject
	private WxTplListService wxTplListService;

	@At("")
	@Ok("beetl:/private/wx/tpl/list/index.html")
	@RequiresAuthentication
	public void index() {

	}

	@At
	@Ok("json:full")
	@RequiresAuthentication
	public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return wxTplListService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/private/wx/tpl/list/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建Wx_tpl_list", msg = "")
    public Object addDo(@Param("..") Wx_tpl_list wxTplList, HttpServletRequest req) {
		try {
			wxTplListService.insert(wxTplList);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/tpl/list/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
		return wxTplListService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改Wx_tpl_list", msg = "ID:${args[0].id}")
    public Object editDo(@Param("..") Wx_tpl_list wxTplList, HttpServletRequest req) {
		try {

			wxTplList.setOpAt((int) (System.currentTimeMillis() / 1000));
			wxTplListService.updateIgnoreNull(wxTplList);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At({"/delete","/delete/?"})
    @Ok("json")
    @SLog(tag = "删除Wx_tpl_list", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String id, @Param("ids") String[] ids ,HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				wxTplListService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				wxTplListService.delete(id);
    			req.setAttribute("id", id);
			}
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At("/detail/?")
    @Ok("beetl:/private/wx/tpl/list/detail.html")
    @RequiresAuthentication
	public Object detail(String id) {
		if (!Strings.isBlank(id)) {
			return wxTplListService.fetch(id);

		}
		return null;
    }

}
