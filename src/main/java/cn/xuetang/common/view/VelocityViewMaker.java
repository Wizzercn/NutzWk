package cn.xuetang.common.view;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

/**
 * Created by Wizzer on 14-9-23.
 */
public class VelocityViewMaker implements ViewMaker {
    @Override
    public View make(Ioc ioc, String type, String value) {
        if ("vm".equalsIgnoreCase(type)) {
            return new VelocityLayoutView(value);
        }
        return null;
    }
}
