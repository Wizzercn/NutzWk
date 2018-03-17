package cn.wizzer.app.web.commons.ig;

import org.nutz.el.opt.RunMethod;

/**
 * Created by wizzer on 2018/3/17.
 */
public interface IdGenerator extends RunMethod {
    String next(String tableName, String prefix) throws Exception;
}