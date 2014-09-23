package cn.xuetang.modules.sys.bean;

import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.DB;

/**
 * @author Wizzer
 * @time 2014-02-27 10:01:23
 */
@Table("SYS_TASK")
public class Sys_task {
    @Column
    @Id
//    @Prev({
//            @SQL(db = DB.ORACLE, value = "SELECT SYS_TASK_S.nextval FROM dual")
//    })
    private int task_id;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String task_code;
    @Column
    private int task_type;
    @Column
    private String task_name;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String job_class;
    @Column
    private int execycle;
    @Column
    private int day_of_month;
    @Column
    private int day_of_week;
    @Column
    private int hour;
    @Column
    private int minute;
    @Column
    private int interval_hour;
    @Column
    private int interval_minute;
    @Column
    private int task_interval_unit;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String cron_expression;
    @Column
    private int is_enable;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String task_remark;
    @Column
    private long user_id;
    @Column
    private String create_time;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String param_value;
    @Column
    private int task_interval;
    @Column
    private int task_threadnum;
    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getJob_class() {
        return job_class;
    }

    public void setJob_class(String job_class) {
        this.job_class = job_class;
    }

    public int getExecycle() {
        return execycle;
    }

    public void setExecycle(int execycle) {
        this.execycle = execycle;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getInterval_hour() {
        return interval_hour;
    }

    public void setInterval_hour(int interval_hour) {
        this.interval_hour = interval_hour;
    }

    public int getInterval_minute() {
        return interval_minute;
    }

    public void setInterval_minute(int interval_minute) {
        this.interval_minute = interval_minute;
    }

    public int getTask_interval_unit() {
        return task_interval_unit;
    }

    public void setTask_interval_unit(int task_interval_unit) {
        this.task_interval_unit = task_interval_unit;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public int getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(int is_enable) {
        this.is_enable = is_enable;
    }

    public String getTask_remark() {
        return task_remark;
    }

    public void setTask_remark(String task_remark) {
        this.task_remark = task_remark;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getParam_value() {
        return param_value;
    }

    public void setParam_value(String param_value) {
        this.param_value = param_value;
    }

    public int getTask_interval() {
        return task_interval;
    }

    public void setTask_interval(int task_interval) {
        this.task_interval = task_interval;
    }

    public int getTask_threadnum() {
        return task_threadnum;
    }

    public void setTask_threadnum(int task_threadnum) {
        this.task_threadnum = task_threadnum;
    }
}