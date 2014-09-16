package cn.xuetang.common.task.job;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.util.*;
import cn.xuetang.modules.app.bean.App_info;
import cn.xuetang.modules.user.bean.User_conn_wx;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.*;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 获取微信用户资料
 * Created by Wizzer on 14-4-2.
 */
@IocBean
public class UserInfoJob extends BaseAction implements Job {
    @Inject
    protected Dao dao;
    @Inject
    protected WeixinUtil weixinUtil;
    @Inject
    protected UrlUtil urlUtil;
    private final static Log log = Logs.get();
    private static String task_code = "";
    private static String param_value = "";
    private static int task_interval = 0;
    private static int task_id = 0;
    private static int task_threadnum = 1;
    private static int pageSize = 20;
    private ExecutorService pool;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jdm = context.getJobDetail().getJobDataMap();
            param_value = Strings.sNull(jdm.get("param_value"));
            task_code = Strings.sNull(jdm.get("task_code"));
            task_interval = NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) : 1000;
            task_id = NumberUtils.toInt(Strings.sNull(jdm.get("task_id")));
            task_threadnum = NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) : 1;
            log.info("PushDataJob param_value:" + param_value + " task_code:" + task_code + " task_interval:" + task_interval + " task_threadnum:" + task_threadnum + " task_id:" + task_id);
            pageSize = NumberUtils.toInt(param_value) > 0 ? NumberUtils.toInt(param_value) : 20;
            init();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void init() {
        pool = Executors.newFixedThreadPool(task_threadnum);
        List<App_info> infoList = daoCtl.list(dao, App_info.class, Cnd.where("1", "=", 1));
        for (App_info info : infoList) {
            List<User_conn_wx> list = daoCtl.listPage(dao, User_conn_wx.class, 1, pageSize, Cnd.where("status", "=", 0).and("appid", "=", info.getId()).asc("id")).getList(User_conn_wx.class);
            String access_token = weixinUtil.getGloalsAccessToken(dao, info.getId());
            for (User_conn_wx wx : list) {
                pool.execute(new UserThread(access_token, wx));
            }
        }
    }

    class UserThread extends Thread {
        private String access_token;
        private User_conn_wx wx;

        public UserThread(String access_token, User_conn_wx wx) {
            this.access_token = access_token;
            this.wx = wx;
        }

        public void run() {
            try {
                Thread.sleep(task_interval);
                String res = urlUtil.getOneHtml("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + wx.getOpenid() + "&lang=zh_CN", "UTF-8");
                Map<String, Object> map = Json.fromJson(Map.class, res);
                if (map.get("errcode") == null) {
                    log.info("nickname:" + Strings.sNull(map.get("nickname")));
                    wx.setWx_nickname(EmojiFilter.filterEmoji(Strings.sNull(map.get("nickname"))));
                    wx.setWx_sex(NumberUtils.toInt(Strings.sNull(map.get("sex"))));
                    wx.setWx_city(Strings.sNull(map.get("city")));
                    wx.setWx_country(Strings.sNull(map.get("country")));
                    wx.setWx_province(Strings.sNull(map.get("province")));
                    wx.setWx_headimgurl(Strings.sNull(map.get("headimgurl")));
                    wx.setWx_time(DateUtil.long2DateStr(NumberUtils.toLong(Strings.sNull(map.get("subscribe_time")))));
                    wx.setStatus(1);
                    daoCtl.update(dao, wx);
                } else {
                    wx.setStatus(2);
                    daoCtl.update(dao, wx);
                    log.info("UserInfoJob.getInfo:" + map.get("errmsg"));
                }
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
