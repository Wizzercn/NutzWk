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
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.*;
import org.nutz.weixin.spi.WxApi2;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
            String return_code = resp.getString("return_code", "");//SUCCESS
            String code_url = resp.getString("code_url", "");
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success("system.success", code_url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    public Object redpack(@Param("redpack") int redpack, @Param("openid") String openid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(Cnd.NEW().limit(1, 1));
            NutMap payinfo = Json.fromJson(NutMap.class, config.getPayInfo());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(config.getId());
            WxPayRedPack redPack = new WxPayRedPack();
            redPack.setNonce_str(R.UU32());
            redPack.setMch_billno(payinfo.getString("wxpay_mchid") + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            redPack.setMch_id(payinfo.getString("wxpay_mchid"));
            redPack.setWxappid(config.getAppid());
            redPack.setSend_name("大鲨鱼");
            redPack.setRe_openid(openid);
            redPack.setTotal_amount(redpack);
            redPack.setTotal_num(1);
            redPack.setWishing("新年快乐");
            redPack.setClient_ip(Lang.getIP(req));
            redPack.setAct_name("扫码得红包");
            redPack.setRemark("扫得越多得的越多！");
            redPack.setScene_id("PRODUCT_1");
            File file = new File(Globals.AppRoot + "/WEB-INF/cert/wx/" + config.getId() + ".p12");
            NutMap resp = wxApi2.send_redpack(payinfo.getString("wxpay_key"), redPack,
                    file,
                    payinfo.getString("wxpay_mchid"));
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success(Json.toJson(resp));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    public Object redpackGroup(@Param("redpack") int redpack, @Param("openid") String openid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(Cnd.NEW().limit(1, 1));
            NutMap payinfo = Json.fromJson(NutMap.class, config.getPayInfo());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(config.getId());
            WxPayRedPackGroup redPack = new WxPayRedPackGroup();
            redPack.setNonce_str(R.UU32());
            redPack.setMch_billno(payinfo.getString("wxpay_mchid") + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            redPack.setMch_id(payinfo.getString("wxpay_mchid"));
            redPack.setWxappid(config.getAppid());
            redPack.setSend_name("大鲨鱼的裂变");
            redPack.setRe_openid(openid);
            redPack.setTotal_amount(redpack);
            redPack.setTotal_num(3);
            redPack.setAmt_type("ALL_RAND");
            redPack.setWishing("新年快乐");
            redPack.setAct_name("扫码得红包");
            redPack.setRemark("扫得越多得的越多！");
            redPack.setScene_id("PRODUCT_1");
            File file = new File(Globals.AppRoot + "/WEB-INF/cert/wx/" + config.getId() + ".p12");
            NutMap resp = wxApi2.send_redpackgroup(payinfo.getString("wxpay_key"), redPack,
                    file,
                    payinfo.getString("wxpay_mchid"));
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success(Json.toJson(resp));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    public Object transfers(@Param("redpack") int redpack, @Param("openid") String openid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(Cnd.NEW().limit(1, 1));
            NutMap payinfo = Json.fromJson(NutMap.class, config.getPayInfo());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(config.getId());
            WxPayTransfers wxPayTransfers = new WxPayTransfers();
            wxPayTransfers.setNonce_str(R.UU32());
            wxPayTransfers.setAmount(redpack);
            wxPayTransfers.setCheck_name("NO_CHECK");
            wxPayTransfers.setDesc("付款测试");
            wxPayTransfers.setMchid(payinfo.getString("wxpay_mchid"));
            wxPayTransfers.setMch_appid(config.getAppid());
            wxPayTransfers.setPartner_trade_no(payinfo.getString("wxpay_mchid") + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            wxPayTransfers.setOpenid(openid);
            wxPayTransfers.setSpbill_create_ip(Lang.getIP(req));
            wxPayTransfers.setRe_user_name("大鲨鱼");
            File file = new File(Globals.AppRoot + "/WEB-INF/cert/wx/" + config.getId() + ".p12");
            NutMap resp = wxApi2.pay_transfers(payinfo.getString("wxpay_key"), wxPayTransfers,
                    file,
                    payinfo.getString("wxpay_mchid"));
            log.debug("resp:::" + Json.toJson(resp));
            return Result.success(Json.toJson(resp));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }
}
