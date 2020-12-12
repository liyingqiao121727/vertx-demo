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

public class Pay extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "pay";

    public static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(payTypeId, "pay_type_id");put(parentId, "parent_id");put(payDate, "pay_date");
        put(cause, "cause");put(billNum, "bill_num");put(billMoney, "bill_money");
        put(receiptNum, "receipt_num");put(receiptMoney, "receipt_money");put(remark, "remark");
        put(projName, "proj_name");put(projNo, "proj_no");put(projId, "proj_id");put(share, "share");
        put(endTime, "end_time");put(money, "money");put(rate, "rate");put(shareTime, "share_time");
        put(isShare, "is_share");
    }};
    static {
        Pay.map.putAll(BaseModel.map);
    }

    /**
     * 支出项目类型id
     */
    @SwaggerProperty(value = "支出项目类型id")
    private static final String payTypeId = "payTypeId";
    /**
     * 父id
     */
    @SwaggerProperty(value = "父id")
    private static final String parentId = "parentId";
    /**
     * 日期
     */
    @SwaggerProperty(value = "日期")
    private static final String payDate = "payDate";
    /**
     * 事由
     */
    @SwaggerProperty(value = "事由")
    private static final String cause = "cause";
    /**
     * 发票张数
     */
    @SwaggerProperty(value = "发票张数")
    private static final String billNum = "billNum";
    /**
     * 发票金额
     */
    @SwaggerProperty(value = "发票金额")
    private static final String billMoney = "billMoney";
    /**
     * 收据张数
     */
    @SwaggerProperty(value = "收据张数")
    private static final String receiptNum = "receiptNum";
    /**
     * 收据金额
     */
    @SwaggerProperty(value = "收据金额")
    private static final String receiptMoney = "receiptMoney";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 项目名称
     */
    @SwaggerProperty(value = "项目名称")
    private static final String projName = "projName";
    /**
     * 项目编号
     */
    @SwaggerProperty(value = "项目编号")
    private static final String projNo = "projNo";
    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String projId = "projId";
    /**
     * 分摊 null:不是分摊项目，0:待分摊，1.已分摊
     */
    @SwaggerProperty(value = "分摊 null:不是分摊项目，0:待分摊，1.已分摊")
    private static final String share = "share";
    /**
     * 是否是分摊项目
     */
    @SwaggerProperty(value = "是否是分摊项目")
    private static final String isShare = "isShare";
    /**
     * 分摊时间
     */
    @SwaggerProperty(value = "分摊时间")
    private static final String shareTime = "shareTime";
    /**
     * 结束时间
     */
    @SwaggerProperty(value = "结束时间")
    private static final String endTime = "endTime";
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
     * 区分报销与成本
     */
    @SwaggerProperty(value = "区分报销与成本", type = Boolean.class)
    private static final String parentIdIsNull = "parentIdIsNull";
    /**
     * 结束时间是否为空（用作查询条件，区分财务成本和管理成本）
     */
    @SwaggerProperty(value ="结束时间是否为空（用作查询条件，区分财务成本和管理成本）", type = Boolean.class)
    public static final String endTimeIsNull = "endTimeIsNull";

    public Pay(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public Pay() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, pay_type_id as payTypeId, parent_id as parentId, pay_date as payDate, cause, " +
                " bill_num as billNum, " +
                " bill_money as billMoney, receipt_num as receiptNum, receipt_money as receiptMoney, remark, " +
                " proj_name as projName, proj_no as projNo, share, end_time as endTime, money, rate, " +
                " share_time as shareTime, proj_id as projId, " +
                BaseModel.baseColumnList();
    }

    public static String baseColumnListP() {
        return " p.id, p.pay_type_id as payTypeId, p.parent_id as parentId, p.pay_date as payDate, p.cause, " +
                " p.bill_num as billNum, p.bill_money as billMoney, " +
                " p.receipt_num as receiptNum, p.receipt_money as receiptMoney, p.remark, " +
                " p.proj_name as projName, p.proj_no as projNo, p.share, p.end_time as endTime, p.money, p.rate, " +
                " p.share_time as shareTime, p.proj_id as projId, ";
    }

    public static String insertBatch(List<Pay> list, JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE).append(
                " ( id, pay_type_id, parent_id, pay_date, cause, bill_num, bill_money, " +
                        " receipt_num, receipt_money, remark, proj_name, proj_no, share, " +
                        " end_time, money, rate, share_time, proj_id, ")
                .append(BaseModel.baseColumn()).append(" ) values    ");
        for (Pay pay : list) {
            params.add(pay.getId()).add(pay.getPayTypeId()).add(pay.getParentId()).add(pay.getPayDate())
                    .add(pay.getCause()).add(pay.getBillNum()).add(pay.entries.getValue(billMoney))
                    .add(pay.getReceiptNum()).add(pay.entries.getValue(receiptMoney)).add(pay.getRemark())
                    .add(pay.getProjName()).add(pay.getProjNo()).add(pay.getShare())
                    .add(pay.getEndTime()).add(pay.getMoney()).add(pay.getRate())
                    .add(pay.getProjId()).add(pay.getShareTime());
            pay.addParams(params);
            sb.append("( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ");
        }

        return sb.replace(sb.length()-3, sb.length() -1, " ; ").toString();
    }

    public static String deleteByParentId() {
        return " delete from " + TABLE + " where parent_id = ? ";
    }

    public static String getPayByParentId() {
        return " select " + baseColumnListP() + " p.is_share as isShare, pt.`name` as typeName, ptp.`name` as typeProjName from " +
                " ( select * from " + TABLE + " where parent_id = ? ) p " +
                " left join " + DicPayType.TABLE + " pt on pt.id = p.pay_type_id left join " + DicPayTypeParent.TABLE +
                " ptp on pt.parent_id = ptp.id;";
    }

    public static String getPayByIdIn(int size) {
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnListP())
                .append(" , pt.`name` as typeName, ptp.`name` as typeProjName from " +
                        " ( select * from ").append(TABLE).append(" where id in (    ");
        for (int i = 0; i < size; i++) {
            sb.append(" ?  , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ").append(" ) ) p " +
                " left join " + DicPayType.TABLE + " pt on pt.id = p.pay_type_id left join "
                + DicPayTypeParent.TABLE + " ptp on pt.parent_id = ptp.id;").toString();
    }



    public static String updateShare(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ").append(TABLE).append(" set `share` = 1, share_time = now() where id in ( ");
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sBuilder.append(" , ? ");
        }
        sb.append(size > 0 ? sBuilder.substring(2) : "")
                .append(" ) ; ");
        //.append(" ) and `share` is not null ; ");
        return sb.toString();
    }

    /*public static String updateShare(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" update ").append(TABLE).append(" set share = 1, share_time = now() where parent_id = ? " +
                    "and share is not null ; ");
        }
        return sb.toString();
    }*/

    public static String updateShareByParentIdAndProjectId(PayProjectShare pps, JsonArray params) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ").append(TABLE).append(" set share = 1, share_time = now() " +
                " where parent_id = ? and proj_id = ? ; ");
        //"and share is not null ; ");
        params.add(pps.getPayParentId()).add(pps.getProjectId());
        return sb.toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " , is_share as isShare from " + TABLE + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);

        if (Boolean.TRUE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is null ");
        }
        if (Boolean.FALSE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is not null ");
        }

        if (Boolean.TRUE.equals(this.getParentIdIsNull())) {
            sb.append(" and parent_id is null ");
        }
        if (Boolean.FALSE.equals(this.getParentIdIsNull())) {
            sb.append(" and parent_id is not null ");
        }
        if (null != this.getCause() && !"".equals(this.getCause().trim())) {
            sb.append(" and cause = ? ");
            params.add(this.getCause());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);

        if (Boolean.TRUE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is null ");
        }
        if (Boolean.FALSE.equals(this.getEndTimeIsNull())) {
            sb.append(" and end_time is not null ");
        }

        if (Boolean.TRUE.equals(this.getParentIdIsNull())) {
            sb.append(" and parent_id is null ");
        }
        if (Boolean.FALSE.equals(this.getParentIdIsNull())) {
            sb.append(" and parent_id is not null ");
        }
        if (null != this.getCause() && !"".equals(this.getCause().trim())) {
            sb.append(" and cause = ? ");
            params.add(this.getCause());
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

    public String getPayTypeId() {
        return this.entries.getString(payTypeId);
    }

    public void setPayTypeId(String payTypeId) {
        this.entries.put(Pay.payTypeId, payTypeId);
    }
    public String getParentId() {
        return this.entries.getString(parentId);
    }

    public void setParentId(String parentId) {
        this.entries.put(Pay.parentId, parentId);
    }
    public String getPayDate() {
        return this.entries.getString(payDate);
    }

    public void setPayDate(String payDate) {
        this.entries.put(Pay.payDate, payDate);
    }
    public String getCause() {
        return this.entries.getString(cause);
    }

    public void setCause(String cause) {
        this.entries.put(Pay.cause, cause);
    }
    public Integer getBillNum() {
        return this.entries.getInteger(billNum);
    }

    public void setBillNum(Integer billNum) {
        this.entries.put(Pay.billNum, billNum);
    }
    public BigDecimal getBillMoney() {
        String billMoney = this.entries.getString(Pay.billMoney);
        if (null == billMoney || "".equals(billMoney.trim())) {
            return null;
        }
        return new BigDecimal(billMoney);
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.entries.put(Pay.billMoney, null == billMoney ? null : billMoney.toString());
    }
    public Integer getReceiptNum() {
        return this.entries.getInteger(receiptNum);
    }

    public void setReceiptNum(Integer receiptNum) {
        this.entries.put(Pay.receiptNum, receiptNum);
    }
    public BigDecimal getReceiptMoney() {
        String receiptMoney = this.entries.getString(Pay.receiptMoney);
        if (null == receiptMoney || "".equals(receiptMoney.trim())) {
            return null;
        }
        return new BigDecimal(receiptMoney);
    }

    public void setReceiptMoney(BigDecimal receiptMoney) {
        this.entries.put(Pay.receiptMoney, null == receiptMoney ? null : receiptMoney.toString());
    }
    public String getRemark() {
        return this.entries.getString(remark);
    }

    public void setRemark(String remark) {
        this.entries.put(Pay.remark, remark);
    }
    public String getProjName() {
        return this.entries.getString(projName);
    }

    public void setProjName(String projName) {
        this.entries.put(Pay.projName, projName);
    }
    public String getProjNo() {
        return this.entries.getString(projNo);
    }

    public void setProjNo(String projNo) {
        this.entries.put(Pay.projNo, projNo);
    }
    public Integer getShare() {
        return this.entries.getInteger(share);
    }

    public void setShare(Integer share) {
        this.entries.put(Pay.share, share);
    }
    public String getEndTime() {
        return this.entries.getString(endTime);
    }

    public void setEndTime(String endTime) {
        this.entries.put(Pay.endTime, endTime);
    }
    public String getMoney() {
        return this.entries.getString(money);
    }

    public void setMoney(String money) {
        this.entries.put(Pay.money, money);
    }
    public String getRate() {
        return this.entries.getString(rate);
    }

    public void setRate(String rate) {
        this.entries.put(Pay.rate, rate);
    }

    public String getShareTime() {
        return this.entries.getString(shareTime);
    }

    public Boolean getEndTimeIsNull() {
        return this.entries.getBoolean(endTimeIsNull);
    }

    public Boolean getParentIdIsNull() {
        return this.entries.getBoolean(parentIdIsNull);
    }

    public String getProjId() {
        return entries.getString(projId);
    }
}
