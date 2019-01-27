package cn.wizzer.app.web.commons.ext.beetl;

import org.beetl.core.Format;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2018/1/23.
 */
public class FileSizeFormat implements Format {
    public Object format(Object data, String pattern) {
        return Strings.formatSizeForReadBy1024(Long.valueOf(Strings.sNull(data)));
    }
}
