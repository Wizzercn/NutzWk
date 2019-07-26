package cn.wizzer.app.web.commons.utils;

import org.nutz.lang.Lang;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by wizzer on 2018/6/28.
 */
public class SignUtil {

    public static String createSign(String appkey, Map<String, Object> params) {
        Map<String, Object> map = MapUtil.sortMapByKey(params);
        StringBuffer sb = new StringBuffer();
        Set<String> keySet = map.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String k = it.next();
            String v = (String) map.get(k);
            if (null != v && !"".equals(v)
                    && !"sign".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("appkey=" + appkey);
        String sign = Lang.md5(sb.toString());
        return sign;
    }

}
