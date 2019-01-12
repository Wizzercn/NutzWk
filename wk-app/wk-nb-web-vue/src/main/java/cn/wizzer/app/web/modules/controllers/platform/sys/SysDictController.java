package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_dict;
import cn.wizzer.app.sys.modules.services.SysDictService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/platform/sys/dict")
public class SysDictController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysDictService sysDictService;

    @At("")
    @Ok("beetl:/platform/sys/dict/index.html")
    @RequiresPermissions("sys.manager.dict")
    public Object index() {
        return sysDictService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path"));
    }


    @At("/child")
    @Ok("json")
    @RequiresAuthentication
    public Object child(@Param("pid") String pid, HttpServletRequest req) {
        List<Sys_dict> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and("parentId", "=", "").or("parentId", "is", null);
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.asc("location").asc("path");
        list = sysDictService.query(cnd);
        for (Sys_dict sysDict : list) {
            if (sysDictService.count(Cnd.where("parentId", "=", sysDict.getId())) > 0) {
                sysDict.setHasChildren(true);
            }
            NutMap map = Lang.obj2nutmap(sysDict);
            map.addv("expanded", false);
            map.addv("children", new ArrayList<>());
            treeList.add(map);
        }
        return Result.success().addData(treeList);
    }

    @At("/tree")
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        try {
            List<NutMap> treeList = new ArrayList<>();
            if (Strings.isBlank(pid)) {
                NutMap root = NutMap.NEW().addv("value", "root").addv("label", "不选择菜单");
                treeList.add(root);
            }
            Cnd cnd = Cnd.NEW();
            if (Strings.isBlank(pid)) {
                cnd.and("parentId", "=", "").or("parentId", "is", null);
            } else {
                cnd.and("parentId", "=", pid);
            }
            cnd.asc("location").asc("path");
            List<Sys_dict> list = sysDictService.query(cnd);
            for (Sys_dict sysDict : list) {
                NutMap map = NutMap.NEW().addv("value", sysDict.getId()).addv("label", sysDict.getName());
                if (sysDict.isHasChildren()) {
                    map.addv("children", new ArrayList<>());
                }
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.add")
    @SLog(tag = "新建字典", msg = "字典名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_dict dict, @Param(value = "parentId",df = "") String parentId, HttpServletRequest req) {
        try {
            dict.setHasChildren(false);
            dict.setOpBy(StringUtil.getPlatformUid());
            sysDictService.save(dict, parentId);
            sysDictService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.dict")
    public Object edit(String id, HttpServletRequest req) {
        try {
            return Result.success().addData(sysDictService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    @SLog(tag = "编辑字典", msg = "字典名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_dict dict, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            dict.setOpBy(StringUtil.getPlatformUid());
            dict.setOpAt(Times.getTS());
            sysDictService.updateIgnoreNull(dict);
            sysDictService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.delete")
    @SLog(tag = "删除字典", msg = "字典名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_dict dict = sysDictService.fetch(id);
            req.setAttribute("name", dict.getName());
            sysDictService.deleteAndChild(dict);
            sysDictService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    @SLog(tag = "启用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object enable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", sysDictService.fetch(menuId).getName());
            sysDictService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", menuId));
            sysDictService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    @SLog(tag = "禁用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object disable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", sysDictService.fetch(menuId).getName());
            sysDictService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", menuId));
            sysDictService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/menuAll")
    @Ok("json")
    @RequiresPermissions("sys.manager.dict")
    public Object menuAll(HttpServletRequest req) {
        try {
            List<Sys_dict> list = sysDictService.query(Cnd.NEW().asc("location").asc("path"));
            NutMap menuMap = NutMap.NEW();
            for (Sys_dict unit : list) {
                List<Sys_dict> list1 = menuMap.getList(unit.getParentId(), Sys_dict.class);
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
                list1.add(unit);
                menuMap.put(unit.getParentId(), list1);
            }
            return Result.success().addData(getTree(menuMap, ""));
        } catch (Exception e) {
            return Result.error();
        }
    }

    private List<NutMap> getTree(NutMap menuMap, String pid) {
        List<NutMap> treeList = new ArrayList<>();
        List<Sys_dict> subList = menuMap.getList(pid, Sys_dict.class);
        for (Sys_dict menu : subList) {
            NutMap map = Lang.obj2nutmap(menu);
            map.put("label", menu.getName());
            if (menu.isHasChildren() || (menuMap.get(menu.getId()) != null)) {
                map.put("children", getTree(menuMap, menu.getId()));
            }
            treeList.add(map);
        }
        return treeList;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            sysDictService.execute(Sqls.create("update sys_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    sysDictService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            sysDictService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
