package com.qingcloud.saas.annotation;


import java.lang.annotation.*;


/**
 * @author Alex
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpiLog {

}
