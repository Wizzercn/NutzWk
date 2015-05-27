package cn.wizzer.common.util;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@SuppressWarnings("restriction")
public class ImageUtil extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "image/jpeg;charset=GB2312";
    private static final String allcode[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9"};

    private static final Font mFont = new Font("Times New Roman", Font.BOLD, 18);// 设置字体

    public static Font IFont = new Font("宋体", Font.PLAIN, 18);// 设置字体

    public static int x = 10; // 坐标
    public static int y = 10;

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);
        ServletOutputStream out = response.getOutputStream();
        getImage(out, request, response);
        out.close();
    }

    private void getImage(ServletOutputStream out, HttpServletRequest request,
                          HttpServletResponse response) {
        try {

            HttpSession session = request.getSession(true);

            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            int width = 60, height = 20;

            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB); // 设置图片大小的
            Graphics gra = image.getGraphics();
            Random random = new Random();

            gra.setColor(getRandColor(200, 250)); // 设置背景色
            gra.fillRect(0, 0, width, height);

            gra.setColor(Color.black); // 设置字体色
            gra.setFont(mFont);

            // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到

            for (int i = 0; i < 100; i++) {
                gra.setColor(getRandColor(120, 220));
                x = random.nextInt(width);
                y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                gra.drawLine(x, y, x + xl, y + yl);
            }

            // 取随机产生的认证码(4位)
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                String rand = allcode[random.nextInt(10)];
                sRand += rand;
                // 将认证码显示到图象中
                gra.setColor(new Color(20 + random.nextInt(110), 20 + random
                        .nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                gra.drawString(rand, 13 * i + 6, 12 + random.nextInt(5));
            }
            session.setAttribute("ValidateCode", sRand);
            try {
                ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色

        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static Font getIFont() {
        return IFont;
    }

    public static void setIFont(Font IFont) {
        ImageUtil.IFont = IFont;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        ImageUtil.y = y;
    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        ImageUtil.x = x;
    }

}
