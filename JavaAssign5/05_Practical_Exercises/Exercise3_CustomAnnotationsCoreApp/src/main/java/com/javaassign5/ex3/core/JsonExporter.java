package com.javaassign5.ex3.core;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaassign5.ex3.annotations.JsonName;

/**
 * Connects OUR custom annotation with a LIBRARY managed by Maven:
 * Gson asks this FieldNamingStrategy for every field name, and we answer with @JsonName.
 */
public final class JsonExporter {

    /** Uses @JsonName if present, otherwise the Java field name. */
    static final FieldNamingStrategy JSON_NAME_STRATEGY = field -> {
        JsonName name = field.getAnnotation(JsonName.class);
        return name != null ? name.value() : field.getName();
    };

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingStrategy(JSON_NAME_STRATEGY)
            .setPrettyPrinting()
            .create();

    private JsonExporter() { }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }
}
