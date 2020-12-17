package com.budwk.app.sys.services;


import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_app_list;

import java.util.List;

public interface SysAppListService extends BaseService<Sys_app_list> {
    List<String> getAppNameList();
}
