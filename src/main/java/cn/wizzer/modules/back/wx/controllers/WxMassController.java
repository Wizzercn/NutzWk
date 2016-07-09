package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxMassNewsService;
import cn.wizzer.modules.back.wx.services.WxMassService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/private/wx/msg/mass")
@Filters({@By(type = PrivateFilter.class)})
public class WxMassController {
    private static final Log log = Logs.get();
    @Inject
    WxMassService wxMassService;
    @Inject
    WxMassNewsService wxMassNewsService;
    @Inject
    WxConfigService wxConfigService;

    @At({"/", "/?"})
    @Ok("beetl:/private/wx/msg/mass/index.html")
    @RequiresAuthentication
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
    }

    @At("/massData/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object massData(String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxMassService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/news/?")
    @Ok("beetl:/private/wx/msg/mass/news.html")
    @RequiresAuthentication
    public void news(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
    }

    @At("/newsData/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object newsData(String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxMassNewsService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/addNews/?")
    @Ok("beetl:/private/wx/msg/mass/add.html")
    @RequiresAuthentication
    public void add(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        req.getSession().setAttribute("wxid", wxid);
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/uploadThumb/?")
    @Ok("json")
    @RequiresAuthentication
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadThumb(String wxid, @Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        if (err != null && err.getAdaptorErr() != null) {
            return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
        } else if (tf == null) {
            return Result.error("空文件");
        } else {
            try {
                WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
                WxResp resp = wxApi2.media_upload("thumb", tf.getFile());
                if (resp.errcode() > 0) {
                    return Result.error(resp.errmsg());
                }
                return Result.success("上传成功",resp.get("thumb_media_id"));
            } catch (Exception e) {
                return Result.error("系统错误");
            } catch (Throwable e) {
                return Result.error("图片格式错误");
            }
        }
    }
}
