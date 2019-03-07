package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_app_conf;
import cn.wizzer.framework.base.service.BaseService;

import java.util.List;

public interface SysAppConfService extends BaseService<Sys_app_conf> {
    List<String> getConfNameList();
}
