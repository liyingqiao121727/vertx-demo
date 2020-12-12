package com.bgi.business.model.mis;

import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
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

public class BigAgreeSupply extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "con_change";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");
        put(operateType, "operate_type");put(operator, "operator");
        put(changeType, "fie_change_type");
        //put(addMoney, "add_money");put(minusMoney, "minus_money");
        put(exeResult, "exe_result");
        put(gmtCreate, "fie_file_time");
        put(money, "fie_con_change_amt");
        //put(filePath, "file_path");
        put(bigAgreeId, "for_foreig_key");
        put(beforeMoney, "fie_con_amt");
        put(afterMoney, "fie_con_final_amt");
        put(remark, "remark");
    }};

    //static { BigAgreeSupply.map.putAll(BaseModel.map); }

    /**
     * 操作类型
     */
    @SwaggerProperty(value = "操作类型")
    private static final String operateType = "operateType";
    /**
     * 操作类型
     */
    @SwaggerProperty(value = "变更类型")
    private static final String changeType = "changeType";
    /**
     * 操作人
     */
    @SwaggerProperty(value = "操作人")
    private static final String operator = "operator";
    /**
     * 变更金额
     */
    @SwaggerProperty(value = "变更金额")
    private static final String money = "money";
    /**
     * 修改金额
     */
    @SwaggerProperty(value = "修改金额")
    private static final String changeMoney = "changeMoney";
    /**
     * 增加金额
     */
    @SwaggerProperty(value = "增加金额")
    private static final String addMoney = "addMoney";
    /**
     * 删减金额
     */
    @SwaggerProperty(value = "删减金额")
    private static final String minusMoney = "minusMoney";
    /**
     * 变更前金额
     */
    @SwaggerProperty(value = "变更前金额")
    private static final String beforeMoney = "beforeMoney";
    /**
     * 变更后金额
     */
    @SwaggerProperty(value = "变更后金额")
    private static final String afterMoney = "afterMoney";
    /**
     * 工程id
     */
    @SwaggerProperty(value = "工程id")
    private static final String projId = "projId";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 执行结果
     */
    @SwaggerProperty(value = "执行结果")
    private static final String exeResult = "exeResult";
    /**
     * 申请时间
     */
    @SwaggerProperty(value = "申请时间")
    private static final String applyTime = "applyTime";
    /**
     * 附件
     */
    @SwaggerProperty(value = "附件")
    private static final String filePath = "filePath";
    /**
     * 大合同id
     */
    @SwaggerProperty(value = "大合同id")
    private static final String bigAgreeId = "bigAgreeId";
    /**
     * 是否到账
     */
    @SwaggerProperty(value = "是否到账")
    private static final String account = "account";

    public BigAgreeSupply(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public BigAgreeSupply(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public BigAgreeSupply() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, operate_type as operateType, `operator`, " +
                " (case when fie_change_type = '" + Constant.MIS_BIG_AGREE_CHANGE_ADD +
                "' then fie_con_change_amt else 0 end) as addMoney, " +
                " (case when fie_change_type = '" + Constant.MIS_BIG_AGREE_CHANGE_MINUS +
                "' then fie_con_change_amt else 0 end) as minusMoney, " +
                " fie_con_amt as beforeMoney, fie_con_final_amt as afterMoney, " +
                " `remark`, exe_result as exeResult, " +
                " fie_file_time as applyTime, null as filePath, for_foreig_key as bigAgreeId ";
    }

    public static String baseColumnListJoinA() {
        return " a.id, a.operate_type as operateType, a.`operator`, " +
                " (case when a.fie_change_type = '" + Constant.MIS_BIG_AGREE_CHANGE_ADD +
                "' then a.fie_con_change_amt else 0 end) as addMoney, " +
                " (case when a.fie_change_type = '" + Constant.MIS_BIG_AGREE_CHANGE_MINUS +
                "' then a.fie_con_change_amt else 0 end) as minusMoney, " +
                " a.fie_con_amt as beforeMoney, a.fie_con_final_amt as afterMoney, " +
                " a.`remark`, a.exe_result as exeResult, " +
                " a.fie_file_time as applyTime, null as filePath, a.for_foreig_key as bigAgreeId, " +
                " null as gmtCreate ";
    }

    private static final String AGREE_TABLE = "contract";
    private static final String AGREE_TABLE1 = "ep_contract";

    public static String sumByBigAgreeId(String bigAgreeId, JsonArray params) {
        params.add(bigAgreeId);
        return " select sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_ADD +
                " then fie_con_change_amt else 0 end ) as addMoney, " +
                " sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_MINUS +
                " then fie_con_change_amt else 0 end ) as minusMoney from " + TABLE +
                " where for_foreig_key in ( select id from " + AGREE_TABLE +
                " where fie_con_id in ( select fie_con_con_id from " + AGREE_TABLE1 +
                " where id = ? and del_flag = 0 ) and del_flag = 0 ) and del_flag = 0 ; ";
    }

    public static String sumByBigAgreeId1(String bigAgreeId, JsonArray params) {
        params.add(bigAgreeId);
        return " select sum(add_money) as addMoney, sum(minus_money) as minusMoney from " + TABLE +
                " where big_agree_id = ? and del_flag = 0 ; ";
    }

    public static String sumByProjId(String projId, JsonArray params) {
        params.add(projId);
        return " select sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_ADD +
                " then fie_con_change_amt else 0 end ) as addMoney, " +
                " sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_MINUS +
                " then fie_con_change_amt else 0 end ) as minusMoney from " + TABLE +
                " where for_foreig_key in ( select id from " + AGREE_TABLE +
                " where fie_con_id in ( select fie_con_con_id from " + AGREE_TABLE1 +
                " where for_foreig_key = ? and del_flag = 0 ) and del_flag = 0 ) and del_flag = 0 ; ";
    }

    public static String getBigAgreeId(String bigAgreeId, JsonArray params) {
        params.add(bigAgreeId);
        return " select id from " + AGREE_TABLE + " where fie_con_id = ( select fie_con_con_id from " +
                AGREE_TABLE1 + " where id = ? ) ;";
    }

    public static String countByBigAgreeId(String bigAgreeId, JsonArray params) {
        params.add(bigAgreeId);
        return " select count(*) as countNum from " + TABLE + " where for_foreig_key in ( select id from " +
                AGREE_TABLE + " where fie_con_id = ( select fie_con_con_id from " +
                AGREE_TABLE1 + " where id = ? ) ) and del_flag = 0 ;";
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + " , a.operate_type as `label`, u.name as realName from " +
                TABLE + " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId().trim())) {
            sb.append(" and for_foreig_key = ( select id from " + AGREE_TABLE +
                    " where fie_con_id = ( select fie_con_con_id from " + AGREE_TABLE1 +
                    " where id = ? )) ");
            params.add(this.getBigAgreeId());
        }
        sb.append(" ) b on a.id = b.bid left join ").append(EarthEnergyUser.TABLE)
                .append(" u on u.id = a.operator");
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
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId().trim())) {
            sb.append(" and for_foreig_key in ( select id from " + AGREE_TABLE +
                    " where fie_con_id in ( select fie_con_con_id from " + AGREE_TABLE1 +
                    " where id = ? )) ");
            params.add(this.getBigAgreeId());
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

    public String getOperateType() {
        return this.entries.getString(operateType);
    }

    public void setOperateType(String operateType) {
        this.entries.put(BigAgreeSupply.operateType, operateType);
    }
    public String getOperator() {
        return this.entries.getString(operator);
    }

    public void setOperator(String operator) {
        this.entries.put(BigAgreeSupply.operator, operator);
    }
    public String getAddMoney() {
        return this.entries.getString(BigAgreeSupply.addMoney);
    }

    public void setAddMoney(BigDecimal addMoney) {
        this.entries.put(BigAgreeSupply.addMoney, null == addMoney ? null : addMoney.toString());
    }
    public String getMinusMoney() {
        return this.entries.getString(BigAgreeSupply.minusMoney);
    }

    public void setMinusMoney(BigDecimal minusMoney) {
        this.entries.put(BigAgreeSupply.minusMoney, null == minusMoney ? null : minusMoney.toString());
    }
    public Integer getExeResult() {
        return this.entries.getInteger(exeResult);
    }

    public void setExeResult(Integer exeResult) {
        this.entries.put(BigAgreeSupply.exeResult, exeResult);
    }
    public Instant getApplyTime() {
        return this.entries.getInstant(applyTime);
    }

    public void setApplyTime(Instant applyTime) {
        this.entries.put(BigAgreeSupply.applyTime, applyTime);
    }
    public String getFilePath() {
        return this.entries.getString(BigAgreeSupply.filePath);
    }

    public void setFilePath(String filePath) {
        this.entries.put(BigAgreeSupply.filePath, filePath);
    }
    public String getBigAgreeId() {
        return this.entries.getString(bigAgreeId);
    }

    public void setBigAgreeId(String bigAgreeId) {
        this.entries.put(BigAgreeSupply.bigAgreeId, bigAgreeId);
    }

    public String getBeforeMoney() {
        return entries.getString(beforeMoney);
    }

    public String getAfterMoney() {
        return entries.getString(afterMoney);
    }

    public String getRemark() {
        return entries.getString(remark);
    }

    public Integer getAccount() {
        return entries.getInteger(account);
    }

    public String getMoney() {
        return entries.getString(money);
    }

    public void setMoney(String money) {
        entries.put(BigAgreeSupply.money, money);
    }

    public String getChangeType() {
        return entries.getString(changeType);
    }

    public void setChangeType(String changeType) {
        entries.put(BigAgreeSupply.changeType, changeType);
    }

    public String getProjId() {
        return entries.getString(projId);
    }

    public String getChangeMoney() {
        return entries.getString(changeMoney);
    }
}
