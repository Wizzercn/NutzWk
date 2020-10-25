package cn.wizzer.test;

import cn.wizzer.app.sys.commons.core.DubboRpcSysMainLauncher;
import cn.wizzer.app.sys.modules.models.Sys_msg;
import cn.wizzer.app.sys.modules.services.SysMsgService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.boot.NbApp;
import org.nutz.boot.test.junit4.NbJUnit4Runner;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.random.R;

/**
* 测试系统消息相关
* @author: 蛋蛋的忧伤
* @date: 2020/10/24 15:01
**/
@IocBean
@RunWith(NbJUnit4Runner.class)
public class SysMsgServiceTest extends Assert {
    @Inject
    private SysMsgService  sysMsgService;

    @Test
    public void notifyTest() {
        Sys_msg msg = new Sys_msg();
        msg.setType("user");
        msg.setTitle("测试消息"+ R.get());
        msg.setNote("hello!");
        msg.setUrl("");
        msg.setSendAt(System.currentTimeMillis());
        // 该方法会调用notify
        sysMsgService.saveMsg(msg,new String[]{"superadmin"});
    }

    @Test
    public void innerMsgTest() {
        // 该方法会调用innerMsg
        sysMsgService.getMsg("superadmin");
    }

    // 测试类可提供public的static的createNbApp方法,用于定制当前测试类所需要的NbApp对象.
    // 测试类带@IocBean或不带@IocBean,本规则一样生效
    // 若不提供,默认使用当前测试类作为MainLauncher.
    // 也可以自定义NbJUnit4Runner, 继承NbJUnit4Runner并覆盖其createNbApp方法
    public static NbApp createNbApp() {
        NbApp nb = new NbApp().setMainClass(DubboRpcSysMainLauncher.class).setPrintProcDoc(false);
        nb.getAppContext().setMainPackage("cn.wizzer");
        return nb;
    }
}
