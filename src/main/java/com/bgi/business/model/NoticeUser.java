package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

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

public class NoticeUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "notice_user";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(noticeId, "notice_id");put(userId, "user_id");put(read, "read");
    }};

    static {
        NoticeUser.map.putAll(BaseModel.map);
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
    @SwaggerProperty(value ="是否阅读")
    private static final String read = "read";

    public NoticeUser(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public NoticeUser() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, notice_id as noticeId, user_id as userId, read, " + BaseModel.baseColumnList();
    }

    public static String deleteByNoticeId() {
        return " delete from " + TABLE + " where notice_id = ? ; ";
    }

    public static String insertBatch(List<NoticeUser> list, JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE).append(
                " ( id, notice_id, user_id, `read`, ")
                .append(BaseModel.baseColumn()).append(" ) values    ");
        for (NoticeUser user : list) {
            params.add(user.getId()).add(user.getNoticeId()).add(user.getUserId()).add(user.getRead());
            user.addParams(params);
            sb.append("( ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ");
        }

        return sb.replace(sb.length()-3, sb.length() -1, " ; ").toString();
    }

    public static String updateNoticeUserTypeByUserIdAndNoticeId(
            String userId, String noticeId, Short read, JsonArray param) {
        param.add(read).add(userId).add(noticeId);
        return " update " + TABLE + " set `read` = ? where user_id = ? and notice_id = ? and del_flag = 0; ";
    }

    public static String getByNoticeId() {
        return " select id, notice_id as noticeId, user_id as userId, `read`, real_name as realName from " +
                " ( select * from " + TABLE + " where notice_id = ? and del_flag = 0 ) n left join " +
                " ( select id as uid, real_name from " + EarthEnergyUser.TABLE + " where del_flag = 0 ) u " +
                " on u.uid = n.user_id;";
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
        this.entries.put(NoticeUser.noticeId, noticeId);
    }

    public String getUserId() {
        return this.entries.getString(userId);
    }

    public void setUserId(String userId) {
        this.entries.put(NoticeUser.userId, userId);
    }

    public Integer getRead() {
        return this.entries.getInteger(read);
    }

    public void setRead(Integer read) {
        this.entries.put(NoticeUser.read, read);
    }
}
