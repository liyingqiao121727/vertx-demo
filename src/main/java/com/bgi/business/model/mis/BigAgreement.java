package com.bgi.business.model.mis;

import com.bgi.vtx.annotation.SwaggerProperty;
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

public class BigAgreement extends BaseModel {

    public static class Contract extends BaseModel {
        public static final String TABLE = "contract";
        protected static final HashMap<String, String> map = new HashMap<String, String>(){{
            put(id, "id");
            put(agreeNo, "fie_con_no");
            put(name, "fie_con_name");
            put(contractId, "fie_con_id");
        }};

        @Override
        public HashMap<String, String> getMap() {
            return map;
        }

        public Contract(JsonObject jsonObject) {
            super(jsonObject, TABLE);
        }

        public Contract() {
            super(null, TABLE, false);
        }

        public String getAgreeNo() {
            return this.entries.getString(agreeNo);
        }

        public String getName() {
            return this.entries.getString(name);
        }

        public String getContractId() {
            return this.entries.getString(contractId);
        }

        public void setAgreeNo(String agreeNo) {
            this.entries.put(BigAgreement.agreeNo, agreeNo);
        }

        public void setName(String name) {
            this.entries.put(BigAgreement.name, name);
        }

        public void setContractId(String contractId) {
            this.entries.put(BigAgreement.contractId, contractId);
        }

    }

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "ep_contract";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");put(agreeTypeId, "fie_con_type");
        //put(professionTypeId, "profession_type_id");
        put(agreeNo, "fie_con_no");
        put(name, "fie_contra_name");put(money, "fie_con_engine_amt");put(salesman, "fie_con_manager");
        //put(bizProperty, "biz_property");
        put(finalMoney, "fie_con_engine_final_amt");put(employer, "fie_con_name");
        put(projectId, "for_foreig_key");
        put(contractId, "fie_con_con_id");
    }};
    
    static {
        //BigAgreement.map.putAll(BaseModel.map);
    }
    /**
     * 合同类型id
     */
    @SwaggerProperty(value = "合同类型id", type = Long.class)
    private static final String agreeTypeId = "agreeTypeId";
    /**
     * 专业类型id
     */
    @SwaggerProperty(value = "专业类型id", type = Long.class)
    private static final String professionTypeId = "professionTypeId";
    /**
     * 合同编号
     */
    @SwaggerProperty(value = "合同编号")
    private static final String agreeNo = "agreeNo";
    /**
     * 营销经理
     */
    @SwaggerProperty(value ="营销经理")
    private static final String cmo = "cmo";
    /**
     * 合同名称
     */
    @SwaggerProperty(value = "合同名称")
    private static final String name = "name";
    /**
     * 合同金额
     */
    @SwaggerProperty(value = "合同金额")
    private static final String money = "money";
    /**
     * 销售经理
     */
    @SwaggerProperty(value = "销售经理")
    private static final String salesman = "salesman";
    /**
     * 业务属性
     */
    @SwaggerProperty(value = "业务属性")
    private static final String bizProperty = "bizProperty";
    /**
     * 最终金额
     */
    @SwaggerProperty(value = "最终金额")
    private static final String finalMoney = "finalMoney";
    /**
     * 发包人
     */
    @SwaggerProperty(value = "发包人")
    private static final String employer = "employer";
    /**
     * 项目主键
     */
    @SwaggerProperty(value = "项目主键")
    private static final String projectId = "projectId";
    /**
     * 合同id
     */
    @SwaggerProperty(value = "合同id")
    private static final String contractId = "contractId";
    /**
     * 最终合同变更金额
     */
    @SwaggerProperty(value = "最终合同变更金额")
    private static final String changeMoney = "changeMoney";

    public BigAgreement(JsonObject jsonObject) {
        super(jsonObject, TABLE);
        if (null == this.getContractId()) {
            this.setContractId(this.getId());
        }
    }

    public BigAgreement(boolean init) {
        super(null, TABLE, init);
        if (null == this.getContractId()) {
            this.setContractId(this.getId());
        }
    }

    public BigAgreement() {
        super(null, TABLE);
        if (null == this.getContractId()) {
            this.setContractId(this.getId());
        }
    }

    public static String baseColumnList() {
        return " id, agree_type_id as agreeTypeId, agree_no as agreeNo, `name`, money, " +
                " salesman, final_money as finalMoney, employer, project_id as projectId, " +
                BaseModel.baseColumnList();
    }

    public static String baseColumnListJoinA() {
        return " a.id, a.agree_type_id as agreeTypeId, a.agree_no as agreeNo, a.`name`, a.money, " +
                " a.salesman, a.final_money as finalMoney, a.employer, a.project_id as projectId, " +
                " a.gmt_create as gmtCreate ";
    }

    public static String baseColumnListJoin() {
        return " a.id, a.fie_con_type as agreeTypeId, a.fie_con_no as agreeNo, a.fie_contra_name as `name`, " +
                " a.fie_con_engine_amt as money, a.fie_con_manager as salesman, " +
                " a.fie_con_engine_final_amt as finalMoney, " +
                " a.fie_con_name as employer, a.for_foreig_key as projectId ";
                //" , " +
                //" b.fie_contra_start_date as gmtCreate ";
    }

    public static String getByProjIds(int size) {
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnListJoin())
                .append(" from ( select * from " + TABLE + " where for_foreig_key in (    ");
        for (int i = 0; i < size; i++) {
            sb.append(" ? , ");
        }
        return sb.replace(sb.length() - 3, sb.length() - 1, " ) and del_flag = 0 ) a ; ").toString();
    }

    public static String getById(String id, JsonArray params) {
        params.add(id);
        StringBuilder sb = new StringBuilder(" select ").append(baseColumnListJoin())
                .append(" from " + TABLE + " a where a.id = ? and a.del_flag = 0 ; ");
        return sb.toString();
    }

    public static String getByProjId(String projId, JsonArray params) {
        params.add(projId);
        return " select " + baseColumnListJoin() + " , t.show_value as label from ( select * from " + TABLE +
                " where for_foreig_key = ? and del_flag = 0 ) a " +
                " left join " + Dictionary.TABLE + " t on t.id = a.fie_con_type;";
    }

    public static String deleteByProjId(String id, JsonArray params2) {
        params2.add(id);
        return " update " + TABLE + " set del_flag = 1 where for_foreig_key = ? ";
    }

    public String updateByProjId(JsonArray params) {
        Map<String, String> colMap = this.getMap();
        StringBuilder sql = new StringBuilder(" update ").append(TABLE).append(" set ");
        for (Map.Entry<String, String> entry : colMap.entrySet()) {
            if (this.entries.containsKey(entry.getKey())) {
                sql.append(" `").append(entry.getValue()).append("` = ? , ");
                params.add(this.entries.getValue(entry.getKey()));
            }
        }
        sql = sql.replace(sql.length()-3, sql.length()-1, " where for_foreig_key = ? ;\r\n ");
        params.add(this.getProjectId());
        return sql.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public Long getAgreeTypeId() {
        return this.entries.getLong(agreeTypeId);
    }

    public void setAgreeTypeId(Long agreeTypeId) {
        this.entries.put(BigAgreement.agreeTypeId, agreeTypeId);
    }
    public Long getProfessionTypeId() {
        return this.entries.getLong(professionTypeId);
    }

    public void setProfessionTypeId(Long professionTypeId) {
        this.entries.put(BigAgreement.professionTypeId, professionTypeId);
    }
    public String getAgreeNo() {
        return this.entries.getString(agreeNo);
    }

    public void setAgreeNo(String agreeNo) {
        this.entries.put(BigAgreement.agreeNo, agreeNo);
    }
    public String getName() {
        return this.entries.getString(BigAgreement.name);
    }

    public void setName(String name) {
        this.entries.put(BigAgreement.name, name);
    }
    public String getMoney() {
        return this.entries.getString(BigAgreement.money);
    }

    public void setMoney(String money) {
        this.entries.put(BigAgreement.money, money);
    }
    public String getSalesman() {
        return this.entries.getString(salesman);
    }

    public void setSalesman(String salesman) {
        this.entries.put(BigAgreement.salesman, salesman);
    }
    public String getBizProperty() {
        return this.entries.getString(bizProperty);
    }

    public void setBizProperty(Long bizProperty) {
        this.entries.put(BigAgreement.bizProperty, bizProperty);
    }
    public String getFinalMoney() {
        return this.entries.getString(BigAgreement.finalMoney);
    }

    public void setFinalMoney(String finalMoney) {
        this.entries.put(BigAgreement.finalMoney, finalMoney);
    }
    public String getEmployer() {
        return this.entries.getString(employer);
    }

    public void setEmployer(String employer) {
        this.entries.put(BigAgreement.employer, employer);
    }
    public String getProjectId() {
        return this.entries.getString(projectId);
    }

    public void setProjectId(String projectId) {
        this.entries.put(BigAgreement.projectId, projectId);
    }

    public String getChangeMoney() {
        return entries.getString(changeMoney);
    }

    public void setChangeMoney(String changeMoney) {
        entries.put(BigAgreement.changeMoney, changeMoney);
    }

    public String getContractId() {
        return entries.getString(contractId);
    }

    public void setContractId(String contractId) {
        entries.put(BigAgreement.contractId, contractId);
    }
}
