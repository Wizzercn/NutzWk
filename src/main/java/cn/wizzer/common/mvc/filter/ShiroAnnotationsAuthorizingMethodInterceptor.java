package cn.wizzer.common.mvc.filter;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;

class ShiroAnnotationsAuthorizingMethodInterceptor extends AnnotationsAuthorizingMethodInterceptor {

	public static final ShiroAnnotationsAuthorizingMethodInterceptor DEFAULT_AUTH = new ShiroAnnotationsAuthorizingMethodInterceptor();

	public void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException {
		super.assertAuthorized(methodInvocation);
	}
}