package cn.wizzer.framework.base.service;

import cn.wizzer.framework.page.Pagination;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.util.NutMap;

import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface BaseService<T> {

    Dao dao();

    int count(Condition cnd);

    int count();

    int count(String tableName, Condition cnd);

    int count(String tableName);

    T fetch(long id);

    T fetch(String id);

    <T> T fetchLinks(T obj, String regex);

    <T> T fetchLinks(T obj, String regex, Condition cnd);

    T fetch(Condition cnd);

    T fetchx(Object... pks);

    boolean exists(Object... pks);

    T insert(T obj);

    void insert(String tableName, Chain chain);

    T fastInsert(T obj);

    int update(Object obj);

    int updateIgnoreNull(Object obj);

    int update(Chain chain, Condition cnd);

    int update(String tableName, Chain chain, Condition cnd);

    int getMaxId();

    int delete(long id);

    int delete(int id);

    int delete(String id);

    void delete(Integer[] ids);

    void delete(Long[] ids);

    void delete(String[] ids);

    int clear();

    int clear(String tableName);

    int clear(Condition cnd);

    int clear(String tableName, Condition cnd);

    int vDelete(String id);

    int vDelete(String[] ids);

    T getField(String fieldName, long id);

    T getField(String fieldName, int id);

    T getField(String fieldName, String name);

    T getField(String fieldName, Condition cnd);

    List<T> query(String fieldName, Condition cnd);

    List<T> query(Condition cnd);

    String getSubPath(String tableName, String cloName, String value);

    int count(Sql sql);

    List<Record> list(Sql sql);

    Map getMap(Sql sql);

    Pagination listPage(Integer pageNumber, Condition cnd);

    Pagination listPage(Integer pageNumber, Sql sql);

    Pagination listPage(Integer pageNumber, String tableName, Condition cnd);

    Pagination listPage(Integer pageNumber, int pageSize, Condition cnd);

    Pagination listPage(Integer pageNumber, int pageSize, Condition cnd, String fieldName);

    Pagination listPage(Integer pageNumber, int pageSize, String tableName, Condition cnd);

    Pagination listPage(Integer pageNumber, int pageSize, Sql sql);

    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);

    NutMap data(int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName, Cnd subCnd);

    NutMap data(int length, int start, int draw, Sql countSql, Sql orderSql);
}
