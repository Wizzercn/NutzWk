package cn.wizzer.app.cms.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

@IocBean(args = {"refer:dao"})
public class CmsChannelServiceImpl extends BaseServiceImpl<Cms_channel> implements CmsChannelService {
    public CmsChannelServiceImpl(Dao dao) {
        super(dao);
    }

    /**
     * 新增菜单
     *
     * @param channel
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Cms_channel channel, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Cms_channel pp = this.fetch(pid);
            path = pp.getPath();
        } else pid = "";
        channel.setPath(getSubPath("cms_channel", "path", path));
        channel.setParentId(pid);
        dao().insert(channel);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }

    /**
     * 级联删除菜单
     *
     * @param channel
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteAndChild(Cms_channel channel) {
        dao().execute(Sqls.create("delete from cms_channel where path like @path").setParam("path", channel.getPath() + "%"));
        dao().execute(Sqls.create("delete from cms_article where channelId=@id or channelId in(SELECT id FROM cms_channel WHERE path like @path)").setParam("id", channel.getId()).setParam("path", channel.getPath() + "%"));
        if (!Strings.isEmpty(channel.getParentId())) {
            int count = count(Cnd.where("parentId", "=", channel.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update cms_channel set hasChildren=0 where id=@pid").setParam("pid", channel.getParentId()));
            }
        }
    }
}
