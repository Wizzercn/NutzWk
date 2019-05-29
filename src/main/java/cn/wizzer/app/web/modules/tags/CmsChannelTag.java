package cn.wizzer.app.web.modules.tags;

import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import org.beetl.core.GeneralVarTagBinding;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * beetl标签-CMS栏目详情
 * Created by wizzer on 2018/11/11.
 */
@IocBean
public class CmsChannelTag extends GeneralVarTagBinding {
    @Inject
    private CmsChannelService cmsChannelService;

    @Override
    public void render() {
        String id = Strings.sNull(this.getAttributeValue("id"));
        String code = Strings.sNull(this.getAttributeValue("code"));
        Cms_channel channel = cmsChannelService.getChannel(id, code);
        this.binds(channel);
        this.doBodyRender();
    }
}
