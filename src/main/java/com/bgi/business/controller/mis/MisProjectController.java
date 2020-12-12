package com.bgi.business.controller.mis;

import com.bgi.business.model.mis.realback.BigAgreement;
import com.bgi.business.model.mis.realback.Project;
import com.bgi.business.model.mis.realback.ProjectDetail;
import com.bgi.business.model.mis.realback.SubpackageAgreement;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.common.SQLpage;
import com.bgi.common.SqlResult;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.business.model.mis.realback.BigAgreement;
import com.bgi.business.model.mis.realback.Project;
import com.bgi.business.model.mis.realback.ProjectDetail;
import com.bgi.business.model.mis.realback.SubpackageAgreement;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.common.SQLpage;
import com.bgi.common.SqlResult;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component(value = "misProjectController")
@Controller(value = MisProjectController.class, name = "项目信息")
public class MisProjectController extends BaseController {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(MisProjectController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> misMysqlOperation;

    private @Component.Autowired
    SQLClient misExtsqlClient;

    private @Component.Autowired
    SQLClient misSqlClient;

    @RequestMapping(path="/project2/insert", method=HttpMethod.PUT,
            request=Project.class, response=RespVo.class, description = "新增项目信息")
    public Handler<RoutingContext> insert() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            Project project = new Project(bodyBuf.toJsonObject());
            if (null == project.getProjStatus()) {
                project.setProjStatus(0);
            }
            String sql = project.insertOne(params);
            misMysqlOperation.deal(routingContext, (success, failed, conn) -> {
                conn.updateWithParams(sql, params, new HandlerWrap<>(routingContext, result -> {
                    if (!result.succeeded()) {
                        failed.failed(conn, true, null);
                        routingContext.response().end(RespVo.failure("新增失败", result.cause()).toString());
                        return;
                    }
                    BigAgreement agreement = new BigAgreement();
                    agreement.setAgreeNo(project.getAgreementNo());
                    //agreement.setBizProperty(project.getBizProperty());
                    //agreement.setProfessionTypeId(project.getProfessionalTypes());
                    agreement.setProjectId(project.getId());
                    JsonArray array = new JsonArray(new LinkedList());
                    String sql1 = agreement.insertOne(array);
                    conn.updateWithParams(sql1, array, new HandlerWrap<>(routingContext, res -> {
                        if (!res.succeeded()) {
                            failed.failed(conn, true, null);
                            routingContext.response().end(RespVo.failure("新增失败", res.cause()).toString());
                            return;
                        }
                        success.success(conn);
                        routingContext.response().end(RespVo.success("新增成功").toString());
                    }));
                }));
            });
            //this.insertOne(misMysqlOperation, routingContext, new Project(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/project2/update", method=HttpMethod.POST,
            request=Project.class, response=RespVo.class, description = "修改项目信息")
    public Handler<RoutingContext> updateById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            Project project = new Project(bodyBuf.toJsonObject());
            String sql = project.updateById(params);
            misMysqlOperation.deal(routingContext, (success, failed, conn) -> {
                conn.updateWithParams(sql, params, new HandlerWrap<>(routingContext, result -> {
                    if (!result.succeeded()) {
                        failed.failed(conn, true, null);
                        routingContext.response().end(RespVo.failure("修改失败", result.cause()).toString());
                        return;
                    }
                    BigAgreement agreement = new BigAgreement();
                    agreement.setAgreeNo(project.getAgreementNo());
                    //agreement.setBizProperty(project.getBizProperty());
                    //agreement.setProfessionTypeId(project.getProfessionalTypes());
                    agreement.setProjectId(project.getId());
                    JsonArray array = new JsonArray(new LinkedList());
                    String sql1 = agreement.updateById(array);
                    conn.updateWithParams(sql1, array, new HandlerWrap<>(routingContext, res -> {
                        if (!res.succeeded()) {
                            failed.failed(conn, true, null);
                            routingContext.response().end(RespVo.failure("修改失败", res.cause()).toString());
                            return;
                        }
                        success.success(conn);
                        routingContext.response().end(RespVo.success("修改成功").toString());
                    }));
                }));
            });

