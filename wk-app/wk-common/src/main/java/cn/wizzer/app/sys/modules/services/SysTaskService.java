package cn.wizzer.app.sys.modules.services;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.sys.modules.models.Sys_task;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
@RpcService
public interface SysTaskService extends BaseService<Sys_task> {

}
