package cn.wizzer.common.mvc.view;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class VelocityViewMaker implements ViewMaker {
    public View make(Ioc ioc, String type, String value) {
        if ("vm".equalsIgnoreCase(type)) {
            return new VelocityLayoutView(value);
        }
        return null;
    }
}
