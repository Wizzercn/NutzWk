package cn.wizzer.common.service;

import org.apache.commons.mail.HtmlEmail;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 邮件发送
 * Created by Wizzer.cn on 2015/6/29.
 */
@IocBean
public class EmailService {
    private static final Log log = Logs.get();

    @Inject("refer:$ioc")
    protected Ioc ioc;

    public boolean send(String to, String subject, String html) {
        try {
            HtmlEmail email = ioc.get(HtmlEmail.class);
            email.setSubject(subject);
            email.setHtmlMsg(html);
            email.addTo(to);
            email.send();
            return true;
        } catch (Throwable e) {
            log.info("send email fail", e);
            return false;
        }
    }
}
