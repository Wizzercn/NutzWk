package cn.wizzer.modules.sys.bean;

import cn.wizzer.common.service.core.BasePojo;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
@Table("sys_user")
@TableIndexes({@Index(name = "INDEX_USERNAME", fields = {"username"}, unique = true)})
public class Sys_user extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 64)
    @Prev(els = {@EL("uuid()")})
    private String id;
    @Column
    @Comment("登录名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String username;
    @Column("passwd")
    @Comment("密码")
    @ColDefine(type = ColType.VARCHAR, width = 128)
    private String password;// transient 修饰符可让此字段不在对象里显示
    @Column
    @Comment("加盐")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String salt;
    @Column("is_online")
    @Comment("是否在线")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean online;
    @Column("is_locked")
    @Comment("是否锁定")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean locked;
    @Comment("系统管理员")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean system;
    @Column("register_ip")
    @ColDefine(type = ColType.VARCHAR, width = 15)
    private String registerIp;
    @Prev(els = @EL("$me.now()"))
    @Column("login_time")
    @Comment("登陆时间")
    protected Date loginTime;
    @Column("login_ip")
    @Comment("登陆IP")
    @ColDefine(type = ColType.VARCHAR, width = 15)
    private String loginIp;
    @Column("login_count")
    @Comment("登陆次数")
    @ColDefine(type = ColType.INT)
    private int loginCount;
    @Column("login_menu")
    @Comment("登陆菜单")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginMenu;
    @Column("login_theme")
    @Comment("登陆皮肤")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginTheme;
    @Column("login_sidebar")
    private boolean loginSidebar;
    @Column("login_boxed")
    private boolean loginBoxed;
    @Column("login_scroll")
    private boolean loginScroll;
    @ManyMany(from = "user_id", relation = "sys_user_role", target = Sys_role.class, to = "role_id")
    protected List<Sys_role> roles;
    @ManyMany(from = "user_id", relation = "sys_user_unit", target = Sys_role.class, to = "unit_id")
    protected List<Sys_unit> units;
    @One(target = Sys_user_profile.class, field = "id", key = "userId")
    protected Sys_user_profile profile;
    protected List<Sys_menu> menus;
    protected List<Sys_menu> firstMenus;
    protected Map<String, List<Sys_menu>> secondMenus;
    protected Map<String, String> idMenus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public List<Sys_role> getRoles() {
        return roles;
    }

    public void setRoles(List<Sys_role> roles) {
        this.roles = roles;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public String getLoginMenu() {
        return loginMenu;
    }

    public void setLoginMenu(String loginMenu) {
        this.loginMenu = loginMenu;
    }

    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
    }

    public Sys_user_profile getProfile() {
        return profile;
    }

    public void setProfile(Sys_user_profile profile) {
        this.profile = profile;
    }

    public List<Sys_menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Sys_menu> menus) {
        this.menus = menus;
    }

    public List<Sys_menu> getFirstMenus() {
        return firstMenus;
    }

    public void setFirstMenus(List<Sys_menu> firstMenus) {
        this.firstMenus = firstMenus;
    }

    public Map<String, List<Sys_menu>> getSecondMenus() {
        return secondMenus;
    }

    public void setSecondMenus(Map<String, List<Sys_menu>> secondMenus) {
        this.secondMenus = secondMenus;
    }

    public boolean isLoginSidebar() {
        return loginSidebar;
    }

    public void setLoginSidebar(boolean loginSidebar) {
        this.loginSidebar = loginSidebar;
    }

    public boolean isLoginBoxed() {
        return loginBoxed;
    }

    public void setLoginBoxed(boolean loginBoxed) {
        this.loginBoxed = loginBoxed;
    }

    public boolean isLoginScroll() {
        return loginScroll;
    }

    public void setLoginScroll(boolean loginScroll) {
        this.loginScroll = loginScroll;
    }

    public Map<String, String> getIdMenus() {
        return idMenus;
    }

    public void setIdMenus(Map<String, String> idMenus) {
        this.idMenus = idMenus;
    }

    public List<Sys_unit> getUnits() {
        return units;
    }

    public void setUnits(List<Sys_unit> units) {
        this.units = units;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
