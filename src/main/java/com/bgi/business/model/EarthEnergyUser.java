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

public class EarthEnergyUser extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "earth_energy_user";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(userId, "user_id");put(deptId, "dept_id");put(postId, "post_id");
        put(visit, "visit");put(fixMoney, "fix_money");put(fixBenefit, "fix_benefit");
        put(institutePost, "institute_post");put(senioritySubsidy, "seniority_subsidy");
        put(certificateSubsidy, "certificate_subsidy");put(food, "food");
        put(outsideSubsidySt, "out_subsidy_st");put(projSubsidySt, "proj_subsidy_st");
        put(deptName, "dept_name");put(postName, "post_name");put(realName, "real_name");
        put(loginName, "login_name");put(isShare, "is_share");put(nightShift, "night_shift");
    }};

    static {
        EarthEnergyUser.map.putAll(BaseModel.map);
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
     * 是否参与分摊
     */
    @SwaggerProperty(value = "是否参与分摊")
    private static final String isShare = "isShare";
    /**
     * 夜班补助
     */
    @SwaggerProperty(value = "夜班补助")
    private static final String nightShift = "nightShift";
    /**
     * 真实姓名
     */
    @SwaggerProperty(value = "真实姓名")
    private static final String realName = "realName";
    /**
     * 登录名
     */
    @SwaggerProperty(value = "登录名")
    private static final String loginName = "loginName";
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

    public EarthEnergyUser(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public EarthEnergyUser(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public EarthEnergyUser() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, user_id as userId, dept_id as deptId, dept_name as deptName, real_name as realName, " +
                " post_id as postId, post_name as postName, visit, fix_money as fixMoney, food, " +
                " fix_benefit as fixBenefit, institute_post as institutePost, " +
                " seniority_subsidy as senioritySubsidy, login_name as loginName, " +
                " certificate_subsidy as certificateSubsidy, is_share as isShare, night_shift as nightShift, " +
                " out_subsidy_st as outsideSubsidySt, proj_subsidy_st as projSubsidySt, " + BaseModel.baseColumnList();
    }

    /*public static String insertBatch(List<EarthEnergyUser> list, JsonArray params) {
        StringBuilder sb = new StringBuilder(" insert into ").append(TABLE).append(
                " ( id, user_id, dept_id, post_id, visit, fix_money, fix_benefit, institute_post, " +
                        " seniority_subsidy, certificate_subsidy, dept_name, post_name, real_name, " +
                        " out_subsidy_st, proj_subsidy_st, food, ")
                .append(BaseModel.baseColumn()).append(" ) values    ");
        for (EarthEnergyUser user : list) {
            params.add(user.getId()).add(user.getUserId()).add(user.getDeptId()).add(user.getPostId())
                    .add(user.getVisit()).add(user.entries.getString(fixMoney))
                    .add(user.entries.getString(fixBenefit))
                    .add(user.entries.getString(institutePost)).add(user.entries.getString(senioritySubsidy))
                    .add(user.entries.getString(certificateSubsidy))
                    .add(user.getDeptName()).add(user.getPostName()).add(user.getRealName())
                    .add(user.entries.getString(outsideSubsidySt)).add(user.entries.getString(projSubsidySt))
                    .add(user.entries.getString(food));
            user.addParams(params);
            sb.append("( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ) , ");
        }

        return sb.replace(sb.length()-3, sb.length() -1, " ; ").toString();
    }*/

    public static String getByUserId() {
        return "select " + baseColumnList() +  " from " + TABLE + " where user_id = ? ;";
    }

    public static String getVisitByUserId() {
        return "select visit from " + TABLE + " where user_id = ? ;";
    }

    public static String getVisitByUserName(String username, JsonArray params) {
        params.add(username);
        return " select user_id as userId from " + TABLE + " where login_name = ? and del_flag = 0 and visit = 1; ";
    }

    public static String getByLoginNames(List<String> loginNames) {
        StringBuilder sql = new StringBuilder(" select id, login_name as loginName from ").append(TABLE)
                .append(" where login_name in ( ");
        StringBuilder sql1 = new StringBuilder();
        for (int i = 0; i < loginNames.size(); i++) {
            sql1.append(" , ? ");
        }
        sql.append(sql1.length() > 2 ? sql1.substring(2) : "").append(" ) and del_flag = 0 ; ");
        return sql.toString();
    }

    public String updateMsgById(JsonArray params) {
        params.add(this.getRealName()).add(this.getDeptId()).add(this.getDeptName())
                .add(this.getPostName()).add(this.getId());
        return " update " + TABLE + " set real_name = ? , dept_id = ? , " +
                " dept_name = ? , post_name = ? where id = ? ; ";
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " from " +
                TABLE + " u inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId()) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        sb.append(sqLpage.toString()).append(" ) u1 on u.id = u1.bid ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getUserId()) {
            sb.append(" and user_id = ? ");
            params.add(this.getUserId());
        }
        return sb.append(" ; ").toString();
    }

    @Override
    public Map<String, String> getMap() {
        return EarthEnergyUser.map;
    }

    @Override
    protected String columnList() {
        return baseColumnList();
    }

    public String getRealName() {
        return this.entries.getString(realName);
    }

    public void setRealName(String realName) {
        this.entries.put(EarthEnergyUser.realName, realName);
    }

    public Long getUserId() {
        return this.entries.getLong(userId);
    }

    public void setUserId(Long userId) {
        this.entries.put(EarthEnergyUser.userId, userId);
    }
    public Long getDeptId() {
        return this.entries.getLong(deptId);
    }

    public void setDeptId(Long deptId) {
        this.entries.put(EarthEnergyUser.deptId, deptId);
    }
    public String getPostId() {
        return this.entries.getString(postId);
    }

    public void setPostId(String postId) {
        this.entries.put(EarthEnergyUser.postId, postId);
    }
    public Integer getVisit() {
        return this.entries.getInteger(visit);
    }

    public void setVisit(Integer visit) {
        this.entries.put(EarthEnergyUser.visit, visit);
    }
    public BigDecimal getFixMoney() {
        String fixMoney = this.entries.getString(EarthEnergyUser.fixMoney);
        if (null == fixMoney || "".equals(fixMoney.trim())) {
            return null;
        }
        return new BigDecimal(fixMoney);
    }

    public void setFixMoney(BigDecimal fixMoney) {
        this.entries.put(EarthEnergyUser.fixMoney, null == fixMoney ? null : fixMoney.toString());
    }
    public BigDecimal getFixBenefit() {
        String fixBenefit = this.entries.getString(EarthEnergyUser.fixBenefit);
        if (null == fixBenefit || "".equals(fixBenefit.trim())) {
            return null;
        }
        return new BigDecimal(fixBenefit);
    }

    public void setFixBenefit(BigDecimal fixBenefit) {
        this.entries.put(EarthEnergyUser.fixBenefit, null == fixBenefit ? null : fixBenefit.toString());
    }
    public BigDecimal getInstitutePost() {
        String institutePost = this.entries.getString(EarthEnergyUser.institutePost);
        if (null == institutePost || "".equals(institutePost.trim())) {
            return null;
        }
        return new BigDecimal(institutePost);
    }

    public void setInstitutePost(BigDecimal institutePost) {
        this.entries.put(EarthEnergyUser.institutePost, null == institutePost ? null : institutePost.toString());
    }
    public BigDecimal getSenioritySubsidy() {
        String senioritySubsidy = this.entries.getString(EarthEnergyUser.senioritySubsidy);
        if (null == senioritySubsidy || "".equals(senioritySubsidy.trim())) {
            return null;
        }
        return new BigDecimal(senioritySubsidy);
    }

    public void setSenioritySubsidy(BigDecimal senioritySubsidy) {
        this.entries.put(EarthEnergyUser.senioritySubsidy,
                null == senioritySubsidy ? null : senioritySubsidy.toString());
    }
    public BigDecimal getCertificateSubsidy() {
        String certificateSubsidy = this.entries.getString(EarthEnergyUser.certificateSubsidy);
        if (null == certificateSubsidy || "".equals(certificateSubsidy.trim())) {
            return null;
        }
        return new BigDecimal(certificateSubsidy);
    }

    public void setCertificateSubsidy(BigDecimal certificateSubsidy) {
        this.entries.put(EarthEnergyUser.certificateSubsidy,
                null == certificateSubsidy ? null : certificateSubsidy.toString());
    }
    public BigDecimal getFood() {
        String food = this.entries.getString(EarthEnergyUser.food);
        if (null == food || "".equals(food.trim())) {
            return null;
        }
        return new BigDecimal(food);
    }

    public void setFood(BigDecimal food) {
        this.entries.put(EarthEnergyUser.food, null == food ? null : food.toString());
    }

    public String getDeptName() {
        return this.entries.getString(deptName);
    }

    public void setDeptName(String deptName) {
        this.entries.put(EarthEnergyUser.deptName, deptName);
    }

    public String getPostName() {
        return this.entries.getString(postName);
    }

    public void setPostName(String postName) {
        this.entries.put(EarthEnergyUser.postName, postName);
    }

    public BigDecimal getOutsideSubsidySt() {
        String outsideSubsidySt = this.entries.getString(EarthEnergyUser.outsideSubsidySt);
        if (null == outsideSubsidySt || "".equals(outsideSubsidySt.trim())) {
            return null;
        }
        return new BigDecimal(outsideSubsidySt);
    }

    public void setOutsideSubsidySt(String outsideSubsidySt) {
        this.entries.put(EarthEnergyUser.outsideSubsidySt, outsideSubsidySt);
    }

    public BigDecimal getProjSubsidySt() {
        String projSubsidySt = this.entries.getString(EarthEnergyUser.projSubsidySt);
        if (null == projSubsidySt || "".equals(projSubsidySt.trim())) {
            return null;
        }
        return new BigDecimal(projSubsidySt);
    }

    public void setProjSubsidySt(String projSubsidySt) {
        this.entries.put(EarthEnergyUser.projSubsidySt, projSubsidySt);
    }

    public String getLoginName() {
        return entries.getString(loginName);
    }
}
