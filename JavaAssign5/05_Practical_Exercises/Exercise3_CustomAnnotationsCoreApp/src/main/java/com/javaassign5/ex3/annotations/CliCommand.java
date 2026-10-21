package com.javaassign5.ex3.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Turns a method into a command of the Task Tracker. The help screen is generated from these values. */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CliCommand {
    /** The word the user types, e.g. "add". */
    String name();
    /** How to call it, e.g. "add &lt;priority&gt; &lt;title...&gt;". */
    String usage() default "";
    /** One-line explanation for the help screen. */
    String description();
    /** Minimum number of arguments. */
    int minArgs() default 0;
}
