package com.javaassign5.ex1.validation;

/** One failed validation rule: which field, which rule, and a readable message. */
public final class Violation {

    private final String field;
    private final String rule;
    private final String message;

    public Violation(String field, String rule, String message) {
        this.field = field;
        this.rule = rule;
        this.message = message;
    }

    public String getField()   { return field; }
    public String getRule()    { return rule; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return field + " " + message + " [@" + rule + "]";
    }
}
