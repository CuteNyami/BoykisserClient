package net.luconia.boykisser.internal;

import java.lang.annotation.*;

/**
 * Used to say that it's an internal function
 * @author Nyami
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE,
        ElementType.FIELD, ElementType.PACKAGE})
@Inherited
@Documented
public @interface Internal {
    String value() default "";
}
