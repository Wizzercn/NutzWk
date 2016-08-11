package cn.wizzer.modules.open.api.token;

import cn.wizzer.common.base.Result;
import cn.wizzer.modules.back.sys.models.Sys_api;
import cn.wizzer.modules.back.sys.services.ApiService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wizzer on 2016/8/11.
 */
@IocBean
@At("/open/api/token")
public class TokenController {
    private static final Log log = Logs.get();
    @Inject
    private ApiService apiService;

    @At("/get")
    @Ok("json")
    public Object get(@Param("appId") String appId, @Param("appSecret") String appSecret) {
        try {
            Sys_api api = apiService.fetch(Cnd.where("appId", "=", appId).and("appSecret", "=", appSecret));
            if (api == null)
                return Result.error("appId or apiSecret error");
            NutMap map = new NutMap();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, +2);
            date = c.getTime();
            map.addv("expires", 7200);
            map.addv("token", apiService.generateToken(date,appId));
            return Result.success("ok", map);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return Result.error("fail");
        }
    }
}
