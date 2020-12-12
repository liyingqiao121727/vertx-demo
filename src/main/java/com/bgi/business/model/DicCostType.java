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

public class DicCostType extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "dic_cost_type";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");
    }};

    static {
        DicCostType.map.putAll(BaseModel.map);
    }

    /**
     * 成本名称（施工，设备，材料等）
     */
    @SwaggerProperty(value = "成本名称（施工，设备，材料等）")
    private static final String name = "name";

    public DicCostType(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, " + BaseModel.baseColumnList();
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
        this.entries.put(DicCostType.name, name);
    }

}
