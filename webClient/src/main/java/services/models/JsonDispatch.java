package services.models;

import io.vertx.core.json.JsonObject;


/**
 * @author Mario Manzanarez
 * @since 7/22/22
 */
public class JsonDispatch {
    protected Long guiid;
    protected String SkrTrackId;
    protected String json;

    public JsonDispatch(JsonObject jsonObject) {
    }

    public JsonDispatch() {
    }

    public JsonDispatch(Long GUIID) {
        this.guiid = GUIID;

    }

    public JsonDispatch(Long guiid, String json) {
        this.guiid = guiid;
        this.json = json;
    }

    public JsonDispatch(Long guiid, String skrTrackId, String json) {
        this.guiid = guiid;
        SkrTrackId = skrTrackId;
        this.json = json;
    }

    public Long getGuiid() {
        return guiid;
    }

    public void setGuiid(Long guiid) {
        this.guiid = guiid;
    }

    public String getSkrTrackId() {
        return SkrTrackId;
    }

    public void setSkrTrackId(String skrTrackId) {
        SkrTrackId = skrTrackId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "JsonDispatch{" +
                "guiid=" + guiid +
                ", SkrTrackId='" + SkrTrackId + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
