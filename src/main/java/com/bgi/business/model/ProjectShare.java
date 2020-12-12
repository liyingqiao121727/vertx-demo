package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
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

public class ProjectShare extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "project_share";

    protected static final HashMap<String, String> map = new HashMap<String, String>(5){{
        put(projId, "proj_id");put(payParentId, "pay_parent_id");put(money, "money");put(remark, "remark");
        put(shareTime, "share_time");
    }};
    static {
        ProjectShare.map.putAll(BaseModel.map);
    }

    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";
    /**
     * 报销id
     */
    @SwaggerProperty(value = "报销id")
    private static final String payParentId = "payParentId";
    /**
     * 报销id
     */
    @SwaggerProperty(value = "报销类型id")
    private static final String payTypeId = "payTypeId";
    /**
     * 金额
     */
    @SwaggerProperty(value = "金额")
    private static final String money = "money";
    /**
     * 类型
     */
    @SwaggerProperty(value = "类型")
    private static final String type = "type";

    /**
     * 报销时间开始
     */
    @SwaggerProperty(value = "报销时间开始")
    private static final String payTimeStart = "payTimeStart";

    /**
     * 报销时间
     */
    @SwaggerProperty(value = "报销时间")
    private static final String payTime = "payTime";
    /**
     * 分摊时间
     */
    @SwaggerProperty(value = "分摊时间")
    private static final String shareTime = "shareTime";

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

    public ProjectShare(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public ProjectShare(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public ProjectShare() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, proj_id as projId, pay_parent_id as payParentId, money, remark, " + BaseModel.baseColumnList();
    }

    public static String sumMoneyByProjIdAndPayTypeId(String projId, String payTypeId, JsonArray jsonArray) {
        jsonArray.add(projId).add(payTypeId);
        return " select sum(ps.money) as money from ( select proj_id, pay_parent_id, money from " + TABLE +
                " where proj_id = ? and del_flag = 0 ) ps inner join " + PayParent.TABLE +
                " pp on pp.id = ps.pay_parent_id and pp.pay_type_id = ? and pp.del_flag = 0; ";
    }

    public static String sumMoneyByProjIdAndType(String projId, Short type, JsonArray jsonArray) {
        jsonArray.add(projId).add(type);
        return " select sum(ps.money) as money from ( select proj_id, pay_parent_id, money from " + TABLE +
                " where proj_id = ? and del_flag = 0 ) ps inner join " + PayParent.TABLE +
                " pp on pp.id = ps.pay_parent_id and pp.type = ? and pp.del_flag = 0; ";
    }

    public static String sumByProjIdAndTypeAndTime(String projId, String time, Integer type, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select '").append(Constant.FINANCE).append("' as `name`, " +
                " sum(ps.money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? and del_flag = 0 ");
        if (null != time && !"".equals(time)) {
            sb.append(" and share_time like ? ");
            params.add(time + "%");
        }
        sb.append(") ps right join ( select * from ").append(PayParent.TABLE)
                .append(" where id in ( select pay_parent_id from ").append(TABLE)
                .append(" where proj_id = ? and del_flag = 0 ");
        params.add(projId);
        if (null != time && !"".equals(time)) {
            sb.append(" and gmt_create like ? ");
            params.add(time + "%");
        }
        sb.append(" ) and del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) pp on pp.id = ps.pay_parent_id ; ");

        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTimeBetween(
            String projId, String startTime, String endTime, Integer type, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select '").append(Constant.FINANCE).append("' as `name`, " +
                " sum(ps.money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? and del_flag = 0 ");
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            sb.append(" and share_time between ? and ? ");
            params.add(startTime).add(endTime);
        }
        sb.append(") ps right join ( select * from ").append(PayParent.TABLE)
                .append(" where id in ( select pay_parent_id from ").append(TABLE)
                .append(" where proj_id = ? and del_flag = 0 ");
        params.add(projId);
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            sb.append(" and gmt_create between ? and ? ");
            params.add(startTime).add(endTime);
        }
        sb.append(" ) and del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) pp on pp.id = ps.pay_parent_id ; ");

        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTime(String projId, String time, Integer type,
                                                   Integer payType, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, " +
                " sum(ps.money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? and del_flag = 0 ");
        if (null != time && !"".equals(time)) {
            sb.append(" and share_time like ? ");
            params.add(time + "%");
        }
        sb.append(") ps right join ( select * from ").append(PayParent.TABLE)
                .append(" where id in ( select pay_parent_id from ").append(TABLE)
                .append(" where proj_id = ? and del_flag = 0 ");
        params.add(projId);
        if (null != time && !"".equals(time)) {
            if (null == type) {
                sb.append(" and gmt_create like ? ");
            } else {
                if (1 == type) {
                    sb.append(" and pay_time like ? ");
                }
                if (2 == type) {
                    sb.append(" and end_time like ? ");
                }
            }
            params.add(time + "%");
        }
        sb.append(" ) and del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) pp on pp.id = ps.pay_parent_id ");

        sb.append(" right join ( select * from ").append(DicPayTypeParent.TABLE).append(" where del_flag = 0 ");
        if (null != payType) {
            sb.append(" and `type` = ? ");
            params.add(payType);
        }
        sb.append(" ) dp on pp.pay_type_id = dp.id group by dp.`name` ; ");

        return sb.toString();
    }

    public static String sumByProjIdAndTypeAndTimeBetween(String projId, String startTime, String endTime, Integer type,
                                                   Integer payType, JsonArray params) {
        params.add(projId);
        StringBuilder sb = new StringBuilder(" select dp.`name`, " +
                " sum(ps.money) as actualMoney from ( select * from ")
                .append(TABLE).append(" where proj_id = ? and del_flag = 0 ");
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            sb.append(" and share_time between ? and ? ");
            params.add(startTime).add(endTime);
        }
        sb.append(") ps right join ( select * from ").append(PayParent.TABLE)
                .append(" where id in ( select pay_parent_id from ").append(TABLE)
                .append(" where proj_id = ? and del_flag = 0 ");
        params.add(projId);
        if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
            if (null == type) {
                sb.append(" and gmt_create between ? and ? ");
            }
            if (1 == type) {
                sb.append(" and pay_time between ? and ? ");
            }
            if (2 == type) {
                sb.append(" and end_time between ? and ? ");
            }
            params.add(startTime).add(endTime);
        }
        sb.append(" ) and del_flag = 0 ");
        if (null != type) {
            sb.append(" and `type` = ? ");
            params.add(type);
        }
        sb.append(" ) pp on pp.id = ps.pay_parent_id ");

        sb.append(" right join ( select * from ").append(DicPayTypeParent.TABLE).append(" where del_flag = 0 ");
        if (null != payType) {
            sb.append(" and `type` = ? ");
            params.add(payType);
        }
        sb.append(" ) dp on pp.pay_type_id = dp.id group by dp.`name` ; ");

        return sb.toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ps.id, ps.proj_id as projId, ps.pay_parent_id as payParentId, ps.money, ps.remark, ")
                .append(PayParent.baseColumnListJoinP())
                .append(", u.real_name as commitName, u1.real_name as passName from " +
                        " ( select id, proj_id, pay_parent_id, money, remark from ")
                .append(TABLE).append(" where del_flag = 0 ");

        if (null != this.getPayParentId()) {
            sql.append(" and pay_parent_id = ? ");
            params.add(this.getPayParentId());
        }
        if (null != this.getProjId()) {
            sql.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }
        sql.append(" ) ps " +
                " inner join ( select * from " + PayParent.TABLE + " where del_flag = 0 ");
        if (null != this.entries.getString(payTimeStart)) {
            sql.append(" and pay_time >= ? ");
            params.add(this.entries.getString(payTimeStart));
        }
        if (null != this.entries.getString(payTimeEnd)) {
            sql.append(" and pay_time <= ? ");
            params.add(this.entries.getString(payTimeEnd));
        }
        if (null != this.entries.getString(payTime)) {
            sql.append(" and pay_time = ? ");
            params.add(this.entries.getString(payTime));
        }
        if (null != this.entries.getString(shareTimeStart)) {
            sql.append(" and share_time >= ? ");
            params.add(this.entries.getString(shareTimeStart));
        }
        if (null != this.entries.getString(shareTimeEnd)) {
            sql.append(" and share_time <= ? ");
            params.add(this.entries.getString(shareTimeEnd));
        }
        if (null != this.getType()) {
            sql.append(" and type = ? ");
            params.add(this.getType());
        }
        if (null != this.getPayTypeId()) {
            sql.append(" and pay_type_id = ? ");
            params.add(this.getPayTypeId());
        }
        sql.append(" ) p on ps.pay_parent_id = p.id " +
                " left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u on u.uid = p.user_id " +
                " left join (select id as uid, real_name from " +
                EarthEnergyUser.TABLE + ") u1 on u1.uid = p.pass_user_id ");
        return sql.append(sqLpage.toString()).append(" ; ").toString();
    }

    @Override
    public String countPage(JsonArray params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) from ( select pay_parent_id from ")
                .append(TABLE).append(" where del_flag = 0 ");

        if (null != this.getPayParentId()) {
            sql.append(" and pay_parent_id = ? ");
            params.add(this.getPayParentId());
        }
        if (null != this.getProjId()) {
            sql.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }
        sql.append(" ) ps " +
                " inner join ( select id from " + PayParent.TABLE + " where del_flag = 0 ");
        if (null != this.entries.getString(payTimeStart)) {
            sql.append(" and pay_time >= ? ");
            params.add(this.entries.getString(payTimeStart));
        }
        if (null != this.entries.getString(payTimeEnd)) {
            sql.append(" and pay_time <= ? ");
            params.add(this.entries.getString(payTimeEnd));
        }
        if (null != this.entries.getString(payTime)) {
            sql.append(" and pay_time = ? ");
            params.add(this.entries.getString(payTime));
        }
        if (null != this.entries.getString(shareTimeStart)) {
            sql.append(" and share_time >= ? ");
            params.add(this.entries.getString(shareTimeStart));
        }
        if (null != this.entries.getString(shareTimeEnd)) {
            sql.append(" and share_time <= ? ");
            params.add(this.entries.getString(shareTimeEnd));
        }
        if (null != this.getType()) {
            sql.append(" and type = ? ");
            params.add(this.getType());
        }
        sql.append(" ) p on ps.pay_parent_id = p.id ");
        return sql.append(" ; ").toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public static String insertBatch(List<ProjectShare> list, JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE).append(
                " ( id, proj_id, pay_parent_id, money, remark, ").append(BaseModel.baseColumn()).append(" ) values    ");
        BigDecimal money  = null;
        for (ProjectShare projShare : list) {
            money = projShare.getMoney();
            params.add(projShare.getId()).add(projShare.getProjId()).add(projShare.getPayParentId())
                    .add(null == money ? null : money.toString()).add(projShare.getRemark());
            projShare.addParams(params);
            sb.append("( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ");
        }

        return sb.replace(sb.length()-3, sb.length() -1, " ; ").toString();
    }

    public static String getByProjId() {
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
        this.entries.put(ProjectShare.projId, projId);
    }
    public String getPayParentId() {
        return this.entries.getString(payParentId);
    }

    public String getPayTypeId() {
        return this.entries.getString(payTypeId);
    }

    public void setPayParentId(String payParentId) {
        this.entries.put(ProjectShare.payParentId, payParentId);
    }
    public BigDecimal getMoney() {
        String money = this.entries.getString(ProjectShare.money);
        if (null == money || "".equals(money.trim())) {
            return null;
        }
        return new BigDecimal(money);
    }

    public void setMoney(BigDecimal money) {
        this.entries.put(ProjectShare.money, null == money ? null : money.toString());
    }

    public String getRemark() {
        return this.entries.getString(remark);
    }

    public Integer getType() {
        return this.entries.getInteger(type);
    }

    public void setRemark(String remark) {
        this.entries.put(ProjectShare.remark, remark);
    }
}
