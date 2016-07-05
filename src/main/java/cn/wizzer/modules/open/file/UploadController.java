package cn.wizzer.modules.open.file;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.util.DateUtil;
import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/open/file/upload")
public class UploadController {
    private static final Log log = Logs.get();

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("json")
    public Object image(@Param("Filedata") TempFile tf, AdaptorErrorContext err, HttpServletRequest req) {
        if (err != null && err.getAdaptorErr() != null) {
            return Result.error("文件大小不符合规定", req);
        } else if (tf == null) {
            return Result.error("空文件", req);
        } else {
            try {
                String p = Globals.AppRoot;
                String f = Globals.AppUploadPath + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU64() + tf.getMeta().getFileExtension();
                Files.copy(tf.getFile(), new File(p + f));
                return Result.success("上传成功", f, req);
            } catch (Exception e) {
                return Result.error("系统错误", req);
            } catch (Throwable e) {
                return Result.error("图片格式错误", req);
            }
        }
    }
}