            /*JsonArray params = new JsonArray(new LinkedList());
            String sql = new Project(bodyBuf.toJsonObject()).updateById(params);
            logger.info(sql);
            this.operate(misMysqlOperation, routingContext, sql, params);*/
        });
    }

    @RequestMapping(path="/project2/delete/:id", method=HttpMethod.DELETE,
            request=String.class, response=RespVo.class, description = "删除项目信息")
    public Handler<RoutingContext> deleteById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Project project = new Project();
            project.setId(routingContext.request().getParam("id"));
            project.setDelFlag(1);
            this.deleteOne(misMysqlOperation, routingContext, project);
        });
    }

    @RequestMapping(path="/project2/query/:id", method=HttpMethod.GET,
            request=String.class, response=Project.class, description = "查询信息")
    public Handler<RoutingContext> queryById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = Project.getOneSimple(routingContext.request().getParam("id"), params);
            this.queryOne(misSqlClient, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/project2/query/detail/:id", method=HttpMethod.GET,
            request=String.class, response=ProjectDetail.class, description = "查询信息")
    public Handler<RoutingContext> queryDetailById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = Project.getOne(routingContext.request().getParam("id"), params);
            this.queryOne(misSqlClient, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/project2/query/page", method=HttpMethod.POST,
            request=SQLpage.class, response=SQLpage.class, description = "分页查询信息")
    public Handler<RoutingContext> queryPage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Project project = new Project(bodyBuf.toJsonObject());
            Integer years = project.getYears();
            if (null != years) {
                project.setCreateTime(LocalDateTime.now().minusYears(years).toString());
            }
            this.queryPage(misSqlClient, routingContext, project);
        });
    }

    @RequestMapping(path="/project2/list", method=HttpMethod.POST,
            request=Project.class, response=Project.class, description = "批量查询信息")
    public Handler<RoutingContext> list() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Project project = new Project(bodyBuf.toJsonObject());
            JsonArray params = new JsonArray(new LinkedList());
            String sql = project.list(params);
            logger.info(sql);
            this.query(misSqlClient, routingContext, sql, params);
        });
    }


    @RequestMapping(path="/project2/agree/list", method=HttpMethod.POST,
            response=Project.class, description = "获取所有项目和合同金额信息")
    public Handler<RoutingContext> getProjectAgreeList() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            ProjectDetail project = new ProjectDetail(bodyBuf.toJsonObject());
            JsonArray params = new JsonArray(new LinkedList());
            String sql = project.list(params);
            logger.info(sql);
            this.query(misSqlClient, routingContext, sql, params);
        });
    }


    @RequestMapping(path="/project2/in", method=HttpMethod.POST,
            response=Project.class, description = "根据[项目id数组]查询项目信息 ")
    public Handler<RoutingContext> getProjectByIdIn() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = bodyBuf.toJsonArray();
            if (params.size() < 1) {
                routingContext.response().end(RespVo.success("查询成功").addContent("data", params).toString());
                return;
            }
            String sql = Project.getProjByIdIn(params.size());
            logger.info(sql);
            this.query(misSqlClient, routingContext, sql, params);
        });
    }


    @RequestMapping(path="/project2/like", method=HttpMethod.POST,
            response=Project.class, description = "根据[项目id数组]查询项目信息 ")
    public Handler<RoutingContext> getProjIdLike() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Project project = new Project(bodyBuf.toJsonObject());
            JsonArray params = new JsonArray(new LinkedList());
            String sql = Project.getProjIdLike(project, params);
            logger.info(sql);
            misSqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, res -> {
                if (!res.succeeded()) {
                    routingContext.response().end(RespVo.failure("查询失败", res.cause()).toString());
                    return;
                }
                routingContext.response().end(RespVo.success("查询成功").addContent("data",
                        new JsonArray(res.result().getResults().parallelStream().map(
                                ele -> ele.getValue(0)).collect(Collectors.toList()))).toString());
            }));
        });
    }

    @RequestMapping(path="/project2/query/id/sum/by/project/:projId", method=HttpMethod.GET,
            response=SubpackageAgreement.class, description = "查询分包合同总额及分包合同id json数组")
    public Handler<RoutingContext> queryIdByProjId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String projId = routingContext.request().getParam("projId");
            JsonArray params = new JsonArray(new ArrayList(1));
            JsonArray params1 = new JsonArray(new ArrayList(1));
            String sql = SubpackageAgreement.getSumByProjId(projId, params);
            String sql1 = SubpackageAgreement.getIdByProjId(projId, params1);

            misSqlClient.getConnection(sqlConnectionAsyncResult -> {
                SQLConnection conn = sqlConnectionAsyncResult.result();
                if (!sqlConnectionAsyncResult.succeeded() || null == conn) {
                    routingContext.response().end(RespVo.failure("查询失败",
                            sqlConnectionAsyncResult.cause()).toString());
                    conn.close();
                    return;
                }
                conn.queryWithParams(sql, params, new HandlerWrap<>(routingContext, result -> {
                    logger.info(sql);
                    if (!result.succeeded() || result.result().getNumRows() < 1) {
                        routingContext.response().end(RespVo.failure("查询总和失败", result.cause()).toString());
                        conn.close();
                        return;
                    }
                    SubpackageAgreement agreement = new SubpackageAgreement(result.result().getRows().get(0));
                    conn.queryWithParams(sql1, params1, new HandlerWrap<>(routingContext, res -> {
                        logger.info(sql1);
                        if (!res.succeeded()) {
                            routingContext.response().end(RespVo.failure("查询id失败", res.cause()).toString());
                            conn.close();
                            return;
                        }
                        if (res.result().getNumRows() < 1) {
                            conn.close();
                            agreement.getEntries().put("list", new JsonArray(new ArrayList(0)));
                        } else {
                            List<String> list = res.result().getResults().parallelStream()
                                    .map(array -> array.getString(0)).collect(Collectors.toList());
                            agreement.getEntries().put("list", new JsonArray(list));
                        }
                        routingContext.response().end(
                                RespVo.success("查询成功").addContent("data", agreement.getEntries()).toString());
                        conn.close();
                    }));
                }));
            });
        });
    }

    @RequestMapping(path="/project2/query/id/sum/by/projects", method=HttpMethod.POST,
            response=SubpackageAgreement.class, description = "查询分包合同总额及分包合同id json数组")
    public Handler<RoutingContext> queryIdByProjIds() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray array = bodyBuf.toJsonArray();
            JsonArray params = new JsonArray(new ArrayList(array.size()));
            JsonArray params1 = new JsonArray(new ArrayList(array.size()));
            String sql = SubpackageAgreement.getSumByProjIds(array, params);
            String sql1 = SubpackageAgreement.getIdByProjIds(array, params1);

            JsonObject object = new JsonObject(new ConcurrentHashMap<>());

            misSqlClient.getConnection(sqlConnectionAsyncResult -> {
                SQLConnection conn = sqlConnectionAsyncResult.result();
                if (!sqlConnectionAsyncResult.succeeded() || null == conn) {
                    routingContext.response().end(RespVo.failure("查询失败",
                            sqlConnectionAsyncResult.cause()).toString());
                    conn.close();
                    return;
                }
                conn.queryWithParams(sql, params, new HandlerWrap<>(routingContext, result -> {
                    logger.info(sql);
                    if (!result.succeeded() || result.result().getNumRows() < 1) {
                        routingContext.response().end(RespVo.failure("查询总和失败", result.cause()).toString());
                        conn.close();
                        return;
                    }
                    object.put("sumList", result.result().getRows());
                    conn.queryWithParams(sql1, params1, new HandlerWrap<>(routingContext, res -> {
                        logger.info(sql1);
                        if (!res.succeeded()) {
                            routingContext.response().end(RespVo.failure("查询id失败", res.cause()).toString());
                            conn.close();
                            return;
                        }
                        if (res.result().getNumRows() < 1) {
                            conn.close();
                            object.put("subAgreeIdList", new JsonArray(new ArrayList(0)));
                        } else {
                            List<JsonObject> list = SqlResult.getRows(res.result(), 2);
                            object.put("subAgreeIdList", new JsonArray(list));
                        }
                        routingContext.response().end(
                                RespVo.success("查询成功").addContent("data", object).toString());
                        conn.close();
                    }));
                }));
            });
        });
    }

}
