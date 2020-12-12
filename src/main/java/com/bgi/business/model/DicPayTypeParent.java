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

public class DicPayTypeParent extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "dic_pay_type_parent";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(type, "type");put(field, "field");
    }};

    static {
        DicPayTypeParent.map.putAll(BaseModel.map);
    }

    /**
     * 支出项目名称
     */
    @SwaggerProperty(value ="支出项目名称")
    private static final String name = "name";

    /**
     * 字段名称
     */
    @SwaggerProperty(value ="字段名称")
    private static final String field = "field";

    /**
     * 类型：0，个人， 1.公司
     */
    @SwaggerProperty(value ="类型：0，个人， 1.公司")
    private static final String type = "type";

    /**
     * 支出项目名称
     */
    @SwaggerProperty(value ="支出类型", type = DicPayType.class,
            collectionType = @SwaggerProperty.SwaggerCollection(DicPayType.class))
    private static final String payTypes = "payTypes";

    public DicPayTypeParent(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, field, " + BaseModel.baseColumnList();
    }

    public static String getAll() {
        return " select p.id, p.`name`, p.field, p.id as payTypes, " +
                " t.id as id, t.parent_id as parentId, t.`name`, t.field from " + TABLE +
                " p left join " + DicPayType.TABLE + " t on p.id = t.parent_id ; ";
    }

    public static String getSelf() {
        return " select p.id, p.`name`, p.field, p.id as payTypes, " +
                " t.id as id, t.parent_id as parentId, t.`name`, t.field from " +
                " ( select * from " + TABLE + " where del_flag = 0 and type = 0 ) p " +
                " left join " + DicPayType.TABLE + " t on p.id = t.parent_id ; ";
    }

    public static String getCompany() {
        return " select id, `name`, field from " + TABLE + " where del_flag = 0 and type = 1 ; ";
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
        this.entries.put(DicPayTypeParent.name, name);
    }
}
