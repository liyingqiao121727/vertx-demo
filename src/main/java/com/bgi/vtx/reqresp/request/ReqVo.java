package com.bgi.vtx.reqresp.request;

import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonObject;

public abstract class ReqVo {

    @SwaggerProperty("版本（1.0，2.x....）")
    private static final String version = "version";

    protected JsonObject entry;

    public ReqVo(JsonObject entry) {
        this.entry = null == entry ? new JsonObject() : entry;
    }

    public JsonObject getEntry() {
        return entry;
    }

    public abstract JsonObject getContents();

    @Override
    public String toString() {
        return entry.toString();
    }

    public String getVersion() {
        return entry.getString(version);
    }

}
