package com.bgi.business.model;

import com.bgi.common.QueryPage;
import com.bgi.common.SQLpage;
import com.bgi.vtx.BaseModel;
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
 * @since 2019-07-17
 */

public class NoticeType extends BaseModel implements QueryPage {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "notice_type";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(msg, "msg");put(show, "show");put(type, "type");
    }};

    static {
        NoticeType.map.putAll(BaseModel.map);
    }

    /**
     * 栏目名称
     */
    @SwaggerProperty(value ="栏目名称")
    private static final String name = "name";

    /**
     * 栏目描述
     */
    @SwaggerProperty(value ="栏目描述")
    private static final String msg = "msg";

    /**
     * 显示
     */
    @SwaggerProperty(value ="显示 0：不显示，1：显示")
    private static final String show = "show";

    /**
     * 数量
     */
    @SwaggerProperty(value ="数量")
    private static final String num = "num";

    /**
     * 类型（0：通知，1：公告）
     */
    @SwaggerProperty(value ="类型（0：通知，1：公告）")
    private static final String type = "type";

    public NoticeType(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public NoticeType() {
        super(null, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, msg, `show`, `type`, " + BaseModel.baseColumnList();
    }

    public static String baseColumn() {
        return " id, `name`, `show`, `type`, " + BaseModel.baseColumnList();
    }

    @Override
    public String queryPage(SQLpage sqLpage, JsonArray params) {
        String sql = " select " + baseColumnList() + " , count(nid) as num from " + TABLE + " a inner join " +
                "( select id as bid from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getShow()) {
            sb.append(" and `show` = ? ");
            params.add(this.getShow());
        }
        sb.append(" order by `type` asc ");
        sb.append(sqLpage.toString()).append(" ) b on a.id = b.bid left join " +
                " ( select id as nid, notice_type_id from " + Notice.TABLE + " ) " +
                "n on a.id = n.notice_type_id group by id ; ");

        return sb.toString();
    }

    @Override
    public String countPage(JsonArray params) {
        String sql = " select count(*) from " + TABLE + " where del_flag = 0 ";
        StringBuilder sb = new StringBuilder(sql);
        if (null != this.getShow()) {
            sb.append(" and `show` = ? ");
            params.add(this.getShow());
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

    public String getName() {
        return this.entries.getString(name);
    }

    public void setName(String name) {
        this.entries.put(NoticeType.name, name);
    }

    public String getMsg() {
        return this.entries.getString(msg);
    }

    public void setMsg(String msg) {
        this.entries.put(NoticeType.msg, msg);
    }

    public Integer getShow() {
        return this.entries.getInteger(show);
    }

    public void setShow(Integer show) {
        this.entries.put(NoticeType.show, show);
    }

    public Integer getType() {
        return this.entries.getInteger(type);
    }

    public void setType(Integer type) {
        this.entries.put(NoticeType.type, type);
    }
}
