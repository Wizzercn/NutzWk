package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_menu;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/25.
 */
@IocBean(args = {"refer:dao"})
public class MenuService extends BaseService<Sys_menu> {
    public MenuService(Dao dao) {
        super(dao);
    }

    /**
     * 级联删除菜单
     *
     * @param id
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteAndChild(String id) {
        String pid = this.fetch(id).getParentId();
        dao().execute(Sqls.create("delete from sys_menu where id = @id").setParam("id", id));
        dao().execute(Sqls.create("delete from sys_menu where parentId = @id").setParam("id", id));
        dao().execute(Sqls.create("delete from sys_role_menu where menu_id=@id or menu_id in(SELECT id FROM sys_menu WHERE parentId=@id)").setParam("id", id));
        if (!Strings.isEmpty(pid)) {
            int count = count(Cnd.where("parentId", "=", pid));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_menu set has_children=false where id=@pid").setParam("pid", pid));
            }
        }
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_menu menu, String pid,String btns) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Sys_menu pp = this.fetch(pid);
            path = pp.getPath();
        }
        menu.setPath(getSubPath("sys_menu", "path", path));
        menu.setParentId(pid);
        menu.setHasChildren(false);
        Sys_menu thisMenu=dao().insert(menu);
        System.out.println("btns::::"+btns);
        if(thisMenu!=null&&!Strings.isEmpty(btns)){
            String[] strs= org.apache.commons.lang3.StringUtils.split(btns,',');
            for(String s:strs){
                Sys_menu btnMenu=new Sys_menu();
                String[] btn= org.apache.commons.lang3.StringUtils.split(s,'|');
                btnMenu.setParentId(thisMenu.getId());
                btnMenu.setName(btn[0]);
                btnMenu.setAliasName(btn[0]);
                btnMenu.setIs_enabled(true);
                btnMenu.setIs_show(false);
                btnMenu.setHasChildren(false);
                btnMenu.setPermission(btn[1]);
                btnMenu.setPath(getSubPath("sys_menu", "path", thisMenu.getPath()));
                btnMenu.setType("button");
                dao().insert(btnMenu);
            }
        }
        if (!Strings.isEmpty(pid)) {
            dao().execute(Sqls.create("update sys_menu set has_children=true where id=@pid").setParam("pid", pid));
        }
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void edit(Sys_menu menu, String pid) {
        if (!Strings.sNull(pid).equals(menu.getParentId())) {
            if (!Strings.isEmpty(menu.getParentId())) {
                Sys_menu pp = this.fetch(menu.getParentId());
                menu.setPath(getSubPath("sys_menu", "path", pp.getPath()));
                menu.setParentId(pp.getId());
            } else {
                menu.setPath(getSubPath("sys_menu", "path", ""));
                menu.setParentId("");
            }
        }
        menu.setCreateTime(new Date());
        menu.setCreateUser(StringUtils.getUid());
        dao().update(menu);
        if (!Strings.isEmpty(pid)) {
            int count = count(Cnd.where("parentId", "=", pid));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_menu set has_children=false where id=@pid").setParam("pid", pid));
            }
        }
        if (!Strings.isEmpty(menu.getParentId()))
            dao().execute(Sqls.create("update sys_menu set has_children=true where id=@id").setParam("id", menu.getParentId()));
        if (menu.isHasChildren()) {
            updatePath(menu.getId(), menu.getPath());
        }
    }

    /**
     * 更新子节点path
     *
     * @param id
     * @param path
     */
    public void updatePath(String id, String path) {
        List<Sys_menu> list = this.query(Cnd.where("parentId", "=", id), null);
        for (Sys_menu menu : list) {
            this.update(Chain.make("path", getSubPath("sys_menu", "path", path)), Cnd.where("id", "=", menu.getId()));
            if (menu.isHasChildren()) {
                updatePath(menu.getId(), menu.getPath());
            }
        }
    }
}
