package com.bgi.vtx.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String id() default "";
    String tag() default "";
    String value() default "";
}
