package cn.wizzer.modules.open.api.test.controllers;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.TokenFilter;
import cn.wizzer.modules.open.api.test.models.Api_test;
import cn.wizzer.modules.open.api.test.services.ApiTestService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2016/8/11.
 */
@IocBean
@At("/open/api/test")
@Filters({@By(type = TokenFilter.class)})
public class ApiTestController {
    private static final Log log = Logs.get();
    @Inject
    private ApiTestService apiTestService;

    /**
     * @api {post} /open/api/test/hi 测试API
     * @apiGroup Test
     * @apiVersion 1.0.0
     * @apiPermission token
     * @apiParam {String}	appId 					appId
     * @apiParam {String}	token 				    token
     * @apiParam {Object}	data 				    数据对象 
     * @apiParam {String}	data.txt			    测试文本
     * @apiParamExample {json} 示例
     * POST /open/api/test/hi
     * {
     *      "appId": "appId",
     *      "token": "token",
     *      "data": "\"txt\": \"你好，大鲨鱼\""
     * }
     * @apiSuccess {number} code 			         code
     * @apiSuccess {String} msg 			         msg
     * @apiSuccessExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     *      "code": 0,
     *      "msg": "ok"
     * }
     * @apiError (失败) {number} code 不等于0
     * @apiError (失败) {string} msg 错误文字描述
     * @apiErrorExample {json} 示例
     * HTTP/1.1 200 OK
     * {
     *      "code": 1,
     *      "msg": "fail"
     * }
     */
    @At
    @Ok("json")
    @POST
    public Object hi(@Param("..") NutMap map, HttpServletRequest req) {
        try {
            log.debug("map::" + Json.toJson(map));
            Api_test test = Json.fromJson(Api_test.class, Strings.sNull(map.get("data")));
            apiTestService.testTx(test);
            return Result.success("ok");
        } catch (Exception e) {
            return Result.error("fail");
        }
    }
}
