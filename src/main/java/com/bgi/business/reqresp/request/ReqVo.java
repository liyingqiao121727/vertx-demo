package com.bgi.business.reqresp.request;

import io.vertx.core.json.JsonObject;

public class ReqVo extends com.bgi.vtx.reqresp.request.ReqVo {

    public ReqVo(JsonObject entry) {
        super(entry);
    }

    @Override
    public JsonObject getContents() {

        return null;
    }
}
