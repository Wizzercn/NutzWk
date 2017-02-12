package cn.wizzer.app.web.commons.slog.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SLog {
    String tag();

    String msg();

    boolean before() default false;

    boolean after() default true;

    boolean error() default false;

    boolean async() default true;
}