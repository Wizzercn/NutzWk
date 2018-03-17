package cn.wizzer.app.sys.modules.controllers;

import cn.wizzer.app.sys.modules.models.Sys_area;
import cn.wizzer.app.sys.modules.services.SysAreaService;
import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.utils.PinyinUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import cn.wizzer.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IocBean
@At("/platform/sys/area")
@Ok("json:full")
@AdaptBy(type = JsonAdaptor.class)
public class SysAreaController {
    private static final Log log = Logs.get();
    @Inject
    private SysAreaService sysAreaService;

    @At
    public NutMap data(@Param("length") int length, @Param("start") int start,
                       @Param("draw") int draw, @Param("cnd") Cnd cnd, @Param("linkName") String linkName) {
        return sysAreaService.data(length, start, draw, cnd, linkName);
    }
}
