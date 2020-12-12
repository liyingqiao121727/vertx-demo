package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author xue
 * @since 2019-09-05
 */
public class GlodonCost extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "glodon_cost";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(projId, "proj_id");put(orderNo, "order_no");put(params, "params");
        put(standard, "standard");put(unit, "unit");put(name, "name");
        put(totalAmount, "total_amount");put(aliasNo, "alias_no");put(type, "type");
        put(totalUnitPrice, "total_unit_price");put(totalMoney, "total_money");
        put(targetAmount, "target_amount");put(targetUnitPrice, "target_unit_price");
        put(targetMoney, "target_money");put(settleAmount, "settle_amount");
        put(settleUnitPrice, "settle_unit_price");put(rowNo, "row_no");
        put(settleMoney, "settle_money");put(remark, "remark");put(alias, "alias");
        put(actualAmount, "actual_amount");put(actualUnitPrice, "actual_unit_price");
        put(actualMoney, "actual_money");
    }};

    static {
        GlodonCost.map.putAll(BaseModel.map);
    }

    /**
     * 所属项目id
     */
    @SwaggerProperty(value = "所属项目id")
    private static final String projId = "projId";
    /**
     * 序号
     */
    @SwaggerProperty(value = "序号")
    private static final String orderNo = "orderNo";
    /**
     * 项目名称
     */
    @SwaggerProperty(value = "项目名称")
    private static final String name = "name";
    /**
     * 参数
     */
    @SwaggerProperty(value = "参数")
    private static final String params = "params";
    /**
     * 规格型号
     */
    @SwaggerProperty(value = "规格型号")
    private static final String standard = "standard";
    /**
     * 单位
     */
    @SwaggerProperty(value = "单位")
    private static final String unit = "unit";
    /**
     * 总承包合同-工程量
     */
    @SwaggerProperty(value = "总承包合同-工程量")
    private static final String totalAmount = "totalAmount";
    /**
     * 总承包合同-单价
     */
    @SwaggerProperty(value = "总承包合同-单价")
    private static final String totalUnitPrice = "totalUnitPrice";
    /**
     * 总承包合同-金额
     */
    @SwaggerProperty(value = "总承包合同-金额")
    private static final String totalMoney = "totalMoney";
    /**
     * 目标成本-工程量
     */
    @SwaggerProperty(value = "目标成本-工程量")
    private static final String targetAmount = "targetAmount";
    /**
     * 目标成本-单价
     */
    @SwaggerProperty(value = "目标成本-单价")
    private static final String targetUnitPrice = "targetUnitPrice";
    /**
     * 目标成本-金额
     */
    @SwaggerProperty(value = "目标成本-金额")
    private static final String targetMoney = "targetMoney";
    /**
     * 实际成本-工程量
     */
    @SwaggerProperty(value = "实际成本-工程量")
    private static final String actualAmount = "actualAmount";
    /**
     * 实际成本-单价
     */
    @SwaggerProperty(value = "实际成本-单价")
    private static final String actualUnitPrice = "actualUnitPrice";
    /**
     * 实际成本-金额
     */
    @SwaggerProperty(value = "实际成本-金额")
    private static final String actualMoney = "actualMoney";
    /**
     * 结算金额-工程量
     */
    @SwaggerProperty(value = "结算金额-工程量")
    private static final String settleAmount = "settleAmount";
    /**
     * 结算金额-单价
     */
    @SwaggerProperty(value = "结算金额-单价")
    private static final String settleUnitPrice = "settleUnitPrice";
    /**
     * 结算金额-金额
     */
    @SwaggerProperty(value = "结算金额-金额")
    private static final String settleMoney = "settleMoney";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 别名
     */
    @SwaggerProperty(value = "别名")
    private static final String alias = "alias";
    /**
     * 序号别名
     */
    @SwaggerProperty(value = "序号别名")
    private static final String aliasNo = "aliasNo";
    /**
     * 行号
     */
    @SwaggerProperty(value = "行号")
    private static final String rowNo = "rowNo";
    /**
     * 类型
     */
    @SwaggerProperty(value = "类型 类型 1.人工费(总);2.主材费;3.设备费;4.机械费;5.辅材;6.措施费;7.其他(风险费用);8.其他",
            type = Integer.class)
    private static final String type = "type";

    public GlodonCost(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public GlodonCost() {
        super(null, TABLE);
    }

    public GlodonCost(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public static String deleteByProjId(String projId, String type, JsonArray param) {
        param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder(" delete from " + TABLE + " where proj_id = ? ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(type);
        }
        return sqlBuilder.append(" ; ").toString();
    }

    public static String getSumByProjId(String projId, String type, JsonArray param) {
        /*param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder(" select ").append(" sum(total_amount) as totalAmount, " +
                " sum(total_unit_price) as totalUnitPrice, sum(total_money) as totalMoney, " +
                " sum(target_amount) as targetAmount, sum(target_unit_price) as targetUnitPrice, " +
                " sum(target_money) as targetMoney, " +
                " sum(settle_amount) as settleAmount, sum(settle_unit_price) as settleUnitPrice, " +
                " sum(settle_money) as settleMoney from " + TABLE + " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(Short.parseShort(type));
        }
        return sqlBuilder.append(" ; ").toString();*/
        param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder(" select ").append(" sum(aa.totalAmount) as totalAmount, " +
                " sum(aa.totalUnitPrice) as totalUnitPrice, sum(aa.totalMoney) as totalMoney, " +
                " sum(aa.targetAmount) as targetAmount, sum(aa.targetUnitPrice) as targetUnitPrice, " +
                " sum(aa.targetMoney) as targetMoney, " +
                " sum(aa.settleAmount) as settleAmount, sum(aa.settleUnitPrice) as settleUnitPrice, " +
                " sum(aa.settleMoney) as settleMoney, " +
                " sum(aa.actualAmount) as actualAmount, sum(aa.actualUnitPrice) as actualUnitPrice, " +
                " sum(aa.actualMoney) as actualMoney from " +
                "( ( select ").append(baseColumnListJoinA()).append(
                " , a.row_no as rowNo, p.number as actualAmount, p.unit_price as actualUnitPrice, " +
                        " p.total as actualMoney from ( select * from " + TABLE +
                        " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(Short.parseShort(type));
        }
        param.add(projId).add(projId);
        sqlBuilder.append(" ) a left join ( select pp.*, ga.`type` as ga_type, ga.alias as ga_alias from " +
                " ( select * from " + Purchase.TABLE +
                " where project_id = ? and del_flag = 0) pp left join " + GlodonAlias.TABLE +
                " ga on ga.origin_name = pp.`type` and ga.del_flag = 0 ) p " +
                " on a.`alias` = p.`name` and a.`type` = p.ga_type ) ")
                .append(" union ").append(" ( select ").append(baseColumnListJoinA()).append(
                " , a.row_no as rowNo" +
                " , p.number as actualAmount, p.unit_price as actualUnitPrice, p.total as actualMoney " +
                " from ( select * from " + TABLE + " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(Short.parseShort(type));
        }
        param.add(projId);
        sqlBuilder.append(" ) a right join ( select pp.*, ga.`type` as ga_type, ga.alias as ga_alias from " +
                " ( select * from " + Purchase.TABLE +
                " where project_id = ? and del_flag = 0) pp left join " + GlodonAlias.TABLE +
                " ga on ga.origin_name = pp.`type` and ga.del_flag = 0 ) p " +
                " on a.`alias` = p.`name` and a.`type` = p.ga_type ) ")
                .append(" ) aa where aa.rowNo is not null ");
        if (null != type) {
            sqlBuilder.append(" and ( aa.type is null or aa.type = ? ) ");
            param.add(Short.parseShort(type));
        }
        sqlBuilder.append(" ; ");

        return sqlBuilder.toString();
    }

    public static String getSumByProjId1(String projId, Short type, JsonArray param) {
        param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder("  select ")
                .append(" sum(a.total_amount) as totalAmount, " +
                        " sum(a.total_unit_price) as totalUnitPrice, sum(a.total_money) as totalMoney, " +
                        " sum(a.target_amount) as targetAmount, sum(a.target_unit_price) as targetUnitPrice, " +
                        " sum(a.target_money) as targetMoney, " +
                        " sum(a.settle_amount) as settleAmount, sum(a.settle_unit_price) as settleUnitPrice, " +
                        " sum(a.settle_money) as settleMoney, " +
                        " sum(a.actual_amount) as actualAmount, sum(a.actual_unit_price) as actualUnitPrice, " +
                        " sum(a.actual_money) as actualMoney from ").append(TABLE)
                .append(" a where a.proj_id = ? and a.del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and a.type = ? ");
            param.add(type);
        }
        sqlBuilder.append("  order by a.type, a.row_no ");
        sqlBuilder.append(" ; ");

        return sqlBuilder.toString();
    }

    public static String getAllColByProjIdType(String projId, Short type, JsonArray param) {
        param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder("  select ")
                .append(baseColumnListJoinA1()).append(
                        " , a.row_no as rowNo, a.actual_amount as actualAmount, a.actual_unit_price as actualUnitPrice, " +
                                " a.actual_money as actualMoney from ( select * from " + TABLE +
                                " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(type);
        }
        sqlBuilder.append(" ) a order by a.type, a.row_no ");
        sqlBuilder.append(" ; ");

        return sqlBuilder.toString();
    }

    public static String getByProjId(String projId, String type, JsonArray param) {
        Short typeShort = null;
        if (null != type) {
            typeShort = Short.parseShort(type);
        }
        return getCostAndPayByProjId(projId, typeShort, param);
    }

    public static String getCostAndPayByProjId(String projId, Short type, JsonArray param) {
        param.add(projId);
        StringBuilder sqlBuilder = new StringBuilder(" select * from ( ( select ")
                .append(baseColumnListJoinA()).append(
                " , a.row_no as rowNo, p.number as actualAmount, p.unit_price as actualUnitPrice, " +
                        " p.total as actualMoney from ( select * from " + TABLE +
                        " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(type);
        }
        param.add(projId).add(projId);
        sqlBuilder.append(" ) a left join ( select pp.*, ga.`type` as ga_type, ga.alias as ga_alias from " +
                " ( select * from " + Purchase.TABLE +
                " where project_id = ? and del_flag = 0) pp left join " + GlodonAlias.TABLE +
                " ga on ga.origin_name = pp.`type` and ga.del_flag = 0 ) p " +
                " on a.`alias` = p.`name` and a.`type` = p.ga_type ) ")
                .append(" union ").append(" ( select ").append(baseColumnListJoinA()).append(
                " , (case when a.row_no is null then ( select count(*) from " + TABLE + " where proj_id = ? " +
                        " and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(type);
        }
        param.add(projId);
        sqlBuilder.append(") end ) as rowNo" +
                " , p.number as actualAmount, p.unit_price as actualUnitPrice, p.total as actualMoney " +
                " from ( select * from " + TABLE + " where proj_id = ? and del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and type = ? ");
            param.add(type);
        }
        param.add(projId);
        sqlBuilder.append(" ) a right join ( select pp.*, ga.`type` as ga_type, ga.alias as ga_alias from " +
                " ( select * from " + Purchase.TABLE +
                " where project_id = ? and del_flag = 0) pp left join " + GlodonAlias.TABLE +
                " ga on ga.origin_name = pp.`type` and ga.del_flag = 0 ");
        if (null != type) {
            sqlBuilder.append(" and ga.`type` = ? ");
            param.add(type);
        }
        sqlBuilder.append(") p on a.`alias` = p.`name` and a.`type` = p.ga_type ) ")
                .append(" order by `type`, rowNo asc ) aa where aa.rowNo is not null ");
        if (null != type) {
            sqlBuilder.append(" and ( aa.type is null or aa.type = ? ) ");
            param.add(type);
        }
        sqlBuilder.append(" ; ");

        return sqlBuilder.toString();
    }

    public static String baseColumnList() {
        return " id, proj_id as projId, order_no as orderNo, params, standard, unit, name, remark, alias, " +
                " total_amount as totalAmount, total_unit_price as totalUnitPrice, total_money as totalMoney, " +
                " target_amount as targetAmount, target_unit_price as targetUnitPrice, target_money as targetMoney, " +
                " settle_amount as settleAmount, settle_unit_price as settleUnitPrice, settle_money as settleMoney, " +
                " alias_no as aliasNo, `type`, row_no as rowNo, " + BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinA() {
        return " a.id, (case when a.proj_id is null then p.project_id else a.proj_id end) as projId, " +
                " a.order_no as orderNo, a.params, a.standard, a.unit, " +
                " (case when a.`name` is null then p.`name` else a.`name` end) as `name`, " +
                " a.remark, " +
                " (case when a.alias is null then p.`name` else a.alias end) as alias, " +
                " a.total_amount as totalAmount, a.total_unit_price as totalUnitPrice, a.total_money as totalMoney, " +
                " a.target_amount as targetAmount, a.target_unit_price as targetUnitPrice, a.target_money as targetMoney, " +
                " a.settle_amount as settleAmount, a.settle_unit_price as settleUnitPrice, a.settle_money as settleMoney, " +
                " a.alias_no as aliasNo, " +
                " ( case when a.`type` is null then " +
                "     ( case when p.ga_type is null then 100 else p.ga_type end ) else a.`type` end ) as `type`, " +
                " a.gmt_create as gmtCreate ";
    }

    public static String baseColumnListJoinA1() {
        return " a.id, a.proj_id as projId, " +
                " a.order_no as orderNo, a.params, a.standard, a.unit, " +
                " a.`name` as `name`, " +
                " a.remark, " +
                " a.alias as alias, " +
                " a.total_amount as totalAmount, a.total_unit_price as totalUnitPrice, a.total_money as totalMoney, " +
                " a.target_amount as targetAmount, a.target_unit_price as targetUnitPrice, a.target_money as targetMoney, " +
                " a.settle_amount as settleAmount, a.settle_unit_price as settleUnitPrice, a.settle_money as settleMoney, " +
                " a.alias_no as aliasNo, " +
                " a.`type` as `type`, " +
                " a.gmt_create as gmtCreate ";
    }

    public static Map<String, GlodonCost> result2NameMap(ResultSet result) {
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
        Map<String, GlodonCost> map = new HashMap<>(result.getNumRows());
        List<JsonArray> list = result.getResults();
        JsonObject jsonObject = null;
        GlodonCost glodonCost = null;
        for (JsonArray jsonArray : list) {
            jsonObject = new JsonObject();
            for (int j = 0; j < jsonArray.size(); j++) {
                jsonObject.put(cols[j], jsonArray.getValue(j));
            }
            glodonCost = new GlodonCost(jsonObject);
            map.put(jsonArray.getString(nameIndex), glodonCost);
        }

        return map;
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    public String getProjId() {
        return entries.getString(projId);
    }

    public String getOrderNo() {
        return entries.getString(orderNo);
    }

    public String getName() {
        return entries.getString(name);
    }

    public String getParams() {
        return entries.getString(params);
    }

    public String getStandard() {
        return entries.getString(standard);
    }

    public String getUnit() {
        return entries.getString(unit);
    }

    public String getTotalAmount() {
        return entries.getString(totalAmount);
    }

    public String getTotalUnitPrice() {
        return entries.getString(totalUnitPrice);
    }

    public String getTotalMoney() {
        return entries.getString(totalMoney);
    }

    public String getTargetAmount() {
        return entries.getString(targetAmount);
    }

    public String getTargetUnitPrice() {
        return entries.getString(targetUnitPrice);
    }

    public String getTargetMoney() {
        return entries.getString(targetMoney);
    }

    public String getSettleAmount() {
        return entries.getString(settleAmount);
    }

    public String getSettleUnitPrice() {
        return entries.getString(settleUnitPrice);
    }

    public String getSettleMoney() {
        return entries.getString(settleMoney);
    }

    public String getRemark() {
        return entries.getString(remark);
    }

    public Integer getType() {
        return entries.getInteger(type);
    }

    public String getAlias() {
        return entries.getString(alias);
    }

    public String getAliasNo() {
        return entries.getString(aliasNo);
    }

    public void setProjId(String projId) {
        this.entries.put(GlodonCost.projId, projId);
    }

    public void setOrderNo(String orderNo) {
        this.entries.put(GlodonCost.orderNo, orderNo);
    }

    public void setName(String name) {
        this.entries.put(GlodonCost.name, name);
    }

    public void setParams(String params) {
        this.entries.put(GlodonCost.params, params);
    }

    public void setStandard(String standard) {
        this.entries.put(GlodonCost.standard, standard);
    }

    public void setUnit(String unit) {
        this.entries.put(GlodonCost.unit, unit);
    }

    public void setTotalAmount(String totalAmount) {
        this.entries.put(GlodonCost.totalAmount, totalAmount);
    }

    public void setTotalUnitPrice(String totalUnitPrice) {
        this.entries.put(GlodonCost.totalUnitPrice, totalUnitPrice);
    }

    public void setTotalMoney(String totalMoney) {
        this.entries.put(GlodonCost.totalMoney, totalMoney);
    }

    public void setTargetAmount(String targetAmount) {
        this.entries.put(GlodonCost.targetAmount, targetAmount);
    }

    public void setTargetUnitPrice(String targetUnitPrice) {
        this.entries.put(GlodonCost.targetUnitPrice, targetUnitPrice);
    }

    public void setTargetMoney(String targetMoney) {
        this.entries.put(GlodonCost.targetMoney, targetMoney);
    }

    public void setRemark(String remark) {
        this.entries.put(GlodonCost.remark, remark);
    }

    public void setAlias(String alias) {
        this.entries.put(GlodonCost.alias, alias);
    }

    public void setAliasNo(String aliasNo) {
        this.entries.put(GlodonCost.aliasNo, aliasNo);
    }

    public void setType(Integer type) {
        this.entries.put(GlodonCost.type, type);
    }

    public void setSettleAmount(String settleAmount) {
        this.entries.put(GlodonCost.settleAmount, settleAmount);
    }

    public void setSettleUnitPrice(String settleUnitPrice) {
        this.entries.put(GlodonCost.settleUnitPrice, settleUnitPrice);
    }

    public void setSettleMoney(String settleMoney) {
        this.entries.put(GlodonCost.settleMoney, settleMoney);
    }

    public void setRowNo(Long rowNo) {
        entries.put(GlodonCost.rowNo, rowNo);
    }

    public String getActualAmount() {
        return entries.getString(actualAmount);
    }

    public String getActualUnitPrice() {
        return entries.getString(actualUnitPrice);
    }

    public String getActualMoney() {
        return entries.getString(actualMoney);
    }

    public void setActualAmount(String actualAmount) {
        entries.put(GlodonCost.actualAmount, actualAmount);
    }

    public void setActualUnitPrice(String actualUnitPrice) {
        entries.put(GlodonCost.actualUnitPrice, actualUnitPrice);
    }

    public void setActualMoney(String actualMoney) {
        entries.put(GlodonCost.actualMoney, actualMoney);
    }
}
