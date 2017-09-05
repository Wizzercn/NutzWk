package cn.wizzer.framework.shiro.interceptor;

import org.apache.shiro.aop.MethodInvocation;
import org.nutz.aop.InterceptorChain;
import org.nutz.mvc.ActionContext;

import java.lang.reflect.Method;

/**
 * 封装shiro的注解拦截器所需要的逻辑
 * @author wendal<wendal1985@gmail.com>
 *
 */
public class NutShiroInterceptor implements MethodInvocation {
    
    
    InterceptorChain chain;
    ActionContext ac;

    public NutShiroInterceptor(InterceptorChain chain) {
        this.chain = chain;
    }

    public NutShiroInterceptor(ActionContext ac) {
        this.ac = ac;
    }

    public Object proceed() throws Throwable {
        if (chain != null)
            return chain.doChain().getReturn();
        return null;
    }

    public Object getThis() {
        if (chain != null)
            return chain.getCallingObj();
        return ac.getModule();
    }

    public Method getMethod() {
        if (chain != null)
            return chain.getCallingMethod();
        return ac.getMethod();
    }

    public Object[] getArguments() {
        if (chain != null)
            return chain.getArgs();
        return ac.getMethodArgs();
    }
}
