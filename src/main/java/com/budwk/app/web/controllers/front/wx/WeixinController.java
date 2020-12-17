package com.budwk.app.web.controllers.front.wx;

import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.web.commons.ext.wx.WxHandler;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.weixin.util.Wxs;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/public/weixin")
public class WeixinController {
    private static final Log log = Logs.get();
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxHandler wxHandler;

    public WeixinController() {
        Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
    }

    @At({"/api", "/api/?"})
    @Fail("http:200")
    public View msgIn(String key, HttpServletRequest req) throws IOException {
        return Wxs.handle(wxHandler, req, key);
    }

}
