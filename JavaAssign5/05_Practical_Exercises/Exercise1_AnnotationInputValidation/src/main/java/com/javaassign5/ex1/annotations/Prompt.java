package com.javaassign5.ex1.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** UI metadata: the question shown to the user and the order of the questions. */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Prompt {
    /** Text shown to the user. */
    String value();
    /** Lower numbers are asked first. */
    int order();
}
