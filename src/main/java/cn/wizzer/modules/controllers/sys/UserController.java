package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.*;
import org.nutz.dao.Chain;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @At
    @Ok("beetl:/private/sys/user/pass.html")
    public void pass() {

    }

    @At
    @Ok("beetl:/private/sys/user/custom.html")
    @RequiresUser
    public void custom() {

    }

    @At
    @Ok("json")
    @RequiresUser
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        log.debug("ids:::" + ids);
        try{
            userService.update(Chain.make("customMenu",ids),Cnd.where("id","=",req.getAttribute("uid")));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if(!Strings.isBlank(ids)){
                user.setCustomMenu(ids);
                user.setCustomMenus(menuService.query(Cnd.where("id","in",ids.split(","))));
            }
            return Result.success("保存成功", req);
        }catch (Exception e){
            return Result.error("保存失败", req);
        }
    }

    @At
    @Ok("json")
    @RequiresUser
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
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
        } else {
            return Result.error("登录失效", req);
        }
    }
}
