package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemove;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

import java.util.List;

@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysMsgUserService.class)
@CacheDefaults(cacheName = "sys_msg_user")
public class SysMsgUserServiceImpl extends BaseServiceImpl<Sys_msg_user> implements SysMsgUserService {
    public SysMsgUserServiceImpl(Dao dao) {
        super(dao);
    }

    /**
     * 获取未读消息数量
     *
     * @param loginname
     * @return
     */
    @CacheResult(cacheKey = "${args[0]}_getUnreadNum")
    public int getUnreadNum(String loginname) {
        int size = this.count(Cnd.where("delFlag", "=", false).and("loginname", "=", loginname)
                .and("status", "=", 0));
        return size;
    }

    /**
     * 获取未读消息列表
     *
     * @param loginname
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @CacheResult(cacheKey = "${args[0]}_getUnreadList")
    public List<Sys_msg_user> getUnreadList(String loginname, int pageNumber, int pageSize) {
        return this.query(Cnd.where("delFlag", "=", false).and("loginname", "=", loginname)
                .desc("opAt"), "msg", Cnd.orderBy().desc("sendAt"), new Pager().setPageNumber(pageNumber).setPageSize(pageSize));
    }

    @CacheRemove(cacheKey = "${args[0]}_*")
    //可以通过el表达式加 * 通配符来批量删除一批缓存
    public void deleteCache(String loginname) {

    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
