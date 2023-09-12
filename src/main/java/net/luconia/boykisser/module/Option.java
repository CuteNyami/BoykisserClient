package net.luconia.boykisser.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Option annotation
 * <br>
 * Used for module settings
 * @author Nyami
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {

    /**
     * Display name
     * <br>
     * Used to show the setting with its name in
     * the mod menu/settings menu
     */
    String displayName();

    /**
     * Placeholder
     * <br>
     * Used for input boxes
     * <p>Note: the field has to be a {@link String}</p>
     */
    String placeholder() default "";

    /**
     * Min slider value
     */
    double min() default 0;

    /**
     * Max slider value
     */
    double max() default 1;

    /**
     * Let the user change the alpha value of a color
     * <br>
     * Used for color pickers
     */
    boolean alpha() default true;
}
