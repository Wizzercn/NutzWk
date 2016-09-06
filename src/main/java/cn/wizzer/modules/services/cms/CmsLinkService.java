package cn.wizzer.modules.services.cms;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.cms.Cms_link;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer on 2016/7/18.
 */
@IocBean(args = {"refer:dao"})
public class CmsLinkService extends Service<Cms_link> {
    public CmsLinkService(Dao dao) {
        super(dao);
    }
}
