package com.bgi.business.model.mis;

import com.bgi.business.model.ProjectUser;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.ProjectUser;
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

public class XMProject extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "project";

    protected static final HashMap<String, String> map = new HashMap<String, String>(5){{
        put(projNo, "proj_no");put(name, "name");
    }};
    static {
        XMProject.map.putAll(BaseModel.map);
    }

    /**
     * 项目编号
     */
    @SwaggerProperty(value = "工程编号")
    private static final String projNo = "projNo";
    /**
     * 项目名称
     */
    @SwaggerProperty(value = "工程名称")
    private static final String name = "name";

    public XMProject(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public XMProject() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, proj_no as projNo, `name`, `desc`, location, remark, " +
                " proj_status as projStatus, finish_time as finishTime, create_time as createTime, "
                + BaseModel.baseColumnList();
    }

    public static String getProjByUserId() {
        return " select " + baseColumnList() + " from " + TABLE +
                " p right join ( select proj_id from " + ProjectUser.TABLE +
                " where user_id = ? ) u on p.id = u.proj_id;";
    }

    public static String getProjIdLike(XMProject project, JsonArray params) {
        String sql = " select id from " + TABLE + " where del_flag = 0 ";
        StringBuilder builder = new StringBuilder(sql);
        if (null != project.getName()) {
            builder.append(" and `name` = ? ");
            params.add("%" + project.getName() + "%");
        }
        if (null != project.getProjNo()) {
            builder.append(" and proj_no = ? ");
            params.add("%" + project.getProjNo() + "%");
        }

        return builder.append(" ; ").toString();
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
        StringBuilder sb = new StringBuilder(" select id, proj_no as projNo, `name` from ")
                .append(TABLE).append(" where id in ( ");
        for (int i = 0; i < size; i++) {
            sb.append(" ? , ");
        }
        return sb.replace(sb.length()-3, sb.length()-1, "  ").append(" ) and del_flag = 0 ; ").toString();
    }

    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " from " + TABLE + " a inner join ( select id as bid from " +
                TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and name = ? ");
            params.add(this.getName());
        }
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and name = ? ");
            params.add(this.getName());
        }
        return sb.append(" ; ").toString();
    }

    public String list(JsonArray params) {
        String sql = " select " + baseColumnList() + " from " + TABLE + " where del_flag = 0";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getName() && !"".equals(this.getName().trim())) {
            sb.append(" and name = ? ");
            params.add(this.getName());
        }

        return sb.toString();
    }

    public String getProjNo() {
        return this.entries.getString(projNo);
    }

    public void setProjNo(String projNo) {
        this.entries.put(XMProject.projNo, projNo);
    }
    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(XMProject.name, name);
    }
}
