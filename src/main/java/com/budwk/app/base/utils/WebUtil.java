package com.budwk.app.base.utils;

import org.nutz.json.JsonFormat;
import org.nutz.lang.Encoding;
import org.nutz.mvc.view.UTF8JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wizzer@qq.com
 */
public class WebUtil {

    public static String AjaxEncode = Encoding.UTF8;

    public static boolean isAjax(HttpServletRequest req) {
        String value = req.getHeader("X-Requested-With");
        return value != null && "XMLHttpRequest".equalsIgnoreCase(value.trim());
    }

    public static void rendAjaxResp(HttpServletRequest req, HttpServletResponse resp, Object re) {
        try {
            if (AjaxEncode != null) {
                resp.setCharacterEncoding(AjaxEncode);
            }
            (new UTF8JsonView(JsonFormat.compact())).render(req, resp, re);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
