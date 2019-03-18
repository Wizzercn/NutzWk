package cn.wizzer.app.web.modules.controllers.open.ueditor;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.boot.starter.ftp.FtpService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/bd")
public class BdController {
    @Inject
    private FtpService ftpService;

    @At
    @Ok("json")
    public Object index(@Param("action") String action, HttpServletRequest req) {
        return Json.fromJson(new String(Streams.readBytesAndClose(Files.findFileAsStream("static/assets/plugins/ueditor/nutz/config.json"))).replace("$base", Globals.AppBase));
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("json")
    @RequiresAuthentication
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadimage(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return nutMap.addv("state", "FAIL");
            } else if (tf == null) {
                return nutMap.addv("state", "FAIL");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = Globals.AppFileDomain + filePath + fileName;
                if (ftpService.upload(filePath, fileName, tf.getInputStream())) {
                    nutMap.addv("state", "SUCCESS");
                    nutMap.addv("url", url);
                    nutMap.addv("original", tf.getSubmittedFileName());
                    nutMap.addv("type", suffixName);
                    nutMap.addv("size", tf.getSize());
                    return nutMap;
                } else {
                    return nutMap.addv("state", "FAIL");
                }
            }
        } catch (Exception e) {
            return nutMap.addv("state", "FAIL");
        } catch (Throwable e) {
            return nutMap.addv("state", "FAIL");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:fileUpload"})
    @POST
    @At
    @Ok("json")
    @RequiresAuthentication
    public Object uploadfile(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return nutMap.addv("state", "FAIL");
            } else if (tf == null) {
                return nutMap.addv("state", "FAIL");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = Globals.AppFileDomain + filePath + fileName;
                if (ftpService.upload(filePath, fileName, tf.getInputStream())) {
                    nutMap.addv("state", "SUCCESS");
                    nutMap.addv("url", url);
                    nutMap.addv("original", tf.getSubmittedFileName());
                    nutMap.addv("type", suffixName);
                    nutMap.addv("size", tf.getSize());
                    return nutMap;
                } else {
                    return nutMap.addv("state", "FAIL");
                }
            }
        } catch (Exception e) {
            return nutMap.addv("state", "FAIL");
        } catch (Throwable e) {
            return nutMap.addv("state", "FAIL");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:videoUpload"})
    @POST
    @At
    @Ok("json")
    @RequiresAuthentication
    public Object uploadvideo(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return nutMap.addv("state", "FAIL");
            } else if (tf == null) {
                return nutMap.addv("state", "FAIL");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = Globals.AppFileDomain + filePath + fileName;
                if (ftpService.upload(filePath, fileName, tf.getInputStream())) {
                    nutMap.addv("state", "SUCCESS");
                    nutMap.addv("url", url);
                    nutMap.addv("original", tf.getSubmittedFileName());
                    nutMap.addv("type", suffixName);
                    nutMap.addv("size", tf.getSize());
                    return nutMap;
                } else {
                    return nutMap.addv("state", "FAIL");
                }
            }
        } catch (Exception e) {
            return nutMap.addv("state", "FAIL");
        } catch (Throwable e) {
            return nutMap.addv("state", "FAIL");
        }
    }
}
