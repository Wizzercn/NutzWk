package com.budwk.app.web.tags;

import com.budwk.app.cms.models.Cms_link;
import com.budwk.app.cms.services.CmsLinkService;
import org.apache.commons.lang3.math.NumberUtils;
import org.beetl.core.tag.GeneralVarTagBinding;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.List;

/**
 * beetl标签-友情链接
 * Created by wizzer on 2018/11/11.
 */
@IocBean
public class CmsLinkListTag extends GeneralVarTagBinding {
    @Inject
    private CmsLinkService cmsLinkService;

    @Override
    public void render() {
        String code = Strings.sNull(this.getAttributeValue("code"));
        int size = NumberUtils.toInt(Strings.sNull(this.getAttributeValue("size")), 1);
        List<Cms_link> list = cmsLinkService.getLinkList(code, size);
        list.forEach(link -> {
            this.binds(link);
            this.doBodyRender();
        });
    }
}
