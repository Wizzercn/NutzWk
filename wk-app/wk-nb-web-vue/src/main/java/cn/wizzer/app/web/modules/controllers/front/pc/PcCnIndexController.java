package cn.wizzer.app.web.modules.controllers.front.pc;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * 中文站点演示
 * Created by wizzer on 2018/11/11.
 */
@IocBean
@At("/public/pc/cn")
public class PcCnIndexController {

    @At(value = {"/index"}, top = false)
    @Ok("beetl:/public/pc/cn/index/index.html")
    public void index(@Param(value = "page", df = "1") int page, @Param(value = "size", df = "15") int size, @Param(value = "startDate", df = "") String startDate, @Param(value = "endDate", df = "") String endDate, HttpServletRequest req) {
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);
        req.setAttribute("pageNumber", page);
        req.setAttribute("pageSize", size);
    }
}
