package cn.wizzer.app.web.commons.slog.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SLog {
    String type() default "platform";
    /**
     * 标签
     *
     * @return
     */
    String tag();

    String msg() default "";

    /**
     * 是否记录传递参数
     *
     * @return 消息模板
     */
    boolean param() default false;

    /**
     * 记录执行结果
     *
     * @return 消息模板
     */
    boolean result() default false;

    /**
     * 是否异步执行,默认为true
     *
     * @return true, 如果需要异步执行
     */
    boolean async() default true;
}