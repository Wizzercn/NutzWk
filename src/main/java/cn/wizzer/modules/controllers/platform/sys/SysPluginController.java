package cn.wizzer.modules.controllers.platform.sys;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.plugin.IPlugin;
import cn.wizzer.common.plugin.PluginMaster;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.models.sys.Sys_plugin;
import cn.wizzer.modules.services.sys.SysPluginService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Wizzer on 2016/12/5.
 */
@IocBean
@At("/platform/sys/plugin")
@Filters({@By(type = PrivateFilter.class)})
public class SysPluginController {
    private static final Log log = Logs.get();
    @Inject
    private PluginMaster pluginMaster;
    @Inject
    private SysPluginService sysPluginService;

    @At("")
    @Ok("beetl:/platform/sys/plugin/index.html")
    @RequiresAuthentication
    public Object index() {
        return sysPluginService.query(Cnd.where("disabled", "=", false).asc("code"));
    }

    @RequiresRoles("sysadmin")
    @At
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:fileUpload"})
    @Ok("json")
    public Object addDo(@Param("code") String code,
                        @Param("className") String className,
                        @Param("Filedata") TempFile tf, @Param("args") String[] args)
            throws IOException {
        try {
            byte[] buf = Streams.readBytesAndClose(tf.getInputStream());
            IPlugin plugin = pluginMaster.build(className, buf);
            pluginMaster.register(code, plugin, args);
            String p = Globals.AppRoot;
            String f = Globals.AppUploadPath + "/plugin/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf("."));
            Files.write(new File(p + f), tf.getInputStream());
            Sys_plugin sysPlugin = new Sys_plugin();
            sysPlugin.setCode(code);
            sysPlugin.setClassName(className);
            sysPlugin.setArgs(StringUtils.toString(args));
            sysPlugin.setDisabled(false);
            sysPlugin.setPath(f);
            sysPluginService.insert(sysPlugin);
            return Result.success("system.success");
        } catch (Exception e) {
            log.debug("plugin load error", e);
            return Result.error("system.error");
        }
    }

    @RequiresRoles("sysadmin")
    @At
    @POST
    public void delete(String key) {
        pluginMaster.remove(key);
    }
}
