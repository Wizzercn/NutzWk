package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.framework.base.Result;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/sys/msg/user")
public class SysMsgUserController{
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysMsgUserService sysMsgUserService;

    @At("")
    @Ok("beetl:/platform/sys/msg/user/index.html")
    @RequiresPermissions("platform.sys.msg.user")
    public void index() {
    }

    @At("/data")
    @Ok("json:full")
    @RequiresPermissions("platform.sys.msg.user")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return sysMsgUserService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/add")
    @Ok("beetl:/platform/sys/msg/user/add.html")
    @RequiresPermissions("platform.sys.msg.user")
    public void add() {

    }

    @At("/addDo")
    @Ok("json")
    @RequiresPermissions("platform.sys.msg.user.add")
    @SLog(tag = "Sys_msg_user", msg = "${args[0].id}")
    public Object addDo(@Param("..")Sys_msg_user sysMsgUser, HttpServletRequest req) {
		try {
			sysMsgUserService.insert(sysMsgUser);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/msg/user/edit.html")
    @RequiresPermissions("platform.sys.msg.user")
    public void edit(String id,HttpServletRequest req) {
		req.setAttribute("obj", sysMsgUserService.fetch(id));
    }

    @At("/editDo")
    @Ok("json")
    @RequiresPermissions("platform.sys.msg.user.edit")
    @SLog(tag = "Sys_msg_user", msg = "${args[0].id}")
    public Object editDo(@Param("..")Sys_msg_user sysMsgUser, HttpServletRequest req) {
		try {
            sysMsgUser.setOpBy(StringUtil.getPlatformUid());
			sysMsgUser.setOpAt(Times.getTS());
			sysMsgUserService.updateIgnoreNull(sysMsgUser);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("platform.sys.msg.user.delete")
    @SLog(tag = "Sys_msg_user", msg = "${req.getAttribute('id')}")
    public Object delete(String id, @Param("ids")  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				sysMsgUserService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				sysMsgUserService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/msg/user/detail.html")
    @RequiresPermissions("platform.sys.msg.user")
	public void detail(String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", sysMsgUserService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
    }

}
