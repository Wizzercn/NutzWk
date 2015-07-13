package cn.wizzer.common.service.core;

import cn.wizzer.common.page.Pagination;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Lang;
import org.nutz.service.EntityService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/13.
 */
public class BaseService<T> extends EntityService<T> {
    protected final static int DEFAULT_PAGE_NUMBER = 20;

    public BaseService() {
        super();
    }

    public BaseService(Dao dao) {
        super(dao);
    }

    public T fetch(long id) {
        return this.dao().fetch(this.getEntityClass(), id);
    }

    public T fetch(String name) {
        return this.dao().fetch(this.getEntityClass(), name);
    }

    public int delete(String name) {
        return this.dao().delete(this.getEntityClass(), name);
    }

    public int delete(long id) {
        return this.dao().delete(this.getEntityClass(), id);
    }

    public int delete(int id) {
        return this.dao().delete(this.getEntityClass(), id);
    }

    public int getMaxId() {
        return this.dao().getMaxId(this.getEntityClass());
    }

    /**
     * 通过LONG主键获取部分字段值
     *
     * @param fieldName
     * @param id
     * @return
     */
    public T getField(String fieldName, long id) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), id);
    }

    /**
     * 通过INT主键获取部分字段值
     *
     * @param fieldName
     * @param id
     * @return
     */
    public T getField(String fieldName, int id) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Integer[] ids) {
        this.dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids) {
        this.dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(String[] ids) {
        this.dao().clear(getEntityClass(), Cnd.where("id", "in", ids));
    }

    /**
     * 通过NAME主键获取部分字段值
     *
     * @param fieldName
     * @param name
     * @return
     */
    public T getField(String fieldName, String name) {
        return Daos.ext(this.dao(), FieldFilter.create(getEntityClass(), fieldName))
                .fetch(getEntityClass(), name);
    }


    /**
     * 计算子节点ID
     *
     * @param tableName
     * @param cloName
     * @param value
     * @param <T>
     * @return
     */
    public <T> String getSubId(String tableName, String cloName, String value) {
        final String val = value;
        Sql sql = Sqls.create("select " + cloName + " from " + tableName
                + " where " + cloName + " like '" + value + "____' order by "
                + cloName + " desc");
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                String rsvalue = val + "0001";
                if (rs != null && rs.next()) {
                    rsvalue = rs.getString(1);
                    int newvalue = NumberUtils.toInt(rsvalue
                            .substring(rsvalue.length() - 4)) + 1;
                    rsvalue = rsvalue.substring(0, rsvalue.length() - 4)
                            + new java.text.DecimalFormat("0000")
                            .format(newvalue);
                }
                return rsvalue;
            }
        });
        this.dao().execute(sql);
        return sql.getString();

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
        this.dao().execute(sql);
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
        Pager pager = this.dao().createPager(pageNumber, pageSize);
        List<T> list = this.dao().query(getEntityClass(), cnd, pager);
        pager.setRecordCount(this.dao().count(getEntityClass(), cnd));
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
