package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_plugin;
import cn.wizzer.app.sys.modules.services.SysPluginService;
import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.plugin.IPlugin;
import cn.wizzer.app.web.commons.plugin.PluginMaster;
import cn.wizzer.framework.base.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import java.io.File;
import java.io.IOException;

/**
 * Created by Wizzer on 2016/12/5.
 */
@IocBean
@At("/platform/sys/plugin")
public class SysPluginController {
    private static final Log log = Logs.get();
    @Inject
    private PluginMaster pluginMaster;
    @Inject
    private SysPluginService sysPluginService;

    @At("")
    @Ok("beetl:/platform/sys/plugin/index.html")
    @RequiresPermissions("sys.manager.plugin")
    public Object index() {
        return sysPluginService.query(Cnd.where("disabled", "=", false).asc("code"));
    }

    @At
    @Ok("json")
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:fileUpload"})
    @RequiresPermissions("sys.manager.plugin.add")
    public Object addDo(@Param("code") String code,
                        @Param("className") String className,
                        @Param("Filedata") TempFile tf, @Param("args") String[] args)
            throws IOException {
        try {
            String name = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".")).toLowerCase();
            String p = Globals.AppRoot;
            String f = Globals.AppUploadPath + "/plugin/" + R.UU32() + name;
            File file = new File(p + f);
            Files.createFileIfNoExists(file);
            Files.write(file, tf.getInputStream());
            IPlugin plugin;
            if (".jar".equals(name)) {
                plugin = pluginMaster.buildFromJar(file, className);
            } else {
                byte[] buf = Files.readBytes(file);
                plugin = pluginMaster.build(className, buf);
            }
            pluginMaster.register(code, plugin, args);
            Sys_plugin sysPlugin = new Sys_plugin();
            sysPlugin.setCode(code);
            sysPlugin.setClassName(className);
            sysPlugin.setArgs(StringUtils.toString(args));
            sysPlugin.setDisabled(false);
            sysPlugin.setPath(f);
            sysPluginService.insert(sysPlugin);
            return Result.success("system.success");
        } catch (Exception e) {
            log.debug("plugin install error", e);
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @RequiresPermissions("sys.manager.plugin.delete")
    public Object delete(String id) {
        try {
            Sys_plugin sysPlugin = sysPluginService.fetch(id);
            pluginMaster.remove(sysPlugin.getCode());
            //被占用删不掉文件啊 Files.deleteFile(new File(Globals.AppRoot + sysPlugin.getPath()));
            sysPluginService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            log.debug("plugin uninstall error", e);
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @RequiresPermissions("sys.manager.plugin.update")
    public Object enable(String id) {
        try {
            Sys_plugin sysPlugin = sysPluginService.fetch(id);
            String name = sysPlugin.getPath().substring(sysPlugin.getPath().indexOf(".")).toLowerCase();
            File file = new File(Globals.AppRoot + sysPlugin.getPath());
            String[] p = new String[]{};
            IPlugin plugin;
            if (".jar".equals(name)) {
                plugin = pluginMaster.buildFromJar(file, sysPlugin.getClassName());
            } else {
                byte[] buf = Files.readBytes(file);
                plugin = pluginMaster.build(sysPlugin.getClassName(), buf);
            }
            if (!Strings.isBlank(sysPlugin.getArgs())) {
                p = org.apache.commons.lang3.StringUtils.split(sysPlugin.getArgs(), ",");
            }
            pluginMaster.register(sysPlugin.getCode(), plugin, p);
            sysPlugin.setDisabled(false);
            sysPluginService.update(sysPlugin);
            return Result.success("system.success");
        } catch (Exception e) {
            log.debug("plugin uninstall error", e);
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @RequiresPermissions("sys.manager.plugin.update")
    public Object disable(String id) {
        try {
            Sys_plugin sysPlugin = sysPluginService.fetch(id);
            pluginMaster.remove(sysPlugin.getCode());
            sysPlugin.setDisabled(true);
            sysPluginService.update(sysPlugin);
            return Result.success("system.success");
        } catch (Exception e) {
            log.debug("plugin uninstall error", e);
            return Result.error("system.error");
        }
    }
}
