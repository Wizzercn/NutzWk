package cn.wizzer.modules.services.cms;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.cms.Cms_site;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer on 2016/7/18.
 */
@IocBean(args = {"refer:dao"})
public class CmsSiteService extends Service<Cms_site> {
    public CmsSiteService(Dao dao) {
        super(dao);
    }
}
