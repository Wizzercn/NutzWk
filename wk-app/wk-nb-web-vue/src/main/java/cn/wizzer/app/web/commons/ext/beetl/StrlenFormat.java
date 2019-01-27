package cn.wizzer.app.web.commons.ext.beetl;

import org.beetl.core.Format;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2018/2/6.
 */
public class StrlenFormat implements Format {

    public Object format(Object data, String pattern) {
        if (data == null) {
            return "";
        }
        String s = Strings.sNull(data);
        if (pattern != null && s.length() > Integer.valueOf(pattern)) {
            return s.substring(0, Integer.valueOf(pattern));
        }
        return s;
    }

}