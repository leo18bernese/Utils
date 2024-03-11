package me.leoo.utils.bukkit.commands.v2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {

    /**
     * Can be the default value only for strings, otherwise it need a specific value
     * Example: @Optional("10")
     * Example: @Optional("true")
     *
     * @return the default value
     */
    String value() default "";

}
