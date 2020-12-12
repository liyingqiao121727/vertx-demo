package com.bgi.common;

import io.vertx.core.json.JsonArray;

import java.util.Map;

public interface QueryPage {
    String queryPage(SQLpage sqLpage, JsonArray params);
    String countPage(JsonArray params);
    Map<String, String> getMap();
}
