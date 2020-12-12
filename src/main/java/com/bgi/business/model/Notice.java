package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.annotation.SwaggerProperty.SwaggerCollection;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author xue
 * @since 2019-07-17
 */
public class Notice extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "notice";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(title, "title");put(subtitle, "subtitle");put(msg, "msg");put(userId, "user_id");
        put(type, "type");put(state, "state");put(noticeTypeId, "notice_type_id");put(pdf, "pdf");
        put(single, "single");put(tipTime, "tip_time");put(singleUserId, "single_user_id");
        put(showTitle, "show_title");
    }};

    static {
        Notice.map.putAll(BaseModel.map);
    }

    /**
     * 标题
     */
    @SwaggerProperty(value = "标题")
    private static final String title = "title";

    /**
     * 子标题
     */
    @SwaggerProperty(value ="子标题")
    private static final String subtitle = "subtitle";
    /**
     * 内容
     */
    @SwaggerProperty(value ="内容")
    private static final String msg = "msg";
    /**
     * pdf
     */
    @SwaggerProperty(value ="pdf")
    private static final String pdf = "pdf";
    /**
     * 发布人员id
     */
    @SwaggerProperty(value ="发布人员id")
    private static final String userId = "userId";
    /**
     * 栏目id
     */
    @SwaggerProperty(value ="栏目id")
    private static final String noticeTypeId = "noticeTypeId";
    /**
     * 类型
     */
    @SwaggerProperty(value ="类型 0:公告， 1：通知, 2:代办事项", type = Integer.class)
    private static final String type = "type";
    /**
     * 是否阅读
     */
    @SwaggerProperty(value ="是否阅读", type = Integer.class)
    private static final String read = "read";
    /**
     * 发布状态
     */
    @SwaggerProperty(value ="发布状态 0：待发布， 1：成功， 2，失败")
    private static final String state = "state";
    /**
     * 独立完成（0：非独立完成，1：独立完成）
     */
    @SwaggerProperty(value ="独立完成（0：非独立完成，1：独立完成）")
    public static final String single = "single";
    /**
     * 独立完成（0：非独立完成，1：独立完成）
     */
    @SwaggerProperty(value ="独立完成用户id")
    public static final String singleUserId = "singleUserId";
    /**
     * 代办时间
     */
    @SwaggerProperty(value ="代办时间")
    private static final String tipTime = "tipTime";
    /**
     * 是否显示标题
     */
    @SwaggerProperty(value ="是否显示标题", type = Short.class)
    private static final String showTitle = "showTitle";
    /**
     * 接受用户id
     */
    @SwaggerProperty(value ="接受用户id")
    public static final String customUserId = "customUserId";
    /**
     * 接受用户id
     */
    @SwaggerProperty(value ="接受用户id s", collectionType = @SwaggerProperty.SwaggerCollection(String.class))
    public static final String customUserIds = "customUserIds";
    /**
     * 接受用户id
     */
    @SwaggerProperty(value ="代办事项用户信息（json数组）", collectionType = @SwaggerProperty.SwaggerCollection(TipUser.class))
    public static final String tipUsers = "tipUsers";
    /**
     * 项目id
     */
    @SwaggerProperty(value ="项目id")
    private static final String projId = "projId";
    /**
     * 大合同id
     */
    @SwaggerProperty(value ="大合同id")
    private static final String bigAgreeId = "bigAgreeId";

    public Notice(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public Notice(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public Notice() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, title, subtitle, msg, pdf, user_id as userId, type, state, notice_type_id as noticeTypeId, " +
                " show_title as showTitle, "
                + BaseModel.baseColumnList();
    }

    public static String baseColumn() {
        return " id, title, subtitle, user_id as userId, type, state, notice_type_id as noticeTypeId, " +
                " show_title as showTitle, "
                + BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinA() {
        return " a.id, a.title, a.subtitle, a.msg, a.user_id as userId, a.`type`, a.`state`, a.single, " +
                " a.tip_time as tipTime, a.notice_type_id as noticeTypeId, a.gmt_create as gmtCreate ";
    }

    public static String countByNoticeTypeId() {
        return " select count(*) from " + TABLE + " where notice_type_id = ? ; ";
    }

    public static String getByUserIdJoin() {
        return " select " +
                baseColumn() +
                " from ( select * from " +
                TABLE +
                " where state = 0 and del_flag = 0 ) n " +
                " inner join ( select notice_id, user_id as uid from " +
                NoticeUser.TABLE +
                " where del_flag = 0, and user_id = ? ) u on n.id = u.notice_id ; ";
    }

    public static String deleteById() {
        return " delete from " + TABLE + " where id = ? ; ";
    }

    public static String getById(String id, JsonArray params) {
        params.add(id);
        return " select " + baseColumnList() + " , u.uid as customerUserIds, u.uid as userId, u.real_name as realName " +
                " from ( select * from " + TABLE + " where id = ? and del_flag = 0 ) n left join " +
                " ( select notice_id as nid, user_id as n_user_id from " + NoticeUser.TABLE + " where del_flag = 0 ) " +
                " nu on n.id = nu.nid left join ( select id as uid, real_name from " +
                EarthEnergyUser.TABLE + " where del_flag = 0) u on u.uid = nu.n_user_id; ";
    }

    /*public String insertOne(JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE).append(
                " ( id, title, msg, user_id, type, state, notice_type_id, ")
                .append(BaseModel.baseColumn()).append(" ) values    ");
        params.add(this.getId()).add(this.getTitle()).add(this.getMsg()).add(this.getUserId())
                .add(this.getType()).add(this.getState()).add(this.getNoticeTypeId());
        this.addParams(params);
        sb.append("( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

        return sb.replace(sb.length()-3, sb.length() -1, " ; ").toString();
    }*/

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        boolean customUserIdExist = null != this.getCustomUserId() && !"".equals(this.getCustomUserId().trim());
        String sql = new StringBuilder(" select ")
                .append(baseColumn())
                .append(" , b.tname as typeName ")
                .append(customUserIdExist ? " , b.`read` " : " ")
                .append(" from " + TABLE + " a inner join ( select id as bid , t.tname ")
                .append(customUserIdExist ? " , c.`read` " : " ")
                .append(" from " + TABLE + " d inner join ( select id as tid, `name` as tname from " +
                        NoticeType.TABLE + " where `show` = 1 ) t on d.notice_type_id = t.tid ").toString();
        StringBuilder sb = new StringBuilder(sql);
        if (customUserIdExist) {
            sb.append(" inner join ( select notice_id as nid, `read` from ").append(NoticeUser.TABLE)
                    .append(" where user_id = ? ) c on c.nid = d.id ");
            params.add(this.getCustomUserId());
        }
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" inner join ( select notice_id as pid from ").append(NoticeProject.TABLE)
                    .append(" where proj_id = ? ) p on p.pid = d.id ");
            params.add(this.getProjId());
        }
        sb.append(" where d.del_flag = 0 ");
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and d.user_id = ? ");
            params.add(this.getUserId());
        }
        if (null != this.getType()) {
            sb.append(" and d.type = ? ");
            params.add(this.getType());
        }
        if (null != this.getTitle()) {
            sb.append(" and d.title like ? ");
            params.add("%" + this.getTitle() + "%");
        }
        if (null != this.getState()) {
            sb.append(" and d.state = ? ");
            params.add(this.getState());
        }
        if (null != this.getNoticeTypeId() && !"".equals(this.getNoticeTypeId().trim())) {
            sb.append(" and d.notice_type_id = ? ");
            params.add(this.getNoticeTypeId());
        }
        sb.append(" ) b on a.id = b.bid ").append(sqLpage.toString("a."));

        return sb.append(" ; ").toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        if (null != this.getType()) {
            sb.append(" and type = ? ");
            params.add(this.getType());
        }
        if (null != this.getTitle()) {
            sb.append(" and title like ? ");
            params.add("%" + this.getTitle() + "%");
        }
        if (null != this.getState()) {
            sb.append(" and state = ? ");
            params.add(this.getState());
        }
        if (null != this.getNoticeTypeId() && !"".equals(this.getNoticeTypeId().trim())) {
            sb.append(" and notice_type_id = ? ");
            params.add(this.getNoticeTypeId());
        }
        sb.append(" ) b on a.id = b.bid ");
        sb.append(" inner join ( select id as tid from " + NoticeType.TABLE +
                " where `show` = 1 ) t on a.notice_type_id = t.tid ");
        if (null != this.getCustomUserId() && !"".equals(this.getCustomUserId().trim())) {
            sb.append(" inner join ( select notice_id as nid from ").append(NoticeUser.TABLE)
                    .append(" where user_id = ? ) c on c.nid = b.bid ");
            params.add(this.getCustomUserId());
        }
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" inner join ( select notice_id as pid from ").append(NoticeProject.TABLE)
                    .append(" where proj_id = ? ) p on p.pid = b.bid ");
            params.add(this.getProjId());
        }

        return sb.append(" ; ").toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getTitle() {
        return this.entries.getString(title);
    }

    public String getSubtitle() {
        return this.entries.getString(subtitle);
    }

    public void setTitle(String title) {
        this.entries.put(Notice.title, title);
    }
    public String getMsg() {
        return this.entries.getString(msg);
    }

    public void setMsg(String msg) {
        this.entries.put(Notice.msg, msg);
    }
    public String getUserId() {
        return this.entries.getString(userId);
    }

    public void setUserId(String userId) {
        this.entries.put(Notice.userId, userId);
    }
    public String getCustomUserId() {
        return this.entries.getString(customUserId);
    }

    public void setCustomUserId(String customUserId) {
        this.entries.put(Notice.customUserId, customUserId);
    }
    public String getProjId() {
        return this.entries.getString(projId);
    }

    public void setProjId(String projId) {
        this.entries.put(Notice.projId, projId);
    }
    public Integer getType() {
        return this.entries.getInteger(type);
    }

    public void setType(Integer type) {
        this.entries.put(Notice.type, type);
    }
    public Integer getState() {
        return this.entries.getInteger(state);
    }

    public String getSingleUserId() {
        return entries.getString(singleUserId);
    }

    public void setSingleUserId(String singleUserId) {
        entries.put(Notice.singleUserId, singleUserId);
    }

    public void setState(Integer state) {
        this.entries.put(Notice.state, state);
    }

    public JsonArray getCustomUserIds() {
        return this.entries.getJsonArray(Notice.customUserIds);
    }

    public String getNoticeTypeId() {
        return this.entries.getString(noticeTypeId);
    }
    public String getPdf() {
        return this.entries.getString(pdf);
    }

    public void setPdf(String pdf) {
        this.entries.put(Notice.pdf, pdf);
    }

    public String getBigAgreeId() {
        return this.entries.getString(bigAgreeId);
    }

    public void setBigAgreeId(String bigAgreeId) {
        this.entries.put(Notice.bigAgreeId, bigAgreeId);
    }

    public List<TipUser> getTipUsers() {
        JsonArray userArray = entries.getJsonArray(tipUsers);
        if (null == userArray) {
            return new LinkedList<>();
        }
        List<TipUser> users = new ArrayList<>(userArray.size());
        TipUser user = null;
        for (int i = 0; i < userArray.size(); i++) {
            user = new TipUser(userArray.getJsonObject(i));
            user.setNoticeId(this.getId());
            if (null == user.getState()) {
                user.setState(0);
            }
            if (null == user.getTag()) {
                user.setTag(0);
            }
            users.add(user);
        }
        return users;
    }
}
