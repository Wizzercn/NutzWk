package cn.wizzer.app.code.commons.builder;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.Ioc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据数据库结构生成model,service,controller,views
 * Created by wizzer on 2017/1/23.
 */
public class TableDescLoader extends Loader {


    @Override
    public Map<String, TableDescriptor> loadTables(Ioc ioc,
                                                   String basePackageName, String basePath, String baseUri, String servPackageName, String modPackageName) throws SQLException {


        DataSource ds = ioc.get(DataSource.class);
        Dao dao = new NutDao(ds);
        Sql sql = Sqls.create("select database()");

        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                if (rs.next()) {
                    return rs.getString(1);
                }
                return null;
            }
        });
        dao.execute(sql);
        String database = sql.getString();


        Sql tableSchemaSql = Sqls.create("select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA = '"
                + database + "'");

        tableSchemaSql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        record.put(columnName, rs.getObject(columnName));
                    }
                    result.add(record);


                }
                return result;
            }
        });
        dao.execute(tableSchemaSql);


        List<Map> columns = tableSchemaSql.getList(Map.class);

        Map<String, TableDescriptor> tables = new HashMap<String, TableDescriptor>();
        for (Map<String, Object> columnInfo : columns) {
            String tableName = (String) columnInfo.get("TABLE_NAME");

            ColumnDescriptor column = new ColumnDescriptor();
            column.columnName = (String) columnInfo.get("COLUMN_NAME");
            if ("opAt".equals(column.columnName) || "opBy".equals(column.columnName) || "delFlag".equals(column.columnName)) {
                continue;
            }
            column.setDefaultValue(columnInfo.get("COLUMN_DEFAULT"));
            column.dataType = (String) columnInfo.get("DATA_TYPE");
            column.nullable = "YES".equals(columnInfo.get("IS_NULLABLE"));
            column.primary = "PRI".equals(columnInfo.get("COLUMN_KEY"));

            String columnType = (String) columnInfo.get("COLUMN_TYPE");
            column.setColumnType(columnType);
            column.setComment((String) columnInfo.get("COLUMN_COMMENT"));

            TableDescriptor table = tables.get(tableName);
            if (table == null) {
                table = new TableDescriptor(tableName, null, basePackageName, baseUri, servPackageName, modPackageName);
                tables.put(tableName, table);
            }
            if (column.primary) {
                table.setPkType(column.getSimpleJavaTypeName());
            }
            table.addColumn(column);
        }
        Sql infomationSchemaSql = Sqls.create("select * from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '"
                + database + "'");
        infomationSchemaSql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        record.put(columnName, rs.getObject(columnName));
                    }
                    result.add(record);


                }
                return result;
            }
        });
        dao.execute(infomationSchemaSql);


        List<Map> tableInfos = infomationSchemaSql.getList(Map.class);

        for (Map<String, Object> tableInfo : tableInfos) {
            String tableName = (String) tableInfo.get("TABLE_NAME");
            String comment = (String) tableInfo.get("TABLE_COMMENT");

            TableDescriptor table = tables.get(tableName);
            if (table != null) {
                table.setComment(comment);
            }
        }


        return tables;
    }
}
