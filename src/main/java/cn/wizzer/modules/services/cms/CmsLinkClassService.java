package cn.wizzer.modules.services.cms;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.cms.Cms_link_class;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer on 2016/7/18.
 */
@IocBean(args = {"refer:dao"})
public class CmsLinkClassService extends Service<Cms_link_class> {
    public CmsLinkClassService(Dao dao) {
        super(dao);
    }
}
