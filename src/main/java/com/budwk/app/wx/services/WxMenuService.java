package com.budwk.app.wx.services;

import com.budwk.app.wx.models.Wx_menu;
import com.budwk.app.base.service.BaseService;

public interface WxMenuService extends BaseService<Wx_menu> {
    void save(Wx_menu menu, String pid);

    void deleteAndChild(Wx_menu menu);
}
