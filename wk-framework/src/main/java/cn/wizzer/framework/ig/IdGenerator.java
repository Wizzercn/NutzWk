package cn.wizzer.framework.ig;

import org.nutz.el.opt.RunMethod;

public interface IdGenerator extends RunMethod {

    String next(String key) throws Exception;
}
