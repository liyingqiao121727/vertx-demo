package com.bgi.business.model.mis;

import com.bgi.common.QueryPage;
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

    public static final String TABLE = "org_user";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(id, "id");put(deptId, "company_dept_id");put(isEnable, "is_enable");
        put(isLoginable, "is_loginable");put(state, "state");put(delFlag, "del_flag");
        put(accountId, "account_id");put(realName, "name");
    }};

    static {
        //EarthEnergyUser.map.putAll(BaseModel.map);
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
    @SwaggerProperty(value = "是否启用：1；启用 2：停用")
    private static final String isEnable = "isEnable";
    /**
     * 是否能够访问本系统
     */
    @SwaggerProperty(value = "是否可以登录 1：是 0：否")
    private static final String isLoginable = "isLoginable";
    /**
     * 真实姓名
     */
    @SwaggerProperty(value = "真实姓名")
    private static final String realName = "realName";
    /**
     * 状态
     */
    @SwaggerProperty(value = "状态")
    private static final String state = "state";
    /**
     * 部门id
     */
    @SwaggerProperty(value = "部门id")
    private static final String accountId = "accountId";

    public EarthEnergyUser(JsonObject jsonObject) {
        super(jsonObject, TABLE, false);
    }

    public EarthEnergyUser(JsonObject jsonObject, boolean init) {
        super(jsonObject, TABLE, init);
    }

    public EarthEnergyUser() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, id as userId, `name` as realName ";
    }

    public static String getByUserId() {
        return "select " + baseColumnList() +  " from " + TABLE + " where user_id = ? ;";
    }

    public static String getByDeptId() {
        return "select " + baseColumnList() +  " from " + TABLE + " where company_dept_id = ? " +
                "and del_flag = 0 and is_loginable = 1 ;";
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " from " +
                TABLE + " u inner join ( select id as bid from " +
                TABLE + " where company_dept_id in " + Constant.EARTH_ENERGY_DEPT_IDS +
                " and del_flag = 0 and is_loginable = 1 and is_enable = 1 and state = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getId()) {
            sb.append(" and id = ? ");
            params.add(this.getId());
        }
        sb.append(sqLpage.toString()).append(" ) u1 on u.id = u1.bid ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where company_dept_id in " +
                Constant.EARTH_ENERGY_DEPT_IDS +
                " and del_flag = 0 and is_loginable = 1  and is_enable = 1 and state = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getId()) {
            sb.append(" and id = ? ");
            params.add(this.getId());
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
        this.entries.put(EarthEnergyUser.realName, realName);
    }

    public Object getUserId() {
        return this.entries.getValue(userId);
    }

    public void setUserId(Object userId) {
        this.entries.put(EarthEnergyUser.userId, userId);
    }
    public Long getDeptId() {
        return this.entries.getLong(deptId);
    }

    public void setDeptId(Long deptId) {
        this.entries.put(EarthEnergyUser.deptId, deptId);
    }

    public String getIsEnable() {
        return entries.getString(isEnable);
    }
    public void setIsEnable(Integer isEnable) {
        this.entries.put(EarthEnergyUser.isEnable, isEnable);
    }

    public String getIsLoginable() {
        return entries.getString(isLoginable);
    }
    public void setIsLoginable(Integer isLoginable) {
        this.entries.put(EarthEnergyUser.isLoginable, isLoginable);
    }

    public String getState() {
        return entries.getString(state);
    }
    public void setState(Integer state) {
        this.entries.put(EarthEnergyUser.state, state);
    }

    public String getAccountId() {
        return entries.getString(accountId);
    }
    public void setAccountId(String accountId) {
        this.entries.put(EarthEnergyUser.accountId, accountId);
    }
}
