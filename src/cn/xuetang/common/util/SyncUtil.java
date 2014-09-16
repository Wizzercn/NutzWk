package cn.xuetang.common.util;

import cn.xuetang.common.config.Globals;
import org.apache.commons.lang.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 前台负载均衡部署时，同步更新全局参数
 * Created by Wizzer on 14-5-21.
 */
@IocBean
public class SyncUtil {
    private final static Log log = Logs.get();
    @Inject
    protected UrlUtil urlUtil;

    public void sendMsg(String type) {
        String urls = Strings.sNull(Globals.SYS_CONFIG.get("sync_url"));
        String key = Strings.sNull(Globals.SYS_CONFIG.get("sync_key"));
        String[] url = StringUtils.split(urls, ",");
        for (int i = 0; i < url.length; i++) {
            SendUtil sendUtil = new SendUtil(type, key, url[i]);
            sendUtil.start();
        }
    }

    class SendUtil extends Thread {
        private String type;
        private String url;
        private String key;

        public SendUtil(String t, String k, String u) {
            this.type = t;
            this.key = k;
            this.url = u;
        }

        public void run() {
            try {
                urlUtil.getOneHtml(url + "?key=" + key + "&type=" + type, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }
    }
}
