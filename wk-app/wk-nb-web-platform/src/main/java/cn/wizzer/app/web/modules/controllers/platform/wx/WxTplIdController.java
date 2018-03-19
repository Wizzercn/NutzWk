package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.ext.wx.WxService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_tpl_id;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxTplIdService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/wx/tpl/id")
public class WxTplIdController {
	private static final Log log = Logs.get();
	@Inject
	@Reference
	private WxTplIdService wxTplIdService;
	@Inject
	@Reference
	private WxConfigService wxConfigService;
	@Inject
	private WxService wxService;

	@At({"", "/index/?"})
	@Ok("beetl:/platform/wx/tpl/id/index.html")
	@RequiresPermissions("wx.tpl.id")
	public void index(String wxid, HttpServletRequest req) {
		List<Wx_config> list = wxConfigService.query(Cnd.NEW());
		if (list.size() > 0 && Strings.isBlank(wxid)) {
			wxid = list.get(0).getId();
		}
		req.setAttribute("wxList", list);
		req.setAttribute("wxid", Strings.sBlank(wxid));
	}

	@At
	@Ok("json:full")
	@RequiresPermissions("wx.tpl.id")
	public Object data(@Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
		if(!Strings.isBlank(wxid)){
			cnd.and("wxid","=",wxid);
		}
    	return wxTplIdService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/platform/wx/tpl/id/add.html")
    @RequiresPermissions("wx.tpl.id")
	public void add(String wxid, HttpServletRequest req) {
		req.setAttribute("wxid", wxid);
    }

    @At
    @Ok("json")
    @SLog(tag = "添加模板", msg = "")
	@RequiresPermissions("wx.tpl.id.add")
	public Object addDo(@Param("..") Wx_tpl_id wxTplId, HttpServletRequest req) {
		try {
			WxApi2 wxApi2 = wxService.getWxApi2(wxTplId.getWxid());
			WxResp wxResp=wxApi2.template_api_add_template(wxTplId.getId());
			if (wxResp.errcode() == 0) {
				wxTplId.setTemplate_id(wxResp.template_id());
				wxTplIdService.insert(wxTplId);
				return Result.success("system.success");
			}
			return Result.error("system.error");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At({"/delete","/delete/?"})
    @Ok("json")
    @SLog(tag = "删除模板", msg = "ID:${args[3].getAttribute('id')}")
	@RequiresPermissions("wx.tpl.id.delete")
	public Object delete(String id,@Param("wxid")String wxid, @Param("ids") String[] ids ,HttpServletRequest req) {
		try {
			WxApi2 wxApi2 = wxService.getWxApi2(wxid);
			if(ids!=null&&ids.length>0){
				for (String i:ids){
					Wx_tpl_id wxTplId=wxTplIdService.fetch(i);
					WxResp wxResp=wxApi2.template_api_del_template(wxTplId.getTemplate_id());
					if (wxResp.errcode() == 0) {
						wxTplIdService.delete(i);
						return Result.success("system.success");
					}
				}
				wxTplIdService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				Wx_tpl_id wxTplId=wxTplIdService.fetch(id);
				WxResp wxResp=wxApi2.template_api_del_template(wxTplId.getTemplate_id());
				if (wxResp.errcode() == 0) {
					wxTplIdService.delete(id);
					return Result.success("system.success");
				}
    			req.setAttribute("id", id);
			}
			return Result.success("system.error");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At("/detail/?")
    @Ok("beetl:/platform/wx/tpl/id/detail.html")
    @RequiresPermissions("wx.tpl.id")
	public Object detail(String id) {
		if (!Strings.isBlank(id)) {
			return wxTplIdService.fetch(id);

		}
		return null;
    }

}
