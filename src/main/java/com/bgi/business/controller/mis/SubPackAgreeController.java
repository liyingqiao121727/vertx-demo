package com.bgi.business.controller.mis;

import com.bgi.common.BaseController;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.mis.realback.SubpackageAgreement;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedList;

@Component(value = "subPackAgreeController")
@Controller(value = SubPackAgreeController.class, name = "分包合同信息")
public class SubPackAgreeController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(SubPackAgreeController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> misMysqlOperation;

    private @Component.Autowired
    SQLClient misExtsqlClient;

    private @Component.Autowired
    SQLClient misSqlClient;

    @RequestMapping(path="/sub2/pack/agree/insert", method=HttpMethod.PUT,
            request=SubpackageAgreement.class, response=RespVo.class, description = "新增分包合同信息")
    public Handler<RoutingContext> insertBatch() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            SubpackageAgreement agreement = new SubpackageAgreement(bodyBuf.toJsonObject());
            String sql = agreement.insertOne(params);
            logger.info(sql);
            this.operate(misMysqlOperation, routingContext, sql, params);
            //this.insertOne(misMysqlOperation, routingContext, new SubpackageAgreement(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/sub2/pack/agree/update", method=HttpMethod.POST,
            request=SubpackageAgreement.class, response=RespVo.class, description = "修改分包合同信息")
    public Handler<RoutingContext> updateById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            SubpackageAgreement agreement = new SubpackageAgreement(bodyBuf.toJsonObject());
            String sql = agreement.updateById(params);
            this.operate(misMysqlOperation, routingContext, sql, params);
            //this.updateOne(misMysqlOperation, routingContext, new SubpackageAgreement(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/sub2/pack/agree/delete/:id", method=HttpMethod.DELETE,
            response=RespVo.class, description = "删除分包合同信息")
    public Handler<RoutingContext> deleteById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            SubpackageAgreement agreement = new SubpackageAgreement();
            agreement.setId(routingContext.request().getParam("id"));
            this.deleteOne(misMysqlOperation, routingContext, agreement);
        });
    }

    @RequestMapping(path="/sub2/pack/agree/query/:id", method=HttpMethod.GET,
            response=SubpackageAgreement.class, description = "查询分包合同信息")
    public Handler<RoutingContext> queryById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            this.getById(misExtsqlClient, routingContext, json -> {
                SubpackageAgreement agreement = new SubpackageAgreement(json);
                agreement.setId(routingContext.request().getParam("id"));
                return agreement;
            });
        });
    }

    @RequestMapping(path="/sub2/pack/agree/page", method=HttpMethod.POST,
            reqParams = {
                    @SwaggerProperty(fieldName = "Authorization", in = SwaggerProperty.HEADER, type = String.class, value = "登录token"),
                    @SwaggerProperty(fieldName = "page", in = SwaggerProperty.PARAMS, type = Integer.class, value = "起始页（从1开始）"),
                    @SwaggerProperty(fieldName = "limit", in = SwaggerProperty.PARAMS, type = Integer.class, value = "每页数目"),
                    @SwaggerProperty(fieldName = "orderBy", in = SwaggerProperty.PARAMS, type = String.class, value = "排序列（可选）"),
                    @SwaggerProperty(fieldName = "sort", in = SwaggerProperty.PARAMS, type = String.class, value = "asc/desc（可选）")
            },
            request = SubpackageAgreement.class,
            respParams = {
                    @SwaggerProperty(fieldName = "total", type = Integer.class, value = "返回总数"),
                    @SwaggerProperty(fieldName = "list", type = SubpackageAgreement.class, collectionType = {
                            @SwaggerProperty.SwaggerCollection(SubpackageAgreement.class)}, value = "[] json数组 查询结果")
            }, description = "分页查询报销信息")
    public Handler<RoutingContext> payParentPage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            SubpackageAgreement agreement = new SubpackageAgreement(bodyBuf.toJsonObject(), false);
            this.queryPage(misSqlClient, routingContext, agreement);
        });
    }

}
