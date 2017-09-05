package cn.wizzer.app.web.modules.controllers.open.ueditor;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.framework.util.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Encoding;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/bd")
public class BdController {
    @At
    @Ok("json")
    public Object index(@Param("action") String action, HttpServletRequest req) {
        InputStream ins = Mvcs.getServletContext().getResourceAsStream("/assets/plugins/ueditor/nutz/config.json");
        String json = Streams.readAndClose(new InputStreamReader(ins, Encoding.CHARSET_UTF8));
        return Json.fromJson(json.replace("$base", Globals.AppBase));
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
                String p = Globals.AppRoot;
                String f = Globals.AppUploadPath + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                Files.write(new File(p + f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppBase + f);
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
                String p = Globals.AppRoot;
                String f = Globals.AppUploadPath + "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                Files.write(new File(p + f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppBase + f);
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
                String p = Globals.AppRoot;
                String f = Globals.AppUploadPath + "/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
                Files.write(new File(p + f), tf.getInputStream());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppBase + f);
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
