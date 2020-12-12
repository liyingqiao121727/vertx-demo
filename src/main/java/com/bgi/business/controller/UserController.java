package com.bgi.business.controller;

import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.model.ProjectUser;
import com.bgi.business.model.mis.EEuser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.model.ProjectUser;
import com.bgi.business.model.mis.EEuser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.vtx.annotation.SwaggerProperty.SwaggerCollection;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component(value = "userController")
@Controller(value = UserController.class, name = "用户信息")
public class UserController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(UserController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> mysqlOperation;

    private @Component.Autowired
    SQLClient extsqlClient;

    private @Component.Autowired
    SQLClient sqlClient;

    @RequestMapping(path="/user/insert/batch", method=HttpMethod.PUT,
            request=EarthEnergyUser.class, response=RespVo.class, description = "批量新增用户信息 [{},{}]")
    public Handler<RoutingContext> insertBatch() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray jsonArray = bodyBuf.toJsonArray();
            List<EarthEnergyUser> list = new ArrayList<>(jsonArray.size());
            //List<EEuser> eEusers = new ArrayList<>(jsonArray.size());
            EarthEnergyUser user = null;
            EEuser eEuser = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                user = new EarthEnergyUser();
                //eEuser = new EEuser(jsonArray.getJsonObject(i));
                eEuser.setUserId(user.getId());
                list.add(user);
                //eEusers.add(eEuser);
            }
            JsonArray params = new JsonArray(new LinkedList());
            String sql = EarthEnergyUser.insertMany(list, params);
            //JsonArray array = new JsonArray(new LinkedList());
            //String sql1 = EEuser.insertMany(eEusers, array);
            logger.info(sql);
            mysqlOperation.deal(routingContext, (success, failed, conn) -> {
                conn.updateWithParams(sql, params, new HandlerWrap<>(routingContext, result -> {
                    if (!result.succeeded()) {
                        failed.failed(conn, true, null);
                        routingContext.response().end(RespVo.failure("新增失败", result.cause()).toString());
                        return;
                    }
                    success.success(conn);
                    routingContext.response().end(RespVo.success("新增成功").toString());
                    return;
                    /*conn.updateWithParams(sql1, array, new HandlerWrap<>(r -> {
                        if (!r.succeeded()) {
                            failed.failed(conn, true, null);
                            routingContext.response().end(RespVo.failure("新增失败", r.cause()).toString());
                            return;
                        }
                    }));*/
                }));
            });
            this.operate(mysqlOperation, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/user/update", method=HttpMethod.POST,
            request=EarthEnergyUser.class, response=RespVo.class, description = "修改用户信息")
    public Handler<RoutingContext> updateById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            EarthEnergyUser user = new EarthEnergyUser(bodyBuf.toJsonObject());
            String sql = user.updateById(params);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, params);
            //this.updateOne(mysqlOperation, routingContext, new EarthEnergyUser(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/user/delete/:id", method=HttpMethod.DELETE,
            response=RespVo.class, description = "删除用户信息")
    public Handler<RoutingContext> deleteById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            EarthEnergyUser user = new EarthEnergyUser();
            user.setId(routingContext.request().getParam("id"));
            this.deleteOne(mysqlOperation, routingContext, user);
        });
    }

    @RequestMapping(path="/user/query/:bgiUserId", method=HttpMethod.GET,
            response=EarthEnergyUser.class, description = "根据岩云宝userId查询本系统用户信息")
    public Handler<RoutingContext> queryByBGIuserId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            extsqlClient.queryWithParams(EarthEnergyUser.getByUserId(), new JsonArray(new ArrayList(1))
                    .add(routingContext.request().getParam("bgiUserId")), new HandlerWrap<>(routingContext, resp -> {
                if (!resp.succeeded() || resp.result().getNumRows() < 1) {
                    routingContext.response().end(RespVo.failure("查询失败", resp.cause()).toString());
                    return;
                }
                routingContext.response().end(RespVo.success("查询成功").addContent(
                        "data", resp.result().getRows().get(0)).toString());
            }));
        });
    }

    @RequestMapping(path="/user/query/no/auth/:bgiUserId", method=HttpMethod.GET,
            response=EarthEnergyUser.class, description = "根据岩云宝userId查询本系统用户信息")
    public Handler<RoutingContext> queryByBGIuserIdNoAuth() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            extsqlClient.queryWithParams(EarthEnergyUser.getByUserId(), new JsonArray(new ArrayList(1))
                    .add(routingContext.request().getParam("bgiUserId")), new HandlerWrap<>(routingContext, resp -> {
                if (!resp.succeeded() || resp.result().getNumRows() < 1) {
                    routingContext.response().end(RespVo.failure("查询失败", resp.cause()).toString());
                    return;
                }
                routingContext.response().end(RespVo.success("查询成功").addContent(
                        "data", resp.result().getRows().get(0)).toString());
            }));
        });
    }

    @RequestMapping(path="/user/query/no/auth/by/username/:loginName", method=HttpMethod.GET,
            response=EarthEnergyUser.class, description = "根据loginName查询本系统用户userId")
    public Handler<RoutingContext> queryByBGIuserNameNoAuth() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1));
            String sql = EarthEnergyUser.getVisitByUserName(routingContext.request().getParam("loginName"), params);
            extsqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, resp -> {
                if (!resp.succeeded() || resp.result().getNumRows() < 1) {
                    routingContext.response().end(RespVo.failure("查询失败", resp.cause()).toString());
                    return;
                }
                routingContext.response().end(RespVo.success("查询成功").addContent(
                        "data", resp.result().getRows().get(0)).toString());
            }));
        });
    }

    @RequestMapping(path="/user1/query/page", method=HttpMethod.POST,
            reqParams = {
                    @SwaggerProperty(fieldName = "Authorization", in = SwaggerProperty.HEADER, type = String.class, value = "登录token"),
                    @SwaggerProperty(fieldName = "page", in = SwaggerProperty.PARAMS, type = Integer.class, value = "起始页（从1开始）"),
                    @SwaggerProperty(fieldName = "limit", in = SwaggerProperty.PARAMS, type = Integer.class, value = "每页数目"),
                    @SwaggerProperty(fieldName = "orderBy", in = SwaggerProperty.PARAMS, type = String.class, value = "排序列（可选）"),
                    @SwaggerProperty(fieldName = "sort", in = SwaggerProperty.PARAMS, type = String.class, value = "asc/desc（可选）")
            },
            request=EarthEnergyUser.class, respParams = {
            @SwaggerProperty(fieldName = "total", type = Integer.class, value = "返回总数"),
            @SwaggerProperty(fieldName = "list", type = EarthEnergyUser.class,
                    collectionType = {@SwaggerProperty.SwaggerCollection(EarthEnergyUser.class)}, value = "[] json数组 查询结果")
    }, description = "分页查询本系统用户信息")
    public Handler<RoutingContext> queryPage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            EarthEnergyUser user = new EarthEnergyUser(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, user);
        });
    }


    @RequestMapping(path="/user/project/by/user", method=HttpMethod.GET,
            response=ProjectUser.class, description = "通过userId 批量查询信息")
    public Handler<RoutingContext> getProjectByUser() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1))
                    .add(routingContext.request().getParam("userId"));
            String sql = ProjectUser.getByUserId();
            logger.info(sql);
            //this.queryOne(extsqlClient, routingContext, sql, params);
            this.query(sqlClient, routingContext, sql, params);
        });
    }

}
