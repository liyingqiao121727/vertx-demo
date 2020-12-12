package com.bgi.business.model.mis;

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

public class EEuser extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "org_user";

    public static final String TABLE1 = "org_user_detail";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");put(userId, "user_id");
        //put(deptId, "dept_id");put(postId, "post_id");
        put(visit, "visit");put(fixMoney, "fix_money");put(fixBenefit, "fix_benefit");
        put(institutePost, "institute_post");put(senioritySubsidy, "seniority_subsidy");
        put(certificateSubsidy, "certificate_subsidy");put(food, "food");
        //put(deptName, "dept_name");put(postName, "post_name");
        put(realName, "out_sys_name");
        put(username, "login_name");put(education, "education");
        put(practicingRequirements, "practicing_requirements");
        put(addr, "addr");
        put(email, "email");
        put(sex, "sex");
        put(phone, "tel");
    }};

    static {
        //EEuser.map.putAll(BaseModel.map);
    }

    /**
     * 岩云宝用户id
     */
    @SwaggerProperty(value = "岩云宝用户id")
    private static final String userId = "userId";
    /**
     * 部门id
     */
    @SwaggerProperty(value = "部门id")
    private static final String deptId = "deptId";
    /**
     * 岗位id
     */
    @SwaggerProperty(value = "岗位id")
    private static final String postId = "postId";
    /**
     * 是否能够访问本系统
     */
    @SwaggerProperty(value = "是否能够访问本系统")
    private static final String visit = "visit";
    /**
     * 真实姓名
     */
    @SwaggerProperty(value = "真实姓名")
    private static final String realName = "realName";
    /**
     * 用户名
     */
    @SwaggerProperty(value = "用户名")
    private static final String username = "username";
    /**
     * 学历
     */
    @SwaggerProperty(value = "学历")
    private static final String education = "education";
    /**
     * 执业资格
     */
    @SwaggerProperty(value = "执业资格")
    private static final String practicingRequirements = "practicingRequirements";
    /**
     * 固定岗位工资
     */
    @SwaggerProperty(value = "固定岗位工资")
    private static final String fixMoney = "fixMoney";
    /**
     * 固定效益
     */
    @SwaggerProperty(value = "固定效益")
    private static final String fixBenefit = "fixBenefit";
    /**
     * 所岗级
     */
    @SwaggerProperty(value = "所岗级")
    private static final String institutePost = "institutePost";
    /**
     * 工龄补贴
     */
    @SwaggerProperty(value = "工龄补贴")
    private static final String senioritySubsidy = "senioritySubsidy";
    /**
     * 证书补助
     */
    @SwaggerProperty(value = "证书补助")
    private static final String certificateSubsidy = "certificateSubsidy";
    /**
     * 项目补贴标准
     */
    @SwaggerProperty(value ="项目补贴标准")
    private static final String projSubsidySt = "projSubsidySt";
    /**
     * 外业补贴标准
     */
    @SwaggerProperty(value ="外业补贴标准")
    private static final String outsideSubsidySt = "outsideSubsidySt";
    /**
     * 餐补
     */
    @SwaggerProperty(value = "餐补")
    private static final String food = "food";
    /**
     * 子部门名称
     */
    @SwaggerProperty(value = "子部门名称")
    private static final String deptName = "deptName";
    /**
     * 岗位名称
     */
    @SwaggerProperty(value = "岗位名称")
    private static final String postName = "postName";
    /**
     * 地址
     */
    @SwaggerProperty(value = "地址")
    private static final String addr = "addr";
    /**
     * 邮件
     */
    @SwaggerProperty(value = "邮件")
    private static final String email = "email";
    /**
     * 性别
     */
    @SwaggerProperty(value = "性别")
    private static final String sex = "sex";
    /**
     * 电话
     */
    @SwaggerProperty(value = "电话")
    private static final String phone = "phone";

    public EEuser(JsonObject jsonObject) {
        super(jsonObject, TABLE1);
    }

    public EEuser() {
        super(null, TABLE1);
    }

    public static String baseColumnListJoin() {
        return " a.id, a.user_id as userId, a.out_sys_name as realName, 1 as visit, " +
                " a.login_name as username, a.sex, a.tel as phone,  a.addr, " +
                " a.email, a.education, a.practicing_requirements as practicingRequirements ";
    }

    public static String baseColumnListJoinAll() {
        return " a.id, a.user_id as userId, a.out_sys_name as realName, 1 as visit, " +
                " a.login_name as username, a.sex, a.tel as phone,  a.addr, " +
                " a.email, a.education, a.practicing_requirements as practicingRequirements ";
    }

    public static String baseColumnList() {
        return " id, user_id as userId, out_sys_name as realName, 1 as visit, " +
                " login_name as username, sex, tel as phone,  addr, " +
                " email, a.education, a.practicing_requirements as practicingRequirements ";
    }

    public static String baseColumnList1() {
        return " id, user_id as userId, dept_id as deptId, dept_name as deptName, real_name as realName, " +
                " post_id as postId, post_name as postName, visit, fix_money as fixMoney, food, " +
                " fix_benefit as fixBenefit, institute_post as institutePost, " +
                " seniority_subsidy as senioritySubsidy, " +
                " certificate_subsidy as certificateSubsidy, " +
                " out_subsidy_st as outSubsidySt, proj_subsidy_st as projSubsidySt, " + BaseModel.baseColumnList();
    }

    public static String getByUserId() {
        return "select " + baseColumnListJoinAll() +  " from " + TABLE1 + " a where a.user_id = ? ";
    }

    public static String getVisitByUserId() {
        return "select visit from " + TABLE + " where user_id = ? ;";
    }

    public static String canVisit() {
        return " select user_id from ( select user_id from " + TABLE1 + " where login_name = ? ) a " +
                " inner join " + TABLE + " b on a.user_id = b.id " +
                " and b.del_flag = 0 and b.is_enable = 1 and b.is_loginable = 1 and b.state = 1 ; ";
    }

    public String updateMsgById(JsonArray params) {
        params.add(this.getRealName()).add(this.getDeptId()).add(this.getDeptName())
                .add(this.getPostName()).add(this.getId());
        return " update " + TABLE + " set real_name = ? , dept_id = ? , " +
                " dept_name = ? , post_name = ? where id = ? ; ";
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnListJoin() + " from " +
                TABLE1 + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 and is_enable = 1 and is_loginable = 1 " +
                " and state = 1 and company_dept_id in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId()) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }

        sb.append(" ) u1 on a.user_id = u1.bid ");
        if (null != this.getUsername()) {
            sb.append(" and a.login_name like ? ");
            params.add("%" + this.getUsername() + "%");
        }
        if (null != this.getRealName()) {
            sb.append(" and a.out_sys_name like ? ");
            params.add("%" + this.getRealName() + "%");
        }
        if (null != this.getEducation()) {
            sb.append(" and a.education like ? ");
            params.add("%" + this.getEducation() + "%");
        }
        if (null != this.getPracticingRequirements()) {
            sb.append(" and a.practicing_requirements like ? ");
            params.add("%" + this.getPracticingRequirements() + "%");
        }

        return sb.append(sqLpage.toString()).append(" ; ").toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from ( select id from " + TABLE +
                " where del_flag = 0 and is_enable = 1 and is_loginable = 1 " +
                " and state = 1 and company_dept_id in " + Constant.EARTH_ENERGY_DEPT_IDS;
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId()) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        sb.append(") a inner join ").append(TABLE1).append(" b on a.id = b.user_id ");
        if (null != this.getUsername()) {
            sb.append(" and b.login_name like ? ");
            params.add("%" + this.getUsername() + "%");
        }
        if (null != this.getRealName()) {
            sb.append(" and b.out_sys_name like ? ");
            params.add("%" + this.getRealName() + "%");
        }
        if (null != this.getEducation()) {
            sb.append(" and b.education like ? ");
            params.add("%" + this.getEducation() + "%");
        }
        if (null != this.getPracticingRequirements()) {
            sb.append(" and b.practicing_requirements like ? ");
            params.add("%" + this.getPracticingRequirements() + "%");
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

    public String getRealName() {
        return this.entries.getString(realName);
    }

    public void setRealName(String realName) {
        this.entries.put(EEuser.realName, realName);
    }

    public Object getUserId() {
        return this.entries.getValue(userId);
    }

    public void setUserId(Object userId) {
        this.entries.put(EEuser.userId, userId);
    }
    public Long getDeptId() {
        return this.entries.getLong(deptId);
    }

    public void setDeptId(Long deptId) {
        this.entries.put(EEuser.deptId, deptId);
    }
    public String getPostId() {
        return this.entries.getString(postId);
    }

    public void setPostId(String postId) {
        this.entries.put(EEuser.postId, postId);
    }
    public Integer getVisit() {
        return this.entries.getInteger(visit);
    }

    public void setVisit(Integer visit) {
        this.entries.put(EEuser.visit, visit);
    }
    public BigDecimal getFixMoney() {
        String fixMoney = this.entries.getString(EEuser.fixMoney);
        if (null == fixMoney || "".equals(fixMoney.trim())) {
            return null;
        }
        return new BigDecimal(fixMoney);
    }

    public void setFixMoney(BigDecimal fixMoney) {
        this.entries.put(EEuser.fixMoney, null == fixMoney ? null : fixMoney.toString());
    }
    public BigDecimal getFixBenefit() {
        String fixBenefit = this.entries.getString(EEuser.fixBenefit);
        if (null == fixBenefit || "".equals(fixBenefit.trim())) {
            return null;
        }
        return new BigDecimal(fixBenefit);
    }

    public void setFixBenefit(BigDecimal fixBenefit) {
        this.entries.put(EEuser.fixBenefit, null == fixBenefit ? null : fixBenefit.toString());
    }
    public BigDecimal getInstitutePost() {
        String institutePost = this.entries.getString(EEuser.institutePost);
        if (null == institutePost || "".equals(institutePost.trim())) {
            return null;
        }
        return new BigDecimal(institutePost);
    }

    public void setInstitutePost(BigDecimal institutePost) {
        this.entries.put(EEuser.institutePost, null == institutePost ? null : institutePost.toString());
    }
    public BigDecimal getSenioritySubsidy() {
        String senioritySubsidy = this.entries.getString(EEuser.senioritySubsidy);
        if (null == senioritySubsidy || "".equals(senioritySubsidy.trim())) {
            return null;
        }
        return new BigDecimal(senioritySubsidy);
    }

    public void setSenioritySubsidy(BigDecimal senioritySubsidy) {
        this.entries.put(EEuser.senioritySubsidy,
                null == senioritySubsidy ? null : senioritySubsidy.toString());
    }
    public BigDecimal getCertificateSubsidy() {
        String certificateSubsidy = this.entries.getString(EEuser.certificateSubsidy);
        if (null == certificateSubsidy || "".equals(certificateSubsidy.trim())) {
            return null;
        }
        return new BigDecimal(certificateSubsidy);
    }

    public void setCertificateSubsidy(BigDecimal certificateSubsidy) {
        this.entries.put(EEuser.certificateSubsidy,
                null == certificateSubsidy ? null : certificateSubsidy.toString());
    }
    public BigDecimal getFood() {
        String food = this.entries.getString(EEuser.food);
        if (null == food || "".equals(food.trim())) {
            return null;
        }
        return new BigDecimal(food);
    }

    public void setFood(BigDecimal food) {
        this.entries.put(EEuser.food, null == food ? null : food.toString());
    }

    public String getDeptName() {
        return this.entries.getString(deptName);
    }

    public void setDeptName(String deptName) {
        this.entries.put(EEuser.deptName, deptName);
    }

    public String getPostName() {
        return this.entries.getString(postName);
    }

    public void setPostName(String postName) {
        this.entries.put(EEuser.postName, postName);
    }

    public BigDecimal getOutsideSubsidySt() {
        String outsideSubsidySt = this.entries.getString(EEuser.outsideSubsidySt);
        if (null == outsideSubsidySt || "".equals(outsideSubsidySt.trim())) {
            return null;
        }
        return new BigDecimal(outsideSubsidySt);
    }

    public void setOutsideSubsidySt(String outsideSubsidySt) {
        this.entries.put(EEuser.outsideSubsidySt, outsideSubsidySt);
    }

    public BigDecimal getProjSubsidySt() {
        String projSubsidySt = this.entries.getString(EEuser.projSubsidySt);
        if (null == projSubsidySt || "".equals(projSubsidySt.trim())) {
            return null;
        }
        return new BigDecimal(projSubsidySt);
    }

    public void setProjSubsidySt(String projSubsidySt) {
        this.entries.put(EEuser.projSubsidySt, projSubsidySt);
    }

    public String getUsername() {
        return entries.getString(username);
    }

    public String getEducation() {
        return entries.getString(education);
    }

    public String getPracticingRequirements() {
        return entries.getString(practicingRequirements);
    }
}
