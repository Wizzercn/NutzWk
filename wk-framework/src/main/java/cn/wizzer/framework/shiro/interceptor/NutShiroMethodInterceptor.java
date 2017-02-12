package cn.wizzer.framework.shiro.interceptor;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.Collection;

/**
 * 将Shiro注解,映射为NutAop的拦截器
 * 
 * @author wendal<wendal1985@gmail.com>
 *
 */
@IocBean
public class NutShiroMethodInterceptor extends AnnotationsAuthorizingMethodInterceptor implements MethodInterceptor {

	public NutShiroMethodInterceptor(Collection<AuthorizingAnnotationMethodInterceptor> interceptors) {
		super.getMethodInterceptors().addAll(interceptors);
	}

	public NutShiroMethodInterceptor() {
	}

	@Override
	public void filter(InterceptorChain chain) throws Throwable {
		assertAuthorized(new NutShiroInterceptor(chain));
		chain.doChain();
	}

	// 暴露父类的方法
	@Override
	public void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException {
		super.assertAuthorized(methodInvocation);
	}
}
