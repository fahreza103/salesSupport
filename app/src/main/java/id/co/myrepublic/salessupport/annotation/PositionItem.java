package id.co.myrepublic.salessupport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import id.co.myrepublic.salessupport.constant.RowItem;

/**
 * Used to define text position on CommonRowAdapter
 * it can be mainText, mainText2, subText, or subText2
 *
 * @author Fahreza Tamara
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PositionItem {
    /**
     * Type of the position
     * @return
     */
    RowItem type();

    /**
     * Text after rowitem value
     * @return
     */
    String postfix() default "";

    /**
     * Text before rowitem value
     * @return
     */
    String prefix() default "";
}
