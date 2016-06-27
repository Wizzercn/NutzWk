package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.*;
import org.nutz.dao.Chain;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
public class UserController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    MenuService menuService;
    @Inject
    UnitService unitService;

    @At("")
    @Ok("beetl:/private/sys/user/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("unitid") String unitid, @Param("loginname") String loginname, @Param("nickname") String nickname, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid)&&!"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        if (!Strings.isBlank(loginname))
            cnd.and("loginname", "like", "%" + loginname + "%");
        if (!Strings.isBlank(nickname))
            cnd.and("nickname", "like", "%" + nickname + "%");
        return userService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "root");
        obj.put("text", "所有用户");
        obj.put("children", false);
        tree.add(obj);
        for (Sys_unit unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At
    @Ok("beetl:/private/sys/user/pass.html")
    @RequiresAuthentication
    public void pass() {

    }

    @At
    @Ok("beetl:/private/sys/user/custom.html")
    @RequiresAuthentication
    public void custom() {

    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            userService.update(Chain.make("customMenu", ids), Cnd.where("id", "=", req.getAttribute("uid")));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (!Strings.isBlank(ids)) {
                user.setCustomMenu(ids);
                user.setCustomMenus(menuService.query(Cnd.where("id", "in", ids.split(","))));
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        String old = new Sha256Hash(oldPassword, user.getSalt(), 1024).toBase64();
        if (old.equals(user.getPassword())) {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(newPassword, salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            userService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", user.getId()));
            return Result.success("修改成功", req);
        } else {
            return Result.error("原密码不正确", req);
        }
    }
}
