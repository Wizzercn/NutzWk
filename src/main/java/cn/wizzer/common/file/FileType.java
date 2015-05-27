package cn.wizzer.common.file;

import org.nutz.lang.Strings;

/**
 * 类描述：
 * 创建人：Wizzer
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-12-25 下午1:13:30
 */
public class FileType {
    /**
     * 根据文件名获取文件类型，默认返回other
     *
     * @param suffixname
     * @return
     */
    public static String getFileType(String suffixname) {
        String str = Strings.sNull(suffixname).toLowerCase();
        if ("jpg,gif,png,jpeg".contains(str)) {
            return "images";
        } else if ("doc,docx,xls,xlsx,ppt,pptx,wps,pdf,txt,chm".contains(str)) {
            return "document";
        } else if ("mp3".contains(str)) {
            return "music";
        } else if ("mp4,3gp,rm,swf,flv,asf,wmv,wma,avi".contains(str)) {
            return "video";
        } else if ("7z,zip,rar,ios,jar".contains(str)) {
            return "archive";
        }
        return "other";
    }

    /**
     * 根据文件类型获取文件后缀名，默认返回nameFilter配置
     *
     * @param filetype
     * @return
     */
    public static String getSuffixname(String filetype) {
        if ("images".equals(filetype)) {
            return "*.jpg,*.gif,*.png,*.jpeg";
        } else if ("document".equals(filetype)) {
            return "*.doc,*.docx,*.xls,*.xlsx,*.ppt,*.pptx,*.wps,*.pdf,*.txt,*.chm";
        } else if ("music".equals(filetype)) {
            return "*.mp3";
        } else if ("video".equals(filetype)) {
            return "*.mp4,3gp,*.rm,*.swf,*.flv,*.asf,*.wmv,*.wma,*.avi";
        } else if ("archive".equals(filetype)) {
            return "*.7z,*.zip,*.rar,*.ios,*.jar";
        }
        return "*.txt,*.jpg";
    }
}
