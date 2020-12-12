package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;

/**
 * <p>
 * 
 * </p>
 *
 * @author xue
 * @since 2019-12-24
 */
public class ProjectTotal extends BaseModel {

    private static final long serialVersionUID = 1L;


    public static final String TABLE = "project_total";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(projId, "proj_id");
        put(name, "name");
        put(agreeMoney, "agree_money");
        put(budgetCost, "budget_cost");
        put(actualCost, "actual_cost");
        put(settleMoney, "settle_money");
        put(budgetGrossProfit, "budget_gross_profit");
        put(settleGrossProfit, "settle_gross_profit");
        put(remark, "remark");
        put(seqNo, "seq_no");
    }};

    static {
        ProjectTotal.map.putAll(BaseModel.map);
    }

    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";
    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";
    /**
     * 合同金额
     */
    @SwaggerProperty(value = "合同金额")
    private static final String agreeMoney = "agreeMoney";
    /**
     * 预算成本
     */
    @SwaggerProperty(value = "预算成本")
    private static final String budgetCost = "budgetCost";
    /**
     * 实际成本
     */
    @SwaggerProperty(value = "实际成本")
    private static final String actualCost = "actualCost";
    /**
     * 结算金额
     */
    @SwaggerProperty(value = "结算金额")
    private static final String settleMoney = "settleMoney";
    /**
     * 预算毛利
     */
    @SwaggerProperty(value = "预算毛利")
    private static final String budgetGrossProfit = "budgetGrossProfit";
    /**
     * 结算毛利
     */
    @SwaggerProperty(value = "结算毛利")
    private static final String settleGrossProfit = "settleGrossProfit";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 备注
     */
    @SwaggerProperty(value = "序号")
    private static final String seqNo = "seqNo";

    public ProjectTotal(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public ProjectTotal() {
        super(null, TABLE);
    }

    public ProjectTotal(boolean init) {
        super(null, TABLE, init);
    }

    public static String baseColumnList() {
        return " id, proj_id as projId, `name`, agree_money as agreeMoney, budget_cost as budgetCost, " +
                " actual_cost as actualCost, settle_money as settleMoney, budget_gross_profit as budgetGrossProfit, " +
                " settle_gross_profit as settleGrossProfit, `remark`, " + BaseModel.baseColumnList();
    }

    public static String getByProjId() {
        return " select " + baseColumnList() + " from " + TABLE + " where proj_id = ? and del_flag = 0 order by seq_no ; ";
    }

    public static String deleteByProjId() {
        return " delete from " + TABLE + " where proj_id = ? ; ";
    }

    @Override
    public HashMap<String, String> getMap() {
        return map;
    }

    public void setProjId(String projId) {
        entries.put(ProjectTotal.projId, projId);
    }

    public void setName(String name) {
        entries.put(ProjectTotal.name, name);
    }

    public void setAgreeMoney(String agreeMoney) {
        entries.put(ProjectTotal.agreeMoney, agreeMoney);
    }

    public void setBudgetCost(String budgetCost) {
        entries.put(ProjectTotal.budgetCost, budgetCost);
    }

    public void setActualCost(String actualCost) {
        entries.put(ProjectTotal.actualCost, actualCost);
    }

    public void setSettleMoney(String settleMoney) {
        entries.put(ProjectTotal.settleMoney, settleMoney);
    }

    public void setBudgetGrossProfit(String budgetGrossProfit) {
        entries.put(ProjectTotal.budgetGrossProfit, budgetGrossProfit);
    }

    public void setSettleGrossProfit(String settleGrossProfit) {
        entries.put(ProjectTotal.settleGrossProfit, settleGrossProfit);
    }

    public void setRemark(String remark) {
        entries.put(ProjectTotal.remark, remark);
    }

    public void setSeqNo(Integer seqNo) {
        entries.put(ProjectTotal.seqNo, seqNo);
    }

    public String getProjId() {
        return entries.getString(projId);
    }

    public String getName() {
        return entries.getString(name);
    }

    public String getAgreeMoney() {
        return entries.getString(agreeMoney);
    }

    public String getBudgetCost() {
        return entries.getString(budgetCost);
    }

    public String getActualCost() {
        return entries.getString(actualCost);
    }

    public String getSettleMoney() {
        return entries.getString(settleMoney);
    }

    public String getBudgetGrossProfit() {
        return entries.getString(budgetGrossProfit);
    }

    public String getSettleGrossProfit() {
        return entries.getString(settleGrossProfit);
    }

    public String getRemark() {
        return entries.getString(remark);
    }

    public Integer getSeqNo() {
        return entries.getInteger(seqNo);
    }
}
