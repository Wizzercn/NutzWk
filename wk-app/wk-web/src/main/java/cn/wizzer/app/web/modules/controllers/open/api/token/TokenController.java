package cn.wizzer.app.web.modules.controllers.open.api.token;

import cn.wizzer.app.sys.modules.models.Sys_api;
import cn.wizzer.app.sys.modules.services.SysApiService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.util.DateUtil;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

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
    private SysApiService apiService;

    /**
     * @api {post} /open/api/token/get 获取Token
     * @apiGroup Token
     * @apiVersion 1.0.0
     * @apiPermission anyone
     * @apiParam {String}	appId 					appId
     * @apiParam {String}	sign 				appId+appSecret+yyyyMMddHH 计算出的MD5值
     * @apiParamExample {json} 示例
     * POST /open/api/token
     * {
     * "appId": "appId",
     * "sign": "sign"
     * }
     * @apiSuccess {number} code 			         code
     * @apiSuccess {String} msg 			         msg
     * @apiSuccess {Object[]} data 				数据对象
     * @apiSuccess {number} data.expires 			有效期
     * @apiSuccess {String} data.token 			Token
     * @apiSuccessExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 0,
     * "msg": "ok",
     * "data": {
     * "token": ""eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0IiwiZXhwIjoxNDcwOTA5OTc4fQ._T7egDYhCL27jCvEv4J0cyjRj8s_YLj2gZjjTA8mzk81mTdeM-JXnH7VmtfaenW33BpJJzs2Hs2sXiiNHdzU6Q",
     * "expires": 7200,
     * }
     * }
     * @apiError (失败) {number} code 不等于0
     * @apiError (失败) {string} msg 错误文字描述
     * @apiErrorExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     * "code": 1
     * "msg": "token invalid"
     * }
     */
    @At("/get")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object get(@Param("..") NutMap map) {
        try {
            Sys_api api = apiService.fetch(Cnd.where("appId", "=", map.getString("appid", "")));
            if (api == null)
                return Result.error("appId error");
            if (!Lang.md5(map.getString("appid", "") + api.getAppSecret() + DateUtil.format(new Date(), "yyyyMMddHH")).equalsIgnoreCase(map.getString("sign", "")))
                return Result.error("sign error");
            NutMap resmap = new NutMap();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, +2);
            date = c.getTime();
            resmap.addv("expires", 7200);
            resmap.addv("token", apiService.generateToken(date, map.getString("appid", "")));
            return Result.success("ok", resmap);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return Result.error("fail");
        }
    }
}
