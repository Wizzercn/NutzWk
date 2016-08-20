package cn.wizzer.modules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@RunWith(NutzWkIocTestRunner.class)
@IocBean
public class SimpleTest extends Assert {
    
    @Inject("refer:$ioc")
    protected Ioc ioc;
    
    @Test
    public void test_dao_ok() {
        Dao dao = ioc.get(Dao.class);
        assertNotNull(dao);
    }
    
}
