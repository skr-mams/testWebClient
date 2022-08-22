package services.adapter;

import com.google.gson.Gson;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.StringEscapeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta clase funciona para convertir diferentes formatos
 * a formatos entendidos por el lenguaje para manipularlos más fácilmente
 * ejemplo: json -> pojo
 */
public abstract class Adapter {
    /***
     * Conversión de datos de un JSON a un objeto de JAVA
     * @param json
     * @param <T> classof
     * @return Object Resultado de la conversión
     * version: 1.0.1 uso de librería vert.x
     */
    public static <T> Object JSONToObject(String json, Class<T> classof) {
        JsonObject serializador = new JsonObject(json);
        return serializador.mapTo(classof);
    }

    /**
     * Parsea una cadena a un tipo LocalDate
     *
     * @param parse cadena a ser parseada
     * @return LocalDate generado
     */
    public static LocalDate parseStringToLocalDate(String parse) {
        try {
            return LocalDate.parse(parse);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Decodifica un tipo LocalTime a una cadena
     *
     * @param time LocalTime a ser decodificado
     * @return cadena resultado de la codificiación
     */
    public static String decodeTime(LocalTime time) {
        if (time == null)
            return null;
        else {
            return time.toString();
        }
    }

    /**
     * Decodifica un tipo LocalDate a una cadena
     *
     * @param date LocalDate a ser decodificado
     * @return cadena resultado de la codificiación
     */
    public static String decodeDate(LocalDate date) {
        if (date == null)
            return null;
        else {
            return date.toString();
        }
    }

    /**
     * Parsea una cadena a un tipo LocalTime
     *
     * @param parse cadena a ser parseada
     * @return LocalTime generado
     */
    public static LocalTime parseStringToLocalTime(String parse) {
        try {
            return LocalTime.parse(parse);
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * Conversión de un objeto de JAVA a un JSON
     * @param object
     * @return String Resultado de la conversion en una cadena de texto
     */
    public static JsonObject ObjectToJSON(Object object) {
        Gson serializable = new Gson();
        return new JsonObject(serializable.toJson(object));
    }

    /***
     * Escapa una cadena
     * @param Value
     * @return String Resultado del escape
     */
    public static String Escape(String Value) {
        return StringEscapeUtils.escapeHtml(Value);
    }

    /**
     * Crea un arreglo de json (JsonArray) a partir de una lista
     * de objetos
     *
     * @param listToConvert lista de objetos a convertir
     * @return JsonArray con los valores de la lista de objetos
     */
    public static JsonArray listToJsonArray(List<?> listToConvert) {
        JsonArray result = new JsonArray();
        listToConvert.forEach(row -> {
            JsonObject object = Adapter.ObjectToJSON(row);
            result.add(object);
        });
        return result;
    }


    /**
     * Crea una nueva lista a partir de un jsonarray enviado desde el cliente web y la convierte
     * a un tipo de lista POJO
     *
     * @param ctx            contexto de la ruta de donde se obtendra el jsonarray
     * @param nameArray      nombre con el que se envia el jsonarray
     * @param classToConvert clase a la que se convertira la lista
     * @param <T>            clase generica a la que se convertira la lista
     * @return lista pojo
     */
    public static <T> List<T> getListToArray(RoutingContext ctx, String nameArray, Class<T> classToConvert) {
        return ctx.getBodyAsJson().getJsonArray(nameArray).stream().map(json -> {
            JsonObject jsonObject = (JsonObject) json;
            return jsonObject.mapTo(classToConvert);
        }).collect(Collectors.toList());
    }

    /**
     * Crea una nueva lista NÚMERICA a partir de un jsonarray enviado desde el cliente web y la convierte
     * a un tipo de lista POJO
     *
     * @param ctx       contexto de la ruta de donde se obtendra el jsonarray
     * @param nameArray nombre con el que se envia el jsonarray
     * @return lista pojo
     */
    public static List<Long> getListToArray(RoutingContext ctx, String nameArray) {
        return ctx.getBodyAsJson().getJsonArray(nameArray).stream().map(idObject ->
                        (Integer) idObject
                ).map(Long::new)
                .collect(Collectors.toList());
    }


    /**
     * Retorna el id de la entidad reactora, enviada en un json
     *
     * @param ctx contexto de la ruta
     * @return id de la entidad reactora
     */
    public static Long getIdToReactiveEntity(RoutingContext ctx) {
        return ctx.getBodyAsJson().getLong("id");
    }

    /**
     * Retorna el id de la entidad reactora, enviada en un json
     *
     * @param ctx contexto de la ruta
     * @return id de la entidad reactora
     */
    public static Integer getIdToReactiveEntityInteger(RoutingContext ctx) {
        return ctx.getBodyAsJson().getInteger("id");
    }

    /**
     * Retorna el uuid de la entidad reactora, enviada en un json
     *
     * @param ctx contexto de la ruta
     * @return id de la entidad reactora
     */
    public static String getIdToReactiveEntityString(RoutingContext ctx) {
        return ctx.getBodyAsJson().getString("id");
    }

    /**
     * Obtiene un pojo desde una solicitud dinámica
     *
     * @param ctx       contexto de la ruta
     * @param classType metadata de la clase a convertir el json
     * @param <T>       genérico al que se convierte el json
     * @return pojo de tipo específico
     */
    public <T> T getDynamicBody(RoutingContext ctx, Class<T> classType) {
        return ctx.getBodyAsJson().getJsonObject("body").mapTo(classType);
    }
}

