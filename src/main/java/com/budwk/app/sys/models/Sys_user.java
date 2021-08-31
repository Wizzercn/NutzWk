package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;
import org.nutz.integration.json4excel.annotation.J4EIgnore;
import org.nutz.integration.json4excel.annotation.J4EName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
    @PrevInsert(uu32 = true)
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
    @Comment("创建时间")
    @J4EIgnore
    @PrevInsert(now = true)
    private Long createAt;

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
    @Comment("菜单样式")
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String menuTheme;

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
    private String unitId;

    @Column
    @J4EIgnore
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String unitPath;

    @One(field = "unitId")
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

}
