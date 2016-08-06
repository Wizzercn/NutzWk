package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_tpl_log;
import cn.wizzer.modules.back.wx.services.WxTplLogService;
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
@At("/private/wx/tpl/log")
@Filters({@By(type = PrivateFilter.class)})
public class WxTplLogController {
	private static final Log log = Logs.get();
	@Inject
	private WxTplLogService wxTplLogService;

	@At("")
	@Ok("beetl:/private/wx/tpl/log/index.html")
	@RequiresAuthentication
	public void index() {

	}

	@At
	@Ok("json:full")
	@RequiresAuthentication
	public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return wxTplLogService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/private/wx/tpl/log/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建Wx_tpl_log", msg = "")
    public Object addDo(@Param("..") Wx_tpl_log wxTplLog, HttpServletRequest req) {
		try {
			wxTplLogService.insert(wxTplLog);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/tpl/log/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
		return wxTplLogService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改Wx_tpl_log", msg = "ID:${args[0].id}")
    public Object editDo(@Param("..") Wx_tpl_log wxTplLog, HttpServletRequest req) {
		try {

			wxTplLog.setOpAt((int) (System.currentTimeMillis() / 1000));
			wxTplLogService.updateIgnoreNull(wxTplLog);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At({"/delete","/delete/?"})
    @Ok("json")
    @SLog(tag = "删除Wx_tpl_log", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String id, @Param("ids") String[] ids ,HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				wxTplLogService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				wxTplLogService.delete(id);
    			req.setAttribute("id", id);
			}
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At("/detail/?")
    @Ok("beetl:/private/wx/tpl/log/detail.html")
    @RequiresAuthentication
	public Object detail(String id) {
		if (!Strings.isBlank(id)) {
			return wxTplLogService.fetch(id);

		}
		return null;
    }

}
