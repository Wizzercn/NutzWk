package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_dict;
import cn.wizzer.app.sys.modules.services.SysDictService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/platform/sys/dict")
public class SysDictController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysDictService dictService;

    @At("")
    @Ok("beetl:/platform/sys/dict/index.html")
    @RequiresPermissions("sys.manager.dict")
    public Object index() {
        return dictService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path"));
    }

    @At
    @Ok("beetl:/platform/sys/dict/add.html")
    @RequiresPermissions("sys.manager.dict")
    public Object add(@Param("pid") String pid) {
        return Strings.isBlank(pid) ? null : dictService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.add")
    @SLog(tag = "新建字典", msg = "字典名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_dict dict, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            dict.setOpBy(StringUtil.getPlatformUid());
            dictService.save(dict, parentId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/child/?")
    @Ok("beetl:/platform/sys/dict/child.html")
    @RequiresPermissions("sys.manager.dict")
    public Object child(String id) {
        return dictService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/dict/edit.html")
    @RequiresPermissions("sys.manager.dict")
    public Object edit(String id, HttpServletRequest req) {
        Sys_dict dict = dictService.fetch(id);
        if (dict != null) {
            req.setAttribute("parentUnit", dictService.fetch(dict.getParentId()));
        }
        return dict;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    @SLog(tag = "编辑字典", msg = "字典名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_dict dict, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            dict.setOpBy(StringUtil.getPlatformUid());
            dict.setOpAt(Times.getTS());
            dictService.updateIgnoreNull(dict);
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
            Sys_dict dict = dictService.fetch(id);
            req.setAttribute("name", dict.getName());
            dictService.deleteAndChild(dict);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict")
    public Object tree(@Param("pid") String pid) {
        List<Sys_dict> list = dictService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_dict dict : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", dict.getId());
            obj.put("text", dict.getName());
            obj.put("children", dict.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At
    @Ok("beetl:/platform/sys/dict/sort.html")
    @RequiresPermissions("sys.manager.dict")
    public void sort(HttpServletRequest req) {
        List<Sys_dict> list = dictService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_dict> firstMenus = new ArrayList<>();
        Map<String, List<Sys_dict>> secondMenus = new HashMap<>();
        for (Sys_dict menu : list) {
            if (menu.getPath().length() > 4) {
                List<Sys_dict> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.dict.edit")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds= StringUtils.split(ids, ",");
            int i=0;
            dictService.execute(Sqls.create("update sys_dict set location=0"));
            for(String s:menuIds){
                if(!Strings.isBlank(s)){
                    dictService.update(org.nutz.dao.Chain.make("location",i),Cnd.where("id","=",s));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
