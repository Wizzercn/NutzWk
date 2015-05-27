package cn.wizzer.modules.file;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.wizzer.common.action.BaseAction;
import cn.wizzer.common.config.Globals;
import cn.wizzer.common.file.FileType;
import cn.wizzer.common.filter.GlobalsFilter;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.common.util.DecodeUtil;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-12-17 下午1:12:37
 */
@IocBean
@At("/private/file")
@Filters({@By(type = GlobalsFilter.class)})
public class FileAction extends BaseAction {
    @Inject
    protected UploadAdaptor upload;
    private final Log log = Logs.get();

    @At
    @Ok("raw")
    @AdaptBy(type = UploadAdaptor.class, args = "ioc:upload")
    public String upload(@Param("Filedata") TempFile tmpFile, @Param("ueditor") String ueditor1, @Param("filetype") String filetype, @Param("title") String title, @Param("file_password") String file_password, @Param("file_username") String file_username, AdaptorErrorContext errCtx) {
        boolean ueditor = false;
        if ("true".equals(Strings.sNull(ueditor1)))
            ueditor = true;
        Map<String, Object> js = new HashMap<String, Object>();
        if (errCtx != null) {
            if (errCtx.getAdaptorErr() != null) {
                if (ueditor) {
                    js.put("state", errorMsg(errCtx.getAdaptorErr()));
                } else {
                    js.put("error", errorMsg(errCtx.getAdaptorErr()));
                    js.put("msg", "");
                }
                return Json.toJson(js);
            }
            for (Throwable e : errCtx.getErrors()) {
                if (e != null) {
                    if (ueditor) {
                        js.put("state", errorMsg(e));
                    } else {
                        js.put("error", errorMsg(e));
                        js.put("msg", "");
                    }
                    return Json.toJson(js);
                }
            }
        }
        if (Strings.isBlank(file_username) || Strings.isBlank(file_password)) {
            if (ueditor) {
                js.put("state", "错误：请配置文件服务器用户名及密码！");
            } else {
                js.put("error", "错误：请配置文件服务器用户名及密码！");
                js.put("msg", "");
            }
            return Json.toJson(js);
        }
        FileServer fileServer = Mvcs.getIoc().get(FileServer.class);
        if (!fileServer.getUsername().equals(file_username) || !fileServer.getPassword().equals(DecodeUtil.Decrypt(file_password, "file"))) {
            if (ueditor) {
                js.put("state", "错误：文件服务器用户名或密码不正确！");
            } else {
                js.put("error", "错误：文件服务器用户名或密码不正确！");
                js.put("msg", "");
            }
            return Json.toJson(js);
        }
        String filename = tmpFile.getMeta().getFileLocalName();
        File file = tmpFile.getFile();
        String suffixname = Files.getSuffixName(file).toLowerCase();
        String ss = FileType.getSuffixname(filetype);
        if (!ss.contains(suffixname)) {
            if (ueditor) {
                js.put("state", "错误：不允许的文件扩展名，允许：" + ss);
            } else {
                js.put("error", "错误：不允许的文件扩展名，允许：" + ss);
                js.put("msg", "");
            }
            return Json.toJson(js);
        }
        if (tmpFile == null || tmpFile.getFile().length() < 10) {
            if (ueditor) {
                js.put("state", "错误：文件大小不可小于10B！");
            } else {
                js.put("error", "错误：文件大小不可小于10B！");
                js.put("msg", "");
            }
            return Json.toJson(js);

        }


        long len = tmpFile.getFile().length();

        filename = filename.substring(0, filename.lastIndexOf(".")) + "." + suffixname;
        String date = DateUtil.getToday().replace("-", "/");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fname = uuid + "." + Files.getSuffixName(file).toLowerCase();
        String dest = webPath(date, fname, suffixname);
        try {
            Files.move(file, new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
            if (ueditor) {
                js.put("state", "错误：文件服务器IO异常！");
            } else {
                js.put("error", "错误：文件服务器IO异常！");
                js.put("msg", "");
            }
            return Json.toJson(js);
        }
        Map<String, Object> fs = new HashMap<String, Object>();
        if (ueditor) {
            js.put("state", "SUCCESS");
            js.put("original", filename);
            js.put("url", "/upload/" + FileType.getFileType(suffixname) + "/" + date + "/" + fname);
            js.put("title", title);
        } else {
            fs.put("filename", filename);
            fs.put("filepath", "/upload/" + FileType.getFileType(suffixname) + "/" + date + "/" + fname);
            fs.put("filesize", getFileSize(len, 2));
            js.put("error", "");
            js.put("msg", fs);
        }
        return Json.toJson(js);

    }

    public String webPath(String date, String fname, String suffixname) {
        String newfilepath = Mvcs.getServletContext().getRealPath(
                "/upload/" + FileType.getFileType(suffixname) + "/" + date
                        + "/");
        String file=newfilepath+"/"+fname;
        String savepath = Strings.sNull(Globals.SYS_CONFIG.get("file_savepath"));
        if (!Strings.isBlank(savepath)) {
            newfilepath = savepath + "/upload/" + FileType.getFileType(suffixname) + "/" + date
                    + "/";
            file=newfilepath+fname;
        }
        Files.createDirIfNoExists(newfilepath);
        return file;
    }

    /**
     * 根据异常提示错误信息
     *
     * @param t
     * @return
     */
    private String errorMsg(Throwable t) {
        if (t == null || t.getClass() == null) {
            return "错误：未知system错误！";
        } else {
            String className = t.getClass().getSimpleName();
            if (className.equals("UploadUnsupportedFileNameException")) {
                String name = upload.getContext().getNameFilter();
                return "错误：无效的文件扩展名，支持的扩展名：" + name.substring(name.indexOf("(") + 1, name.lastIndexOf(")")).replace("|", ",");
            } else if (className.equals("UploadUnsupportedFileTypeException")) {
                return "错误：不支持的文件类型！";
            } else if (className.equals("UploadOutOfSizeException")) {
                return "错误：文件超出" + getFileSize(upload.getContext().getMaxFileSize(), 2) + "MB";
            } else if (className.equals("UploadStopException")) {
                return "错误：上传中断！";
            } else {
                return "错误：未知错误！";
            }
        }
    }

    /**
     * 返回文件大小，单位MB
     *
     * @param filesize
     * @param scale
     * @return
     */
    private double getFileSize(long filesize, int scale) {
        BigDecimal bd1 = new BigDecimal(Long.toString(filesize));
        BigDecimal bd2 = new BigDecimal(Long.toString(1024));
        return bd1.divide
                (bd2, scale, BigDecimal.ROUND_HALF_UP).divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
