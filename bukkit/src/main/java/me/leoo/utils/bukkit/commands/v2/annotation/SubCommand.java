package me.leoo.utils.bukkit.commands.v2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    String parent() default "";

    String[] value() default {};

    CommandExecutor executor() default CommandExecutor.PLAYER;

    boolean confirmation() default false;
}
