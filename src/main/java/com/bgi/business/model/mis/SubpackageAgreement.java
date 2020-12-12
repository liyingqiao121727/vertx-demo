package com.bgi.business.model.mis;

import com.bgi.common.SQLpage;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
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

public class SubpackageAgreement extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static class SubContract extends BaseModel {
        public static final String TABLE = "sub_contract";
        protected static final HashMap<String, String> map = new HashMap<String, String>(){{
            put(id, "id");
            put(agreeNo, "fie_subcon_no");
            put(name, "fie_name_subcon");
            put(subContractId, "fie_subcon_id");
        }};

        @Override
        public HashMap<String, String> getMap() {
            return map;
        }

        public SubContract(JsonObject jsonObject) {
            super(jsonObject, TABLE);
        }

        public SubContract() {
            super(null, TABLE, false);
        }

        public String getAgreeNo() {
            return this.entries.getString(agreeNo);
        }

        public String getName() {
            return this.entries.getString(name);
        }

        public String getSubContractId() {
            return this.entries.getString(subContractId);
        }

        public void setAgreeNo(String agreeNo) {
            this.entries.put(SubpackageAgreement.agreeNo, agreeNo);
        }

        public void setName(String name) {
            this.entries.put(SubpackageAgreement.name, name);
        }

