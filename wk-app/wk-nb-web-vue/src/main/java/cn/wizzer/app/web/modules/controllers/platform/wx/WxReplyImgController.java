package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.ext.wx.WxService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.DateUtil;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_reply_img;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxReplyImgService;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
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
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/platform/wx/reply/img")
public class WxReplyImgController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxReplyImgService wxReplyImgService;
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @At({"/", "/index/?"})
    @Ok("beetl:/platform/wx/reply/img/index.html")
    @RequiresPermissions("wx.reply")
    public void index(String wxid, HttpServletRequest req) {
        Wx_config wxConfig = null;
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxConfig = list.get(0);
        }
        if (Strings.isNotBlank(wxid)) {
            wxConfig = wxConfigService.fetch(wxid);
        }
        req.setAttribute("wxConfig", wxConfig);
        req.setAttribute("wxList", list);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.img.add")
    @SLog(tag = "添加回复图片", msg = "图片路径:${args[0].picurl}")
    public Object addDo(@Param("..") Wx_reply_img img, HttpServletRequest req) {
        try {
            img.setOpBy(StringUtil.getPlatformUid());
            wxReplyImgService.insert(img);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("wx.reply")
    public Object edit(String id) {
        try {
            return Result.success().addData(wxReplyImgService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.img.edit")
    @SLog(tag = "修改回复图片", msg = "图片路径:${args[0].picurl}")
    public Object editDo(@Param("..") Wx_reply_img img, HttpServletRequest req) {
        try {
            wxReplyImgService.updateIgnoreNull(img);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.reply.img.delete")
    @SLog(tag = "删除回复图片", msg = "图片路径:${args[1].getAttribute('picurl')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("picurl", wxReplyImgService.fetch(id).getPicurl());
            wxReplyImgService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("wx.reply.img.delete")
    @SLog(tag = "删除回复图片", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String ids, HttpServletRequest req) {
        try {
            wxReplyImgService.delete(StringUtils.split(ids, ","));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wx.reply")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxReplyImgService.listPage(pageNumber, pageSize, cnd));
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/uploadImage/?")
    @Ok("json")
    @RequiresPermissions("wx.reply")
    @SuppressWarnings("deprecation")
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadImage(String wxid, @Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                WxApi2 wxApi2 = wxService.getWxApi2(wxid);
                WxResp resp = wxApi2.add_material("image", tf.getFile());
                if (resp.errcode() != 0) {
                    return Result.error(resp.errmsg());
                }
                String uri = "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                String f = Globals.AppUploadPath + uri;
                Files.write(new File(f), tf.getInputStream());
                return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("media_id"))
                        .addv("picurl", Globals.AppUploadBase + uri));
            }
        } catch (Exception e) {
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("图片格式错误");
        }
    }
}
