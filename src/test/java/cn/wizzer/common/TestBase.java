package cn.wizzer.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocLoader;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.mvc.annotation.IocBy;

import net.sf.ehcache.CacheManager;

public class TestBase extends Assert {

	protected Ioc ioc;
	
	@Before
	public void before() throws Exception {
		CacheManager.getInstance();
		IocBy iocBy = cn.wizzer.common.core.Module.class.getAnnotation(IocBy.class);
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
	}

	protected void _before() throws Exception {}
	protected void _after() throws Exception {}
}
