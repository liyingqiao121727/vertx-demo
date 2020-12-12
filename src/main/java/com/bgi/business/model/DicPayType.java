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

public class DicPayType extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "dic_pay_type";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(parentId, "parent_id");put(field, "field");
    }};

    static {
        DicPayType.map.putAll(BaseModel.map);
    }

    /**
     * 支出项目名称
     */
    @SwaggerProperty(value ="支出项目名称")
    private static final String name = "name";

    /**
     * 字段
     */
    @SwaggerProperty(value ="字段")
    private static final String field = "field";

    /**
     * 支出项目名称
     */
    @SwaggerProperty(value ="所属项目 父id")
    private static final String parentId = "parentId";

    public DicPayType(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, field, parent_id as parentId, " + BaseModel.baseColumnList();
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
        this.entries.put(DicPayType.name, name);
    }

    public String getParentId() {
        return this.entries.getString(parentId);
    }

    public void setParentId(String parentId) {
        this.entries.put(DicPayType.parentId, parentId);
    }
}
