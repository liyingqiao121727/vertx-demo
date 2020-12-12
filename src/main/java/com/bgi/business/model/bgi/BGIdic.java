package com.bgi.business.model.bgi;

import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.annotation.SwaggerProperty;
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

public class BGIdic extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final String TABLE = "`bgi-all`.test_dictionary";

    static {
        BGIdic.map.putAll(BaseModel.map);
    }

    /**
     * 名称
     */
    @SwaggerProperty(value = "名称")
    private static final String name = "name";

    public BGIdic(JsonObject jsonObject) {
        super(jsonObject, TABLE);
    }
}
