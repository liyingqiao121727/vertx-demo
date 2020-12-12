package com.bgi.business.model.mis;

import com.bgi.vtx.annotation.SwaggerProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.bgi.constant.Constant;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 检测业务字典表
 * </p>
 *
 * @author xue
 * @since 2019-07-10
 */
public class Dictionary extends BaseModel {

    private static final long serialVersionUID = 1L;
    public static final String TABLE = "dictionary";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(code, "agree_type_id");put(label, "profession_type_id");
        put(typeDesc, "agree_no");
        put(description, "name");put(sort, "money");put(unit, "salesman");
        put(parentId, "biz_property");put(deptId, "final_money");
    }};

    static {
        //Dictionary.map.putAll(BaseModel.map);
    }
    /**
     * 编码
     */
    @SwaggerProperty(value ="编码")
    private static final String code = "code";
    /**
     * 标签名
     */
    @SwaggerProperty(value ="标签名")
    private static final String label = "label";
    /**
     * 类型
     */
    @SwaggerProperty(value ="类型")
    private static final String typeDesc = "typeDesc";
    /**
     * 描述
     */
    @SwaggerProperty(value ="描述")
    private static final String description = "description";
    /**
     * 排序（升序）
     */
    @SwaggerProperty(value ="排序（升序）", type = Long.class)
    private static final String sort = "sort";
    /**
     * 单位
     */
    @SwaggerProperty(value ="单位")
    private static final String unit = "unit";
    /**
     * 父id
     */
    @SwaggerProperty(value ="父id")
    private static final String parentId = "parentId";
    /**
     * 部门主键
     */
    @SwaggerProperty(value ="部门主键")
    private static final String deptId = "deptId";

    @SwaggerProperty(value ="子树")
    private static final String children = "children";

    public Dictionary(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, ref_enum_id as code, show_value as label, enum_value as `typeDesc`, `desc` as description, " +
                " sort_no as sort, level_no as unit, parent_id as parentId, account_id as deptId ";
        //+ BaseModel.baseColumnList();
    }

    public static String getTreeByParentId(String parentId, JsonArray params) {
        params.add(parentId);
        StringBuilder sql = new StringBuilder(" select ").append(baseColumnList()).append(" from ").append(TABLE)
                .append(" where parent_id = ? " +
                        " and state = 1 and output_switch = 1 " +
                        //" and earth_energy_type = 1 order by sort_no;");
                        " order by sort_no;");
        return sql.toString();
    }

    public static String getTreeByCodeAndParentId(String code, String type, JsonArray params) {
        params.add(code).add(code);//.add(parentId);
        StringBuilder sql = new StringBuilder(" select ").append(baseColumnList()).append(" from ").append(TABLE)
                .append(" where ref_enum_id = ? and parent_id = 0 or parent_id in ( select id from " + TABLE +
                        " where ref_enum_id = ? and parent_id = 0 and state = 1 and output_switch = 1 ) " +
                        "and state = 1 and output_switch = 1 ");
        /*if (null != type && !"".equals(type.trim())) {
            sql.append(" and earth_energy_type = ? ");
            params.add(type);
        }*/
        sql.append(" order by sort_no;");
        return sql.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getCode() {
        return entries.getString(code);
    }

    public void setCode(String code) {
        this.entries.put(Dictionary.code, code);
    }
    public String getLabel() {
        return entries.getString(label);
    }

    public void setLabel(String label) {
        this.entries.put(Dictionary.label, label);
    }
    public String getTypeDesc() {
        return entries.getString(typeDesc);
    }

    public void setTypeDesc(String typeDesc) {
        this.entries.put(Dictionary.typeDesc, typeDesc);
    }
    public String getDescription() {
        return entries.getString(description);
    }

    public void setDescription(String description) {
        this.entries.put(Dictionary.description, description);
    }
    public Long getSort() {
        return entries.getLong(sort);
    }

    public void setSort(Long sort) {
        this.entries.put(Dictionary.sort, sort);
    }
    public String getUnit() {
        return entries.getString(unit);
    }

    public void setUnit(String unit) {
        this.entries.put(Dictionary.unit, unit);
    }
    public String getParentId() {
        return entries.getString(parentId);
    }

    public void setParentId(String parentId) {
        this.entries.put(Dictionary.parentId, parentId);
    }
    public String getDeptId() {
        return entries.getString(deptId);
    }

    public void setDeptId(String deptId) {
        this.entries.put(Dictionary.deptId, deptId);
    }

    public JsonArray getChildren() {
        return entries.getJsonArray(children);
    }

    public void setChildren(JsonArray children) {
        this.entries.put(Dictionary.children, children);
    }

    public void add(Dictionary dictionary) {
        JsonArray array = this.getChildren();
        if (null == array) {
            array = new JsonArray();
            this.setChildren(array);
        }
        array.add(dictionary.entries);
    }

}
