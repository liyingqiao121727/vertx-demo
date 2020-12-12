package com.bgi.business.model.mis;

import com.bgi.common.SQLpage;
import com.bgi.constant.Constant;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.ProjectUser;
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

public class Project extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "engine_proj_info";

    protected static final HashMap<String, String> map = new HashMap<String, String>(5){{
        put(id, "id");
        put(projNo, "fie_engine_no");put(name, "fie_engine_name");put(desc, "desc");
        put(projStatus, "fie_engine_state");
        put(location, "fie_engine_loc");
        put(buildArea, "build_area");put(airCondition, "air_condition");
        put(filePath, "fie_upload_attachments");
        put(analyse, "analyse");put(remark, "remark");
        put(finishTime, "fie_engine_finish_date");
        put(glodonRemark, "glodon_remark");
        put(projLevel, "fie_engine_grade");put(linkman, "fie_linkman");
        put(tel, "fie_fixed_telephone");put(postcode, "fie_zip_code");
        put(workDept, "work_dept");
        //put(cmo, "cmo");
        put(pm, "fie_engine_host");put(otherPresident, "fie_other_hosts");
        put(tender, "fie_is_bid");//put(createTime, "fie_proj_date");

        put(essential, "essential");put(longitude, "longitude");
        put(latitude, "latitude");
        put(finish, "fie_is_finished");put(generalSituation, "fie_engine_survey");

        //put(xmProjId, "xm_proj_id");
        put(professionalTypes, "fie_profes_type");put(bizProperty, "fie_busine_attributes");
        put(bigWorkDept, "fie_impl_dept");put(isShare, "is_share");

        //put(agreementNo, "agreement_no");



        put(gmtCreate, "fie_proj_date");
    }};
    static {
        //Project.map.putAll(BaseModel.map);
    }

    /**
     * 项目编号
     */
    @SwaggerProperty(value = "工程编号")
    private static final String projNo = "projNo";

    /**
     * 合同编号（入参）
     */
    @SwaggerProperty(value = "合同编号（入参）")
    public static final String agreeNo = "agreeNo";
    /**
     * 项目名称
     */
    @SwaggerProperty(value = "工程名称")
    private static final String name = "name";
    /**
     * 描述
     */
    @SwaggerProperty(value = "描述")
    private static final String desc = "desc";
    /**
     * 广联达成本报表备注
     */
    @SwaggerProperty(value = "广联达成本报表备注")
    private static final String glodonRemark = "glodonRemark";
    /**
     * 位置
     */
    @SwaggerProperty(value = "位置")
    private static final String location = "location";
    /**
     * 建筑面积
     */
    @SwaggerProperty(value = "建筑面积")
    public static final String buildArea = "buildArea";
    /**
     * 空调面积
     */
    @SwaggerProperty(value = "空调面积")
    public static final String airCondition = "airCondition";
    /**
     * 文件路径
     */
    @SwaggerProperty(value = "文件路径")
    private static final String filePath = "filePath";
    /**
     * 生产成本分析
     */
    @SwaggerProperty(value = "生产成本分析")
    private static final String analyse = "analyse";
    /**
     * 备注
     */
    @SwaggerProperty(value = "备注")
    private static final String remark = "remark";
    /**
     * 状态（0：创建；1：开始；2：正常结束；3：非正常结束）
     */
    @SwaggerProperty(value = "状态（0：创建；1：开始；2：正常结束；3：非正常结束）")
    private static final String projStatus = "projStatus";
    /**
     * 项目id
     */
    @SwaggerProperty(value = "项目id")
    private static final String xmProjId = "xmProjId";
    /**
     * 项目名称
     */
    @SwaggerProperty(value = "项目名称")
    private static final String xmProjName = "xmProjName";
    /**
     * 项目编号
     */
    @SwaggerProperty(value = "项目编号")
    private static final String xmProjNo = "xmProjNo";
    /**
     * 专业类型
     */
    @SwaggerProperty(value ="专业类型")
    private static final String professionalTypes = "professionalTypes";
    /**
     * 业务属性
     */
    @SwaggerProperty(value ="业务属性")
    private static final String bizProperty = "bizProperty";
    /**
     * 合同编号
     */
    @SwaggerProperty(value ="合同编号")
    private static final String agreementNo = "agreementNo";
    /**
     * 工程等级
     */
    @SwaggerProperty(value ="工程等级")
    private static final String projLevel = "projLevel";
    /**
     * 工程量要素
     */
    @SwaggerProperty(value ="工程量要素")
    private static final String essential = "essential";
    /**
     * 工程概况
     */
    @SwaggerProperty(value ="工程概况")
    private static final String generalSituation = "generalSituation";
    /**
     * 经度
     */
    @SwaggerProperty(value ="经度")
    private static final String longitude = "longitude";
    /**
     * 经度
     */
    @SwaggerProperty(value ="纬度")
    private static final String latitude = "latitude";
    /**
     * 联系人
     */
    @SwaggerProperty(value ="联系人")
    private static final String linkman = "linkman";
    /**
     * 手机号
     */
    @SwaggerProperty(value ="手机号")
    private static final String tel = "tel";
    /**
     * 邮编
     */
    @SwaggerProperty(value ="邮编")
    private static final String postcode = "postcode";
    /**
     * 实施部门
     */
    @SwaggerProperty(value ="实施部门")
    private static final String workDept = "workDept";
    /**
     * 营销经理
     */
    @SwaggerProperty(value ="营销经理")
    private static final String cmo = "cmo";
    /**
     * 工程主持人
     */
    @SwaggerProperty(value ="工程主持人")
    private static final String pm = "pm";
    /**
     * 其他主持人
     */
    @SwaggerProperty(value ="其他主持人")
    private static final String otherPresident = "otherPresident";
    /**
     * 是否投标
     */
    @SwaggerProperty(value ="是否投标", type = Integer.class)
    private static final String tender = "tender";
    /**
     * 是否结项
     */
    @SwaggerProperty(value ="是否结项", type = Integer.class)
    private static final String finish = "finish";
    /**
     * 实施部门（院级别）
     */
    @SwaggerProperty(value = "实施部门（院级别）")
    private static final String bigWorkDept = "bigWorkDept";
    /**
     * 立项时间
     */
    @SwaggerProperty(value ="立项时间")
    private static final String createTime = "createTime";
    /**
     * 结项时间
     */
    @SwaggerProperty(value ="结项时间")
    private static final String finishTime = "finishTime";
    /**
     * 结项时间
     */
    @SwaggerProperty(value ="是否是分摊项目")
    private static final String isShare = "isShare";
    /**
     * 近1年，近3年，近5年
     */
    @SwaggerProperty(value ="近1年，近3年，近5年", type = Integer.class)
    private static final String years = "years";

    public Project(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public Project(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public Project() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, proj_no as projNo, `name`, `desc`, location, remark, `finish`, " +
                " proj_status as projStatus, finish_time as finishTime, create_time as createTime, "
                + BaseModel.baseColumnList();
    }

    public static String columnListJoinA() {
        return " a.id, a.fie_engine_no as projNo, a.fie_engine_name as `name`, a.fie_engine_survey as `desc`, " +
                " a.fie_engine_loc as location, " +
                " build_area as buildArea, air_condition as airCondition, " +
                " a.fie_upload_attachments as filePath, " +
                " analyse as analyse, a.remark as remark, " +
                " a.fie_engine_state as projStatus,  a.fie_engine_grade as projLevel, " +
                " glodon_remark as glodonRemark, " +
                " longitude as longitude, " +
                " a.fie_linkman as linkman, " +
                " a.fie_linkman as linkmanName, " +
                " a.fie_fixed_telephone as tel, " +
                " a.fie_zip_code as postcode, " +
                " a.fie_impl_dept as bigWorkDept, " +
                " work_dept as workDept, " +
                " a.fie_engine_host as pm, " +
                " latitude as latitude, essential as essential, a.fie_engine_survey as generalSituation, " +
                " a.fie_is_finished as `finish`," +
                " a.fie_other_hosts as otherPresident, a.fie_is_bid as tender, a.fie_proj_date as createTime, " +
                " a.fie_profes_type as professionalTypes, a.fie_busine_attributes as bizProperty, " +
                " a.fie_engine_finish_date as finishTime, a.fie_proj_date as gmtCreate, " +
                " null as gmtModified ";
    }

    public static String columnListJoinA1() {
        return " a.id, a.proj_no as projNo, a.`name`, a.`desc`, a.location, a.build_area as buildArea, " +
                " a.air_condition as airCondition, a.file_path as filePath, a.analyse, a.remark, " +
                " a.proj_status as projStatus,  a.proj_level as projLevel, a.glodon_remark as glodonRemark, " +
                " a.longitude, a.linkman, a.tel, a.postcode, a.work_dept as workDept, a.cmo, a.pm, a.finish, " +
                " a.latitude, a.essential, a.general_situation as generalSituation, a.`finish`," +
                " a.other_president as otherPresident, a.tender, a.create_time as createTime, " +
                " a.professional_types as professionalTypes, a.biz_property as bizProperty, " +
                " a.finish_time as finishTime, a.gmt_create as gmtCreate, a.gmt_modified as gmtModified ";
    }

    public static String columnListJoinASimple() {
        return " a.id, a.fie_engine_no as projNo, a.fie_engine_name as `name`, a.fie_engine_survey as `desc`, " +
                " a.fie_engine_loc as location, " +
                " a.fie_engine_survey as remark, " +
                " a.fie_is_finished as `finish`, " +
                " a.fie_engine_state as projStatus, a.fie_engine_finish_date as finishTime, " +
                " a.fie_proj_date as createTime,  a.fie_proj_date as gmtCreate, null as gmtModified ";
    }

    public static String columnListJoinASimple1() {
        return "  a.id, a.proj_no as projNo, a.`name`, a.`desc`, a.location, a.remark, a.`finish`, " +
                " a.proj_status as projStatus, a.finish_time as finishTime, a.create_time as createTime, " +
                " a.gmt_create as gmtCreate, a.gmt_modified as gmtModified ";
    }

    public static String getProjByUserId() {
        return " select " + baseColumnList() + " from " + TABLE +
                " p right join ( select proj_id from " + ProjectUser.TABLE +
                " where user_id = ? ) u on p.id = u.proj_id;";
    }

    public static String getProjIdLike(Project project, JsonArray params) {
        String sql = " select id from " + TABLE + " where fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder builder = new StringBuilder(sql);
        if (null != project.getName()) {
            builder.append(" and `fie_engine_name` = ? ");
            params.add("%" + project.getName() + "%");
        }
        if (null != project.getProjNo()) {
            builder.append(" and fie_engine_no = ? ");
            params.add("%" + project.getProjNo() + "%");
        }
        if (null != project.getProjStatus()) {
            builder.append(" and fie_engine_state = ? ");
            params.add(project.getProjStatus());
        }
        if (null != project.getFinish()) {
            builder.append(" and `fie_is_finished` = ? ");
            params.add(project.getFinish());
        }

        return builder.append(" ; ").toString();
    }

    public static String getOne(String id, JsonArray params) {
        params.add(id);
        return " select " + columnListJoinA() + " , a.is_share as isShare , b.fie_con_no as agreementNo, " +
                " b.fie_con_manager as cmo, " +
                " u1.`name` as cmoName, u2.`name` as pmName, u3.`name` as otherPresidentName " +
                " from ( select * from " + TABLE +
                " where id = ? and del_flag = 0 and fie_impl_dept in " +
                Constant.EARTH_ENERGY_DEPT_IDS + ") a left join " +
                BigAgreement.TABLE + " b on b.for_foreig_key = a.id left join " + EarthEnergyUser.TABLE +
                " u1 on u1.id = b.fie_con_manager left join " + EarthEnergyUser.TABLE +
                " u2 on u2.id = a.fie_engine_host left join " + EarthEnergyUser.TABLE +
                " u3 on u3.id = a.fie_other_hosts; ";
    }

    public static String getOneSimple(String id, JsonArray params) {
        params.add(id);
        return " select " + columnListJoinASimple() + " , b.fie_con_no as agreementNo, b.salesman as cmo " +
                " from ( select * from " + TABLE + " where id = ? and del_flag = 0 and fie_impl_dept in " +
                Constant.EARTH_ENERGY_DEPT_IDS + ") a left join " +
                BigAgreement.TABLE + " b on b.for_foreig_key = a.id ; ";
    }

    public static String deleteById(String id, JsonArray params1) {
        params1.add(id);
        return " update " + TABLE + " set del_flag = 1 where id = ? ; ";
    }

    public static String sumProject() {
        return " select sum(a.fie_engine_amt) as bigAgreeMoney, sum(a.fie_engine_final_amt) as bigAgreeFinalMoney, " +
                " sum(b.fie_sub_pay_amt) as totalMoney, sum(c.fie_subcon_final_amt) as finalMoney, " +
                " 0 as feeMoney from " +
                " ( select id, fie_engine_amt, fie_engine_final_amt from " + Project.TABLE +
                " where fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS + " and del_flag = 0 ) a " +
                " left join ( select for_foreig_key, fie_sub_pay_amt from " + SubpackagePay.TABLE +
                " where fie_sub_pay_dept in " + Constant.EARTH_ENERGY_DEPT_IDS + " and del_flag = 0) b " +
                " on a.id = b.for_foreig_key left join " + SubpackageAgreement.TABLE +
                " c on a.id = c.for_foreig_key ";
    }

    public static String changeMoney(String projectId, String money, JsonArray params) {
        if (null == money || "".equals(money)) {
            money = "0";
        }
        params.add(money).add(projectId);
        return " update " + TABLE + " set fie_engine_final_amt = fie_engine_final_amt + (?) where id = ? ";
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public static String getProjByIdIn(int size) {
        StringBuilder sb = new StringBuilder(" select ").append(columnListJoinASimple())
                .append(" ,a.is_share as isShare from ")
                .append(TABLE).append(" a where a.id in ( ");
        for (int i = 0; i < size; i++) {
            sb.append(" ? , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ")
                .append(" ) and del_flag = 0 and fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS + "  ; ").toString();
    }

    public static String getProjByIdIn1(int size) {
        StringBuilder sb = new StringBuilder(" select id, proj_no as projNo, `name`, `desc`, " +
                " proj_status as projStatus, finish_time as finishTime, create_time as createTime from ")
                .append(TABLE).append(" where id in ( ");
        for (int i = 0; i < size; i++) {
            sb.append(" ? , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ").append(" ) and del_flag = 0 ; ").toString();
    }

    public String queryPage(SQLpage sqLpage, JsonArray params) {
        StringBuilder sb = new StringBuilder(" select ").append(columnListJoinASimple());
        sb.append(" , a.fie_engine_amt as bigAgreeMoney, a.fie_engine_final_amt as bigAgreeFinalMoney, " +
                " d.id as bigAgrees, d.id as bigAgreeId, d.fie_con_no as agreeNo, d.fie_contra_name as bigAgreeName ");  //_final
        sb.append(" from " + TABLE + " a inner join ( select id as bid from " + TABLE +
                " where del_flag = 0 and fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS);
        if (null != this.getId() && !"".equals(this.getId().trim())) {
            sb.append(" and id = ? ");
            params.add(this.getId());
        }
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and fie_engine_name like ? ");
            params.add("%" + this.getName() + "%");
        }
        if (null != this.getProjNo()) {
            sb.append(" and fie_engine_no like ? ");
            params.add("%" + this.getProjNo() + "%");
        }
        if (null != this.getProjStatus()) {
            sb.append(" and fie_engine_state = ? ");
            params.add(this.getProjStatus());
        }
        if (null != this.getFinish()) {
            sb.append(" and `fie_is_finished` = ? ");
            params.add(this.getFinish());
        }
        if (null != this.getCreateTime()) {
            sb.append(" and fie_proj_date >= ? ");
            params.add(this.getCreateTime());
        }
        if (null != this.getIsShare()) {
            sb.append(" and is_share = ? ");
            params.add(this.getIsShare());
        }
        sb.append(" ) b on a.id = b.bid ");

        sb.append(" inner join ( select * from ").append(BigAgreement.TABLE).append(" where del_flag = 0 ");
        if (null != this.getAgreeNo()) {
            sb.append(" and fie_con_no like ? ");
            params.add("%" + this.getAgreeNo() + "%");
        }
        sb.append(" ) d on d.for_foreig_key = b.bid ").append(sqLpage.toString());

        return sb.append(" ; ").toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from ( select id from " + TABLE +
                " where del_flag = 0 and fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getId() && !"".equals(this.getId().trim())) {
            sb.append(" and id = ? ");
            params.add(this.getId());
        }
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and fie_engine_name like ? ");
            params.add("%" + this.getName() + "%");
        }
        if (null != this.getProjNo()) {
            sb.append(" and fie_engine_no like ? ");
            params.add("%" + this.getProjNo() + "%");
        }
        if (null != this.getProjStatus()) {
            sb.append(" and fie_engine_state = ? ");
            params.add(this.getProjStatus());
        }
        if (null != this.getFinish()) {
            sb.append(" and `fie_is_finished` = ? ");
            params.add(this.getFinish());
        }
        if (null != this.getCreateTime()) {
            sb.append(" and fie_proj_date >= ? ");
            params.add(this.getCreateTime());
        }
        if (null != this.getIsShare()) {
            sb.append(" and is_share = ? ");
            params.add(this.getIsShare());
        }
        sb.append(" ) a ");
        if (null != this.getAgreeNo()) {
            sb.append(" inner join ( select * from ").append(BigAgreement.TABLE).append(" where del_flag = 0 ");
            sb.append(" and fie_con_no like ? ");

            params.add("%" + this.getAgreeNo() + "%");
            sb.append(" ) d on d.for_foreig_key = a.id  ; ");
        }
        return sb.append(" ; ").toString();
    }

    public String queryPage1(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " , bigAgreeId, bigAgreeMoney from " + TABLE +
                " a inner join ( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and name like ? ");
            params.add("%" + this.getName() + "%");
        }
        if (null != this.getProjNo()) {
            sb.append(" and proj_no like ? ");
            params.add("%" + this.getProjNo() + "%");
        }
        if (null != this.getProjStatus()) {
            sb.append(" and proj_status = ? ");
            params.add(this.getProjStatus());
        }
        if (null != this.getFinish()) {
            sb.append(" and `finish` = ? ");
            params.add(this.getFinish());
        }
        if (null != this.getCreateTime()) {
            sb.append(" and create_time >= ? ");
            params.add(this.getCreateTime());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid ");
        sb.append(" inner join ( select project_id as cpid, id as bigAgreeId, money as bigAgreeMoney from ")
                .append(BigAgreement.TABLE).append(" where  del_flag = 0 ");

        if (null != this.getAgreeNo()) {
            sb.append(" and agree_no like ? ");
            params.add("%" + this.getAgreeNo() + "%");
        }
        sb.append(" ) c on c.cpid = b.bid ; ");

        return sb.toString();
    }

    public String countPage1(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and name like ? ");
            params.add("%" + this.getName() + "%");
        }
        if (null != this.getProjNo()) {
            sb.append(" and proj_no like ? ");
            params.add("%" + this.getProjNo() + "%");
        }
        if (null != this.getProjStatus()) {
            sb.append(" and proj_status = ? ");
            params.add(this.getProjStatus());
        }
        if (null != this.getFinish()) {
            sb.append(" and `finish` = ? ");
            params.add(this.getFinish());
        }
        if (null != this.getCreateTime()) {
            sb.append(" and create_time >= ? ");
            params.add(this.getCreateTime());
        }
        if (null != this.getAgreeNo()) {
            sb.append(" and id in ( select project_id from ").append(BigAgreement.TABLE)
                    .append(" where agree_no like ? and del_flag = 0 ) ");
            params.add("%" + this.getAgreeNo() + "%");
        }
        return sb.append(" ; ").toString();
    }

    public String list(JsonArray params) {
        String sql = " select " + columnListJoinASimple() + " , a.is_share as isShare  from ( select * from " + TABLE +
                " where del_flag = 0 and fie_impl_dept in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and fie_engine_name = ? ");
            params.add(this.getName());
        }
        if (null != this.getProjStatus()) {
            sb.append(" and fie_engine_state = ? ");
            params.add(this.getProjStatus());
        }
        if (null != this.getIsShare()) {
            sb.append(" and is_share = ? ");
            params.add(this.getIsShare());
        }
        sb.append(" ) a ; ");

        return sb.toString();
    }

    public String getProjNo() {
        return this.entries.getString(projNo);
    }

    public String getAgreeNo() {
        return this.entries.getString(agreeNo);
    }

    public void setProjNo(String projNo) {
        this.entries.put(Project.projNo, projNo);
    }
    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(Project.name, name);
    }
    public String getDesc() {
        return this.entries.getString(desc);
    }

    public void setDesc(String desc) {
        this.entries.put(Project.desc, desc);
    }
    public String getLocation() {
        return this.entries.getString(location);
    }

    public void setLocation(String location) {
        this.entries.put(Project.location, location);
    }
    public BigDecimal getBuildArea() {
        String buildArea = this.entries.getString(Project.buildArea);
        if (null == buildArea || "".equals(buildArea.trim())) {
            return null;
        }
        return new BigDecimal(buildArea);
    }

    public void setBuildArea(BigDecimal buildArea) {
        this.entries.put(Project.buildArea, null == buildArea ? null : buildArea.toString());
    }
    public String getAirCondition() {
        return this.entries.getString(airCondition);
    }

    public void setAirCondition(String airCondition) {
        this.entries.put(Project.airCondition, airCondition);
    }
    public String getFilePath() {
        return this.entries.getString(filePath);
    }

    public void setFilePath(String filePath) {
        this.entries.put(Project.filePath, filePath);
    }
    public String getAnalyse() {
        return this.entries.getString(analyse);
    }

    public void setAnalyse(String analyse) {
        this.entries.put(Project.analyse, analyse);
    }
    public String getRemark() {
        return this.entries.getString(remark);
    }

    public void setRemark(String remark) {
        this.entries.put(Project.remark, remark);
    }
    public String getProjStatus() {
        return this.entries.getString(projStatus);
    }

    public void setProjStatus(String projStatus) {
        this.entries.put(Project.projStatus, projStatus);
    }

    public String getAgreementNo() {
        return entries.getString(agreementNo);
    }

    public String getCmo() {
        return entries.getString(cmo);
    }

    public Long getBizProperty() {
        return entries.getLong(bizProperty);
    }

    public Long getProfessionalTypes() {
        return entries.getLong(professionalTypes);
    }

    public String getGlodonRemark() {
        return entries.getString(glodonRemark);
    }

    public Integer getFinish() {
        return entries.getInteger(finish);
    }

    public Integer getYears() {
        return entries.getInteger(years);
    }

    public void setCreateTime(String createTime) {
        entries.put(Project.createTime, createTime);
    }

    public String getCreateTime() {
        return entries.getString(createTime);
    }

    public String getBigWorkDept() {
        return entries.getString(bigWorkDept);
    }

    public void setBigWorkDept(String bigWorkDept) {
        entries.put(Project.bigWorkDept, bigWorkDept);
    }

    public Integer getIsShare() {
        return entries.getInteger(isShare);
    }
}
