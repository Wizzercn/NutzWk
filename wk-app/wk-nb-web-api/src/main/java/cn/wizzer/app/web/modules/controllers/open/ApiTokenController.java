package cn.wizzer.app.web.modules.controllers.open;

import cn.wizzer.app.web.commons.utils.TokenUtil;
import cn.wizzer.framework.base.Result;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean
@At("/open/api/token")
public class ApiTokenController {
    private final static Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;
    @Inject
    private TokenUtil tokenUtil;

    @POST
    @At("/get")
    @Ok("json")
    public Object get(@Param("appid") String appid, @Param("sign") String sign, @Param("timestamp") String timestamp, @Param("nonce") String nonce) {
        try {
            String appid_sys = conf.get("apitoken.appid", "");
            String appkey_sys = conf.get("apitoken.appkey", "");
            if (!appid_sys.equals(appid))
                return Result.error("appid不正确");
            if (Times.getTS() - Long.valueOf(timestamp) > 60 * 1000)//时间戳相差大于1分钟则为无效的
                return Result.error("时间戳不正确");
            if (!Lang.sha256(appid + appkey_sys + nonce + timestamp).equalsIgnoreCase(sign))
                return Result.error("sign签名不正确");
            NutMap resmap = new NutMap();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, +2);//设置token有效期为2小时
            date = c.getTime();
            resmap.addv("expires", 7200);//同时把有效期秒数传递给客户端
            resmap.addv("token", tokenUtil.generateToken(date, appid));
            return Result.success("获取token成功", resmap);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return Result.error("获取token失败");
        }
    }
}
