package cn.wizzer.test;

import cn.wizzer.app.web.commons.core.WebApiMainLauncher;
import cn.wizzer.app.web.commons.utils.SignUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.boot.NbApp;
import org.nutz.boot.test.junit4.NbJUnit4Runner;
import org.nutz.http.Header;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean(create = "init")
@RunWith(NbJUnit4Runner.class)
public class ApiFileTest extends Assert {
    private static final Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;
    private String appid;
    private String appkey;
    @Inject
    private RedisService redisService;

    public void init() {
        appid = conf.get("apitoken.appid", "");
        appkey = conf.get("apitoken.appkey", "");
        System.out.println("appid:::" + appid);
        System.out.println("appkey:::" + appkey);
    }

    //测试sign拦截器
    @Test
    public void test_upload_image_by_sign() {
        Request req = Request.create("http://127.0.0.1:9001/open/api/file/upload/image_by_sign", Request.METHOD.POST);
        Map<String, Object> params = new HashMap<>();
        String timestamp = "" + Times.getTS();
        String nonce = R.UU32();
        params.put("appid", appid);
        params.put("timestamp", timestamp);
        params.put("nonce", nonce);
        params.put("sign", SignUtil.createSign(appkey, params));
        req.setParams(params);
        try {
            //签名工具类里需要排除Filedata参数
            req.getParams().put("Filedata", new File("D://1.jpg"));
        } catch (Exception e) {
            log.error("找不到文件哦...");
        }
        System.out.println("params::::" + Json.toJson(params));
        Response resp = Sender.create(req).send();
        if (resp.isOK()) {
            NutMap map = Json.fromJson(NutMap.class, resp.getContent());
            System.out.println("result::::" + Json.toJson(map));
        }
    }

    //测试header sign拦截器
    @Test
    public void test_upload_image_by_header_sign() {
        Request req = Request.create("http://127.0.0.1:9001/open/api/file/upload/image_by_header_sign", Request.METHOD.POST);
        Map<String, Object> params = new HashMap<>();
        String timestamp = "" + Times.getTS();
        String nonce = R.UU32();
        params.put("appid", appid);
        params.put("timestamp", timestamp);
        params.put("nonce", nonce);
        params.put("sign", SignUtil.createSign(appkey, params));
        req.setHeader(Header.create((Map<String, String>) (Map) params));//注意Object都是String类型,才能强转
        try {
            req.getParams().put("Filedata", new File("D://1.jpg"));
        } catch (Exception e) {
            log.error("找不到文件哦...");
        }
        System.out.println("params::::" + Json.toJson(params));
        Response resp = Sender.create(req).send();
        if (resp.isOK()) {
            NutMap map = Json.fromJson(NutMap.class, resp.getContent());
            System.out.println("result::::" + Json.toJson(map));
        }
    }

    // 测试类可提供public的static的createNbApp方法,用于定制当前测试类所需要的NbApp对象.
    // 测试类带@IocBean或不带@IocBean,本规则一样生效
    // 若不提供,默认使用当前测试类作为MainLauncher.
    // 也可以自定义NbJUnit4Runner, 继承NbJUnit4Runner并覆盖其createNbApp方法
    public static NbApp createNbApp() {
        NbApp nb = new NbApp().setMainClass(WebApiMainLauncher.class).setPrintProcDoc(false);
        nb.getAppContext().setMainPackage("cn.wizzer");
        return nb;
    }
}
