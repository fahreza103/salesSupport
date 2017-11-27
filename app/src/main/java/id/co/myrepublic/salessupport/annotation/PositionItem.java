package id.co.myrepublic.salessupport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define text position on CommonRowAdapter
 * it can be mainText, mainText2, subText, or subText2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PositionItem {
    /**
     * Type of the position
     * @return
     */
    String type();

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
