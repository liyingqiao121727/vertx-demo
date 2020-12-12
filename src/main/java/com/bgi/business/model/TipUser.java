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

public class TipUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "tip_user";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(noticeId, "notice_id");put(userId, "user_id");put(read, "read");put(tipTime, "tip_time");
        put(copyType, "copy_type");put(tipDay, "tip_day");put(tag, "tag");put(state, "state");
    }};

    static {
        TipUser.map.putAll(BaseModel.map);
    }

    /**
     * 通知id
     */
    @SwaggerProperty(value ="通知id")
    private static final String noticeId = "noticeId";

    /**
     * 用户id
     */
    @SwaggerProperty(value ="用户id")
    private static final String userId = "userId";

    /**
     * 是否阅读
     */
    @SwaggerProperty(value ="是否阅读", type = Integer.class)
    private static final String read = "read";
    /**
     * 抄送(0:通知负责人，1：抄送)
     */
    @SwaggerProperty(value ="抄送(0:通知负责人，1：抄送)", type = Integer.class)
    public static final String copyType = "copyType";
    /**
     * 提前提醒时间（天）
     */
    @SwaggerProperty(value ="提前提醒时间（天）", type = Integer.class)
    private static final String tipDay = "tipDay";
    /**
     * 标记（五角星）
     */
    @SwaggerProperty(value ="标记（五角星）", type = Integer.class)
    private static final String tag = "tag";
    /**
     * 代办时间
     */
    @SwaggerProperty(value ="代办时间")
    private static final String tipTime = "tipTime";
    /**
     * 状态（0：未完成，1，已完成）
     */
    @SwaggerProperty(value ="状态（0：未完成，1，已完成）", type = Integer.class)
    private static final String state = "state";

    public TipUser(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public TipUser() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, notice_id as noticeId, user_id as userId, read, copy_type as copyType, " +
                " tip_day as tipDay, `tag`, `state`, " + BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinTU() {
        return " tu.id, tu.notice_id as noticeId, tu.user_id as userId, tu.`read`, tu.copy_type as copyType, " +
                " tu.tip_day as tipDay, tu.`tag`, tu.`state`, uu.real_name as realName ";
    }

    public static String updateNoticeUserTypeByUserIdAndNoticeId(
            String userId, String noticeId, Short read, JsonArray param) {
        param.add(read).add(userId).add(noticeId);
        return " update " + TABLE + " set `read` = ? where user_id = ? and notice_id = ? and del_flag = 0; ";
    }

    public String updateTipUserByNoticeId(JsonArray params) {
        Map<String, Object> colMap = this.entries.getMap();
        colMap.remove(gmtCreate);
        colMap.remove(id);
        StringBuilder sets = new StringBuilder();
        Map<String, String> subMap = this.getMap();
        for (Map.Entry<String, Object> entry : colMap.entrySet()) {
            String key = subMap.get(entry.getKey());
            if (map.containsKey(key)) {
                sets.append(", ").append(key).append(" =? ");
                params.add(entry.getValue());
            }
        }
        String sql = "update " + TABLE + " set " + sets.substring(1) + " where notice_id = ? ;";
        params.add(this.entries.getValue(noticeId));
        return sql;
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
        this.entries.put(TipUser.noticeId, noticeId);
    }

    public String getUserId() {
        return this.entries.getString(userId);
    }

    public void setUserId(String userId) {
        this.entries.put(TipUser.userId, userId);
    }

    public Integer getRead() {
        return this.entries.getInteger(read);
    }

    public void setRead(Integer read) {
        this.entries.put(TipUser.read, read);
    }

    public Integer getCopyType() {
        return entries.getInteger(copyType);
    }

    public Integer getTipDay() {
        return entries.getInteger(tipDay);
    }

    public Integer getTag() {
        return entries.getInteger(tag);
    }

    public Integer getState() {
        return entries.getInteger(state);
    }

    public void setTag(int tag) {
        entries.put(TipUser.tag, tag);
    }

    public void setState(int state) {
        entries.put(TipUser.state, state);
    }
}
