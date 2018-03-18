package cn.wizzer.app.web.commons.filter;

import org.nutz.boot.AppContext;
import org.nutz.boot.starter.WebFilterFace;
import org.nutz.boot.starter.WebServletFace;
import org.nutz.boot.starter.nutz.mvc.NbMainModule;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@IocBean
public class RouteFilterStarter implements WebFilterFace {
	
	
	@Inject("refer:$ioc")
	protected Ioc ioc;
	
	@Inject
	protected PropertiesProxy conf;
	
	@Inject
	protected AppContext appContext;

    public String getName() {
        return "routeFilterStarter";
    }

    public String getPathSpec() {
        return "/*";
    }

    public EnumSet<DispatcherType> getDispatches() {
        return EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
    }

    @IocBean(name="routeFilter")
    public RouteFilter createRouteFilter() {
    	return new RouteFilter();
    }
    
    public Filter getFilter() {
        return ioc.get(RouteFilter.class, "routeFilter");
    }

    public Map<String, String> getInitParameters() {
        return new HashMap<>();
    }

    public int getOrder() {
        return 11;
    }
}
