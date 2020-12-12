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

public class WorkType extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "work_type";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");
    }};

    static {
        WorkType.map.putAll(BaseModel.map);
    }

    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";

    public WorkType(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, " + BaseModel.baseColumnList();
    }

    public static String getAll() {
        return " select id, `name` from " + TABLE + " where del_flag = 0; ";
    }

    public static Map<String, WorkType> result2NameMap(ResultSet result) {
        List<String> colNames = result.getColumnNames();
        if (null == colNames || colNames.isEmpty()) {
            return new HashMap<>(0);
        }
        int i = -1, nameIndex = 0;
        String[] cols = new String[colNames.size()];
        for (String col : colNames) {
            ++i;
            nameIndex = name.equals(col) ? i : nameIndex;
            cols[i] = col;
        }
        Map<String, WorkType> map = new HashMap<>(result.getNumRows());
        List<JsonArray> list = result.getResults();
        JsonObject jsonObject = null;
        WorkType workType = null;
        for (JsonArray jsonArray : list) {
            jsonObject = new JsonObject();
            for (int j = 0; j < jsonArray.size(); j++) {
                jsonObject.put(cols[j], jsonArray.getValue(j));
            }
            workType = new WorkType(jsonObject);
            map.put(jsonArray.getString(nameIndex), workType);
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

    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(WorkType.name, name);
    }

}
