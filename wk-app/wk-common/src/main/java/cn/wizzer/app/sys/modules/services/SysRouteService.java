package cn.wizzer.app.sys.modules.services;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.sys.modules.models.Sys_route;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/23.
 */
@RpcService
public interface SysRouteService extends BaseService<Sys_route> {
}
