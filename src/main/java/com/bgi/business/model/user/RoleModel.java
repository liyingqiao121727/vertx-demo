package com.bgi.business.model.user;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonObject;

public class RoleModel extends BaseModel {

    @SwaggerProperty(value = "bgi-roleModel", type = RolePermissionModel.class)
    public static final String TABLE = "user_role";

    public RoleModel(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }
}
