package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.util.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.WxPayUnifiedOrder;
import org.nutz.weixin.spi.WxApi2;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 微信扫码支付测试
 * Created by wizzer on 2017/3/23.
 */
@IocBean
@At("/platform/wx/conf/account/paytest")
public class WxPayTestController {
    private static final Log log = Logs.get();
    @Inject
    private WxConfigService wxConfigService;


    @At("")
    @Ok("beetl:/platform/wx/pay/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("json")
    public Object payWxpay(@Param("money") int money, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(Cnd.NEW().limit(1, 1));
            NutMap payinfo = Json.fromJson(NutMap.class, config.getPayInfo());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(config.getId());
            WxPayUnifiedOrder order = new WxPayUnifiedOrder();
            order.setAppid(config.getAppid());
            order.setMch_id(payinfo.getString("wxpay_mchid"));
            order.setNonce_str(R.UU32());
            order.setBody("Test");//运行环境 -Dfile.encoding=UTF-8
            order.setOut_trade_no(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            order.setTotal_fee(money);
            order.setSpbill_create_ip(Lang.getIP(req));
            order.setNotify_url("http://" + Globals.AppDomain + "/open/pay/wx/back/sendNotify");
            order.setTrade_type("NATIVE");
            NutMap resp = wxApi2.pay_unifiedorder(payinfo.getString("wxpay_key"), order);
            String return_code = resp.getString("return_code","");//SUCCESS
            String code_url = resp.getString("code_url","");
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success("system.success", code_url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }
}
