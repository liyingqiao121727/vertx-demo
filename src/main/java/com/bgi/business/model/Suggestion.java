package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author xue
 * @since 2019-07-17
 */

public class Suggestion extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "suggestion";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(tel, "tel");put(msg, "msg");
    }};

    static {
        Suggestion.map.putAll(BaseModel.map);
    }

    /**
     * 名称
     */
    @SwaggerProperty(value = "电话")
    private static final String tel = "tel";
    /**
     * 父code
     */
    @SwaggerProperty(value = "反馈内容")
    private static final String msg = "msg";

    public Suggestion(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `tel`, msg, " + BaseModel.baseColumnList();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getTel() {
        return this.entries.getString(tel);
    }

    public void setTel(String tel) {
        this.entries.put(Suggestion.tel, tel);
    }
    public String getMsg() {
        return this.entries.getString(msg);
    }

    public void setMsg(String msg) {
        this.entries.put(Suggestion.msg, msg);
    }

}
