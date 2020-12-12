package com.bgi.business.model;

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

public class DicCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "dic_code";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(pid, "pid");
    }};

    static {
        DicCode.map.putAll(BaseModel.map);
    }

    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";
    /**
     * 父code
     */
    @SwaggerProperty(value = "父code")
    private static final String pid = "pid";

    public DicCode(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, pid, " + BaseModel.baseColumnList();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(DicCode.name, name);
    }
    public String getPid() {
        return this.entries.getString(pid);
    }

    public void setPid(String pid) {
        this.entries.put(DicCode.pid, pid);
    }
}
