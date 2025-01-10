package com.wirebarley.bank.common.utils;

import com.google.gson.*;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class JsonUtils {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SS")
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(Map.class, new MapDeserializer())
            .registerTypeAdapter(List.class, new ListDeserializer())
            .registerTypeAdapter(HibernateProxy.class, new HibernateProxyTypeAdapter())
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();


    public static <T> String toJson(T source) {
        return gson.toJson(source);
    }

    public static <T> String toJson(T source, Class<T> targetClass) {
        return gson.toJson(source, targetClass);
    }

    public static <T> T toOrigin(String source, Class<T> targetClass) {
        return gson.fromJson(source, targetClass);
    }

    public static <T> T convert(Object source, Class<T> targetClass) {
        JsonElement jsonElement = gson.toJsonTree(source);
        return gson.fromJson(jsonElement, targetClass);
    }

    public static <T> T overWrite(T origin, Object source) {
        return overWrite(origin, source, false);
    }

    public static <T> T overWrite(T origin, Object source, boolean overWriteNull) {
        Map<String, Object> originMap = convert(origin, Map.class);
        Map<String, Object> sourceMap = convert(source, Map.class);

        for (Map.Entry<String, Object> element : sourceMap.entrySet()) {
            if (element.getValue() != null || overWriteNull) {
                originMap.put(element.getKey(), element.getValue());
            }
        }

        return (T) convert(originMap, origin.getClass());
    }

    public static final String PATTERN_CONTENT = "[\\w]+=+,";
    public static final String PATTERN_END = "[\\w]+=}";

    public static String removeKey( Object source ) {

        String str_source = null;
        if( source != null ) {
            str_source = source.toString();

            str_source = Pattern.compile(PATTERN_CONTENT).matcher(str_source).replaceAll("");
            str_source = Pattern.compile(PATTERN_END).matcher(str_source).replaceAll("}");
        }

        return str_source;
    }

    static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

        private final DateTimeFormatter dateTimeFormatter;

        public LocalDateTimeAdapter() {
            dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss.SS Z")
                    .toFormatter()
                    .withZone(ZoneId.of("UTC"));
        }

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcTyp1e, JsonSerializationContext context) {
            return new JsonPrimitive(dateTimeFormatter.format(localDateTime.atZone(ZoneId.of("UTC"))));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), dateTimeFormatter);
        }
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        private final DateTimeFormatter dateFormatter;

        public LocalDateAdapter() {
            dateFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .toFormatter()
                    .withZone(ZoneId.of("UTC"));
        }

        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormatter.format(localDate.atStartOfDay(ZoneId.of("UTC"))));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), dateFormatter);
        }
    }

}
