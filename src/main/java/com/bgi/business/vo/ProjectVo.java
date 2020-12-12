package com.bgi.business.vo;

import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectVo {

    /**
     * 分包发票金额是否到账
     */
    @SwaggerProperty(value = "分包发票金额是否到账")
    private static final String subPackIsAccount = "subPackIsAccount";

    /**
     * 分包发票金额是否抵扣
     */
    @SwaggerProperty(value = "分包发票金额是否抵扣")
    private static final String subPackIsDeduct = "subPackIsDeduct";

    /**
     * 大合同发票金额是否到账
     */
    @SwaggerProperty(value = "大合同发票金额是否到账")
    private static final String bigAgreeIsAccount = "bigAgreeIsAccount";

    /**
     * 开始时间
     */
    @SwaggerProperty(value = "开始时间")
    private static final String startDate = "startDate";

    /**
     * 结束时间
     */
    @SwaggerProperty(value = "结束时间")
    private static final String endDate = "endDate";

    /**
     * 大合同和分包合同id
     */
    @SwaggerProperty(value = "大合同和分包合同ids, json数组", collectionType = @SwaggerProperty.SwaggerCollection(Id.class))
    private static final String bigAndSubPackIds = "bigAndSubPackIds";

    public static class Id {

        /**
         * 项目id
         */
        @SwaggerProperty(value = "项目id")
        private static final String projId = "projId";
        /**
         * 大合同id
         */
        @SwaggerProperty(value = "大合同id，json数组")
        private static final String bigAgreeIds = "bigAgreeIds";
        /**
         * 分包合同id
         */
        @SwaggerProperty(value = "分包合同id，json数组")
        private static final String subPackIds = "subPackIds";

        private final JsonObject entries;

        public Id(JsonObject entries) {
            this.entries = entries;
        }

        public String getProjId() {
            return String.valueOf(entries.getValue(projId));
        }

        public JsonArray getBigAgreeIds() {
            return entries.getJsonArray(bigAgreeIds);
        }

        public JsonArray getSubPackIds() {
            return entries.getJsonArray(subPackIds);
        }

        public JsonObject getEntries() {
            return entries;
        }
    }

    private final JsonObject entries;

    public ProjectVo(JsonObject entries) {
        this.entries = entries;
    }

    public Integer getSubPackIsAccount() {
        return entries.getInteger(subPackIsAccount);
    }

    public Integer getSubPackIsDeduct() {
        return entries.getInteger(subPackIsDeduct);
    }

    public Integer getBigAgreeIsAccount() {
        return entries.getInteger(bigAgreeIsAccount);
    }

    public List<Id> getBigAndSubPackIds() {
        JsonArray array = entries.getJsonArray(bigAndSubPackIds);
        if (null == array) {
            return new ArrayList<>(0);
        }
        List<Id> list = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            list.add(new Id(array.getJsonObject(i)));
        }
        return list;
    }

    public String getStartDate() {
        return entries.getString(startDate);
    }

    public String getEndDate() {
        return entries.getString(endDate);
    }

    public JsonObject getEntries() {
        return entries;
    }
}
