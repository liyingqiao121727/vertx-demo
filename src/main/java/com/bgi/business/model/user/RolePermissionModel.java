package com.bgi.business.model.user;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.BaseModel;
import io.vertx.core.json.JsonObject;

public class RolePermissionModel extends BaseModel {

    public static final String TABLE = "user_role_permission";

    public RolePermissionModel(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }
}
