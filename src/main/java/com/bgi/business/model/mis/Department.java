package com.bgi.business.model.mis;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
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
public class Department extends BaseModel {

    private static final long serialVersionUID = 1L;
    public static final String TABLE = "org_dept";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(secondName, "second_name");
        put(code, "code");
        put(shortName, "short_name");put(type, "type");put(isGroup, "is_group");
        put(path, "path");put(isInternal, "is_internal");
        put(sort, "sort");put(isEnable, "is_enable");
        put(status, "status");
        put(levelScope, "level_scope");put(orgAccountId, "org_account_id");put(desc, "desc");
    }};

    static {
        Department.map.putAll(BaseModel.map);
    }
    /**
     * 编码
     */
    @SwaggerProperty(value ="编号")
    private static final String code = "code";
    /**
     * 名称
     */
    @SwaggerProperty(value ="名称")
    private static final String name = "name";
    /**
     * 第二名称
     */
    @SwaggerProperty(value ="第二名称")
    private static final String secondName = "secondName";
    /**
     * 简称
     */
    @SwaggerProperty(value ="简称")
    private static final String shortName = "shortName";
    /**
     * 排序（升序）
     */
    @SwaggerProperty(value ="排序（升序）", type = Long.class)
    private static final String sort = "sort";
    /**
     * 机构类型
     */
    @SwaggerProperty(value ="机构类型")
    private static final String type = "type";
    /**
     * 是否是集团 1：是 0：否
     */
    @SwaggerProperty(value ="是否是集团 1：是 0：否")
    private static final String isGroup = "isGroup";
    /**
     * 路径
     */
    @SwaggerProperty(value ="路径")
    private static final String path = "path";
    /**
     * 是否是内部机构 1：是 0：否
     */
    @SwaggerProperty(value ="是否是内部机构 1：是 0：否")
    private static final String isInternal = "isInternal";
    /**
     * 是否启用 1：启用 0：停用
     */
    @SwaggerProperty(value ="是否启用 1：启用 0：停用", type = Long.class)
    private static final String isEnable = "isEnable";
    /**
     * 0：正常 1：停用 2：删除
     */
    @SwaggerProperty(value ="0：正常 1：停用 2：删除")
    private static final String status = "status";
    /**
     * 账户id
     */
    @SwaggerProperty(value ="账户id")
    private static final String orgAccountId = "orgAccountId";
    /**
     * 只对type=account有效
     */
    @SwaggerProperty(value ="只对type=account有效")
    private static final String levelScope = "levelScope";
    /**
     * 路径
     */
    @SwaggerProperty(value ="描述")
    private static final String desc = "desc";

    public Department(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, ref_enum_id as code, show_value as label, enum_value as `typeDesc`, `desc` as description, " +
                " sort_no as sort, level_no as unit, parent_id as parentId, account_id as deptId "
                + BaseModel.baseColumnList();
    }

    public static String getAll() {
        return " select id, `name`, code from " + TABLE + " ; ";
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
        this.entries.put(Department.code, code);
    }

    public Long getSort() {
        return entries.getLong(sort);
    }

    public void setSort(Long sort) {
        this.entries.put(Department.sort, sort);
    }

    public String getName() {
        return entries.getString(name);
    }

    public String getSecondName() {
        return entries.getString(secondName);
    }

    public String getShortName() {
        return entries.getString(shortName);
    }

    public String getType() {
        return entries.getString(type);
    }

    public String getIsGroup() {
        return entries.getString(isGroup);
    }

    public String getPath() {
        return entries.getString(path);
    }

    public String getIsInternal() {
        return entries.getString(isInternal);
    }

    public String getIsEnable() {
        return entries.getString(isEnable);
    }

    public String getStatus() {
        return entries.getString(status);
    }

    public String getOrgAccountId() {
        return entries.getString(orgAccountId);
    }

    public String getDesc() {
        return entries.getString(desc);
    }
}
