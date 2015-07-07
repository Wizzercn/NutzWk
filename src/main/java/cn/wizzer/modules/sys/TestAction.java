package cn.wizzer.modules.sys;

import cn.wizzer.common.service.QrcodeService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by Wizzer.cn on 2015/6/30.
 */
@IocBean
@At("/test")
public class TestAction{
    @Inject
    QrcodeService qrcodeService;
    //二维码生成
    @At("/qrcode")
    @GET
    @Ok("raw:png") // TODO 改进为内嵌logo如何
    public Object getQrcode(@Param("data")String data, @Param("w")int w, @Param("h")int h){
        return qrcodeService.get(data, w, h);
    }
    //设置字符串语言
    @At
    public void setMsg(@Param("id")String id){
        Mvcs.setLocalizationKey(id);
    }
}
