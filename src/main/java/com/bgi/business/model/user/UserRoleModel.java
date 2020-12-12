package com.bgi.business.model.user;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.BaseModel;
import io.vertx.core.json.JsonObject;

public class UserRoleModel extends BaseModel {

    public static final String TABLE = "user_user_role";

    public UserRoleModel(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }
}
