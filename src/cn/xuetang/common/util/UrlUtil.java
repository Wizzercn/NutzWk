package cn.xuetang.common.util;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.http.Header;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.repo.Base64;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wizzer on 14-3-8.
 */
@IocBean
public class UrlUtil {
    private final static Log log = Logs.get();

    /**
     * 读取一个网页全部内容
     */
    public String getOneHtml(final String htmlurl,String encoding) throws IOException {
        URL url;
        String temp;
        final StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlurl);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (final MalformedURLException me) {
            log.error(me);
            throw me;
        } catch (final IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }

    /**
     * 获取post参数值
     *
     * @param in
     * @return
     */
    public String readStreamParameter(ServletInputStream in) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(new String(Strings.sNull(line).getBytes(),Encoding.UTF8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
    /**
     * 获取post参数值
     *
     * @param in
     * @return
     */
    public String readStreamParameterBase64(ServletInputStream in){
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(new String(Strings.sNull(line).getBytes(), Encoding.UTF8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(Base64.decode(buffer.toString().getBytes()));
    }

    public static void main(String[] args) {
        try {
            JsonFormat jsonFormat=new JsonFormat();
            jsonFormat.setAutoUnicode(true);
            jsonFormat.setCompact(true);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("openid", "test");
            map.put("msgid", NumberUtils.toLong(Strings.sNull(1)));
            map.put("msgtype", "text");
            map.put("createtime", NumberUtils.toLong(Strings.sNull(1212212)));
            map.put("content", "猪");
            String str= Json.toJson(SortHashtable.sortByValue(map),jsonFormat);
            String mysignature= Lang.digest("MD5", str.getBytes(), "122qqwqweqwe".getBytes(), 1);
            map.put("signature",mysignature);
            Request reqq = Request.create("http://127.0.0.1/api/wx/user/sendtext?mykey=8bff2c813914045e103c1f08", Request.METHOD.POST);
            reqq.setData(Base64.encodeToString(Json.toJson(map, jsonFormat).getBytes(), false));
            reqq.getHeader().set("Content-Type","application/octet-stream");
            Response resp = Sender.create(reqq).send();
            if (resp.isOK()) {
                Map<String, Object> re = Json.fromJson(Map.class, resp.getContent());
                log.info("re:"+new String(Strings.sNull(re.get("errmsg")).getBytes(),"utf-8"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
