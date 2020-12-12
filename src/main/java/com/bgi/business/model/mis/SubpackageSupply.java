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
import java.time.Instant;
import java.util.Date;
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

public class SubpackageSupply extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "sub_con_change";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        //put(operateType, "fie_change_type");
        //put(operator, "operator");
        //put(addMoney, "add_money");
        //put(minusMoney, "minus_money");
        //put(exeResult, "exe_result");
        put(id, "id");
        put(fieChangeType, "fie_change_type");
        put(fieChangeAmt, "fie_change_amt");
        put(applyTime, "fie_apply_time");
        //put(filePath, "file_path");
        put(subPackAgreeId, "for_foreig_key");
        put(beforeMoney, "fie_change_subcon_amt");
        put(afterMoney, "fie_change_final_subcon_amt");
        put(gmtCreate, "gmt_create");
        put(remark, "remark");
    }};

    /*static {
        SubpackageSupply.map.putAll(BaseModel.map);
    }*/

    /**
     * 操作类型
     */
    @SwaggerProperty(value = "操作类型")
    private static final String operateType = "operateType";
    /**
     * 操作人
     */
    @SwaggerProperty(value = "操作人")
    private static final String operator = "operator";
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
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 变更合同-变更类型
     */
    @SwaggerProperty(value = "变更合同-变更类型")
    private static final String fieChangeType = "fieChangeType";
    /**
     * 变更合同-本次变更金额
     */
    @SwaggerProperty(value = "变更合同-本次变更金额")
    private static final String fieChangeAmt = "fieChangeAmt";
    /**
     * 大合同id
     */
    @SwaggerProperty(value = "分包合同id")
    private static final String subPackAgreeId = "subPackAgreeId";

    public SubpackageSupply(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public SubpackageSupply() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, fie_change_type as operateType, `remark`, " +
                " (case when fie_change_type = '" + Constant.MIS_AGREE_CHANGE_ADD +
                "' then fie_change_amt else 0 end) as addMoney, " +
                " (case when fie_change_type = '" + Constant.MIS_AGREE_CHANGE_MINUS +
                "' then fie_change_amt else 0 end) as minusMoney, " +
                " fie_change_subcon_amt as beforeMoney, fie_change_final_subcon_amt as afterMoney, " +
                //" `remark`, exe_result as exeResult, " +
                " fie_apply_time as applyTime, " +
                //" file_path as filePath, " +
                " for_foreig_key as subPackAgreeId ";
                //BaseModel.baseColumnList();
    }

    public static String baseColumnList1() {
        return " id, operate_type as operateType, operator, add_money as addMoney, minus_money as minusMoney, " +
                " before_money as beforeMoney, after_money as afterMoney, `remark`, exe_result as exeResult, " +
                " apply_time as applyTime, file_path as filePath, sub_pack_agree_id as subPackAgreeId, " +
                BaseModel.baseColumnList();
    }

    public static String sumBySubPackAgreeId(String subPackAgreeId, JsonArray params) {
        params.add(subPackAgreeId);
        return " select sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_ADD +
                " then fie_change_amt else 0 end ) as addMoney, " +
                " sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_MINUS +
                " then fie_change_amt else 0 end ) as minusMoney from " + TABLE +
                " where for_foreig_key = ( select id from " + SUB_AGREE_TABLE +
                " where fie_subcon_id = ( select fie_subcon_id from " + SUB_AGREE_TABLE1 +
                " where id = ? and del_flag = 0) and del_flag = 0 ) and del_flag = 0 ; ";
    }

    public static String sumBySubPackAgreeId1(String subPackAgreeId, JsonArray params) {
        params.add(subPackAgreeId);
        return " select sum(add_money) as addMoney, sum(minus_money) as minusMoney from " + TABLE +
                " where sub_pack_agree_id = ? and del_flag = 0 ; ";
    }

    public static String sumBySubPackAgreeIds(JsonArray params) {
        StringBuilder sql = new StringBuilder(" select " +
                " sum(add_money) as addMoney, sum(minus_money) as minusMoney from " + TABLE +
                " where sub_pack_agree_id in ( ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(", ? ");
        }
        sql.append(sb.substring(1)).append(" ) and del_flag = 0 ; ");
        return sql.toString();
    }

    public static String sumByProjId(String projId, JsonArray supplyParams) {
        supplyParams.add(projId);
        return " select sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_ADD +
                " then fie_change_amt else 0 end ) as addMoney, " +
                " sum( case when fie_change_type = " + Constant.MIS_AGREE_CHANGE_MINUS +
                " then fie_change_amt else 0 end ) as minusMoney from " + TABLE +
                " where for_foreig_key in ( select id from " + SUB_AGREE_TABLE +
                " where fie_subcon_id in ( select fie_subcon_id from " + SUB_AGREE_TABLE1 +
                " where for_foreig_key = ? and del_flag = 0) and del_flag = 0 ) and del_flag = 0 ; ";
    }

    public static String getSubPaceAgreeId(String subPackAgreeId, JsonArray params) {
        params.add(subPackAgreeId);
        return " select id from " + SUB_AGREE_TABLE + " where fie_subcon_id = ( select fie_subcon_id from " +
                SUB_AGREE_TABLE1 + " where id = ? ) ;";
    }

    private String baseColumnListJoinA() {
        return " a.id, a.fie_change_type as operateType, a.`remark` as `remark`, " +
                //" a.operator, " +
                " (case when a.fie_change_type = '" + Constant.MIS_AGREE_CHANGE_ADD +
                "' then a.fie_change_amt else 0 end) as addMoney, " +
                " (case when a.fie_change_type = '" + Constant.MIS_AGREE_CHANGE_MINUS +
                "' then a.fie_change_amt else 0 end) as minusMoney, " +
                " a.fie_change_subcon_amt as beforeMoney, a.fie_change_final_subcon_amt as afterMoney, " +
                //" a.`remark`, a.exe_result as exeResult, " +
                " a.fie_apply_time as applyTime, " +
                //" a.file_path as filePath, " +
                " a.for_foreig_key as subPackAgreeId "
                //+ " , a.gmt_create as gmtCreate "
                ;
    }

    private String baseColumnListJoinA1() {
        return " a.id, a.operate_type as operateType, a.operator, a.add_money as addMoney, a.minus_money as minusMoney, " +
                " a.before_money as beforeMoney, a.after_money as afterMoney, a.`remark`, a.exe_result as exeResult, " +
                " a.apply_time as applyTime, a.file_path as filePath, a.sub_pack_agree_id as subPackAgreeId, " +
                " a.gmt_create as gmtCreate ";
    }

    private static final String SUB_AGREE_TABLE = "sub_contract";

    private static final String SUB_AGREE_TABLE1 = "ep_sub_contract";

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + " , c.show_value as `label`, null as realName from " +
                TABLE + " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getSubPackAgreeId() && !"".equals(this.getSubPackAgreeId().trim())) {
            sb.append(" and for_foreig_key = ( select id from " + SUB_AGREE_TABLE +
                    " where fie_subcon_id = ( select fie_subcon_id from " + SUB_AGREE_TABLE1 +
                    " where id = ? )) ");
            params.add(this.getSubPackAgreeId());
        }
        sb.append(" ) b on a.id = b.bid left join ").append(Dictionary.TABLE)
                .append(" c on c.id = a.fie_change_type ");
        /*sb.append("left join ").append(EarthEnergyUser.TABLE)
                .append(" u on u.id = a.operator");*/
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
        if (null != this.getSubPackAgreeId() && !"".equals(this.getSubPackAgreeId().trim())) {
            sb.append(" and for_foreig_key = ( select id from " + SUB_AGREE_TABLE +
                    " where fie_subcon_id = ( select fie_subcon_id from " + SUB_AGREE_TABLE1 +
                    " where id = ? )) ");
            params.add(this.getSubPackAgreeId());
        }
        return sb.append(" ; ").toString();
    }

    //@Override
    public String queryPage1(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoinA() + " , c.`label`, u.real_name as realName from " +
                TABLE + " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getSubPackAgreeId() && !"".equals(this.getSubPackAgreeId().trim())) {
            sb.append(" and sub_pack_agree_id = ? ");
            params.add(this.getSubPackAgreeId());
        }
        sb.append(" ) b on a.id = b.bid left join ").append(BGIdic.TABLE)
                .append(" c on c.id = a.operate_type left join ").append(EarthEnergyUser.TABLE)
                .append(" u on u.id = a.operator");
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

    public String getOperateType() {
        return this.entries.getString(operateType);
    }

    public void setOperateType(String operateType) {
        this.entries.put(SubpackageSupply.operateType, operateType);
    }
    public String getOperator() {
        return this.entries.getString(operator);
    }

    public void setOperator(String operator) {
        this.entries.put(SubpackageSupply.operator, operator);
    }
    public String getAddMoney() {
        return this.entries.getString(SubpackageSupply.addMoney);
    }

    public void setAddMoney(String addMoney) {
        this.entries.put(SubpackageSupply.addMoney, addMoney);
    }
    public String getMinusMoney() {
        return this.entries.getString(SubpackageSupply.minusMoney);
    }

    public void setMinusMoney(String minusMoney) {
        this.entries.put(SubpackageSupply.minusMoney, minusMoney);
    }
    public Integer getExeResult() {
        return this.entries.getInteger(exeResult);
    }

    public void setExeResult(Integer exeResult) {
        this.entries.put(SubpackageSupply.exeResult, exeResult);
    }
    public Instant getApplyTime() {
        return this.entries.getInstant(applyTime);
    }

    public void setApplyTime(Instant applyTime) {
        this.entries.put(SubpackageSupply.applyTime, applyTime);
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.entries.put(SubpackageSupply.filePath, filePath);
    }
    public String getSubPackAgreeId() {
        return this.entries.getString(subPackAgreeId);
    }

    public void setSubPackAgreeId(String subPackAgreeId) {
        this.entries.put(SubpackageSupply.subPackAgreeId, subPackAgreeId);
    }

    public String getFieChangeAmt() {
        return entries.getString(fieChangeAmt);
    }

    public String getFieChangeType() {
        return entries.getString(fieChangeType);
    }

    public void setFieChangeAmt(String fieChangeAmt) {
        entries.put(SubpackageSupply.fieChangeAmt, fieChangeAmt);
    }

    public void setFieChangeType(String fieChangeType) {
        entries.put(SubpackageSupply.fieChangeType, fieChangeType);
    }
}
