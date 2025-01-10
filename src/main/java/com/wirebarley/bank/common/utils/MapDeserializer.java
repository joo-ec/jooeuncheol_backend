package com.wirebarley.bank.common.utils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class MapDeserializer implements JsonDeserializer {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return obj.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey, entry -> ParseObjectFromElement.SINGLETON.apply(entry.getValue())));
    }

    public enum ParseObjectFromElement implements Function {
        SINGLETON;
        @Nullable
        @Override
        public Object apply(Object obj) {
            Object value = null;
            JsonElement input = null;

            if( obj != null ) input = (JsonElement) obj;

            if (input == null || input.isJsonNull()) {
                value = null;
            } else if (input.isJsonPrimitive()) {
                JsonPrimitive primitive = input.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    value = primitive.getAsInt(); // Number 값은 무조건 integer로 처리
                } else if (primitive.isBoolean()) {
                    value = primitive.getAsBoolean();
                } else {
                    value = primitive.getAsString();
                }
            } else if (input.isJsonArray()) {
                value = Lists.newArrayList(Iterables.transform(input.getAsJsonArray(), this));
            } else if (input.isJsonObject()) {
                value = Maps. newLinkedHashMap(
                        Maps.transformValues(JsonObjectAsMap.INSTANCE.apply(input.getAsJsonObject()), this));
            }
            return value;
        }
    }

    ;public enum JsonObjectAsMap implements Function {
        INSTANCE;

        private final Field members;

        JsonObjectAsMap() {
            try {
                members = JsonObject.class.getDeclaredField("members");
                members.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new UnsupportedOperationException("cannot access gson internals", e);
            }
        }

        @Nullable
        @Override
        public Map apply(@Nullable Object o) {
            try {
                return (Map) members.get(o);
            } catch (IllegalArgumentException e) {
                throw new UnsupportedOperationException("cannot access gson internals", e);
            } catch (IllegalAccessException e) {
                throw new UnsupportedOperationException("cannot access gson internals", e);
            }
        }

    }

}