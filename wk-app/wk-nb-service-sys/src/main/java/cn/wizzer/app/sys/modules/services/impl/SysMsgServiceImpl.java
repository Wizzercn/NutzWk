package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.sys.modules.models.Sys_msg;
import cn.wizzer.app.sys.modules.services.SysMsgService;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysMsgService.class)
public class SysMsgServiceImpl extends BaseServiceImpl<Sys_msg> implements SysMsgService {
    public SysMsgServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private SysMsgUserService sysMsgUserService;
    @Inject
    private SysUserService userService;

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
                //系统后台用户应该不会太多吧……
                List<Sys_user> userList = userService.query(Cnd.where("disabled", "=", false).and("delFlag", "=", false));
                for (Sys_user sysUser : userList) {
                    Sys_msg_user sys_msg_user = new Sys_msg_user();
                    sys_msg_user.setMsgId(dbMsg.getId());
                    sys_msg_user.setStatus(0);
                    sys_msg_user.setLoginname(sysUser.getLoginname());
                    sysMsgUserService.insert(sys_msg_user);
                }
            }
        }
        return dbMsg;
    }

    public void deleteMsg(String id) {
        this.vDelete(id);
        sysMsgUserService.vDelete(Cnd.where("msgId", "=", id));
    }
}
