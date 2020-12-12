package com.bgi.business.model.user;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.BaseModel;
import io.vertx.core.json.JsonObject;

public class UserModel extends BaseModel {

    public static final String TABLE = "earth_energy_user";

    private static final String userName = "userName";
    private static final String password = "password";
    private static final String salt = "salt";

    public UserModel(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public UserModel() {
        super(new JsonObject(), TABLE);
    }

    public String getUserName() {
        return this.entries.getString(UserModel.userName);
    }

    public UserModel setUserName(String userName) {
        this.entries.put(UserModel.userName, userName);
        return this;
    }

    public String getPassword() {
        return this.entries.getString(UserModel.password);
    }

    public UserModel setPassword(String password) {
        this.entries.put(UserModel.password, password);
        return this;
    }

    public String getSalt() {
        return this.entries.getString(UserModel.salt);
    }

    public UserModel setSalt(String salt) {
        this.entries.put(UserModel.salt, salt);
        return this;
    }
}
