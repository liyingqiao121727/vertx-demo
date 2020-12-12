package com.bgi.common;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class SqlResult {

    public static List<JsonObject> getRows(ResultSet resultSet, int... columns) {
        if (null == resultSet) {
            return null;
        }
        if (null == columns || columns.length < 1) {
            return resultSet.getRows();
        }
        List<JsonArray> result = resultSet.getResults();
        if (null == result) {
            return null;
        }
        List<String> columnNames = resultSet.getColumnNames();
        JsonArray res = null;
        Map<Object, Map<Object, JsonObject>>[] colValues = new Map[columns.length + 1];
        Map<Object, Map<Object, JsonObject>>[] colValues1 = new Map[columns.length + 1];
        for (int i = 0; i < colValues.length; i++) {
            colValues[i] = new LinkedHashMap();
            colValues1[i] = new LinkedHashMap<>();
        }
        int[] cols = new int[columns.length+2];
        cols[0] = 0;
        cols[columns.length+1] = columnNames.size();
        for (int i = 0; i < columns.length; i++) {
            cols[i+1] = columns[i];
        }
        Map<Object, JsonObject> map = null;
        JsonObject object = null;
        int num = 0;
        Object obj = null, value = null;
        for (int j = 0; j < result.size(); j++) {
            res = result.get(j);
            for (int i = cols.length - 2; i > 0; i--) {
                num = i - 1;
                int c = cols[num];
                value = res.getValue(c);
                if (null == value) {
                    continue;
                }
                Map<Object, JsonObject> v = colValues[i].get(value);
                if (null != res.getValue(cols[i]) && (null == v || null == v.get(res.getValue(cols[i])))) {
                    object = new JsonObject();
                    for (int k = cols[i]; k < cols[i+1]; k++) {
                        object.put(columnNames.get(k), res.getValue(k));
                    }
                    if (null == v) {
                        colValues[i].put(res.getValue(c), new LinkedHashMap<>());
                    }
                    obj = res.getValue(cols[i]);
                    if (null != obj) {
                        colValues[i].get(res.getValue(c)).put(obj, object);
                        value = res.getValue(cols[i]);
                        if (null == colValues1[i].get(value)) {
                            colValues1[i].put(value, new LinkedHashMap<>());
                        }
                        colValues1[i].get(value).put(obj, object);
                    }
                }
            }

            if (null == colValues[0].get(res.getValue(0))) {
                object = new JsonObject();
                for (int i = 0; i < columns[0]; i++) {
                    object.put(columnNames.get(i), res.getValue(i));
                }
                map = new LinkedHashMap<>();
                map.put(res.getValue(0), object);
                colValues[0].put(res.getValue(0), map);
                colValues1[0].put(res.getValue(0), map);
            }
        }

        for (int i = colValues.length - 1; i > 0; i--) {
            for (Map.Entry<Object, Map<Object, JsonObject>> entry : colValues[i].entrySet()) {
                map = colValues1[i - 1].get(entry.getKey());
                if (null != map) {
                    for (Map.Entry<Object, JsonObject> e : map.entrySet()) {
                        e.getValue().put(columnNames.get(cols[i] - 1),
                                new JsonArray(new ArrayList(entry.getValue().values())));
                    }
                }
            }
        }

        List<JsonObject> rows = new ArrayList(result.size());
        for (Map.Entry<Object, Map<Object, JsonObject>> entry : colValues1[0].entrySet()) {
            rows.addAll(entry.getValue().values());
        }

        return rows;
    }
}
