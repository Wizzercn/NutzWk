package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_api;
import cn.wizzer.app.sys.modules.services.SysApiService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemove;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;


@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysApiService.class)
@CacheDefaults(cacheName = "sys_api")
public class SysApiServiceImpl extends BaseServiceImpl<Sys_api> implements SysApiService {
    public SysApiServiceImpl(Dao dao) {
        super(dao);
    }

    private String getAppid() {
        String appid = R.sg(16).next().replaceAll("_","z");
        if (this.count(Cnd.where("appid", "=", appid)) > 0) {
            return getAppid();
        }
        return appid;
    }

    public void createAppkey(String name, String userId) throws Exception {
        String appid = getAppid();
        Sys_api sysApi = new Sys_api();
        sysApi.setName(name);
        sysApi.setDisabled(false);
        sysApi.setAppid(appid);
        sysApi.setAppkey(R.sg(30).next().replaceAll("_","z"));
        sysApi.setOpBy(userId);
        sysApi.setOpAt(Times.getTS());
        this.insert(sysApi);
        this.getAppkey(appid);//调用生成缓存
    }

    public void deleteAppkey(String appid) throws Exception {
        this.delete(appid);
        this.deleteCache(appid);
    }

    public void updateAppkey(String appid, boolean disabled) throws Exception {
        this.update(Chain.make("disabled", disabled), Cnd.where("appid", "=", appid));
        this.deleteCache(appid);
        this.getAppkey(appid);//调用生成缓存
    }

    //注意这个cacheKey 是和 web-api 对应一致的,便于直接从redis取值,而不用依赖sys模块
    @CacheResult(cacheKey = "${appid}_appkey")
    public String getAppkey(String appid) {
        Sys_api sysApi = this.fetch(Cnd.where("appid", "=", appid).and("disabled", "=", false));
        if (sysApi != null) {
            return sysApi.getAppkey();
        }
        return "";
    }

    @CacheRemove(cacheKey = "${appid}_*")
    //可以通过el表达式加 * 通配符来批量删除一批缓存
    public void deleteCache(String appid) {

    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
