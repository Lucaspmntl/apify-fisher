package com.lucas.scraper.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Map;

// Acessores null-safe para os Map<String, Object> brutos que o Apify devolve no dataset.
// Cada gateway continua montando seu *Out campo a campo (schema muda por actor/plataforma) —
// isso só evita repetir a checagem de null/tipo em cada linha e faz um campo ausente/inesperado
// virar null no DTO em vez de NPE/ClassCastException derrubando a requisição inteira.
public class RawItemMapper {

    private static final Logger log = LoggerFactory.getLogger(RawItemMapper.class);

    private RawItemMapper() {}

    public static String getString(Map<String, Object> raw, String key) {
        Object value = raw.get(key);
        return value instanceof String s ? s : null;
    }

    public static Integer getInteger(Map<String, Object> raw, String key) {
        Object value = raw.get(key);
        return value instanceof Number n ? n.intValue() : null;
    }

    public static Boolean getBoolean(Map<String, Object> raw, String key) {
        Object value = raw.get(key);
        return value instanceof Boolean b ? b : null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> raw, String key) {
        Object value = raw.get(key);
        return value instanceof Map<?, ?> m ? (Map<String, Object>) m : null;
    }

    // Datas do Apify chegam como String ISO-8601 (ex.: "timestamp", "date", "createTimeISO")
    public static OffsetDateTime getIsoDate(Map<String, Object> raw, String key) {
        String value = getString(raw, key);
        if (value == null || value.isBlank()) return null;
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException e) {
            log.warn("Não foi possível parsear a data do campo '{}': {}", key, value);
            return null;
        }
    }

    // Algumas plataformas (ex.: Facebook post) mandam a data como epoch em segundos
    public static OffsetDateTime getEpochSecondsDate(Map<String, Object> raw, String key) {
        Integer epochSeconds = getInteger(raw, key);
        if (epochSeconds == null) return null;
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
    }

    // Extrai o primeiro segmento de path de uma URL (ex.: username em facebook.com/username/posts/123) —
    // a API do Facebook não disponibiliza esse dado em outro campo
    public static String getFirstPathSegment(String url) {
        if (url == null || url.isBlank()) return null;
        try {
            String path = URI.create(url).getPath();
            String[] segments = path.split("/");
            return segments.length > 1 ? segments[1] : null;
        } catch (Exception e) {
            log.warn("Não foi possível extrair o path da URL: {}", url);
            return null;
        }
    }
}
