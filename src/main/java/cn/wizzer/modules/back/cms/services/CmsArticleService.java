package cn.wizzer.modules.back.cms.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.cms.models.Cms_article;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer on 2016/7/18.
 */
@IocBean(args = {"refer:dao"})
public class CmsArticleService extends Service<Cms_article> {
    public CmsArticleService(Dao dao) {
        super(dao);
    }
}
