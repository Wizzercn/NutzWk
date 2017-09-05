package cn.wizzer.framework.view.beetl;

/**
 * Created by wizzer on 2017/2/8.
 */

import org.beetl.core.Format;
import org.nutz.lang.Strings;

public class HtmlEscapeFormat implements Format {

    public Object format(Object data, String pattern) {
        return Strings.escapeHtml(String.valueOf(data == null ? "" : data));
    }

}
