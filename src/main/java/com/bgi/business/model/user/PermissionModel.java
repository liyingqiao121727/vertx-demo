package com.bgi.business.model.user;

import com.bgi.vtx.BaseModel;
import io.vertx.core.json.JsonObject;

public class PermissionModel extends BaseModel {

    public static final String TABLE = "user_permission";

    public PermissionModel(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }
}
