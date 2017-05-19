package cn.wizzer.app;

import cn.wizzer.app.web.commons.core.Module;
import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocLoader;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.mvc.annotation.IocBy;

/**
 * Created by wizzer on 2017/5/19.
 */
public class TestBase extends Assert {

    protected Ioc ioc;

    @Before
    public void before() throws Exception {
        CacheManager.getInstance();
        IocBy iocBy = Module.class.getAnnotation(IocBy.class);
        IocLoader loader = new ComboIocLoader(iocBy.args());
        ioc = new NutIoc(loader);
        _before();
    }

    @After
    public void after() throws Exception {
        _after();
        if (ioc != null) {
            ioc.depose();
        }
        // 主动销毁dubbo的东西
        //ProtocolConfig.destroyAll();
    }

    protected void _before() throws Exception {}
    protected void _after() throws Exception {}
}
