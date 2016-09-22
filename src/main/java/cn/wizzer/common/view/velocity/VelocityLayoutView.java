package cn.wizzer.common.view.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.util.SimplePool;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.config.AtMap;
import org.nutz.mvc.view.AbstractPathView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class VelocityLayoutView extends AbstractPathView {

    private static final Log log = Logs.get();
    protected static final int WRITER_BUFFER_SIZE = 8 * 1024;
    protected SimplePool writerPool = new SimplePool(40);
    private final static String SUFFIX = ".html";


    public VelocityLayoutView(String dest) {
        super(dest);
    }

    public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
        String path = evalPath(req, obj);
        resp.setContentType("text/html;charset=\"UTF-8\"");
        resp.setCharacterEncoding("UTF-8");
        try {
            StringWriter sw = new StringWriter();
            Context context = new VelocityContext();
            context.put("obj", obj);
            context.put("base", req.getContextPath());
            context.put("request", req);
            context.put("session", req.getSession());
            //context.put("cookie", CookieUtils.getCookie(req,resp));
            //请求的参数表,需要兼容之前的p.参数, Fix issue 418
            Map<String, String> p = new HashMap<String, String>();
            for (Object o : req.getParameterMap().keySet()) {
                String key = (String) o;
                String value = req.getParameter(key);
                p.put(key, value);
                context.put(key, value);// 以支持直接获取请求参数
            }
            context.put("p", p);

            Map<String, String> u = new HashMap<String, String>();
            AtMap at = Mvcs.getAtMap();
            if (at != null) {
                for (Object o : at.keys()) {
                    String key = (String) o;
                    u.put(key, at.get(key));
                }
                context.put("u", u);
            }
            Enumeration<?> reqs = req.getAttributeNames();
            while (reqs.hasMoreElements()) {
                String strKey = (String) reqs.nextElement();
                context.put(strKey, req.getAttribute(strKey));
            }
            Template template = Velocity.getTemplate(getPath(path));
            template.merge(context, sw);
            internalRenderTemplate(template, context, resp.getWriter());
        } catch (Exception e) {
            log.error("velocity error", e);
            throw new Exception(e);
        }
    }

    private String getPath(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(("/WEB-INF/views"));
        // 空路径，采用默认规则
        if (Strings.isBlank(path)) {
            sb.append(Mvcs.getServletContext().getRealPath("WEB-INF"));
            sb.append((path.startsWith("/") ? "" : "/"));
            sb.append(Files.renameSuffix(path, SUFFIX));
        }
        // 绝对路径 : 以 '/' 开头的路径不增加 '/WEB-INF'
        else if (path.charAt(0) == '/') {
            String ext = SUFFIX;
            sb.append(path);
            if (!path.toLowerCase().endsWith(ext))
                sb.append(ext);
        }
        // 包名形式的路径
        else {
            sb.append(path.replace('.', '/'));
            sb.append(SUFFIX);
        }
        return sb.toString();
    }

    private void internalRenderTemplate(Template template, Context context, Writer writer) throws Exception {
        VelocityWriter velocityWriter = null;
        try {
            velocityWriter = (VelocityWriter) writerPool.get();
            if (velocityWriter == null) {
                velocityWriter = new VelocityWriter(writer, WRITER_BUFFER_SIZE, true);
            } else {
                velocityWriter.recycle(writer);
            }
            template.merge(context, velocityWriter);
        } catch (Exception pee) {
            throw new Exception(pee);
        } finally {
            if (velocityWriter != null) {
                velocityWriter.flush();
                velocityWriter.recycle(null);
                writerPool.put(velocityWriter);
            }
            writer.flush();
            writer.close();
        }
    }
}
