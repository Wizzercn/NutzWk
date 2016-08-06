package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_tpl_id;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxTplIdService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/private/wx/tpl/id")
@Filters({@By(type = PrivateFilter.class)})
public class WxTplIdController {
	private static final Log log = Logs.get();
	@Inject
	private WxTplIdService wxTplIdService;
	@Inject
	private WxConfigService wxConfigService;

	@At({"", "/index/?"})
	@Ok("beetl:/private/wx/tpl/id/index.html")
	@RequiresAuthentication
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
	@RequiresAuthentication
	public Object data(@Param("wxid") String wxid,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
		if(!Strings.isBlank(wxid)){
			cnd.and("wxid","=",wxid);
		}
    	return wxTplIdService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/private/wx/tpl/id/add.html")
    @RequiresAuthentication
	public void add(String wxid, HttpServletRequest req) {
		req.setAttribute("wxid", wxid);
    }

    @At
    @Ok("json")
    @SLog(tag = "添加模板", msg = "")
    public Object addDo(@Param("..") Wx_tpl_id wxTplId, HttpServletRequest req) {
		try {
			WxApi2 wxApi2 = wxConfigService.getWxApi2(wxTplId.getWxid());
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
    public Object delete(String id,@Param("wxid")String wxid, @Param("ids") String[] ids ,HttpServletRequest req) {
		try {
			WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
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
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At("/detail/?")
    @Ok("beetl:/private/wx/tpl/id/detail.html")
    @RequiresAuthentication
	public Object detail(String id) {
		if (!Strings.isBlank(id)) {
			return wxTplIdService.fetch(id);

		}
		return null;
    }

}
