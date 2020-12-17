package com.budwk.app.sys.services;


import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_app_conf;

import java.util.List;

public interface SysAppConfService extends BaseService<Sys_app_conf> {
    List<String> getConfNameList();
}
