package cn.wizzer.app.web.commons.ext.beetl;

import org.beetl.core.Format;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2018/2/6.
 */
public class Html2TxtFormat implements Format {

    public Object format(Object data, String pattern) {
        if (data == null) {
            return "";
        }
        Document document= Jsoup.parse(Strings.sNull(data));
        String s = document.text();
        if (pattern != null && s.length() > Integer.valueOf(pattern)) {
            return s.substring(0, Integer.valueOf(pattern));
        }
        return s;
    }

}