package cn.wizzer.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SLog {
    String tag();

    String msg();

    boolean before() default true;

    boolean after() default false;

    boolean error() default true;

    boolean async() default true;
}