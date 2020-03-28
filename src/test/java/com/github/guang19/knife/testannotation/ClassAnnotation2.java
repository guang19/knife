package com.github.guang19.knife.testannotation;

import java.lang.annotation.*;

/**
 * @author yangguang
 * @date 2020/3/28
 * @description <p></p>
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ClassAnnotation2
{
}
