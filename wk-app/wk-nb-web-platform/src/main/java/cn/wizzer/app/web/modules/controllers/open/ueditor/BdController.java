package cn.wizzer.app.web.modules.controllers.open.ueditor;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.resource.Scans;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/bd")
public class BdController {
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
                String uri = "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                String f = Globals.AppUploadPath + uri;
                Files.write(new File(f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppUploadBase + uri);
                nutMap.addv("original", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                return nutMap;
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
                String uri = "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                String f = Globals.AppUploadPath + uri;
                Files.write(new File(f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppUploadBase + uri);
                nutMap.addv("original", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                return nutMap;
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
                String uri = "/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                String f = Globals.AppUploadPath + uri;
                Files.write(new File(f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppUploadBase + uri);
                nutMap.addv("original", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                return nutMap;
            }
        } catch (Exception e) {
            return nutMap.addv("state", "FAIL");
        } catch (Throwable e) {
            return nutMap.addv("state", "FAIL");
        }
    }
}
