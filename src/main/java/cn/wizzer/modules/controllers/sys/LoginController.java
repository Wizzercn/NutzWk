package cn.wizzer.modules.controllers.sys;

import cn.apiclub.captcha.Captcha;
import cn.wizzer.modules.services.sys.UserService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/private/login") // 整个模块的路径前缀
@Ok("json:{locked:'password|createAt',ignoreNull:true}") // 忽略password和createAt属性,忽略空属性的json输出
public class LoginController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @At("")
    @Ok("vm:views.private.login")
    @Filters
    public void login() {
    }

    @At("/captcha")
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
        session.setAttribute("captcha", text);
        return captcha.getImage();
    }
}
