package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;
import org.nutz.integration.json4excel.annotation.J4EIgnore;
import org.nutz.integration.json4excel.annotation.J4EName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_user")
@J4EName("用户数据")
@TableIndexes({@Index(name = "INDEX_SYS_USER_LOGINNAMAE", fields = {"loginname"}, unique = true)})
public class Sys_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("用户名")
    @J4EName("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String loginname;

    @Column
    @Comment("密码")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String password;// transient 修饰符可让此字段不在对象里显示

    @Column
    @Comment("密码盐")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String salt;

    @Column
    @Comment("姓名")
    @J4EName("姓名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String username;

    @Column
    @Comment("头像")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String avatar;

    @Column
    @Comment("是否在线")
    @J4EIgnore
    @ColDefine(type = ColType.BOOLEAN)
    private boolean userOnline;

    @Column
    @Comment("是否禁用")
    @J4EIgnore
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("电子邮箱")
    @J4EName("电子邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String email;

    @Column
    @Comment("手机号码")
    @J4EName("手机号码")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String mobile;

    @Column
    @Comment("登陆时间")
    @J4EIgnore
    private Long loginAt;

    @Column
    @Comment("登陆IP")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String loginIp;

    @Column
    @Comment("登陆次数")
    @J4EIgnore
    @ColDefine(type = ColType.INT)
    private Integer loginCount;

    @Column
    @Comment("登陆SessionId")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String loginSessionId;

    @Column
    @Comment("常用菜单")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String customMenu;

    @Column
    @Comment("皮肤样式")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginTheme;

    @Column
    @J4EIgnore
    private boolean loginSidebar;

    @Column
    @J4EIgnore
    private boolean loginBoxed;

    @Column
    @J4EIgnore
    private boolean loginScroll;

    @Column
    @J4EIgnore
    private boolean loginPjax;

    @Column
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitid;

    @One(field = "unitid")
    @J4EIgnore
    private Sys_unit unit;

    @ManyMany(from = "userId", relation = "sys_user_role", to = "roleId")
    @J4EIgnore
    private List<Sys_role> roles;

    @ManyMany(from = "userId", relation = "sys_user_unit", to = "unitId")
    @J4EIgnore
    protected List<Sys_unit> units;

    @J4EIgnore
    protected List<Sys_menu> menus;

    @J4EIgnore
    protected List<Sys_menu> firstMenus;

    @J4EIgnore
    protected Map<String, List<Sys_menu>> secondMenus;

    @J4EIgnore
    private List<Sys_menu> customMenus;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isUserOnline() {
        return userOnline;
    }

    public void setUserOnline(boolean userOnline) {
        this.userOnline = userOnline;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Long loginAt) {
        this.loginAt = loginAt;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(String loginSessionId) {
        this.loginSessionId = loginSessionId;
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

    public boolean isLoginPjax() {
        return loginPjax;
    }

    public void setLoginPjax(boolean loginPjax) {
        this.loginPjax = loginPjax;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public Sys_unit getUnit() {
        return unit;
    }

    public void setUnit(Sys_unit unit) {
        this.unit = unit;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
