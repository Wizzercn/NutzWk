package cn.wizzer.modules.open.api.test.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.open.api.test.models.Api_test;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Dao;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class ApiTestService extends Service<Api_test> {
    public ApiTestService(Dao dao) {
        super(dao);
    }

    /**
     * 事务示例
     *
     * @param test
     * @return
     */
    @Aop(TransAop.READ_COMMITTED)
    public Api_test testTx(Api_test test) {
        return this.insert(test);
    }
}
