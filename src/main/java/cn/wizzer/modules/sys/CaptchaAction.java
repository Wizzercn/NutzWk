package cn.wizzer.modules.sys;

import cn.wizzer.common.util.Toolkit;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

import org.nutz.mvc.annotation.Param;

import cn.apiclub.captcha.Captcha;
/**
 * Created by Wizzer.cn on 2015/6/29.
 */
@IocBean
@At("/captcha")
public class CaptchaAction{
    @At
    @Ok("raw:png")
    public BufferedImage next(HttpSession session, @Param("w") int w, @Param("h") int h) {
        if (w * h < 1) { //长或宽为0?重置为默认长宽.
            w = 200;
            h = 60;
        }
        Captcha captcha = new Captcha.Builder(w, h)
                .addText()
//								.addBackground(new GradiatedBackgroundProducer())
//								.addNoise(new StraightLineNoiseProducer()).addBorder()
//								.gimp(new FishEyeGimpyRenderer())
                .build();
        String text = captcha.getAnswer();
        session.setAttribute(Toolkit.captcha_attr, text);
        return captcha.getImage();
    }
}
