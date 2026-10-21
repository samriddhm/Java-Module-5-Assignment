import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Q26: a small JSON serializer driven by @JsonField (no external libraries).
 * Supports: null, String, char, numbers, booleans, enums, arrays, Collections, Maps,
 * and nested objects that also use @JsonField. Output is pretty-printed.
 */
public final class JsonSerializer {

    private JsonSerializer() { }

    public static String toJson(Object obj) {
        StringBuilder sb = new StringBuilder();
        write(obj, sb, 0);
        return sb.toString();
    }

    private static void write(Object value, StringBuilder sb, int indent) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof String || value instanceof Character || value instanceof Enum) {
            sb.append('"').append(escape(value.toString())).append('"');
        } else if (value instanceof Number || value instanceof Boolean) {
            sb.append(value);
        } else if (value instanceof Map) {
            writeMap((Map<?, ?>) value, sb, indent);
        } else if (value instanceof Collection) {
            writeArray(((Collection<?>) value).iterator(), sb, indent);
        } else if (value instanceof Object[]) {
            writeArray(java.util.Arrays.asList((Object[]) value).iterator(), sb, indent);
        } else {
            writeObject(value, sb, indent);
        }
    }

    /** The annotation-driven part: only @JsonField fields, using the annotation's name. */
    private static void writeObject(Object obj, StringBuilder sb, int indent) {
        sb.append("{");
        boolean first = true;
        for (Field field : obj.getClass().getDeclaredFields()) {
            JsonField jf = field.getAnnotation(JsonField.class);
            if (jf == null || Modifier.isStatic(field.getModifiers())) continue;   // not marked -> skip

            Object fieldValue;
            try {
                field.setAccessible(true);
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot read " + field.getName(), e);
            }
            if (fieldValue == null && !jf.includeNull()) continue;

            String key = jf.value().isEmpty() ? field.getName() : jf.value();
            sb.append(first ? "\n" : ",\n");
            first = false;
            indent(sb, indent + 1);
            sb.append('"').append(escape(key)).append("\": ");
            write(fieldValue, sb, indent + 1);
        }
        if (!first) { sb.append("\n"); indent(sb, indent); }
        sb.append("}");
    }

    private static void writeMap(Map<?, ?> map, StringBuilder sb, int indent) {
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            sb.append(first ? "\n" : ",\n");
            first = false;
            indent(sb, indent + 1);
            sb.append('"').append(escape(String.valueOf(e.getKey()))).append("\": ");
            write(e.getValue(), sb, indent + 1);
        }
        if (!first) { sb.append("\n"); indent(sb, indent); }
        sb.append("}");
    }

    private static void writeArray(Iterator<?> it, StringBuilder sb, int indent) {
        sb.append("[");
        boolean first = true;
        while (it.hasNext()) {
            if (!first) sb.append(", ");
            first = false;
            write(it.next(), sb, indent);
        }
        sb.append("]");
    }

    private static void indent(StringBuilder sb, int level) {
        sb.append("  ".repeat(level));
    }

    /** Escapes characters that are not allowed raw inside JSON strings. */
    private static String escape(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c < 0x20) out.append(String.format("\\u%04x", (int) c));
                    else out.append(c);
            }
        }
        return out.toString();
    }
}
