package com.budwk.app.web.controllers.open.file;

import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.DateUtil;
import com.budwk.app.web.commons.base.Globals;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

;

/**
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/open/file/upload")
public class UploadController {
    private static final Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:fileUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object file(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;

                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + url, tf.getInputStream());
                return Result.success("上传成功", NutMap.NEW().addv("file_type", suffixName).addv("file_name", tf.getSubmittedFileName()).addv("file_size", tf.getSize()).addv("file_url", url));

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("文件格式错误");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:videoUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object video(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;

                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + url, tf.getInputStream());
                return Result.success("上传成功", NutMap.NEW().addv("file_type", suffixName).addv("file_name", tf.getSubmittedFileName()).addv("file_size", tf.getSize()).addv("file_url", url));

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("文件格式错误");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object image(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;

                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + url, tf.getInputStream());
                return Result.success("上传成功", url);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("图片格式错误");
        }
    }


    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:fileUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object cmsfile(@Param("Filedata") TempFile tf, @Param("site") String site, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String prefix = "/" + site + "/www";
                String filePath = "/upload/file/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + prefix + url, tf.getInputStream());
                return Result.success("上传成功", NutMap.NEW().addv("file_type", suffixName).addv("file_name", tf.getSubmittedFileName()).addv("file_size", tf.getSize()).addv("file_url", url));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("文件格式错误");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:videoUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object cmsvideo(@Param("Filedata") TempFile tf, @Param("site") String site, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String prefix = "/" + site + "/www";
                String filePath = "/upload/video/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + prefix + url, tf.getInputStream());
                return Result.success("上传成功", NutMap.NEW().addv("file_type", suffixName).addv("file_name", tf.getSubmittedFileName()).addv("file_size", tf.getSize()).addv("file_url", url));

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("文件格式错误");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("json")
    @SaCheckLogin
    //AdaptorErrorContext必须是最后一个参数
    public Object cmsimage(@Param("Filedata") TempFile tf, @Param("site") String site, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String prefix = "/" + site + "/www";
                String filePath = "/upload/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                String staticPath = conf.get("jetty.staticPath", "/files");
                Files.write(staticPath + prefix + url, tf.getInputStream());
                return Result.success("上传成功", url);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("图片格式错误");
        }
    }
}
