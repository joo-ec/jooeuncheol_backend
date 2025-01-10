package com.wirebarley.bank.common.utils;

import com.google.gson.*;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Type;

public class HibernateProxyTypeAdapter implements JsonSerializer<HibernateProxy>, JsonDeserializer<HibernateProxy> {
    @Override
    public JsonElement serialize(HibernateProxy src, Type typeOfSrc, JsonSerializationContext context) {
        // 프록시 객체를 null로 직렬화
        return JsonNull.INSTANCE;
    }

    @Override
    public HibernateProxy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        throw new UnsupportedOperationException("Deserialization not supported for HibernateProxy");
    }
}