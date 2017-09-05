package cn.wizzer.framework.shiro.interceptor;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.lang.Lang;

import java.lang.reflect.Method;

public class ShiroMethodInterceptor implements MethodInterceptor {

	public void filter(final InterceptorChain chain) throws Throwable {

		try {
			ShiroAnnotationsAuthorizingMethodInterceptor.DEFAULT_AUTH.assertAuthorized(new MethodInvocation() {

				//这个方法不会被执行的.
				public Object proceed() throws Throwable {
					throw Lang.noImplement();
				}

				public Object getThis() {
					return chain.getCallingObj();
				}

				public Method getMethod() {
					return chain.getCallingMethod();
				}

				public Object[] getArguments() {
					return chain.getArgs();
				}
			});
		} catch (AuthorizationException e) {
			// TODO 该如何处理呢? 交给用户自定义?
			throw Lang.wrapThrow(e);
		}
		chain.doChain(); //继续下一个拦截器
	}

}
