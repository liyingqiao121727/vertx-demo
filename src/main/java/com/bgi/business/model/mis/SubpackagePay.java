package com.bgi.business.model.mis;

import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.bgi.BGIdic;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
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

public class SubpackagePay extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "ep_sub_pay";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");
        put(name, "fie_sub_pay_name");
        put(totalMoney, "fie_sub_pay_amt");
        put(remark, "remark");
        //put(subPackAgreeId, "sub_pack_agree_id");
        put(subAgreeNo, "fie_sub_pay_subcon_no");
        put(payTime, "fie_sub_pay_date");
        put(projId, "for_foreig_key");

        put(billTime, "bill_time");
        put(rate, "rate");
        put(isDeduct, "is_deduct");
        put(isAccount, "is_account");
        put(billMoney, "bill_money");
        put(payProj, "pay_proj");
        //put(typeDicId, "type_dic_id");
    }};

    /*static {
        SubpackagePay.map.putAll(BaseModel.map);
    }*/

    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";
    /**
     * 本次支付金额
     */
    @SwaggerProperty(value = "本次支付金额")
    private static final String totalMoney = "totalMoney";
    /**
     * 已开发票金额
     */
    @SwaggerProperty(value = "已开发票金额")
    private static final String billMoney = "billMoney";
    /**
     * 是否到账
     */
    @SwaggerProperty(value = "是否到账(0:未到账，1：到账)")
    private static final String isAccount = "isAccount";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 分包合同id
     */
    @SwaggerProperty(value = "分包合同id")
    private static final String subPackAgreeId = "subPackAgreeId";
    /**
     * 分包合同id
     */
    @SwaggerProperty(value = "分包合同id")
    private static final String subAgreeNo = "subAgreeNo";
    /**
     * 支付时间
     */
    @SwaggerProperty(value = "支付时间")
    private static final String payTime = "payTime";
    /**
     * 开票时间
     */
    @SwaggerProperty(value = "开票时间")
    private static final String billTime = "billTime";
    /**
     * 税率
     */
    @SwaggerProperty(value = "税率")
    private static final String rate = "rate";
    /**
     * 是否抵扣
     */
    @SwaggerProperty(value = "是否抵扣(0:未抵扣，1：抵扣)")
    private static final String isDeduct = "isDeduct";
    /**
     * 类型id
     */
    @SwaggerProperty(value = "支付项目类型id")
    private static final String typeDicId = "typeDicId";
    /**
     * 类型id
     */
    @SwaggerProperty(value = "支付项目")
    private static final String payProj = "payProj";
    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";

    public SubpackagePay(JsonObject jsonObject) {
        super(jsonObject, TABLE, false);
    }

    public SubpackagePay() {
        super(null, TABLE, false);
    }

    public static String baseColumnList() {
        return " id, fie_sub_pay_name as `name`, fie_sub_pay_amt as totalMoney, bill_money as billMoney, " +
                " is_deduct as isAccount, `remark`, " +
                " null as subPackAgreeId, fie_sub_pay_date as payTime, bill_time as billTime, " +
                " `rate`, is_deduct as isDeduct, pay_proj as payProj ";
    }

    public static String baseColumnList1() {
        return " id, `name`, total_money as totalMoney, bill_money as billMoney, is_account as isAccount, " +
                " `remark`, sub_pack_agree_id as subPackAgreeId, pay_time as payTime, bill_time as billTime, " +
                " `rate`, is_deduct as isDeduct, type_dic_id as typeDicId, " + BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinA() {
        return " a.id, a.fie_sub_pay_name as `name`, a.fie_sub_pay_amt as totalMoney, a.bill_money as billMoney, " +
                " a.is_account as isAccount, a.`remark`, " +
                " null as subPackAgreeId, a.fie_sub_pay_date as payTime, a.bill_time as billTime, " +
                " a.`rate`, a.is_deduct as isDeduct, pay_proj as payProj, null as gmtCreate ";
    }

    public static String baseColumnListJoinA1() {
        return " a.id, a.fie_sub_pay_name as `name`, a.total_money as totalMoney, a.bill_money as billMoney, " +
                " a.is_account as isAccount, " +
                " a.`remark`, a.sub_pack_agree_id as subPackAgreeId, a.pay_time as payTime, a.bill_time as billTime, " +
                " a.`rate`, a.is_deduct as isDeduct, a.type_dic_id as typeDicId, a.gmt_create as gmtCreate ";
    }

    public static String sumBySubAgreeNo(String projId, String subAgreeNo, Integer isAccount, Integer isDeduct, JsonArray params) {
        params.add(projId).add(subAgreeNo);
        StringBuilder sql = new StringBuilder(" select sum(fie_sub_pay_amt) as totalMoney, " +
                " sum(bill_money) as billMoney from " + TABLE +
                " where for_foreig_key = ? and fie_sub_pay_subcon_no = ? ");
        if (null != isAccount) {
            sql.append(" and is_account = ? ");
            params.add(isAccount);
        }
        if (null != isDeduct) {
            sql.append(" and is_deduct = ? ");
            params.add(isDeduct);
        }

        return sql.append(" and del_flag = 0 and fie_sub_pay_dept in ")
                .append(Constant.EARTH_ENERGY_DEPT_IDS).append(" ; ").toString();
    }

    public static String sumBySubPackAgreeId1(String subPackAgreeId, Integer isAccount, Integer isDeduct, JsonArray params) {
        params.add(subPackAgreeId);
        StringBuilder sql = new StringBuilder(" select sum(total_money) as totalMoney, " +
                "sum(bill_money) as billMoney from " + TABLE + " where sub_pack_agree_id = ? ");
        if (null != isAccount) {
            sql.append(" and is_account = ? ");
            params.add(isAccount);
        }
        if (null != isDeduct) {
            sql.append(" and is_deduct = ? ");
            params.add(isDeduct);
        }

        return sql.append(" and del_flag = 0 ; ").toString();
    }

    public static String sumBySubAgreeNos(Integer isAccount, Integer isDeduct, JsonArray params) {
        StringBuilder sql = new StringBuilder(" select sum(total_money) as totalMoney, " +
                "sum(bill_money) as billMoney from " + TABLE + " where fie_sub_pay_subcon_no in ( ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(", ? ");
        }
        sql.append(sb.substring(1)).append(" ) ");
        if (null != isAccount) {
            sql.append(" and is_account = ? ");
            params.add(isAccount);
        }
        if (null != isDeduct) {
            sql.append(" and is_deduct = ? ");
            params.add(isDeduct);
        }

        return sql.append(" and del_flag = 0 and fie_sub_pay_dept in ")
                .append(Constant.EARTH_ENERGY_DEPT_IDS).append(" ; ").toString();
    }

    public static String sumByProjId(String projId, String isAccount, String isDeduct, JsonArray params) {
        params.add(projId);
        StringBuilder sql = new StringBuilder(" select sum(fie_sub_pay_amt) as totalMoney, " +
                "sum(bill_money) as billMoney from " + TABLE + " where for_foreig_key = ? ");
        if (null != isAccount) {
            sql.append(" and is_account = ? ");
            params.add(isAccount);
        }
        if (null != isDeduct) {
            sql.append(" and is_deduct = ? ");
            params.add(isDeduct);
        }

        return sql.append(" and del_flag = 0 and fie_sub_pay_dept in ")
                .append(Constant.EARTH_ENERGY_DEPT_IDS).append(" ; ").toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + " , a.pay_proj as `label` from " +
                TABLE + " a inner join ( select id as bid from " + TABLE +
                " where del_flag = 0 and fie_sub_pay_dept in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" and for_foreig_key = ? ");
            params.add(this.getProjId());
        }
        if (null != this.getSubAgreeNo() && !"".equals(this.getSubAgreeNo().trim())) {
            sb.append(" and fie_sub_pay_subcon_no = ? ");
            params.add(this.getSubAgreeNo());
        }
        sb.append(" ) b on a.id = b.bid ");
        /*sb.append("left join ").append(BGIdic.TABLE)
                .append(" c on c.id = a.type_dic_id");*/
        String[] orderBy = sqLpage.getOrderBy();
        if (null != orderBy) {
            for (int i = 0; i < orderBy.length; i++) {
                orderBy[i] = "a." + orderBy[i];
            }
        }

        return sb.append(sqLpage.toString()).toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE +
                " where del_flag = 0 and fie_sub_pay_dept in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getProjId() && !"".equals(this.getProjId().trim())) {
            sb.append(" and for_foreig_key = ? ");
            params.add(this.getProjId());
        }
        if (null != this.getSubAgreeNo() && !"".equals(this.getSubAgreeNo().trim())) {
            sb.append(" and fie_sub_pay_subcon_no = ? ");
            params.add(this.getSubAgreeNo());
        }
        return sb.append(" ; ").toString();
    }

    //@Override
    public String queryPage1(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + " , null as `label` from " +
                TABLE + " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getSubPackAgreeId() && !"".equals(this.getSubPackAgreeId().trim())) {
            sb.append(" and sub_pack_agree_id = ? ");
            params.add(this.getSubPackAgreeId());
        }
        sb.append(" ) b on a.id = b.bid ");
        /*sb.append("left join ").append(BGIdic.TABLE)
                .append(" c on c.id = a.type_dic_id");*/
        String[] orderBy = sqLpage.getOrderBy();
        if (null != orderBy) {
            for (int i = 0; i < orderBy.length; i++) {
                orderBy[i] = "a." + orderBy[i];
            }
        }

        return sb.append(sqLpage.toString()).toString();
    }

    //@Override
    public String countPage1(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getSubPackAgreeId() && !"".equals(this.getSubPackAgreeId().trim())) {
            sb.append(" and sub_pack_agree_id = ? ");
            params.add(this.getSubPackAgreeId());
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

    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(SubpackagePay.name, name);
    }
    public BigDecimal getTotalMoney() {
        String totalMoney = this.entries.getString(SubpackagePay.totalMoney);
        if (null == totalMoney || "".equals(totalMoney.trim())) {
            return null;
        }
        return new BigDecimal(totalMoney);
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.entries.put(SubpackagePay.totalMoney, null == totalMoney ? null : totalMoney.toString());
    }
    public BigDecimal getBillMoney() {
        String afterTwoMonth = this.entries.getString(SubpackagePay.billMoney);
        if (null == afterTwoMonth || "".equals(afterTwoMonth.trim())) {
            return null;
        }
        return new BigDecimal(afterTwoMonth);
    }

    public void setBillMoney(BigDecimal afterTwoMonth) {
        this.entries.put(SubpackagePay.billMoney, null == afterTwoMonth ? null : afterTwoMonth.toString());
    }
    public Integer getIsAccount() {
        return this.entries.getInteger(isAccount);
    }

    public void setIsAccount(Integer isAccount) {
        this.entries.put(SubpackagePay.isAccount, isAccount);
    }
    public String getRemark() {
        return this.entries.getString(SubpackagePay.remark);
    }

    public void setRemark(String remark) {
        this.entries.put(SubpackagePay.remark, remark);
    }
    public String getSubPackAgreeId() {
        return this.entries.getString(subPackAgreeId);
    }

    public void setSubPackAgreeId(String subPackAgreeId) {
        this.entries.put(SubpackagePay.subPackAgreeId, subPackAgreeId);
    }

    public String getPayTime() {
        return entries.getString(SubpackagePay.payTime);
    }

    public String getBillTime() {
        return entries.getString(SubpackagePay.billTime);
    }

    public String getRate() {
        return entries.getString(SubpackagePay.rate);
    }

    public Integer getIsDeduct() {
        return entries.getInteger(SubpackagePay.isDeduct);
    }

    public Integer getTypeDicId() {
        return entries.getInteger(SubpackagePay.typeDicId);
    }

    public String getSubAgreeNo() {
        return entries.getString(subAgreeNo);
    }

    public void setSubAgreeNo(String subAgreeNo) {
        this.entries.put(SubpackagePay.subAgreeNo, subAgreeNo);
    }

    public String getProjId() {
        return entries.getString(projId);
    }
}
