package com.anxin.kitchen.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xujianjun on 2018/4/6.
 */

public class JsonHandler {
    private static JsonHandler handler;

    private JsonHandler() {
    }

    public static synchronized JsonHandler getHandler() {
        Class var0 = JsonHandler.class;
        synchronized (JsonHandler.class) {
            if (handler == null) {
                handler = new JsonHandler();
            }

            return handler;
        }
    }

    public String toJson(Object value) {
        Gson gson = new Gson();
        return gson.toJson(value);
    }

    public <T> T getTarget(String jsonString, Class<T> cls) {
        try {
            Gson e = new Gson();
            return e.fromJson(jsonString, cls);
        } catch (Exception var4) {
            System.err.println("错误：" + var4.getMessage());
            return null;
        }
    }

    public <T> List<T> getListTarget(String jsonString, Type type) {
        try {
            GsonBuilder e = new GsonBuilder();
            e.excludeFieldsWithoutExposeAnnotation();
            Gson gson = e.create();
            return (List) gson.fromJson(jsonString, type);
        } catch (Exception var5) {
            System.err.println("错误：" + var5.getMessage());
            return null;
        }
    }
}
