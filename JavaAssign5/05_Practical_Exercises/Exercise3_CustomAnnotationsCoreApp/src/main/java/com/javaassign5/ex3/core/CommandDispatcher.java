package com.javaassign5.ex3.core;

import com.javaassign5.ex3.annotations.CliCommand;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Finds every @CliCommand method on a handler object (reflection) and calls the right one
 * for each line the user types. The help text is built from the annotations, too.
 */
public class CommandDispatcher {

    private final Object handler;
    private final Map<String, Method> commands = new TreeMap<>();

    public CommandDispatcher(Object handler) {
        this.handler = handler;
        for (Method m : handler.getClass().getDeclaredMethods()) {
            CliCommand cmd = m.getAnnotation(CliCommand.class);
            if (cmd == null) continue;
            if (m.getParameterCount() != 1 || m.getParameterTypes()[0] != String[].class
                    || m.getReturnType() != String.class) {
                throw new IllegalStateException("@CliCommand method " + m.getName()
                        + " must look like: String name(String[] args)");
            }
            if (commands.put(cmd.name(), m) != null) {
                throw new IllegalStateException("duplicate command name: " + cmd.name());
            }
        }
    }

    /** Runs one command line and returns the text to print. */
    public String dispatch(String line) {
        List<String> parts = tokenize(line);
        if (parts.isEmpty()) return "";
        String name = parts.get(0).toLowerCase();
        String[] args = parts.subList(1, parts.size()).toArray(new String[0]);

        if (name.equals("help")) return help();
        Method m = commands.get(name);
        if (m == null) return "Unknown command '" + name + "'. Type 'help'.";

        CliCommand cmd = m.getAnnotation(CliCommand.class);
        if (args.length < cmd.minArgs()) return "Usage: " + cmd.usage();
        try {
            return (String) m.invoke(handler, (Object) args);
        } catch (InvocationTargetException e) {
            return "Error: " + e.getCause().getMessage();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Help screen generated 100% from @CliCommand values. */
    public String help() {
        StringBuilder sb = new StringBuilder("Commands:\n");
        for (Method m : commands.values()) {
            CliCommand c = m.getAnnotation(CliCommand.class);
            String usage = c.usage().isEmpty() ? c.name() : c.usage();
            sb.append("  ").append(StringUtils.rightPad(usage, 28)).append(c.description()).append('\n');
        }
        sb.append("  ").append(StringUtils.rightPad("help", 28)).append("show this list\n");
        sb.append("  ").append(StringUtils.rightPad("exit", 28)).append("quit (interactive mode)");
        return sb.toString();
    }

    /** Splits on spaces but keeps "quoted text" together. */
    static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean quoted = false;
        for (char ch : line.trim().toCharArray()) {
            if (ch == '"') {
                quoted = !quoted;
            } else if (Character.isWhitespace(ch) && !quoted) {
                if (cur.length() > 0) { out.add(cur.toString()); cur.setLength(0); }
            } else {
                cur.append(ch);
            }
        }
        if (cur.length() > 0) out.add(cur.toString());
        return out;
    }
}
