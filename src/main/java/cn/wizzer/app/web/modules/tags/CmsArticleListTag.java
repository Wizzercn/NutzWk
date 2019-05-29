package cn.wizzer.app.web.modules.tags;

import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.framework.page.Pagination;
import org.apache.commons.lang3.math.NumberUtils;
import org.beetl.core.GeneralVarTagBinding;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;

/**
 * beetl标签-CMS文章列表
 * Created by wizzer on 2018/11/11.
 */
@IocBean
public class CmsArticleListTag extends GeneralVarTagBinding {
    @Inject
    private CmsArticleService cmsArticleService;
    @Inject
    private CmsChannelService cmsChannelService;

    @Override
    public void render() {
        String channelId = Strings.sNull(this.getAttributeValue("channelId"));
        String channelCode = Strings.sNull(this.getAttributeValue("channelCode"));
        Cms_channel channel = cmsChannelService.getChannel(channelId, channelCode);
        if (channel != null) {
            channelId = channel.getId();
        }
        int pageNumber = NumberUtils.toInt(Strings.sNull(this.getAttributeValue("pageNumber")));
        int pageSize = NumberUtils.toInt(Strings.sNull(this.getAttributeValue("pageSize")));
        String startDate = Strings.sNull(this.getAttributeValue("startDate"));
        String endDate = Strings.sNull(this.getAttributeValue("endDate"));
        String sortName = Strings.sNull(this.getAttributeValue("sortName"));
        String sortOrder = Strings.sNull(this.getAttributeValue("sortOrder"));
        Pagination page = new Pagination();
        try {
            Cnd cnd = Cnd.NEW();
            //日期起
            if (Strings.isNotBlank(startDate)) {
                cnd.and("publishAt", ">=", Times.d2TS(Times.D(startDate + " 00:00:00")));
            }
            //日期至
            if (Strings.isNotBlank(endDate)) {
                cnd.and("publishAt", "<=", Times.d2TS(Times.D(startDate + " 23:59:59")));
            }
            if (Strings.isNotBlank(channelId)) {
                cnd.and("channelId", "=", channelId);
            }
            cnd.and("disabled", "=", false);
            if (Strings.isNotBlank(sortName) && Strings.isNotBlank(sortOrder)) {
                cnd.orderBy(sortName, sortOrder);
            }
            page = cmsArticleService.listPage(pageNumber, pageSize, cnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.binds(page);
        this.doBodyRender();
    }
}
