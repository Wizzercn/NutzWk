package cn.wizzer.common.service.core;

import cn.wizzer.common.page.Pagination;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.nutz.service.IdEntityService;

import java.util.List;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
public class BaseIdEntityService<T> extends IdEntityService<T> {

    protected final static int DEFAULT_PAGE_NUMBER = 20;

    public BaseIdEntityService() {
        super();
    }

    public BaseIdEntityService(Dao dao) {
        super(dao);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Integer[] ids) {
        dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 自定义SQL返回Record记录集，Record是个MAP但不区分大小写
     * 别返回Map对象，因为MySql和Oracle中字段名有大小写之分
     *
     * @param sql
     * @param <T>
     * @return
     */
    public <T> List<Record> list(Sql sql) {
        sql.setCallback(Sqls.callback.records());
        dao().execute(sql);
        return sql.getList(Record.class);

    }

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param cnd
     * @return
     */
    public Pagination listPage(Integer pageNumber, Condition cnd) {
        return listPage(pageNumber, DEFAULT_PAGE_NUMBER, cnd);
    }

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param cnd
     * @return
     */
    public Pagination listPage(Integer pageNumber, int pageSize, Condition cnd) {
        pageNumber = getPageNumber(pageNumber);
        Pager pager = dao().createPager(pageNumber, pageSize);
        List<T> list = dao().query(getEntityClass(), cnd, pager);
        pager.setRecordCount(dao().count(getEntityClass(), cnd));
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
    }

    /**
     * 默认页码
     *
     * @param pageNumber
     * @return
     */
    protected int getPageNumber(Integer pageNumber) {
        return Lang.isEmpty(pageNumber) ? 1 : pageNumber;
    }

}
