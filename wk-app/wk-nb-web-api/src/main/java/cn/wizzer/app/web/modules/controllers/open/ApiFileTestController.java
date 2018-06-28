package cn.wizzer.app.web.modules.controllers.open;

import cn.wizzer.app.web.commons.filter.ApiHeaderSignFilter;
import cn.wizzer.app.web.commons.utils.SignUtil;
import cn.wizzer.framework.base.Result;
import org.nutz.filepool.NutFilePool;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean
@At("/open/api/file")
public class ApiFileTestController {
    private final static Log log = Logs.get();
    @Inject("refer:tmpFilePool")
    private NutFilePool tmpFilePool;
    @Inject
    private PropertiesProxy conf;
    @Inject
    private RedisService redisService;

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/upload/image_by_sign")
    @Ok("json")
    public Object upload1(@Param("Filedata") TempFile tf, @Param("..") NutMap params, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            log.debug("params::" + Json.toJson(params));
            params.remove("Filedata");//Filedata不参与签名
            Result result = checkSign(params);
            if (result == null) {
                return Result.error("签名出错");
            }
            if (result.getCode() != 0) {
                return result;
            }
            if (err != null && err.getAdaptorErr() != null) {
                return Result.error("文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                long fileId = tmpFilePool.getFileId(tf.getFile());
                //获取临时文件ID,在表单提交的API里通过此ID获取文件
                //tmpFilePool.getFile(fileId,"jpg");
                String suffix = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1).toLowerCase();
                return Result.success("上传成功",
                        NutMap.NEW().addv("file_id", fileId)
                                .addv("file_suffix", suffix));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("文件格式错误");
        }
    }

    @Filters({@By(type = ApiHeaderSignFilter.class)})
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/upload/image_by_header_sign")
    @Ok("json")
    public Object upload2(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return Result.error("文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                long fileId = tmpFilePool.getFileId(tf.getFile());
                //获取临时文件ID,在表单提交的API里通过此ID获取文件
                //tmpFilePool.getFile(fileId,"jpg");
                String suffix = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1).toLowerCase();
                return Result.success("上传成功",
                        NutMap.NEW().addv("file_id", fileId)
                                .addv("file_suffix", suffix));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("文件格式错误");
        }
    }

    private Result checkSign(Map<String, Object> paramMap) {
        try {
            String appid_sys = conf.get("apitoken.appid", "");
            String appkey_sys = conf.get("apitoken.appkey", "");
            log.debug("paramMap:::\r\n" + Json.toJson(paramMap));
            String appid = Strings.sNull(paramMap.get("appid"));
            String sign = Strings.sNull(paramMap.get("sign"));
            String timestamp = Strings.sNull(paramMap.get("timestamp"));
            String nonce = Strings.sNull(paramMap.get("nonce"));
            if (!appid_sys.equals(appid)) {
                return Result.error(1, "appid不正确");
            }
            if (Times.getTS() - Long.valueOf(timestamp) > 60 * 1000) {//时间戳相差大于1分钟则为无效的
                return Result.error(2, "timestamp不正确");
            }
            String nonceCache = redisService.get("api_sign_nonce:" + appid + "_" + nonce);
            if (Strings.isNotBlank(nonceCache)) {//如果一分钟内nonce是重复的则为无效,让nonce只能使用一次
                return Result.error(3, "nonce不正确");

            }
            if (!SignUtil.createSign(appkey_sys, paramMap).equalsIgnoreCase(sign)) {
                return Result.error(4, "sign签名不正确");
            }
            //nonce保存到缓存
            redisService.set("api_sign_nonce:" + appid + "_" + nonce, nonce);
            redisService.expire("api_sign_nonce:" + appid + "_" + nonce, 60);
            return Result.success("验证成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(-1, "系统异常");
        }
    }
}
