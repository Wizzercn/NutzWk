package cn.wizzer.modules.open.api.test.controllers;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.filter.TokenFilter;
import cn.wizzer.modules.open.api.test.models.Api_test;
import cn.wizzer.modules.open.api.test.services.ApiTestService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
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

    @At
    @Ok("json")
    public Object hi(@Param("..") Api_test test, HttpServletRequest req) {
        try {
            apiTestService.testTx(test);
            return Result.success("ok");
        } catch (Exception e) {
            return Result.error("fail");
        }
    }
}
