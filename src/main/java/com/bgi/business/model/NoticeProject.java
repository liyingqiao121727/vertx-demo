package com.bgi.business.model;

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
 * 
 * </p>
 *
 * @author xue
 * @since 2019-07-17
 */

public class NoticeProject extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "notice_project";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(noticeId, "notice_id");put(projId, "proj_id");put(bigAgreeId, "big_agree_id");
    }};

    static {
        NoticeProject.map.putAll(BaseModel.map);
    }

    /**
     * 通知公告id
     */
    @SwaggerProperty(value ="通知公告id")
    private static final String noticeId = "noticeId";

    /**
     * 项目id
     */
    @SwaggerProperty(value ="项目id")
    private static final String projId = "projId";

    /**
     * 项目id
     */
    @SwaggerProperty(value ="大合同id")
    private static final String bigAgreeId = "bigAgreeId";

    public NoticeProject(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public NoticeProject() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, notice_id as noticeId, proj_id as projId, " + BaseModel.baseColumnList();
    }

    public static String deleteByNoticeId() {
        return " delete from " + TABLE + " where notice_id = ? ; ";
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getNoticeId() {
        return this.entries.getString(noticeId);
    }

    public void setNoticeId(String noticeId) {
        this.entries.put(NoticeProject.noticeId, noticeId);
    }

    public String getProjId() {
        return this.entries.getString(projId);
    }

    public void setProjId(String projId) {
        this.entries.put(NoticeProject.projId, projId);
    }

    public String getBigAgreeId() {
        return this.entries.getString(bigAgreeId);
    }

    public void setBigAgreeId(String bigAgreeId) {
        this.entries.put(NoticeProject.bigAgreeId, bigAgreeId);
    }
}
