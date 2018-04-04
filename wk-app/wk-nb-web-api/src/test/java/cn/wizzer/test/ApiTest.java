package cn.wizzer.test;

import cn.wizzer.app.web.commons.core.WebApiMainLauncher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.boot.NbApp;
import org.nutz.boot.test.junit4.NbJUnit4Runner;
import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean(create = "init")
@RunWith(NbJUnit4Runner.class)
public class ApiTest extends Assert {
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

    //先执行这一步获取token
    @Test
    public void test_get_token() {
        Request req = Request.create("http://127.0.0.1:9001/open/api/token/get", Request.METHOD.POST);
        Map<String, Object> params = new HashMap<>();
        String timestamp = "" + Times.getTS();
        String nonce = R.UU32();
        params.put("appid", appid);
        params.put("timestamp", timestamp);
        params.put("nonce", nonce);
        params.put("sign", Lang.sha256(appid + appkey + nonce + timestamp));
        req.setParams(params);
        System.out.println("params::::" + Json.toJson(params));
        Response resp = Sender.create(req).send();
        if (resp.isOK()) {
            NutMap map = Json.fromJson(NutMap.class, resp.getContent());
            System.out.println("result::::" + Json.toJson(map));
            if (map.getInt("code", -1) == 0) {
                //获取token成功，保存到redis测试用
                NutMap data = map.getAs("data", NutMap.class);
                System.out.println("data.token:::" + data.get("token"));
                System.out.println("data.expires:::" + data.get("expires"));
                redisService.set("api_token_test:" + appid, data.getString("token"));
                redisService.expire("api_token_test:" + appid, data.getInt("expires"));
            }
        }
    }

    //然后再执行这里,使用token
    @Test
    public void test_water_test1() {
        Request req = Request.create("http://127.0.0.1:9001/open/api/test/test1", Request.METHOD.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("appid", appid);
        params.put("token", redisService.get("api_token_test:" + appid));
        params.put("openid", "openid123456789");
        req.setParams(params);
        Response resp = Sender.create(req).send();
        if (resp.isOK()) {
            System.out.println("result::::" + resp.getContent());
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
