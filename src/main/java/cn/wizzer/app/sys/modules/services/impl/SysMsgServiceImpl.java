package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_msg;
import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysMsgService;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.framework.page.Pagination;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class SysMsgServiceImpl extends BaseServiceImpl<Sys_msg> implements SysMsgService {
    public SysMsgServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private SysMsgUserService sysMsgUserService;
    @Inject
    private SysUserService sysUserService;

    public Sys_msg saveMsg(Sys_msg sysMsg, String[] users) {
        Sys_msg dbMsg = this.insert(sysMsg);
        if (dbMsg != null) {
            if ("user".equals(dbMsg.getType()) && users != null) {
                for (String loginname : users) {
                    Sys_msg_user sys_msg_user = new Sys_msg_user();
                    sys_msg_user.setMsgId(dbMsg.getId());
                    sys_msg_user.setStatus(0);
                    sys_msg_user.setLoginname(loginname);
                    sysMsgUserService.insert(sys_msg_user);
                }
            }
            if ("system".equals(dbMsg.getType())) {
                Cnd cnd = Cnd.where("disabled", "=", false).and("delFlag", "=", false);
                int total = sysUserService.count(cnd);
                int size = 1000;
                Pagination pagination = new Pagination();
                pagination.setTotalCount(total);
                pagination.setPageSize(size);
                for (int i = 1; i <= pagination.getTotalPage(); i++) {
                    Pagination pagination2 = sysUserService.listPage(i, size, cnd);
                    for (Object sysUser : pagination2.getList()) {
                        Sys_user user = (Sys_user) sysUser;
                        Sys_msg_user sys_msg_user = new Sys_msg_user();
                        sys_msg_user.setMsgId(dbMsg.getId());
                        sys_msg_user.setStatus(0);
                        sys_msg_user.setLoginname(user.getLoginname());
                        sysMsgUserService.insert(sys_msg_user);
                    }
                }
            }
        }
        sysMsgUserService.clearCache();
        return dbMsg;
    }

    public void deleteMsg(String id) {
        this.vDelete(id);
        sysMsgUserService.vDelete(Cnd.where("msgId", "=", id));
        sysMsgUserService.clearCache();
    }
}
