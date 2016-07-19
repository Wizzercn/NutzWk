package cn.wizzer.modules.back.cms.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.cms.models.Cms_channel;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer on 2016/7/18.
 */
@IocBean(args = {"refer:dao"})
public class CmsChannelService extends Service<Cms_channel> {
    public CmsChannelService(Dao dao) {
        super(dao);
    }
}
