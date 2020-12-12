package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author xue
 * @since 2019-12-20
 */
public class MeasureFee extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "measure_fee";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(landName, "land_name");
        put(projId, "proj_id");
        put(typeId, "type_id");
        put(typeName, "type_name");
        put(typeSubId, "type_sub_id");
        put(typeSubName, "type_sub_name");
        put(content, "content");
        put(amount, "amount");
        put(unitPrice, "unit_price");
        put(actualSubTotal, "actual_sub_total");
        put(actualMoney, "actual_money");
        put(actualTotal, "actual_total");
        put(budgetMoney, "budget_money");
        put(budgetTotal, "budget_total");
        put(budgetSubTotal, "budget_sub_total");
        put(remark, "remark");
        put(time, "time");
    }};

    static {
        MeasureFee.map.putAll(BaseModel.map);
    }

    /**
     * 地块
     */
    @SwaggerProperty(value ="地块")
    private static final String landName = "landName";
    /**
     * 项目id
     */
    @SwaggerProperty(value ="项目id")
    private static final String projId = "projId";
    /**
     * 类别id
     */
    @SwaggerProperty(value ="类别id")
    private static final String typeId = "typeId";
    /**
     * 类型名称
     */
    @SwaggerProperty(value ="类型名称")
    private static final String typeName = "typeName";
    /**
     * 类别id
     */
    @SwaggerProperty(value ="子类别id")
    private static final String typeSubId = "typeSubId";
    /**
     * 类型名称
     */
    @SwaggerProperty(value ="子类型名称")
    private static final String typeSubName = "typeSubName";
    /**
     * 内容
     */
    @SwaggerProperty(value ="内容")
    private static final String content = "content";
    /**
     * 数量
     */
    @SwaggerProperty(value ="数量")
    private static final String amount = "amount";
    /**
     * 单位
     */
    @SwaggerProperty(value ="单位")
    private static final String unitPrice = "unitPrice";
    /**
     * 实际-单价
     */
    @SwaggerProperty(value ="实际-小计")
    private static final String actualSubTotal = "actualSubTotal";
    /**
     * 实际-金额
     */
    @SwaggerProperty(value ="实际-金额")
    private static final String actualMoney = "actualMoney";
    /**
     * 实际合计
     */
    @SwaggerProperty(value ="实际合计")
    private static final String actualTotal = "actualTotal";
    /**
     * 预算金额
     */
    @SwaggerProperty(value ="预算金额")
    private static final String budgetMoney = "budgetMoney";
    /**
     * 预算合计
     */
    @SwaggerProperty(value ="预算小计")
    private static final String budgetSubTotal = "budgetSubTotal";
    /**
     * 预算合计
     */
    @SwaggerProperty(value ="预算合计")
    private static final String budgetTotal = "budgetTotal";
    /**
     * 备注
     */
    @SwaggerProperty(value ="备注")
    private static final String remark = "remark";
    /**
     * 时间
     */
    @SwaggerProperty(value ="时间")
    private static final String time = "time";

    public MeasureFee(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public MeasureFee() {
        super(null, TABLE);
    }

    public MeasureFee(JsonObject object, boolean init) {
        super(object, TABLE, init);
    }

    public static String deleteByProjId() {
        return " delete from " + TABLE + " where proj_id = ? ; ";
    }

    public static String sumByProjIdsAndTime(JsonArray projIds, String time, JsonArray params1) {
        StringBuilder sb = new StringBuilder(" select proj_id as projId, sum(actual_total) as actualTotal from ")
                .append(TABLE).append(" where 1=1 ");
        if (null != projIds) {
            sb.append(" and proj_id in (   ");
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < projIds.size(); i++) {
                sb1.append(" ,? ");
                params1.add(projIds.getValue(i));
            }
            sb = sb.append(sb1.length() < 2 ? "" : sb1.substring(2)).append(" ) ");
        }
        if (null != time) {
            sb.append(" and `time` like ? ");
            params1.add(time + "%");
        }
        sb.append(" and type_name = '" + Constant.TOTAL + "' and del_flag = 0 group by proj_id; ");
        return sb.toString();
    }

    public static void fillMap(ResultSet resultSet, Map<String, String> map) {
        if (null == resultSet || resultSet.getNumRows() < 1) {
            return;
        }
        List<JsonObject> list = resultSet.getRows();
        MeasureFee fee = null;
        for (JsonObject object : list) {
            fee = new MeasureFee(object, false);
            map.put(fee.getProjId(), fee.getActualTotal());
        }
    }

    @Override
    public HashMap<String, String> getMap() {
        return map;
    }

    public static String baseColumnList() {
        return " id, land_name as landName, proj_id as projId, type_id as typeId, " +
                " type_name as typeName, type_sub_id as typeSubId, type_sub_name as typeSubName, " +
                " `content`, `amount`, unit_price as unitPrice, actual_money as actualMoney, " +
                " actual_sub_total as actualSubTotal, actual_total as actualTotal, " +
                " budget_money as budgetMoney, budget_sub_total as budgetSubTotal, " +
                " budget_total as budgetTotal, `remark`, `time`, " + BaseModel.baseColumnList();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnList()).append(" from ")
                .append(TABLE).append(" where del_flag = 0 ");
        if (null != this.getProjId()) {
            sb.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }
        if (null != this.getTime()) {
            sb.append(" and `time` = ? ");
            params.add(this.getTime());
        }
        if (null != this.getTypeId()) {
            sb.append(" and type_id = ? ");
            params.add(this.getTypeId());
        }
        if (null != this.getTypeName()) {
            sb.append(" and type_name like ? ");
            params.add("%" + this.getTypeName() + "%");
        }
        if (null != this.getContent()) {
            sb.append(" and `content` like ? ");
            params.add("%" + this.getContent() + "%");
        }
        if (null != this.getLandName()) {
            sb.append(" and `land_name` like ? ");
            params.add("%" + this.getLandName() + "%");
        }
        sb.append(sqLpage.toString());
        return sb.append(" ; ").toString();
    }

    @Override
    public String countPage(JsonArray params) {
        StringBuilder sb = new StringBuilder(" select count(*) from ").append(TABLE)
                .append(" where del_flag = 0 ");
        if (null != this.getProjId()) {
            sb.append(" and proj_id = ? ");
            params.add(this.getProjId());
        }
        if (null != this.getTime()) {
            sb.append(" and `time` = ? ");
            params.add(this.getTime());
        }
        if (null != this.getTypeId()) {
            sb.append(" and type_id = ? ");
            params.add(this.getTypeId());
        }
        if (null != this.getTypeName()) {
            sb.append(" and type_name like ? ");
            params.add("%" + this.getTypeName() + "%");
        }
        if (null != this.getContent()) {
            sb.append(" and `content` like ? ");
            params.add("%" + this.getContent() + "%");
        }
        if (null != this.getLandName()) {
            sb.append(" and `land_name` like ? ");
            params.add("%" + this.getLandName() + "%");
        }
        return sb.append(" ; ").toString();
    }

    public void setLandName(String landName) {
        entries.put(MeasureFee.landName, landName);
    }

    public void setProjId(String projId) {
        entries.put(MeasureFee.projId, projId);
    }

    public void setTypeId(String typeId) {
        entries.put(MeasureFee.typeId, typeId);
    }

    public void setTypeName(String typeName) {
        entries.put(MeasureFee.typeName, typeName);
    }

    public void setTypeSubId(String typeSubId) {
        entries.put(MeasureFee.typeSubId, typeSubId);
    }

    public void setTypeSubName(String typeSubName) {
        entries.put(MeasureFee.typeSubName, typeSubName);
    }

    public void setContent(String content) {
        entries.put(MeasureFee.content, content);
    }

    public void setAmount(String amount) {
        entries.put(MeasureFee.amount, amount);
    }

    public void setUnitPrice(String unitPrice) {
        entries.put(MeasureFee.unitPrice, unitPrice);
    }

    public void setActualSubTotal(String actualSubTotal) {
        entries.put(MeasureFee.actualSubTotal, actualSubTotal);
    }

    public void setActualMoney(String actualMoney) {
        entries.put(MeasureFee.actualMoney, actualMoney);
    }

    public void setActualTotal(String actualTotal) {
        entries.put(MeasureFee.actualTotal, actualTotal);
    }

    public void setBudgetMoney(String budgetMoney) {
        entries.put(MeasureFee.budgetMoney, budgetMoney);
    }

    public void setBudgetSubTotal(String budgetSubTotal) {
        entries.put(MeasureFee.budgetSubTotal, budgetSubTotal);
    }

    public void setBudgetTotal(String budgetTotal) {
        entries.put(MeasureFee.budgetTotal, budgetTotal);
    }

    public void setRemark(String remark) {
        entries.put(MeasureFee.remark, remark);
    }

    public void setTime(String time) {
        entries.put(MeasureFee.time, time);
    }

    public String getLandName() {
        return entries.getString(landName);
    }

    public String getProjId() {
        return entries.getString(projId);
    }

    public String getTypeId() {
        return entries.getString(typeId);
    }

    public String getTypeName() {
        return entries.getString(typeName);
    }

    public String getTypeSubId() {
        return entries.getString(typeSubId);
    }

    public String getTypeSubName() {
        return entries.getString(typeSubName);
    }

    public String getContent() {
        return entries.getString(content);
    }

    public String getAmount() {
        return entries.getString(amount);
    }

    public String getUnitPrice() {
        return entries.getString(unitPrice);
    }

    public String getActualSubTotal() {
        return entries.getString(actualSubTotal);
    }

    public String getActualMoney() {
        return entries.getString(actualMoney);
    }

    public String getActualTotal() {
        return entries.getString(actualTotal);
    }

    public String getBudgetMoney() {
        return entries.getString(budgetMoney);
    }

    public String getBudgetSubTotal() {
        return entries.getString(budgetSubTotal);
    }

    public String getBudgetTotal() {
        return entries.getString(budgetTotal);
    }

    public String getRemark() {
        return entries.getString(remark);
    }

    public String getTime() {
        return entries.getString(time);
    }
}
