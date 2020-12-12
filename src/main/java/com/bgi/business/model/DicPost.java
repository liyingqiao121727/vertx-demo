package com.bgi.business.model;

import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
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

public class DicPost extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "dic_post";

    protected static final HashMap<String, String> map = new HashMap<String, String>(){{
        put(name, "name");put(subsidySt, "subsidy_st");put(projSubsidy, "proj_subsidy");
    }};

    /**
     * 岗位名称
     */
    @SwaggerProperty(value ="岗位名称")
    private static final String name = "name";
    /**
     * 补贴标准
     */
    @SwaggerProperty(value ="补贴标准")
    private static final String subsidySt = "subsidySt";
    /**
     * 项目补贴标准
     */
    @SwaggerProperty(value ="项目补贴标准")
    private static final String projSubsidy = "projSubsidy";

    public DicPost(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }

    public static String baseColumnList() {
        return " id, `name`, subsidy_st as subsidySt, proj_subsidy as projSubsidy, " + BaseModel.baseColumnList();
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
        this.entries.put(DicPost.name, name);
    }
    public BigDecimal getSubsidySt() {
        String subsidySt = this.entries.getString(DicPost.subsidySt);
        if (null == subsidySt || "".equals(subsidySt.trim())) {
            return null;
        }
        return new BigDecimal(subsidySt);
    }

    public void setSubsidySt(BigDecimal subsidySt) {
        this.entries.put(DicPost.subsidySt, null == subsidySt ? null : subsidySt.toString());
    }
    public BigDecimal getProjSubsidy() {
        String projSubsidy = this.entries.getString(DicPost.projSubsidy);
        if (null == projSubsidy || "".equals(projSubsidy.trim())) {
            return null;
        }
        return new BigDecimal(projSubsidy);
    }

    public void setProjSubsidy(BigDecimal projSubsidy) {
        this.entries.put(DicPost.projSubsidy, null == projSubsidy ? null : projSubsidy.toString());
    }

}
