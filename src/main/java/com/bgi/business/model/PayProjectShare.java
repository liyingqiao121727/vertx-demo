package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
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

public class PayProjectShare extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "pay_project_share";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(projId, "proj_id");put(payProjectId, "pay_project_id");put(money, "money");
        put(payTypeId, "pay_type_id");put(remark, "remark");put(payMoney, "pay_money");
    }};
    static {
        PayProjectShare.map.putAll(BaseModel.map);
    }

    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";
    /**
     * 报销id
     */
    @SwaggerProperty(value = "报销项目id")
    private static final String payProjectId = "payProjectId";
    /**
     * 分摊金额
     */
    @SwaggerProperty(value = "分摊金额")
    private static final String money = "money";
    /**
     * 支出金额
     */
    @SwaggerProperty(value = "支出金额")
    private static final String payMoney = "payMoney";
    /**
     * 类型
     */
    @SwaggerProperty(value = "类型")
    private static final String payTypeId = "payTypeId";

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
     * 分摊时间开始
     */
    @SwaggerProperty(value = "分摊时间开始")
    private static final String shareTimeStart = "shareTimeStart";

    /**
     * 分摊时间结束
     */
    @SwaggerProperty(value = "分摊时间结束")
    private static final String shareTimeEnd = "shareTimeEnd";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 报销父id
     */
    @SwaggerProperty(value = "报销父id")
    public static final String payParentId = "payParentId";
    /**
     * 子项项目id
     */
    @SwaggerProperty(value = "子项项目id")
    public static final String projectId = "projectId";

    public PayProjectShare(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public PayProjectShare(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public PayProjectShare() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, proj_id as projId, pay_project_id as payProjectId, pay_type_id as payTypeId, money, remark, "
                + BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinPP() {
        return " pp.id, pp.proj_id as projId, pp.pay_project_id as payProjectId, " +
                " pp.pay_type_id as payTypeId, pp.money, pp.remark, pp.pay_money as payMoney ";
    }

    public static String sumMoneyByProjIdAndType(String projId, String type, JsonArray jsonArray) {
        jsonArray.add(projId).add(type);
        return " select sum(money) as money from " + TABLE + " where proj_id = ? and pay_type_id = ? and del_flag = 0; ";
    }

    public static String sumMoneyByProjIdAndTypeIn(String projId, List<String> typeList, JsonArray jsonArray) {
        jsonArray.add(projId).addAll(new JsonArray(typeList));
        StringBuilder sb = new StringBuilder(" select sum(money) as money from ").append(TABLE)
                .append(" where proj_id = ? and pay_type_id in ( ");
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < typeList.size(); i++) {
            sBuilder.append(" , ? ");
        }
        sb.append(typeList.size() < 1 ? "" : sBuilder.substring(2)).append(" ) and del_flag = 0; ");

        return sb.toString();
    }

    public static String sumByProjIdAndTimeAndType(String projId, String time, Integer type, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, sum(money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? ");
        if (null != time && !"".equals(time)) {
            sb.append(" and gmt_create like ? ");
            params.add(time+"%");
        }
        sb.append(" and del_flag = 0 ) pps right join ").append(DicPayType.TABLE)
                .append(" d on d.id = pps.pay_type_id right join ( select * from ").append(DicPayTypeParent.TABLE)
                .append(" where del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) dp on dp.id = d.parent_id group by dp.`name` ; ");
        return sb.toString();
    }

    public static String sumByProjIdAndTimeAndTypeNonGroup(String projId, String time, Integer type, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, sum(money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? ");
        if (null != time && !"".equals(time)) {
            sb.append(" and gmt_create like ? ");
            params.add(time+"%");
        }
        sb.append(" and del_flag = 0 ) pps right join ").append(DicPayType.TABLE)
                .append(" d on d.id = pps.pay_type_id right join ( select * from ").append(DicPayTypeParent.TABLE)
                .append(" where del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) dp on dp.id = d.parent_id ; ");
        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTimeBetween(String projId, Integer type, String between, String end, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, sum(money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? ");
        if (null != between && !"".equals(between) && null != end && !"".equals(end)) {
            sb.append(" and gmt_create between ? and ? ");
            params.add(between).add(end);
        }
        sb.append(" and del_flag = 0 ) pps right join ").append(DicPayType.TABLE)
                .append(" d on d.id = pps.pay_type_id right join ( select * from ").append(DicPayTypeParent.TABLE)
                .append(" where del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) dp on dp.id = d.parent_id group by dp.`name` ; ");
        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTime(String projId, Integer type, String time, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, sum(money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? ");
        if (null != time && !"".equals(time)) {
            sb.append(" and gmt_create like ? ");
            params.add(time+"%");
        }
        sb.append(" and del_flag = 0 ) pps right join ").append(DicPayType.TABLE)
                .append(" d on d.id = pps.pay_type_id right join ( select * from ").append(DicPayTypeParent.TABLE)
                .append(" where del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) dp on dp.id = d.parent_id group by dp.`name` ; ");
        return sb.toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinPP() + " , " + PayParentProject.baseColumnListJoinP1() +
                " , d.`name` as dicName, t.`name` as dicPname, pay.pay_time as payTime " +
                " from ( select * from " + TABLE + " where del_flag = 0 ";
        StringBuilder builder = new StringBuilder(sql);
        if (null != this.getProjId()) {
            builder.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }

        builder.append(" ) pp left join ( select * from ").append(PayParentProject.TABLE)
                .append(" where del_flag = 0 ");
        if (null != this.getProjectId()) {
            builder.append(" and proj_id = ? ");
            params.add(this.getProjectId());
        }
        builder.append(") p on pp.pay_project_id = p.id left join ").append(DicPayType.TABLE)
                .append(" d on pp.pay_type_id = d.id left join ").append(DicPayTypeParent.TABLE)
                .append(" t on d.parent_id = t.id inner join ( select id, pay_time from ").append(PayParent.TABLE)
                .append(" where del_flag = 0) pay on pay.id = p.pay_parent_id ");

        builder.append(sqLpage.toString("p.")).append(" ; ");

        return builder.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from ( select * from " + TABLE + " where del_flag = 0 ";

        StringBuilder builder = new StringBuilder(sql);
        if (null != this.getProjId()) {
            builder.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }

        builder.append(" ) p inner join ( select id as ppId , pay_parent_id from ")
                .append(PayParentProject.TABLE).append(" where del_flag = 0 ");
        if (null != this.getProjectId()) {
            builder.append(" and proj_id = ? ");
            params.add(this.getProjectId());
        }

        builder.append(" ) pay on pay.ppId = p.pay_project_id " +
                "inner join ( select id, pay_time from pay_parent where del_flag = 0) pay1 " +
                "on pay1.id = pay.pay_parent_id ; ");

        return builder.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public static String getByProjId(String projId, JsonArray params) {
        params.add(projId);
        return " select " + baseColumnList() + " from " + TABLE + " where proj_id = ? ; ";
    }

    public static String getByPayParentId() {
        return " select ps.id, ps.proj_id as projId, ps.pay_parent_id as payParentId, ps.money, ps.remark, " +
                PayParent.baseColumnListJoinP() + ", u.real_name as commitName, u1.real_name as passName " + " from " +
                " ( select id, proj_id, pay_parent_id, money, remark from " + TABLE +
                " where del_flag = 0 and pay_parent_id = ? ) ps " +
                " left join " + PayParent.TABLE + " p on ps.pay_parent_id = p.id " +
                " left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u on u.uid = p.user_id " +
                " left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u1 on u1.uid = p.pass_user_id ";
    }

    public String getProjId() {
        return this.entries.getString(projId);
    }

    public void setProjId(String projId) {
        this.entries.put(PayProjectShare.projId, projId);
    }
    public String getPayProjectId() {
        return this.entries.getString(payProjectId);
    }

    public void setPayProjectId(String payProjectId) {
        this.entries.put(PayProjectShare.payProjectId, payProjectId);
    }
    public BigDecimal getMoney() {
        String money = this.entries.getString(PayProjectShare.money);
        if (null == money || "".equals(money.trim())) {
            return null;
        }
        return new BigDecimal(money);
    }

    public void setMoney(BigDecimal money) {
        this.entries.put(PayProjectShare.money, null == money ? null : money.toString());
    }

    public String getRemark() {
        return this.entries.getString(remark);
    }

    public Integer getPayTypeId() {
        return this.entries.getInteger(payTypeId);
    }

    public void setRemark(String remark) {
        this.entries.put(PayProjectShare.remark, remark);
    }

    public String getPayParentId() {
        return entries.getString(payParentId);
    }

    public String getProjectId() {
        return entries.getString(projectId);
    }
}
