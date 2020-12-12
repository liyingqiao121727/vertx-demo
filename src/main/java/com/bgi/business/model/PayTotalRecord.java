package com.bgi.business.model;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.time.Instant;
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

public class PayTotalRecord extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "pay_total_record";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(registerDate, "register_date");put(projName, "proj_name");put(projNo, "proj_no");
        put(summary, "summary");put(oil, "oil");put(road, "road");
        put(car, "car");put(proj, "proj");put(didi, "didi");
        put(offcial, "offcial");put(travel, "travel");put(mail, "mail");
        put(other, "other");put(subtotal, "subtotal");put(detail, "detail");
        put(real, "real");put(bill, "bill");put(receipt, "receipt");
        put(remark, "remark");put(share, "share");
    }};

    /**
     * 登记日期
     */
    @SwaggerProperty(value ="登记日期")
    private static final String registerDate = "registerDate";
    /**
     * 项目名称
     */
    @SwaggerProperty(value ="项目名称")
    private static final String projName = "projName";
    /**
     * 项目编号
     */
    @SwaggerProperty(value ="项目编号")
    private static final String projNo = "projNo";
    /**
     * 摘要
     */
    @SwaggerProperty(value ="摘要")
    private static final String summary = "summary";
    /**
     * 油费
     */
    @SwaggerProperty(value ="油费")
    private static final String oil = "oil";
    /**
     * 路费
     */
    @SwaggerProperty(value ="路费")
    private static final String road = "road";
    /**
     * 车费
     */
    @SwaggerProperty(value ="车费")
    private static final String car = "car";
    /**
     * 项目/物料费
     */
    @SwaggerProperty(value ="项目/物料费")
    private static final String proj = "proj";
    /**
     * 滴滴
     */
    @SwaggerProperty(value ="滴滴")
    private static final String didi = "didi";
    /**
     * 办公费
     */
    @SwaggerProperty(value ="办公费")
    private static final String offcial = "offcial";
    /**
     * 差旅费
     */
    @SwaggerProperty(value ="差旅费")
    private static final String travel = "travel";
    /**
     * 快递费
     */
    @SwaggerProperty(value ="快递费")
    private static final String mail = "mail";
    /**
     * 其他费用
     */
    @SwaggerProperty(value ="其他费用")
    private static final String other = "other";
    /**
     * 小计
     */
    @SwaggerProperty(value ="小计")
    private static final String subtotal = "subtotal";
    /**
     * 明细金额
     */
    @SwaggerProperty(value ="明细金额")
    private static final String detail = "detail";
    /**
     * 实际支付
     */
    @SwaggerProperty(value ="实际支付")
    private static final String real = "real";
    /**
     * 发票
     */
    @SwaggerProperty(value ="发票")
    private static final String bill = "bill";
    /**
     * 收据
     */
    @SwaggerProperty(value = "收据")
    private static final String receipt = "receipt";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 分摊
     */
    @SwaggerProperty(value = "分摊")
    private static final String share = "share";

    public PayTotalRecord(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, register_date as registerDate, proj_name as projName, proj_no as projNo, summary, " +
                " oil, road, car, proj, didi, offcial, travel, mail, other, subtotal, detail, `real`, " +
                " bill, receipt, remark, share, " + BaseModel.baseColumnList();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public Instant getRegisterDate() {
        return this.entries.getInstant(registerDate);
    }

    public void setRegisterDate(Instant registerDate) {
        this.entries.put(PayTotalRecord.registerDate, registerDate);
    }
    public String getProjName() {
        return this.entries.getString(projName);
    }

    public void setProjName(String projName) {
        this.entries.put(PayTotalRecord.projName, projName);
    }
    public String getProjNo() {
        return this.entries.getString(projNo);
    }

    public void setProjNo(String projNo) {
        this.entries.put(PayTotalRecord.projNo, projNo);
    }
    public String getSummary() {
        return this.entries.getString(summary);
    }

    public void setSummary(String summary) {
        this.entries.put(PayTotalRecord.summary, summary);
    }
    public BigDecimal getOil() {
        String oil = this.entries.getString(PayTotalRecord.oil);
        if (null == oil || "".equals(oil.trim())) {
            return null;
        }
        return new BigDecimal(oil);
    }

    public void setOil(BigDecimal oil) {
        this.entries.put(PayTotalRecord.oil, null == oil ? null : oil.toString());
    }
    public BigDecimal getRoad() {
        String road = this.entries.getString(PayTotalRecord.road);
        if (null == road || "".equals(road.trim())) {
            return null;
        }
        return new BigDecimal(road);
    }

    public void setRoad(BigDecimal road) {
        this.entries.put(PayTotalRecord.road, null == road ? null : road.toString());
    }
    public BigDecimal getCar() {
        String car = this.entries.getString(PayTotalRecord.car);
        if (null == car || "".equals(car.trim())) {
            return null;
        }
        return new BigDecimal(car);
    }

    public void setCar(BigDecimal car) {
        this.entries.put(PayTotalRecord.car, null == car ? null : car.toString());
    }
    public BigDecimal getProj() {
        String proj = this.entries.getString(PayTotalRecord.proj);
        if (null == proj || "".equals(proj.trim())) {
            return null;
        }
        return new BigDecimal(proj);
    }

    public void setProj(BigDecimal proj) {
        this.entries.put(PayTotalRecord.proj, null == proj ? null : proj.toString());
    }
    public BigDecimal getDidi() {
        String didi = this.entries.getString(PayTotalRecord.didi);
        if (null == didi || "".equals(didi.trim())) {
            return null;
        }
        return new BigDecimal(didi);
    }

    public void setDidi(BigDecimal didi) {
        this.entries.put(PayTotalRecord.didi, null == didi ? null : didi.toString());
    }
    public BigDecimal getOffcial() {
        String offcial = this.entries.getString(PayTotalRecord.offcial);
        if (null == offcial || "".equals(offcial.trim())) {
            return null;
        }
        return new BigDecimal(offcial);
    }

    public void setOffcial(BigDecimal offcial) {
        this.entries.put(PayTotalRecord.offcial, null == offcial ? null : offcial.toString());
    }
    public BigDecimal getTravel() {
        String travel = this.entries.getString(PayTotalRecord.travel);
        if (null == travel || "".equals(travel.trim())) {
            return null;
        }
        return new BigDecimal(travel);
    }

    public void setTravel(BigDecimal travel) {
        this.entries.put(PayTotalRecord.travel, null == travel ? null : travel.toString());
    }
    public BigDecimal getMail() {
        String mail = this.entries.getString(PayTotalRecord.mail);
        if (null == mail || "".equals(mail.trim())) {
            return null;
        }
        return new BigDecimal(mail);
    }

    public void setMail(BigDecimal mail) {
        this.entries.put(PayTotalRecord.mail, null == mail ? null : mail.toString());
    }
    public BigDecimal getOther() {
        String other = this.entries.getString(PayTotalRecord.other);
        if (null == other || "".equals(other.trim())) {
            return null;
        }
        return new BigDecimal(other);
    }

    public void setOther(BigDecimal other) {
        this.entries.put(PayTotalRecord.other, null == other ? null : other.toString());
    }
    public BigDecimal getSubtotal() {
        String subtotal = this.entries.getString(PayTotalRecord.subtotal);
        if (null == subtotal || "".equals(subtotal.trim())) {
            return null;
        }
        return new BigDecimal(subtotal);
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.entries.put(PayTotalRecord.subtotal, null == subtotal ? null : subtotal.toString());
    }
    public BigDecimal getDetail() {
        String detail = this.entries.getString(PayTotalRecord.detail);
        if (null == detail || "".equals(detail.trim())) {
            return null;
        }
        return new BigDecimal(detail);
    }

    public void setDetail(BigDecimal detail) {
        this.entries.put(PayTotalRecord.detail, null == detail ? null : detail.toString());
    }
    public BigDecimal getReal() {
        String real = this.entries.getString(PayTotalRecord.real);
        if (null == real || "".equals(real.trim())) {
            return null;
        }
        return new BigDecimal(real);
    }

    public void setReal(BigDecimal real) {
        this.entries.put(PayTotalRecord.real, null == real ? null : real.toString());
    }
    public String getBill() {
        return this.entries.getString(PayTotalRecord.bill);
    }

    public void setBill(String bill) {
        this.entries.put(PayTotalRecord.bill, bill);
    }
    public String getReceipt() {
        return this.entries.getString(receipt);
    }

    public void setReceipt(String receipt) {
        this.entries.put(PayTotalRecord.receipt, receipt);
    }
    public String getRemark() {
        return this.entries.getString(remark);
    }

    public void setRemark(String remark) {
        this.entries.put(PayTotalRecord.remark, remark);
    }
    public Integer getShare() {
        return this.entries.getInteger(share);
    }

    public void setShare(Integer share) {
        this.entries.put(PayTotalRecord.share, share);
    }

}
