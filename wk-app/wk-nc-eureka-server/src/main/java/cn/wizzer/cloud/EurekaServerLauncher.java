package cn.wizzer.cloud;

import org.nutz.boot.NbApp;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2018/3/16.
 */
@IocBean
public class EurekaServerLauncher {
    // 端口是8080
    // 首页 http://127.0.0.1:5000/eureka/status
    public static void main(String[] args) throws Exception {
        new NbApp().setPrintProcDoc(true).run();
    }
}
