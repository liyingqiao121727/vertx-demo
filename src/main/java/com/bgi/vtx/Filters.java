package com.bgi.vtx;

import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component;
import io.vertx.ext.web.Router;

/**
 * 过滤器
 *
 * @author 李英乔
 * @since 2019-05-16
 */
@Component("filters")
public class Filters {

    /**
     * 方法执行前执行
     * @param router
     */
    public void filterBefore(Router router) {

    }

    /**
     * 不生效，没什么用
     * @param router
     */
    public void filterAfter(Router router) {

    }
}
