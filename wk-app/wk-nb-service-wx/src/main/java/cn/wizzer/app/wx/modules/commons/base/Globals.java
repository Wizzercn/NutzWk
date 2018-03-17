package cn.wizzer.app.wx.modules.commons.base;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.impl.WxApi2Impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzer on 2018/3/17.
 */
@IocBean
public class Globals {
    //微信map
    public static Map<String, WxApi2Impl> WxMap=new HashMap<>();
}
