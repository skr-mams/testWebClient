package services.adapter;

import io.vertx.ext.web.RoutingContext;

/**
 * @author Mario Manzanarez
 * @since 8/11/22
 */
public enum DynamicRequest {
    body,
    id;

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
        return ctx.getBodyAsJson().getInteger(String.valueOf(id));
    }

    /**
     * Retorna el uuid de la entidad reactora, enviada en un json
     *
     * @param ctx contexto de la ruta
     * @return id de la entidad reactora
     */
    public static String getIdToReactiveEntityString(RoutingContext ctx) {
        return ctx.getBodyAsJson().getString(String.valueOf(id));
    }

    /**
     * Obtiene un pojo desde una solicitud dinámica
     *
     * @param ctx       contexto de la ruta
     * @param classType metadata de la clase a convertir el json
     * @param <T>       genérico al que se convierte el json
     * @return pojo de tipo específico
     */
    public static <T> T getDynamicBody(RoutingContext ctx, Class<T> classType) {
        return ctx.getBodyAsJson().getJsonObject(String.valueOf(body)).mapTo(classType);
    }
}
