package cn.wizzer.app.web.commons.shiro.remember;


import org.apache.shiro.web.mgt.CookieRememberMeManager;

/**
 * 由于shiro默认按照jdk生成base64编码cookies策略长度超过4K,造成浏览器无法识别
 * 所以已经重写生成cookies策略
 * Created by wizzer on 2017/1/18.
 */
public class LightCookieRememberMeManager extends CookieRememberMeManager {
    public LightCookieRememberMeManager() {
        super();
        setSerializer(new SimplePrincipalSerializer());
    }
}