package cn.wizzer.modules.models.sys;

import cn.wizzer.common.base.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_user")
@TableIndexes({@Index(name = "INDEX_SYS_USER_LOGINNAMAE", fields = {"loginname"}, unique = true)})
public class Sys_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;
    @Column
    @Comment("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String loginname;
    @Column
    @Comment("密码")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String password;// transient 修饰符可让此字段不在对象里显示
    @Column
    @Comment("密码盐")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String salt;
    @Column
    @Comment("昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String nickname;
    @Column
    @Comment("是否在线")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean online;
    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disbaled;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String email;
    @Prev(els = @EL("$me.now()"))
    @Column
    @Comment("登陆时间")
    protected int loginAt;
    @Column
    @Comment("登陆IP")
    @ColDefine(type = ColType.VARCHAR, width = 15)
    private String loginIp;
    @Column
    @Comment("登陆次数")
    @ColDefine(type = ColType.INT)
    private int loginCount;
    @Column
    @Comment("常用菜单")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String customMenu;
    @Column
    @Comment("皮肤样式")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginTheme;
    @Column
    private boolean loginSidebar;
    @Column
    private boolean loginBoxed;
    @Column
    private boolean loginScroll;
    @Column
    @Comment("创建人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String createBy;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitid;
    @ManyMany(from = "userId", relation = "sys_user_role", target = Sys_role.class, to = "roleId")
    protected List<Sys_role> roles;
    @ManyMany(from = "userId", relation = "sys_user_unit", target = Sys_role.class, to = "unitId")
    protected List<Sys_unit> units;
    protected List<Sys_menu> menus;
    protected List<Sys_menu> firstMenus;
    protected Map<String, List<Sys_menu>> secondMenus;
    protected List<Sys_menu> customMenus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isDisbaled() {
        return disbaled;
    }

    public void setDisbaled(boolean disbaled) {
        this.disbaled = disbaled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(int loginAt) {
        this.loginAt = loginAt;
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

    public String getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(String customMenu) {
        this.customMenu = customMenu;
    }

    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public List<Sys_role> getRoles() {
        return roles;
    }

    public void setRoles(List<Sys_role> roles) {
        this.roles = roles;
    }

    public List<Sys_unit> getUnits() {
        return units;
    }

    public void setUnits(List<Sys_unit> units) {
        this.units = units;
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

    public List<Sys_menu> getCustomMenus() {
        return customMenus;
    }

    public void setCustomMenus(List<Sys_menu> customMenus) {
        this.customMenus = customMenus;
    }
}
