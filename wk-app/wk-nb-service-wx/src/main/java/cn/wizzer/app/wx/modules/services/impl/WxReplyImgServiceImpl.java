package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.app.wx.modules.models.Wx_reply_img;
import cn.wizzer.app.wx.modules.services.WxReplyImgService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
@Service(interfaceClass = WxReplyImgService.class)
public class WxReplyImgServiceImpl extends BaseServiceImpl<Wx_reply_img> implements WxReplyImgService {
    public WxReplyImgServiceImpl(Dao dao) {
        super(dao);
    }
}
