package cn.wizzer.modules.open.ueditor;

import cn.wizzer.common.filter.PrivateFilter;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/bd")
@Filters({@By(type = PrivateFilter.class)})
public class BdController {
    @At
    public Object index(){
        return "";
    }
}
