package cn.wizzer.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.Daos;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-11-27 上午9:23:55
 */

public class ObjectCtl {
    /**
     * 增加记录,成功返回对象,失败返回null
     *
     * @param dao
     * @param t
     * @return
     */
    public <T> T addT(Dao dao, T t) {
        T rt;
        try {
            rt = dao.insert(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rt;
    }

    /**
     * 增加记录,成功返回true
     *
     * @param dao
     * @param t
     * @return
     */
    public <T> boolean add(Dao dao, T t) {
        boolean rt = true;
        try {
            dao.insert(t);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return rt;
    }

    /**
     * 修改一条数据
     *
     * @param dao
     * @param t
     * @return
     */
    public <T> boolean update(Dao dao, T t) {
        return dao.updateIgnoreNull(t) == 1;
    }

    /**
     * 根据条件修改指定数据
     *
     * @param dao
     * @param obj
     * @param chain     修改的内容
     * @param condition 条件
     * @return
     */
    public <T> boolean update(Dao dao, Class<T> obj, Chain chain,
                              Condition condition) {
        return dao.update(obj, chain, condition) > 0;
    }

    /**
     * 排序
     *
     * @param tableName
     * @param ids
     * @param rowName
     * @param initvalue
     * @return
     */
    public <T> boolean updateSortRow(Dao dao, String tableName, String[] ids,
                                     String rowName, int initvalue) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Sql sql = Sqls.create("update " + tableName + " set " + rowName
                        + "=" + (i + initvalue) + " where id=" + ids[i]);
                dao.execute(sql);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 执行自定义SQL
     *
     * @param sql
     * @return
     */
    public <T> boolean exeUpdateBySql(Dao dao, Sql sql) {
        try {
            dao.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据整型ID查询一个对象
     *
     * @param dao
     * @param obj
     * @param id
     * @return
     */
    public <T> T detailById(Dao dao, Class<T> obj, int id) {
        T t;
        try {
            t = dao.fetch(obj, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据整型ID查询一个对象
     *
     * @param dao
     * @param obj
     * @param id
     * @return
     */
    public <T> T detailById(Dao dao, Class<T> obj, long id) {
        T t;
        try {
            t = dao.fetch(obj, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据字符串ID查询一个对象
     *
     * @param dao
     * @param obj
     * @param id
     * @return
     */
    public <T> T detailByName(Dao dao, Class<T> obj, String id) {
        T t;
        try {
            t = dao.fetch(obj, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据字符串列名查询一个对象
     *
     * @param dao
     * @param obj
     * @param colname
     * @param name
     * @return
     */
    public <T> T detailByName(Dao dao, Class<T> obj, String colname, String name) {
        T t;
        try {
            t = dao.fetch(obj, Cnd.where(colname, "=", name));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据整型列名查询一个对象
     *
     * @param dao
     * @param obj
     * @param colname
     * @param name
     * @return
     */
    public <T> T detailByName(Dao dao, Class<T> obj, String colname, int name) {
        T t;
        try {
            t = dao.fetch(obj, Cnd.where(colname, "=", name));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据查询条件查询一个对象
     *
     * @param dao
     * @param obj
     * @param cnd
     * @return
     */
    public <T> T detailByCnd(Dao dao, Class<T> obj, Condition cnd) {
        T t;
        try {
            t = dao.fetch(obj, cnd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 根据查询条件查询一个对象
     *
     * @param dao
     * @param obj
     * @param sql
     * @return
     */
    public <T> T detailBySql(Dao dao, Class<T> obj, Sql sql) {
        try {

            Entity<T> entity = dao.getEntity(obj);
            sql.setEntity(entity);
            sql.setCallback(Sqls.callback.entity());
            dao.execute(sql);
            return sql.getObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据整型@Id删除一条记录
     *
     * @param dao
     * @param obj
     * @param id
     * @return
     */
    public <T> boolean deleteById(Dao dao, Class<T> obj, int id) {
        return dao.delete(obj, id) == 1;

    }

    /**
     * 根据长整型@Id删除一条记录
     *
     * @param dao
     * @param obj
     * @param id
     * @return
     */
    public <T> boolean deleteById(Dao dao, Class<T> obj, long id) {
        return dao.delete(obj, id) == 1;

    }

    /**
     * 根据整型@Id批量删除记录
     *
     * @param dao
     * @param obj
     * @param ids
     * @return
     */
    public <T> boolean deleteByIds(Dao dao, Class<T> obj, String[] ids) {
        boolean result = false;
        for (int i = 0; i < ids.length; i++) {
            result = dao.delete(obj,
                    NumberUtils.toInt(Strings.sNull(ids[i]))) == 1;
        }
        return result;

    }

    /**
     * 根据长整型@Id批量删除记录
     *
     * @param dao
     * @param obj
     * @param ids
     * @return
     */
    public <T> boolean deleteByIdsLong(Dao dao, Class<T> obj, String[] ids) {
        boolean result = false;
        for (int i = 0; i < ids.length; i++) {
            result = dao.delete(obj,
                    NumberUtils.toInt(Strings.sNull(ids[i]))) == 1;
        }
        return result;

    }

    /**
     * 根据字符串@Name删除一条记录
     *
     * @param dao
     * @param obj
     * @param name
     * @return
     */
    public <T> boolean deleteByName(Dao dao, Class<T> obj, String name) {
        return dao.delete(obj, name) == 1;
    }

    /**
     * 根据字符型型@Name批量删除记录
     *
     * @param dao
     * @param obj
     * @param ids
     * @return
     */
    public <T> boolean deleteByNames(Dao dao, Class<T> obj, String[] ids) {
        boolean result = false;
        for (int i = 0; i < ids.length; i++) {
            result = dao.delete(obj, Strings.sNull(ids[i])) == 1;
        }
        return result;

    }

    /**
     * 根据条件删除表中数据
     *
     * @param dao
     * @param table
     * @param cnd
     */
    public int delete(Dao dao, String table, Condition cnd) {
        return dao.clear(table, cnd);
    }

    /**
     * 根据条件删除表中数据
     *
     * @param dao
     * @param obj
     * @param cnd
     */
    public int delete(Dao dao, Class<?> obj, Condition cnd) {
        return dao.clear(obj, cnd);
    }

    /**
     * 获取总记录数
     *
     * @param dao
     * @param obj
     * @return
     */
    public <T> int getRowCount(Dao dao, Class<T> obj) {
        return dao.count(obj);
    }

    /**
     * 获取满足条件的记录数
     *
     * @param dao
     * @param obj
     * @param cnd
     * @return
     */
    public <T> int getRowCount(Dao dao, Class<T> obj, Condition cnd) {
        return dao.count(obj, cnd);
    }

    /**
     * 取一个字段的一个值，对于多个字段或多个值，不适用此方法
     *
     * @param dao
     * @param sql
     * @return
     */
    public <T> int getIntRowValue(Dao dao, Sql sql) {
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        });
        dao.execute(sql);
        return sql.getInt();

    }

    /**
     * 根据条件查询数据库中满足条件的数据，返回对象
     *
     * @param dao
     * @param obj
     * @param cnd
     * @return
     */
    public <T> List<T> list(Dao dao, Class<T> obj, Condition cnd) {
        List<T> t;
        try {
            t = dao.query(obj, cnd, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;

    }

    /**
     * 自定义SQL，返回表对象
     *
     * @param dao
     * @param obj
     * @param sql
     * @return
     */
    public <T> List<T> list(Dao dao, Class<T> obj, Sql sql) {
        Entity<T> entity = dao.getEntity(obj);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao.execute(sql);
        return sql.getList(obj);

    }

    /**
     * 自定义SQL分页，返回表对象
     *
     * @param dao
     * @param obj
     * @param sql
     * @param curPage
     * @param pageSize
     * @return
     */
    public <T> List<T> listPage(Dao dao, Class<T> obj, Sql sql, int curPage,
                                int pageSize) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        Entity<T> entity = dao.getEntity(obj);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao.execute(sql);
        return sql.getList(obj);

    }

    /**
     * 自定义SQL分页，返回表对象
     *
     * @param dao
     * @param obj
     * @param sql
     * @param curPage
     * @param pageSize
     * @param count
     * @return
     */
    public <T> String listPage(Dao dao, Class<T> obj, Sql sql, int curPage,
                                int pageSize,int count) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount(count);// 记录数需手动设置
        sql.setPager(pager);
        Entity<T> entity = dao.getEntity(obj);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao.execute(sql);
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", sql.getList(obj));
        return Json.toJson(jsonobj);

    }

    /**
     * 自定义SQL分页，返回表对象
     *
     * @param dao
     * @param obj
     * @param sql
     * @param pager
     * @return
     */
    public <T> List<T> listPage(Dao dao, Class<T> obj, Sql sql, Pager pager) {
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        Entity<T> entity = dao.getEntity(obj);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao.execute(sql);
        return sql.getList(obj);

    }

    /**
     * 自定义SQL，返回绑定对象
     * Record不区分大小写，Map区分大小写，注意mysql和oracle字段名大小写问题
     * @param dao
     * @param sql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> List<Record> list(Dao dao, Sql sql) {
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return sql.getList(Record.class);

    }

    /**
     * 根据查询条件分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param obj
     * @param curPage
     * @param pageSize
     * @param cnd
     * @return
     */
    public <T> QueryResult listPage(Dao dao, Class<T> obj, int curPage,
                                    int pageSize, Condition cnd) {
        Pager pager = dao.createPager(curPage, pageSize);
        List<T> list = dao.query(obj, cnd, pager);
        pager.setRecordCount(dao.count(obj, cnd));// 记录数需手动设置
        return new QueryResult(list, pager);
    }

    /**
     * 根据查询条件分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param obj
     * @param cnd
     * @param pager
     * @return
     */
    public <T> QueryResult listPage(Dao dao, Class<T> obj, Condition cnd, Pager pager) {
        List<T> list = dao.query(obj, cnd, pager);
        pager.setRecordCount(dao.count(obj, cnd));// 记录数需手动设置
        return new QueryResult(list, pager);
    }

    /**
     * 根据查询条件分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param obj
     * @param cnd
     * @param pager
     * @return
     */
    public <T> QueryResult listPager(Dao dao, Class<T> obj, Condition cnd, Pager pager) {
        List<T> list = dao.query(obj, cnd, pager);
        return new QueryResult(list, pager);
    }
    /**
     * 根据查询条件分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param obj
     * @param cnd
     * @param pager
     * @return
     */
    public <T> QueryResult listPage(Dao dao, Class<T> obj, Condition cnd, Pager pager, int count) {
        List<T> list = dao.query(obj, cnd, pager);
        pager.setRecordCount(count);// 记录数需手动设置
        return new QueryResult(list, pager);
    }

    /**
     * 根据查询条件分页,返回封装好的 Easyui.datagrid JSON
     *
     * @param dao
     * @param obj
     * @param curPage
     * @param pageSize
     * @param cnd
     * @return
     */
    public <T> String listPageJson(Dao dao, Class<T> obj, int curPage,
                                   int pageSize, Condition cnd) {
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        Pager pager = dao.createPager(curPage, pageSize);
        List<T> list = dao.query(obj, cnd, pager);
        pager.setRecordCount(dao.count(obj, cnd));//记录数需手动设置
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", list);
        return Json.toJson(jsonobj);
    }

    /**
     * 根据查询条件分页,返回封装好的 Easyui.datagrid JSON
     *
     * @param dao
     * @param obj
     * @param cnd
     * @param pager
     * @return
     */
    public <T> String listPageJson(Dao dao, Class<T> obj, Condition cnd, Pager pager) {
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        List<T> list = dao.query(obj, cnd, pager);
        pager.setRecordCount(dao.count(obj, cnd));//记录数需手动设置
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", list);
        return Json.toJson(jsonobj);
    }

    /**
     * 根据自定义SQL分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param sql
     * @param curPage
     * @param pageSize
     * @return
     */
    public <T> QueryResult listPageSql(Dao dao, Sql sql, int curPage, int pageSize) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return new QueryResult(sql.getList(Record.class), pager);
    }
    /**
     * 根据自定义SQL分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param sql
     * @param curPage
     * @param pageSize
     * @param count
     * @return
     */
    public <T> QueryResult listPageSql(Dao dao, Sql sql, int curPage, int pageSize,int count) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount(count);// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return new QueryResult(sql.getList(Record.class), pager);
    }

    /**
     * 根据自定义SQL分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param sql
     * @param pager
     * @return
     */
    public <T> QueryResult listPageSql(Dao dao, Sql sql, Pager pager) {
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return new QueryResult(sql.getList(Record.class), pager);
    }

    /**
     * 根据自定义SQL分页,返回封装好的QueryResult对象
     *
     * @param dao
     * @param sql
     * @param pager
     * @return
     */
    public <T> QueryResult listPagerSql(Dao dao, Sql sql, Pager pager) {
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return new QueryResult(sql.getList(Record.class), pager);
    }
    /**
     * 根据自定义SQL分页,返回封装好的 Easyui.datagrid JSON
     *
     * @param dao
     * @param sql
     * @param curPage
     * @param pageSize
     * @return
     */
    public <T> String listPageJsonSql(Dao dao, Sql sql, int curPage, int pageSize) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", sql.getList(Record.class));
        return Json.toJson(jsonobj);
    }

    /**
     * 根据自定义SQL分页,返回封装好的 Easyui.datagrid JSON
     *
     * @param dao
     * @param sql
     * @param curPage
     * @param pageSize
     * @param count
     * @return
     */
    public <T> String listPageJsonSql(Dao dao, Sql sql, int curPage, int pageSize, int count) {
        Pager pager = dao.createPager(curPage, pageSize);
        pager.setRecordCount(count);// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", sql.getList(Record.class));
        return Json.toJson(jsonobj);
    }

    /**
     * 根据自定义SQL分页,返回封装好的 Easyui.datagrid JSON
     *
     * @param dao
     * @param sql
     * @param pager
     * @return
     */
    public <T> String listPageJsonSql(Dao dao, Sql sql, Pager pager) {
        pager.setRecordCount((int)Daos.queryCount(dao, sql.toString()));// 记录数需手动设置
        sql.setPager(pager);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        jsonobj.put("total", pager.getRecordCount());
        jsonobj.put("rows", sql.getList(Record.class));
        return Json.toJson(jsonobj);
    }

    /**
     * 取一个字段的一个值，对于多个字段或多个值，不适用此方法
     *
     * @param dao
     * @param sql
     * @return
     */
    public <T> String getStrRowValue(Dao dao, Sql sql) {
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    if (rsmd.getColumnType(1) == 2005) {
                        return Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(1)));
                    } else {
                        return Strings.sNull(rs.getString(1));
                    }
                }
                return "";
            }
        });
        dao.execute(sql);
        return sql.getString();

    }

    /**
     * 根据自定义条件查询数据库中满足条件的数据，返回字符串列表
     *
     * @param dao
     * @param sql
     * @return
     */
    public <T> List<String> getStrRowValues(Dao dao, Sql sql) {
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                List<String> list = new LinkedList<String>();
                String value = "";
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    if (rsmd.getColumnType(1) == 2005) {
                        value = Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(1)));
                    } else {
                        value = Strings.sNull(rs.getString(1));
                    }
                    list.add(value);
                }
                return list;
            }
        });
        dao.execute(sql);
        return sql.getList(String.class);

    }

    /**
     * 取一个或多个字段的值,返回列表类型，每个列表以数组类型存数据
     *
     * @param dao
     * @param sql
     * @return
     */
    public <T> List<List<String>> getMulRowValue(Dao dao, Sql sql) {
        final List<List<String>> value = new ArrayList<List<String>>();
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                ResultSetMetaData rsmd = null;
                if (rs != null) {
                    rsmd = rs.getMetaData();
                    int rowcount = rsmd.getColumnCount();
                    while (rs != null && rs.next()) {
                        List<String> rowvalue = new ArrayList<String>();
                        String temp = "";
                        for (int i = 0; i < rowcount; i++) {
                            if (rsmd.getColumnTypeName(i + 1).toUpperCase()
                                    .equals("CLOB")) {
                                temp = Strings.sNull(DBObject.getClobBody(rs,
                                        rsmd.getColumnName(i + 1)));
                            } else {
                                temp = Strings.sNull(rs.getString(i + 1));
                            }
                            rowvalue.add(temp);
                        }
                        value.add(rowvalue);
                    }
                }

                return null;
            }
        });
        dao.execute(sql);
        return value;

    }

    /**
     * 取一个或多个字段的值,返回列表类型，每个列表以Hashtabl存数据,通过String[] colname绑定字段名
     *
     * @param dao
     * @param sql
     * @param colname
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> List<Hashtable> getMulRowValue(Dao dao, Sql sql, String[] colname) {
        final List<Hashtable> value = new ArrayList<Hashtable>();
        final String[] col = colname;
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                ResultSetMetaData rsmd = null;
                if (rs != null) {
                    rsmd = rs.getMetaData();
                    int rowcount = rsmd.getColumnCount();
                    if (!"".equals(col) && rowcount == col.length) {
                        while (rs != null && rs.next()) {
                            String temp = "";
                            Hashtable<String, String> htable = new Hashtable<String, String>();
                            for (int i = 0; i < rowcount; i++) {
                                if (rsmd.getColumnTypeName(i + 1).toUpperCase()
                                        .equals("CLOB")) {
                                    temp = Strings.sNull(DBObject.getClobBody(rs,
                                            rsmd.getColumnName(i + 1)));
                                } else {
                                    temp = Strings.sNull(rs.getString(i + 1));
                                }
                                htable.put(col[i], temp);

                            }
                            value.add(htable);

                        }
                    } else if (col.length == rowcount - 1) {
                        while (rs != null && rs.next()) {
                            String temp = "";
                            Hashtable<String, String> htable = new Hashtable<String, String>();
                            for (int i = 0; i < rowcount; i++) {
                                if (rsmd.getColumnTypeName(i + 1).toUpperCase()
                                        .equals("CLOB")) {
                                    temp = Strings.sNull(DBObject.getClobBody(rs,
                                            rsmd.getColumnName(i + 1)));
                                } else {
                                    temp = Strings.sNull(rs.getString(i + 1));
                                }
                                if (i == rowcount - 1) {
                                    htable.put("rowid", temp);
                                } else {
                                    htable.put(col[i], temp);
                                }

                            }
                            value.add(htable);

                        }
                    } else {
                        System.out.println("错误:listPageJsonSql(dao,sql,colname) 查询语句字段数与绑定字段数不一致");
                    }
                }

                return null;
            }
        });
        dao.execute(sql);
        return value;

    }


    /**
     * 根据列名得到下一级栏目的下一个值
     *
     * @param dao
     * @param tableName
     * @param cloName
     * @param value
     * @return
     */
    public <T> String getSubMenuId(Dao dao, String tableName, String cloName,
                                   String value) {
        final String val = value;
        Sql sql = Sqls.create("select " + cloName + " from " + tableName
                + " where " + cloName + " like '" + value + "____' order by "
                + cloName + " desc");
        sql.setCallback(new SqlCallback() {
            @Override
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
        dao.execute(sql);
        return sql.getString();

    }

    /**
     * 根据列名得到下一级栏目的下一个值
     *
     * @param dao
     * @param tableName
     * @param cloName
     * @param value
     * @return
     */
    public <T> String getSubMenuId(Dao dao, String tableName, String cloName,
                                   String value, String wheresql) {
        final String val = value;
        Sql sql = Sqls.create("select " + cloName + " from " + tableName
                + " where " + cloName + " like '" + value + "____' " + wheresql
                + " order by " + cloName + " desc");
        sql.setCallback(new SqlCallback() {
            @Override
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
        dao.execute(sql);
        return sql.getString();

    }

    /**
     * 通过查询条件获得Hashtable
     *
     * @param sql
     * @return
     */
    public <T> Hashtable<String, String> getHTable(Dao dao, Sql sql) {
        final Hashtable<String, String> htable = new Hashtable<String, String>();
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                String key = "", value = "";
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    if (rsmd.getColumnType(1) == 2005) {
                        key = Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(1)));
                    } else {
                        key = Strings.sNull(rs.getString(1));
                    }
                    if (rsmd.getColumnType(2) == 2005) {
                        value = Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(2)));
                    } else {
                        value = Strings.sNull(rs.getString(2));
                    }
                    htable.put(key, value);
                }
                return null;
            }
        });
        dao.execute(sql);
        return htable;
    }

    /**
     * 通过查询条件获得Hashtable
     *
     * @param sql
     * @return
     */
    public <T> HashMap<String, String> getHashMap(Dao dao, Sql sql) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                String key = "", value = "";
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    if (rsmd.getColumnType(1) == 2005) {
                        key = Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(1)));
                    } else {
                        key = Strings.sNull(rs.getString(1));
                    }
                    if (rsmd.getColumnType(2) == 2005) {
                        value = Strings.sNull(DBObject.getClobBody(rs,
                                rsmd.getColumnName(2)));
                    } else {
                        value = Strings.sNull(rs.getString(2));
                    }
                    hashMap.put(key, value);
                }
                return null;
            }
        });
        dao.execute(sql);
        return hashMap;
    }

}
