package services.models;

import io.vertx.core.json.JsonObject;

/**
 * @author Mario Manzarez
 * <p>
 * Representa la respuesta a un cliente web
 * preferentemente uno que utilice los objetos
 * dinámicos
 * </p>
 * @version 0.0.1
 */
public class GenericResponse {
    private String message;
    private int state;
    private Object body;

    public GenericResponse() {
    }

    public GenericResponse(JsonObject jsonObject) {
    }

    public GenericResponse(String message, int state, Object body) {
        this.message = message;
        this.state = state;
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "message='" + message + '\'' +
                ", state=" + state +
                ", body=" + body +
                '}';
    }
}
