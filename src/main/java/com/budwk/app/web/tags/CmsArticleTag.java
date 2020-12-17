package com.budwk.app.web.tags;

import com.budwk.app.cms.services.CmsArticleService;
import org.beetl.core.tag.GeneralVarTagBinding;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * beetl标签-CMS文章详情
 * Created by wizzer on 2018/11/11.
 */
@IocBean
public class CmsArticleTag extends GeneralVarTagBinding {
    @Inject
    private CmsArticleService cmsArticleService;

    @Override
    public void render() {
        String id = Strings.sNull(this.getAttributeValue("id"));
        this.binds(cmsArticleService.fetch(Cnd.where("id", "=", id).and("disabled", "=", false)));
        this.doBodyRender();
    }
}
