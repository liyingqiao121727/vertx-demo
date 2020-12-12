package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
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

public class PayParent extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "pay_parent";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(userId, "user_id");put(passUserId, "pass_user_id");put(pass, "pass");
        put(payTime, "pay_time");put(remark, "remark");put(name, "name");
        put(endTime, "end_time");put(type, "type");put(allMoney, "all_money");put(money, "money");
        put(rate, "rate");put(payTypeId, "pay_type_id");put(share, "share");put(shareTime, "share_time");
    }};
    static { PayParent.map.putAll(BaseModel.map); }

    /**
     * 用户id
     */
    @SwaggerProperty(value = "用户id")
    private static final String userId = "userId";
    /**
     * 报销时间
     */
    @SwaggerProperty(value = "报销时间")
    private static final String payTime = "payTime";
    /**
     * 结束时间
     */
    @SwaggerProperty(value = "结束时间")
    private static final String endTime = "endTime";
    /**
     * 类型（个人报销：0， 公司：1）
     */
    @SwaggerProperty(value = "类型（0:个人报销， 1: 公司管理成本, 2:公司财务成本）")
    private static final String type = "type";
    /**
     * 金额（财务成本）
     */
    @SwaggerProperty(value = "金额（财务成本）")
    private static final String allMoney = "allMoney";
    /**
     * 金额（财务成本利息）
     */
    @SwaggerProperty(value = "金额（财务成本利息）")
    private static final String money = "money";
    /**
     * 利率
     */
    @SwaggerProperty(value = "利率")
    private static final String rate = "rate";
    /**
     * 报销类型（公司用 不是报销）
     */
    @SwaggerProperty(value = "报销类型（公司用 不是报销）")
    private static final String payTypeId = "payTypeId";
    /**
     * 审批人
     */
    @SwaggerProperty(value = "审批人")
    private static final String passUserId = "passUserId";
    /**
     * 通过
     */
    @SwaggerProperty(value = "通过(0:待审批；1：通过；2：不通过）")
    private static final String pass = "pass";
    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";
    /**
     * 分摊 0：不含分摊项目或待分摊，1.已分摊
     */
    @SwaggerProperty(value = "分摊 0：不含分摊项目或待分摊，1.已分摊")
    private static final String share = "share";
    /**
     * 分摊时间
     */
    @SwaggerProperty(value = "分摊时间")
    private static final String shareTime = "shareTime";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 接受用户id
     */
    @SwaggerProperty(value ="结束时间是否为空（用作查询条件，区分财务成本和管理成本）", type = Boolean.class)
    public static final String endTimeIsNull = "endTimeIsNull";

    /**
     * 报销时间开始
     */
    @SwaggerProperty(value = "报销时间开始")
    private static final String payTimeStart = "payTimeStart";

    /**
     * 报销时间结束
     */
    @SwaggerProperty(value = "报销时间结束")
    private static final String payTimeEnd = "payTimeEnd";
    /**
     * 报销时间开始
     */
    @SwaggerProperty(value = "结束时间开始")
    private static final String endTimeStart = "endTimeStart";

    /**
     * 报销时间结束
     */
    @SwaggerProperty(value = "结束时间结束")
    private static final String endTimeEnd = "endTimeEnd";
    /**
     * 报销
     */
    @SwaggerProperty(value = "其他", type = Pay.class, collectionType = @SwaggerProperty.SwaggerCollection(Pay.class))
    private static final String pays = "pays";
    /**
     * 项目分摊列表
     */
    @SwaggerProperty(value = "项目分摊列表", type = PayParentProject.class,
            collectionType = @SwaggerProperty.SwaggerCollection(PayParentProject.class))
    private static final String payParentProject = "payParentProject";

    public PayParent(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public PayParent(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public PayParent() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, user_id as userId, `name`, pass_user_id as passUserId, pay_time as payTime, pass, remark, " +
                " end_time as endTime, type, money, all_money as allMoney, rate, pay_type_id as payTypeId, " +
                " share, share_time as shareTime, " +
                BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinP() {
        return " p.id as pId, p.user_id as userId, p.`name`, p.pass_user_id as passUserId, p.pay_time as payTime, p.pass, p.remark, " +
                " p.end_time as endTime, p.type, p.money as totalMoney, p.all_money as allMoney, p.rate, p.pay_type_id as payTypeId, " +
                " p.share, p.share_time as shareTime ";
    }

    public static String deleteById() {
        return " delete from " + TABLE + " where id = ? ; ";
    }

    public static String getPayByIdIn(int size) {
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnList())
                .append(" from ").append(TABLE).append(" where id in (    ");
        for (int i = 0; i < size; i++) {
            sb.append(" ?  , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ").append(" ) ; ").toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + ", u.real_name as commitName, u1.real_name as passName from " +
                TABLE + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }

        if (null != this.getPayTimeStart()) {
            sb.append(" and pay_time >= ? ");
            params.add(this.getPayTimeStart());
        }
        if (null != this.getPayTimeEnd()) {
            sb.append(" and pay_time <= ? ");
            params.add(this.getPayTimeEnd());
        }
        if (null != this.getEndTimeStart()) {
            sb.append(" and end_time >= ? ");
            params.add(this.getEndTimeStart());
        }
        if (null != this.getEndTimeEnd()) {
            sb.append(" and end_time <= ? ");
            params.add(this.getEndTimeEnd());
        }
        if (null != this.getType()) {
            sb.append(" and `type` = ? ");
            params.add(this.getType());
        }
        if (null != this.getShare()) {
            sb.append(" and `share` = ? ");
            params.add(this.getShare());
        }
        if (null != this.getPayTypeId()) {
            sb.append(" and `pay_type_id` = ? ");
            params.add(this.getPayTypeId());
        }
        if (Boolean.TRUE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is null ");
        }
        if (Boolean.FALSE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is not null ");
        }
        if (null != this.getGmtCreate() && !"".equals(this.getGmtCreate().trim())) {
            sb.append(" and gmt_create like ? ");
            params.add(this.getGmtCreate() + "%");
        }
        if (null != this.getPayTime() && !"".equals(this.getPayTime().trim())) {
            sb.append(" and pay_time like ? ");
            params.add(this.getPayTime()+"%");
        }
        if (null != this.getPass()) {
            sb.append(" and pass = ? ");
            params.add(this.getPass());
        }
        if (null != this.getPassUserId() && !"".equals(this.getPassUserId().trim())) {
            sb.append(" and pass_user_id = ? ");
            params.add(this.getPassUserId());
        }
        sb.append(" ) b on a.id = b.bid left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u on u.uid = a.user_id left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u1 on u1.uid = a.pass_user_id ").append(sqLpage.toString()).append(" ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId() && !"".equals(this.getUserId().trim())) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        if (null != this.getPayTimeStart()) {
            sb.append(" and pay_time >= ? ");
            params.add(this.getPayTimeStart());
        }
        if (null != this.getPayTimeEnd()) {
            sb.append(" and pay_time <= ? ");
            params.add(this.getPayTimeEnd());
        }
        if (null != this.getEndTimeStart()) {
            sb.append(" and end_time >= ? ");
            params.add(this.getEndTimeStart());
        }
        if (null != this.getEndTimeEnd()) {
            sb.append(" and end_time <= ? ");
            params.add(this.getEndTimeEnd());
        }
        if (Boolean.TRUE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is null ");
        }
        if (Boolean.FALSE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is not null ");
        }
        if (null != this.getType()) {
            sb.append(" and `type` = ? ");
            params.add(this.getType());
        }
        if (null != this.getShare()) {
            sb.append(" and `share` = ? ");
            params.add(this.getShare());
        }
        if (null != this.getPayTypeId()) {
            sb.append(" and `pay_type_id` = ? ");
            params.add(this.getPayTypeId());
        }
        if (null != this.getGmtCreate() && !"".equals(this.getGmtCreate().trim())) {
            sb.append(" and gmt_create like ? ");
            params.add(this.getGmtCreate() + "%");
        }
        if (null != this.getPayTime() && !"".equals(this.getPayTime().trim())) {
            sb.append(" and pay_time like ? ");
            params.add(this.getPayTime() + "%");
        }
        if (null != this.getPass()) {
            sb.append(" and pass = ? ");
            params.add(this.getPass());
        }
        if (null != this.getPassUserId() && !"".equals(this.getPassUserId().trim())) {
            sb.append(" and pass_user_id = ? ");
            params.add(this.getPassUserId());
        }
        return sb.append(" ; ").toString();
    }

    public static String updateShare(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" update ").append(TABLE).append(" set share = 1, share_time = now() where id = ? ; ");
        }
        return sb.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getUserId() {
        return this.entries.getString(userId);
    }

    public void setUserId(String userId) {
        this.entries.put(PayParent.userId, userId);
    }
    public String getPayTime() {
        return this.entries.getString(payTime);
    }

    public void setPayTime(String payTime) {
        this.entries.put(PayParent.payTime, payTime);
    }
    public String getPassUserId() {
        return this.entries.getString(passUserId);
    }

    public void setPassUserId(String passUserId) {
        this.entries.put(PayParent.passUserId, passUserId);
    }
    public String getRemark() {
        return this.entries.getString(remark);
    }

    public void setRemark(String remark) {
        this.entries.put(PayParent.remark, remark);
    }
    public Integer getPass() {
        return this.entries.getInteger(pass);
    }

    public void setPass(Integer pass) {
        this.entries.put(PayParent.pass, pass);
    }

    public Integer getType() {
        return entries.getInteger(type);
    }

    public void setType(Integer type) {
        this.entries.put(PayParent.type, type);
    }
    public Integer getShare() {
        return this.entries.getInteger(share);
    }

    public void setShare(Integer share) {
        this.entries.put(PayParent.share, share);
    }

    public Boolean getEndTimeIsNull() {
        return this.entries.getBoolean(endTimeIsNull);
    }

    public String getPayTimeStart() {
        return entries.getString(payTimeStart);
    }

    public String getPayTimeEnd() {
        return entries.getString(payTimeEnd);
    }

    public String getEndTimeStart() {
        return entries.getString(endTimeStart);
    }

    public String getEndTimeEnd() {
        return entries.getString(endTimeEnd);
    }

    public String getPayTypeId() {
        return entries.getString(payTypeId);
    }

    public List<Pay> getPays() {
        List<Pay> list = new LinkedList<>();
        JsonArray jsonArray = this.entries.getJsonArray(pays);
        if (null == jsonArray) {
            return null;
        }
        Pay pay = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            pay = new Pay(jsonArray.getJsonObject(i));
            pay.setParentId(this.getId());
            list.add(pay);
        }
        return list;
    }

    public List<PayParentProject> getPayParentProject() {
        List<PayParentProject> list = new LinkedList<>();
        JsonArray jsonArray = this.entries.getJsonArray(payParentProject);
        if (null == jsonArray) {
            return null;
        }
        PayParentProject pay = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            pay = new PayParentProject(jsonArray.getJsonObject(i));
            list.add(pay);
        }
        return list;
    }
}
