package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author xue
 * @since 2019-08-20
 */
public class ProjectUser extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "project_user";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(projId, "proj_id");put(userId, "user_id");put(type, "type");
    }};
    static {
        ProjectUser.map.putAll(BaseModel.map);
    }
    /**
     * 人员id
     */
    @SwaggerProperty(value ="人员id")
    private static final String userId = "userId";
    /**
     * 项目id
     */
    @SwaggerProperty(value ="项目id")
    private static final String projId = "projId";
    /**
     * 人员类型
     */
    @SwaggerProperty(value ="人员类型")
    private static final String type = "type";

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id, user_id as userId, proj_id as projId, `type` from ")
                .append(TABLE).append(" where del_flag = 0 ");
        if (null != this.getEntries().getString(userId)) {
            sb.append(" and user_id = ? ");
            params.add(this.getEntries().getString(userId));
        }
        if (null != this.getEntries().getString(projId)) {
            sb.append(" and proj_id = ? ");
            params.add(this.getEntries().getString(projId));
        }
        sb.append(sqLpage.toString()).append(" ; ");
        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) from ")
                .append(TABLE).append(" where del_flag = 0 ");
        if (null != this.getEntries().getString(userId)) {
            sb.append(" and user_id = ? ");
            params.add(this.getEntries().getString(userId));
        }
        if (null != this.getEntries().getString(projId)) {
            sb.append(" and proj_id = ? ");
            params.add(this.getEntries().getString(projId));
        }
        sb.append(" ; ");
        return sb.toString();
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    public ProjectUser(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public ProjectUser() {
        super(null, TABLE);
    }

    public static String getByUserId() {
        return " select proj_id as projId, user_id as userId, type from " + ProjectUser.TABLE + " where user_id = ?;";
    }

    public static String getByProjId() {
        return " select proj_id as projId, user_id as userId, type from " + ProjectUser.TABLE + " where proj_id = ?;";
    }

    public static String deleteByProjIdAndUserId() {
        return " delete from " + ProjectUser.TABLE + " where proj_id = ? and user_id = ? ;";
    }
}
