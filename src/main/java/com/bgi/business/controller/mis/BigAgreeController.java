package com.bgi.business.controller.mis;

import com.bgi.common.BaseController;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.business.model.mis.realback.BigAgreement;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedList;

@Component(value = "bigAgreeController2")
@Controller(value = BigAgreeController.class, name = "大合同信息")
public class BigAgreeController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(BigAgreeController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> misMysqlOperation;

    private @Component.Autowired
    SQLClient misExtsqlClient;

    private @Component.Autowired
    SQLClient misSqlClient;

    @RequestMapping(path="/big2/agree/insert", method=HttpMethod.PUT,
            request=BigAgreement.class, response=RespVo.class, description = "新增大合同信息")
    public Handler<RoutingContext> insertBatch() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = new BigAgreement(bodyBuf.toJsonObject()).insertOne(params);
            this.operate(misMysqlOperation, routingContext, sql, params);
            //this.insertOne(misMysqlOperation, routingContext, new BigAgreement(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/big2/agree/update", method=HttpMethod.POST,
            request=BigAgreement.class, response=RespVo.class, description = "修改大合同信息")
    public Handler<RoutingContext> updateById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            BigAgreement agreement = new BigAgreement(bodyBuf.toJsonObject());
            JsonArray params = new JsonArray(new LinkedList());
            String sql = agreement.updateById(params);
            this.operate(misMysqlOperation, routingContext, sql, params);
            //this.updateOne(misMysqlOperation, routingContext, new BigAgreement(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/big2/agree/delete/:id", method=HttpMethod.DELETE,
            response=RespVo.class, description = "删除大合同信息")
    public Handler<RoutingContext> deleteById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            BigAgreement agreement = new BigAgreement();
            agreement.setId(routingContext.request().getParam("id"));
            this.deleteOne(misMysqlOperation, routingContext, agreement);
        });
    }

    @RequestMapping(path="/big2/agree/query/:id", method=HttpMethod.GET,
            response=BigAgreement.class, description = "查询大合同信息")
    public Handler<RoutingContext> queryById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String id = routingContext.request().getParam("id");
            this.getById(misExtsqlClient, routingContext, json -> {
                BigAgreement agreement = new BigAgreement(json);
                agreement.setId(id);
                return agreement;
            });
        });
    }

    @RequestMapping(path="/big2/agree/query/by/project/ids", method=HttpMethod.POST,
            response=BigAgreement.class, description = "[ id ] 查询大合同信息")
    public Handler<RoutingContext> queryByProjIds() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = bodyBuf.toJsonArray();
            if (0 == params.size()) {
                routingContext.response().end(RespVo.success("需要传入项目id").addContent("data", null).toString());
                return;
            }
            String sql = BigAgreement.getByProjIds(params.size());
            logger.info(sql);
            this.query(misSqlClient, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/big2/agree/query/by/project/:projId", method=HttpMethod.GET,
            response=BigAgreement.class, description = "根据项目id 查询大合同信息")
    public Handler<RoutingContext> queryByProjId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = BigAgreement.getByProjId(routingContext.request().getParam("projId"), params);
            logger.info(sql);
            this.queryOne(misSqlClient, routingContext, sql, params);
        });
    }

}
