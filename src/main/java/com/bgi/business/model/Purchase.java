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
 * 其他材料表
 * </p>
 *
 * @author xue
 * @since 2019-07-17
 */

public class Purchase extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "purchase";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(type, "type");put(unit, "unit");put(desc, "desc");
        put(unitPrice, "unit_price");put(total, "total");put(number, "number");
        put(projectId, "project_id");put(userId, "user_id");put(purchaseTime, "purchase_time");
        put(remark, "remark");
    }};

    static {
        Purchase.map.putAll(BaseModel.map);
    }

    /**
     * 类别
     */
    @SwaggerProperty(value = "类别")
    private static final String type = "type";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";

    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";
    /**
     * 单位
     */
    @SwaggerProperty(value = "单位")
    private static final String unit = "unit";
    /**
     * 用途
     */
    @SwaggerProperty(value = "用途")
    private static final String desc = "desc";
    /**
     * 单价
     */
    @SwaggerProperty(value = "单价")
    public static final String unitPrice = "unitPrice";
    /**
     * 总价
     */
    @SwaggerProperty(value = "总价")
    public static final String total = "total";
    /**
     * 数量
     */
    @SwaggerProperty(value = "数量")
    private static final String number = "number";
    /**
     * 项目主键
     */
    @SwaggerProperty(value = "项目主键")
    private static final String projectId = "projectId";
    /**
     * 经办人
     */
    @SwaggerProperty(value = "经办人")
    private static final String userId = "userId";
    /**
     * 采购时间
     */
    @SwaggerProperty(value = "采购时间")
    private static final String purchaseTime = "purchaseTime";

    public Purchase(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public Purchase(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public Purchase() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, `type`, `name`, unit, `desc`, unit_price as unitPrice, total, `number`, " +
                " project_id as projectId, user_id as userId, purchase_time as purchaseTime, " +
                BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinA() {
        return " a.id, a.`type`, a.`name`, a.unit, a.`desc`, a.unit_price as unitPrice, a.total, a.`number`, " +
                " a.project_id as projectId, a.user_id as userId, a.purchase_time as purchaseTime ";
    }

    public static String insertBatch(List<Purchase> list, JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE)
                .append(" ( id, `type`, `name`, unit, `desc`, unit_price, total, `number`, project_id, user_id, ")
                .append(BaseModel.baseColumn()).append(" ) values      ");
        for (Purchase purchase : list) {
            sb.append(" ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ");
            params.add(purchase.getId()).add(purchase.getType()).add(purchase.getName()).add(purchase.getUnit()).add(purchase.getDesc())
                    .add(purchase.entries.getString(unitPrice)).add(purchase.entries.getString(total))
                    .add(purchase.getNumber()).add(purchase.getProjectId()).add(purchase.getUserId());
            purchase.addParams(params);
        }
        return sb.replace(sb.length()-3, sb.length()-1, " ; ").toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + ", u.real_name as realName from " + TABLE +
                " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getProjectId() && !"".equals(this.getProjectId().trim())) {
            sb.append(" and `project_id` = ? ");
            params.add(this.getProjectId());
        }
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and `name` = ? ");
            params.add(this.getName());
        }
        if (null != this.getType() && !"".equals(this.getType().trim())) {
            sb.append(" and `type` = ? ");
            params.add(this.getType());
        }
        if (null != this.getPurchaseTime() && !"".equals(this.getPurchaseTime().trim())) {
            sb.append(" and `purchase_time` = ? ");
            params.add(this.getPurchaseTime());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid left join ")
                .append(EarthEnergyUser.TABLE).append(" u on a.user_id = u.id ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getProjectId() && !"".equals(this.getProjectId().trim())) {
            sb.append(" and `project_id` = ? ");
            params.add(this.getProjectId());
        }
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and `name` = ? ");
            params.add(this.getName());
        }
        if (null != this.getType() && !"".equals(this.getType().trim())) {
            sb.append(" and `type` = ? ");
            params.add(this.getType());
        }
        if (null != this.getPurchaseTime() && !"".equals(this.getPurchaseTime().trim())) {
            sb.append(" and `purchase_time` = ? ");
            params.add(this.getPurchaseTime());
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

    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(Purchase.name, name);
    }

    public String getType() {
        return this.entries.getString(type);
    }

    public void setType(String type) {
        this.entries.put(Purchase.type, type);
    }
    public String getUnit() {
        return this.entries.getString(unit);
    }

    public void setUnit(String unit) {
        this.entries.put(Purchase.unit, unit);
    }
    public String getDesc() {
        return this.entries.getString(desc);
    }

    public void setDesc(String desc) {
        this.entries.put(Purchase.desc, desc);
    }
    public BigDecimal getUnitPrice() {
        String unitPrice = this.entries.getString(Purchase.unitPrice);
        if (null == unitPrice || "".equals(unitPrice.trim())) {
            return null;
        }
        return new BigDecimal(unitPrice);
    }

    public void setUnitPrice(String unitPrice) {
        this.entries.put(Purchase.unitPrice, unitPrice);
    }
    public BigDecimal getTotal() {
        String total = this.entries.getString(Purchase.total);
        if (null == total || "".equals(total.trim())) {
            return null;
        }
        return new BigDecimal(total);
    }

    public void setTotal(String total) {
        this.entries.put(Purchase.total, total);
    }
    public String getNumber() {
        return this.entries.getString(number);
    }

    public String getPurchaseTime() {
        return this.entries.getString(purchaseTime);
    }

    public void setNumber(Integer number) {
        this.entries.put(Purchase.number, number);
    }
    public String getProjectId() {
        return this.entries.getString(projectId);
    }

    public void setProjectId(String projectId) {
        this.entries.put(Purchase.projectId, projectId);
    }
    public String getUserId() {
        return this.entries.getString(userId);
    }

    public void setUserId(String userId) {
        this.entries.put(Purchase.userId, userId);
    }

    public void setRemark(String remark) {
        this.entries.put(Purchase.remark, remark);
    }

    public void setPurchaseTime(String purchaseTime) {
        this.entries.put(Purchase.purchaseTime, purchaseTime);
    }

}
