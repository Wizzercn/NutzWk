package cn.wizzer.app.task.commons.ext.quartz;

import org.nutz.boot.AppContext;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.Mvcs;
import org.quartz.utils.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class NutConnectionProvider implements ConnectionProvider {
    
    protected DataSource dataSource;
    protected String iocname = "dataSource";

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() throws SQLException {}

    @SuppressWarnings("deprecation")
    public void initialize() throws SQLException {
        if (dataSource != null)
            return;
        Ioc ioc = AppContext.getDefault().getIoc();
        dataSource = ioc.get(DataSource.class, iocname);
    }

}
