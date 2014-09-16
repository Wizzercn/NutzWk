package cn.xuetang.common.util;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Globals;
import cn.xuetang.modules.app.bean.App_info;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.Param;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信接口工具类
 * Created by Wizzer on 14-4-4.
 */
@IocBean
public class WeixinUtil extends BaseAction {
    private final static Log log = Logs.get();
    @Inject
    protected UrlUtil urlUtil;

    /**
     * 向微信用户推送一条文本信息，48小时内互动
     *
     * @param dao     数据库
     * @param appid   微信接口
     * @param toUser  接收人
     * @param content 文本内容
     * @return
     */
    public String PushTxt(Dao dao, int appid, String toUser, String content) {
        try {
            Map<String, Object> obj = new HashMap<String, Object>();
            Map<String, Object> contentMap = new HashMap<String, Object>();
            contentMap.put("content", content);
            obj.put("touser", toUser);
            obj.put("msgtype", "text");
            obj.put("text", contentMap);
            String access_token = getGloalsAccessToken(dao, appid);
            Request req = Request.create("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token, Request.METHOD.POST);
            req.setData(Json.toJson(obj));
//            System.out.println(Json.toJson(obj));
            //req.getParams().put("access_token", access_token);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return Strings.sNull(resp.getContent());
            }
        } catch (Exception e) {
            log.error(e);
            return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
        }
        return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
    }

    /**
     * 上传图文消息素材，高级群发接口
     *
     * @param dao   数据库
     * @param appid 微信接口
     * @param list  文章列表
     * @return
     */
    public String PushNews(Dao dao, int appid, List<Object> list) {
        try {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("articles", list);
            String access_token = getGloalsAccessToken(dao, appid);
            Request req = Request.create("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + access_token, Request.METHOD.POST);
            req.setData(Json.toJson(obj));
            System.out.println(Json.toJson(obj));
            //req.getParams().put("access_token", access_token);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return Strings.sNull(resp.getContent());
            }
        } catch (Exception e) {
            log.error(e);
            return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
        }
        return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
    }

    /**
     * 向用户推送文章内容，高级群发接口
     *
     * @param dao   数据库
     * @param appid 微信接口
     * @param obj   推送内容
     * @return
     */
    public String PushUser(Dao dao, int appid, Map<String, Object> obj) {
        try {

            String access_token = getGloalsAccessToken(dao, appid);
            Request req = Request.create("https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + access_token, Request.METHOD.POST);
            req.setData(Json.toJson(obj));
            log.debug(Json.toJson(obj));
            //req.getParams().put("access_token", access_token);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return Strings.sNull(resp.getContent());
            }
        } catch (Exception e) {
            log.error(e);
            return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
        }
        return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
    }

    /**
     * 将视频media_id、title、description上传获取新media_id
     *
     * @param dao         数据库
     * @param appid       微信接口
     * @param media_id
     * @param title
     * @param description
     * @return
     */
    public String PushVideo(Dao dao, int appid, String media_id, String title, String description) {
        try {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("media_id", media_id);
            obj.put("title", title);
            obj.put("description", description);
            String access_token = getGloalsAccessToken(dao, appid);
            Request req = Request.create("https://api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=" + access_token, Request.METHOD.POST);
            req.setData(Json.toJson(obj));
            System.out.println(Json.toJson(obj));
            //req.getParams().put("access_token", access_token);
            Response resp = Sender.create(req).send();
            if (resp.isOK()) {
                return Strings.sNull(resp.getContent());
            }
        } catch (Exception e) {
            log.error(e);
            return "{\"errcode\":1001,\"errmsg\":\"system error\"}";
        }
        return "{\"errcode\":1002,\"errmsg\":\"system unkown\"}";
    }

    /**
     * 获取全局access_token，超过7200ms重新获取，否则返回上次值
     *
     * @param dao
     * @param appid
     * @return
     */
    public String getGloalsAccessToken(Dao dao, int appid) {
        String access_token = "";
        boolean resetAccesstoken = false;
        long now = System.currentTimeMillis();
        App_info appInfo = daoCtl.detailById(dao, App_info.class, appid);
        if (!Strings.isBlank(appInfo.getAccess_token())) {
            long ftime = NumberUtils.toLong(Strings.sNull(appInfo.getAccess_time()));
            if ((now - ftime) > 7150 * 1000) {
                resetAccesstoken = true;
            } else {
                access_token = Strings.sNull(appInfo.getAccess_token());
            }
        } else resetAccesstoken = true;
        if (resetAccesstoken) {
            access_token = getAccess_token(appInfo);
            daoCtl.update(dao, App_info.class, Chain.make("access_time", String.valueOf(now)).add("access_token", access_token), Cnd.where("id", "=", appid));
        }
        return access_token;
    }

    /**
     * 获取access_token
     *
     * @param info
     * @return
     */
    public String getAccess_token(App_info info) {
        try {
            String res = urlUtil.getOneHtml("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + info.getApp_key() + "&secret=" + info.getApp_secret(), "UTF-8");
            Map<String, Object> map = Json.fromJson(Map.class, res);
            return Strings.sNull(map.get("access_token"));
        } catch (Exception e) {
            log.error(e);
        }
        return "";
    }

    /**
     * 获取微信图片文件
     *
     * @param access_token
     * @param mediaid
     * @return
     */
    public BufferedImage getImage(String access_token, String mediaid) {
        try {
            URL url = new URL("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaid);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            BufferedImage image = ImageIO.read(inStream);
            inStream.close();
            return image;

        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    /**
     * 获取微信视频文件
     *
     * @param access_token
     * @param mediaid
     * @return
     */
    public ByteArrayOutputStream getVideo(String access_token, String mediaid) {
        try {
            URL url = new URL("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + access_token + "&media_id=" + mediaid);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            inStream.close();
            return outstream;
        } catch (Exception e) {
            log.debug(e);

            return null;
        }
    }
}
