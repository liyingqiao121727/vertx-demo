package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author xue
 * @since 2019-07-17
 */

public class GlodonAlias extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "glodon_alias";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(originName, "origin_name");put(alias, "alias");put(type, "type");
    }};

    static {
        GlodonAlias.map.putAll(BaseModel.map);
    }

    /**
     * 别名
     */
    @SwaggerProperty(value ="别名")
    private static final String alias = "alias";

    /**
     * 原名
     */
    @SwaggerProperty(value ="原名")
    private static final String originName = "originName";
    /**
     * 类型
     */
    @SwaggerProperty(value = "类型 类型 1.人工费(总);2.主材费;3.设备费;4.机械费;5.辅材;6.措施费;7.其他(风险费用);8.其他",
            type = Integer.class)
    private static final String type = "type";

    public GlodonAlias(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `alias`, origin_name as originName, `type`, " + BaseModel.baseColumnList();
    }

    public static String getAll() {
        return " select `alias`, `type`, origin_name as originName from " + TABLE + " ; ";
    }

    public static String getByNonZero() {
        return " select id, `alias`, `type`, origin_name as originName from " + TABLE +
                " where `type` != 0 and del_flag = 0 order by `type` asc; ";
    }

    /*public static Map<String, String> toMap(ResultSet result) {
        List<String> colNames = result.getColumnNames();
        if (null == colNames || colNames.isEmpty()) {
            return new HashMap<>(0);
        }
        int i = -1, aliasIndex = 0, originIndex = 0;
        for (String col : colNames) {
            ++i;
            aliasIndex = alias.equals(col) ? i : aliasIndex;
            originIndex = originName.equals(col) ? i : originIndex;
        }
        Map<String, String> map = new HashMap<>(result.getNumRows());
        List<JsonArray> list = result.getResults();
        for (JsonArray jsonArray : list) {
            map.put(jsonArray.getString(originIndex), jsonArray.getString(aliasIndex));
        }

        return map;
    }*/

    public static Map<String, GlodonAlias> result2Map(ResultSet result) {
        List<String> colNames = result.getColumnNames();
        if (null == colNames || colNames.isEmpty()) {
            return new HashMap<>(0);
        }
        int i = -1, originIndex = 0;//, aliasIndex = 0
        String[] cols = new String[colNames.size()];
        for (String col : colNames) {
            ++i;
            //aliasIndex = alias.equals(col) ? i : aliasIndex;
            originIndex = originName.equals(col) ? i : originIndex;
            cols[i] = col;
        }
        Map<String, GlodonAlias> map = new HashMap<>(result.getNumRows());
        List<JsonArray> list = result.getResults();
        JsonObject jsonObject = null;
        GlodonAlias glodonAlias = null;
        for (JsonArray jsonArray : list) {
            jsonObject = new JsonObject();
            for (int j = 0; j < jsonArray.size(); j++) {
                jsonObject.put(cols[j], jsonArray.getValue(j));
            }
            glodonAlias = new GlodonAlias(jsonObject);
            map.put(jsonArray.getString(originIndex), glodonAlias);
        }

        return map;
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getAlias() {
        return this.entries.getString(alias);
    }

    public void setAlias(String alias) {
        this.entries.put(GlodonAlias.alias, alias);
    }

    public String getOriginName() {
        return this.entries.getString(originName);
    }

    public void setOriginName(String originName) {
        this.entries.put(GlodonAlias.originName, originName);
    }

    public Integer getType() {
        return entries.getInteger(type);
    }

    public void setType(Integer type) {
        this.entries.put(GlodonAlias.type, type);
    }

}