        public void setSubContractId(String subContractId) {
            this.entries.put(SubpackageAgreement.subContractId, subContractId);
        }

    }

    public static final String TABLE = "ep_sub_contract";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");
        put(agreeTypeId, "fie_subcon_type");
        put(agreeNo, "fie_subcon_no");
        put(name, "fie_subcon_name");put(money, "fie_subcon_amt");
        //put(salesman, "salesman");put(bizProperty, "biz_property");put(professionTypeId, "fie_profes_category");
        put(finalMoney, "fie_subcon_final_amt");
        put(employer, "fie_subcon_supply");put(projectId, "for_foreig_key");
        put(bigAgreeId, "fie_subcon_con_id");
        put(subContractId, "fie_subcon_id");

        put(gmtCreate, "fie_subcon_date");
    }};

    static {
        //SubpackageAgreement.map.putAll(BaseModel.map);
    }

    /**
     * 合同类型id
     */
    @SwaggerProperty(value = "合同类型id")
    private static final String agreeTypeId = "agreeTypeId";
    /**
     * 合同编号
     */
    @SwaggerProperty(value = "合同编号")
    private static final String agreeNo = "agreeNo";
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
     * 最终金额
     */
    @SwaggerProperty(value = "最终金额")
    private static final String finalMoney = "finalMoney";
    /**
     * 发包人
     */
    @SwaggerProperty(value = "分包商")
    private static final String employer = "employer";
    /**
     * 项目主键
     */
    @SwaggerProperty(value = "项目主键")
    private static final String projectId = "projectId";
    /**
     * 大合同id
     */
    @SwaggerProperty(value = "大合同id")
    private static final String bigAgreeId = "bigAgreeId";
    /**
     * 分包合同id
     */
    @SwaggerProperty(value = "分包合同id")
    private static final String subContractId = "subContractId";
    /**
     * 销售经理
     */
    /*@SwaggerProperty(value = "销售经理")
    private static final String salesman = "salesman";*/
    /**
     * 业务属性
     */
    /*@SwaggerProperty(value = "业务属性")
    private static final String bizProperty = "bizProperty";*/
    /**
     * 专业类型id
     */
    /*@SwaggerProperty(value = "专业类型id")
    private static final String professionTypeId = "professionTypeId";*/

    public SubpackageAgreement(JsonObject jsonObject) {
        super(jsonObject, TABLE);
        if (null == this.getSubContractId()) {
            this.setSubContractId(this.getId());
        }
    }

    public SubpackageAgreement(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
        if (null == this.getSubContractId()) {
            this.setSubContractId(this.getId());
        }
    }

    public SubpackageAgreement() {
        super(null, TABLE);
        if (null == this.getSubContractId()) {
            this.setSubContractId(this.getId());
        }
    }

    public static String baseColumnList() {
        return " id, agree_type_id as agreeTypeId, profession_type_id as professionTypeId, agree_no as agreeNo, " +
                " `name`, money, salesman, biz_property as bizProperty, final_money as finalMoney, employer, " +
                " project_id as projectId, big_agree_id as bigAgreeId, " + BaseModel.baseColumnList();
    }

    public static String getSumByProjId(String projId, JsonArray params) {
        params.add(projId);//_final
        return " select sum(fie_subcon_amt) as money, sum(fie_subcon_final_amt) as final_money from " +
                TABLE + " where for_foreig_key = ? and del_flag = 0 ; ";
    }

    public static String getSumByProjId1(String projId, JsonArray params) {
        params.add(projId);
        return " select sum(money) as money from " + TABLE + " where project_id = ? and del_flag = 0; ";
    }

    public static String getIdByProjId(String projId, JsonArray params) {
        params.add(projId);
        return " select id from " + TABLE + " where for_foreig_key = ? and del_flag = 0 ; ";
    }

    public static String getSumByProjIds(JsonArray array, JsonArray params) {
        params.addAll(array);
        StringBuilder sBuilder = new StringBuilder(" select for_foreig_key as projectId, " +
                " sum(fie_subcon_amt) as money, sum(fie_subcon_final_amt) as finalMoney from ");
        sBuilder.append(TABLE).append(" where for_foreig_key in ( ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            sb.append(" , ? ");
        }
        sBuilder.append(array.size() > 0 ? sb.substring(2) : sb.toString())
                .append(" ) and del_flag = 0 group by for_foreig_key ; ");
        return sBuilder.toString();
    }

    public static String getSumByProjIds1(JsonArray array, JsonArray params) {
        params.addAll(array);
        StringBuilder sBuilder = new StringBuilder(" select project_id as projectId, sum(money) as money from ");
        sBuilder.append(TABLE).append(" where project_id in ( ");
        StringBuilder sb = new StringBuilder("    ");
        for (int i = 0; i < array.size(); i++) {
            sb.append(" , ? ");
        }
        sBuilder.append(sb.substring(2)).append(" ) and del_flag = 0 group by project_id ; ");
        return sBuilder.toString();
    }

    public static String getIdByProjIds(JsonArray array, JsonArray params) {
        params.addAll(array);
        StringBuilder sBuilder = new StringBuilder(" select for_foreig_key as projectId, id as ids, id from ");
        sBuilder.append(TABLE).append(" where for_foreig_key in ( ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            sb.append(" , ? ");
        }
        sBuilder.append(array.size() > 0 ? sb.substring(2) : sb.toString())
                .append(" ) and del_flag = 0 order by for_foreig_key ; ");
        return sBuilder.toString();
    }

    public static String getIdByProjIds1(JsonArray array, JsonArray params) {
        params.addAll(array);
        StringBuilder sBuilder = new StringBuilder(" select project_id as projectId, id as ids, id from ");
        sBuilder.append(TABLE).append(" where project_id in ( ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            sb.append(" , ? ");
        }
        sBuilder.append(sb.substring(2)).append(" ) and del_flag = 0 order by project_id ; ");
        return sBuilder.toString();
    }

    public static String deleteByProjId(String id, JsonArray params3) {
        params3.add(id);
        return " update " + TABLE + " set del_flag = 1 where for_foreig_key = ? ; ";
    }

    private String baseColumnListJoinA() {
        return " a.id, a.fie_subcon_type as agreeTypeId, a.fie_profes_category as professionTypeId, " +
                " a.fie_con_no as agreeNo, a.fie_con_name as `name`, a.fie_con_amt as money, " +
                " null as salesman, " +
                " a.fie_profes_attributes as bizProperty, " +
                " a.fie_con_final_amt as finalMoney, a.fie_sub_id as employer, a.project_id as projectId, " +
                " a.big_agree_id as bigAgreeId, a.gmt_create as gmtCreate ";
    }

    private static String baseColumnListJoin() {
        return " a.id, a.fie_subcon_type as agreeTypeId, " +
                //" b.fie_profes_category as professionTypeId, " +
                " a.fie_subcon_no as agreeNo, a.fie_subcon_name as `name`, a.fie_subcon_amt as money, " +
                //" null as salesman, " +
                //" b.fie_profes_attributes as bizProperty, " +
                " a.fie_subcon_final_amt as finalMoney, a.fie_subcon_supply as employer, " +
                " a.for_foreig_key as projectId, " +
                " a.fie_subcon_con_id as bigAgreeId, a.fie_subcon_date as gmtCreate ";
    }

    private String baseColumnListJoinA1() {
        return " a.id, a.agree_type_id as agreeTypeId, a.profession_type_id as professionTypeId, " +
                " a.agree_no as agreeNo, a.`name`, a.money, a.salesman, a.biz_property as bizProperty, " +
                " a.final_money as finalMoney, a.employer, a.project_id as projectId, " +
                " a.big_agree_id as bigAgreeId, a.gmt_create as gmtCreate ";
    }

    public static String getById(String id, JsonArray params) {
        params.add(id);
        StringBuilder sBuilder = new StringBuilder(" select ").append(baseColumnListJoin())
                .append(" from ").append(TABLE).append(" a where a.id = ? and a.del_flag = 0; ");

        return sBuilder.toString();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoin() + " , c.show_value as `label` from " +
                TABLE + " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId().trim())) {
            sb.append(" and fie_subcon_con_id = ? ");
            params.add(this.getBigAgreeId());
        }
        if (null != this.getProjectId() && !"".equals(this.getProjectId().trim())) {
            sb.append(" and for_foreig_key = ? ");
            params.add(this.getProjectId());
        }
        if (null != this.getEmployer() && !"".equals(this.getEmployer().trim())) {
            sb.append(" and fie_subcon_supply like ? ");
            params.add("%" + this.getEmployer() + "%");
        }
        if (null != this.getAgreeTypeId() && !"".equals(this.getAgreeTypeId())) {
            sb.append(" and fie_subcon_type = ? ");
            params.add(this.getAgreeTypeId());
        }
        if (null != this.getAgreeNo() && !"".equals(this.getAgreeNo().trim())) {
            sb.append(" and fie_con_no like ? ");
            params.add("%" + this.getAgreeNo() + "%");
        }
        sb.append(" ) d on a.id = d.bid left join ")
                .append(Dictionary.TABLE).append(" c on c.id = a.fie_subcon_type ");
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
        String sql = " select count(*) from ( select * from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getBigAgreeId() && !"".equals(this.getBigAgreeId().trim())) {
            sb.append(" and fie_subcon_con_id = ? ");
            params.add(this.getBigAgreeId());
        }
        if (null != this.getProjectId() && !"".equals(this.getProjectId().trim())) {
            sb.append(" and for_foreig_key = ? ");
            params.add(this.getProjectId());
        }
        if (null != this.getEmployer() && !"".equals(this.getEmployer().trim())) {
            sb.append(" and fie_subcon_supply = ? ");
            params.add(this.getEmployer());
        }
        if (null != this.getAgreeTypeId()) {
            sb.append(" and fie_subcon_type = ? ");
            params.add(this.getAgreeTypeId());
        }
        if (null != this.getAgreeNo() && !"".equals(this.getAgreeNo().trim())) {
            sb.append(" and fie_con_no like ? ");
            params.add("%" + this.getAgreeNo() + "%");
        }
        sb.append(" ) a ");
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

    public String getAgreeTypeId() {
        return this.entries.getString(agreeTypeId);
    }

    public void setAgreeTypeId(String agreeTypeId) {
        this.entries.put(SubpackageAgreement.agreeTypeId, agreeTypeId);
    }
    public String getAgreeNo() {
        return this.entries.getString(agreeNo);
    }

    public void setAgreeNo(String agreeNo) {
        this.entries.put(SubpackageAgreement.agreeNo, agreeNo);
    }
    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(SubpackageAgreement.name, name);
    }
    public String getMoney() {
        return this.entries.getString(SubpackageAgreement.money);
    }

    public void setMoney(String money) {
        this.entries.put(SubpackageAgreement.money, money);
    }
    public String getFinalMoney() {
        return this.entries.getString(SubpackageAgreement.finalMoney);
    }

    public void setFinalMoney(String finalMoney) {
        this.entries.put(SubpackageAgreement.finalMoney, finalMoney);
    }
    public String getEmployer() {
        return this.entries.getString(employer);
    }

    public void setEmployer(String employer) {
        this.entries.put(SubpackageAgreement.employer, employer);
    }
    public String getProjectId() {
        return this.entries.getString(projectId);
    }

    public void setProjectId(String projectId) {
        this.entries.put(SubpackageAgreement.projectId, projectId);
    }

    public String getBigAgreeId() {
        return this.entries.getString(bigAgreeId);
    }

    public String getSubContractId() {
        return this.entries.getString(subContractId);
    }

    private void setSubContractId(String subContractId) {
        this.entries.put(SubpackageAgreement.subContractId, subContractId);
    }
}
