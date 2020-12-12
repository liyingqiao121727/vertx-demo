package com.bgi.business.service.mis;

import com.bgi.business.model.PayProjectShare;
import com.bgi.business.model.ProjectShare;
import com.bgi.business.model.mis.Project;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component("projectService")
public class ProjectService {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(ProjectService.class);

    private @Component.Autowired
    DbOperation<SQLConnection> mysqlOperation;

    public void checkProjIsFinish(SQLConnection conn, RoutingContext routingContext,
                                  List<String> projIds, Runnable runnable) {
        conn.queryWithParams(Project.getProjByIdIn(projIds.size()),
                new JsonArray(projIds), new HandlerWrap<>(routingContext, () -> conn.close(), res -> {
                    List<Project> projects = null;
                    if (res.succeeded() && res.result().getNumRows() > 0) {
                        projects = res.result().getRows().parallelStream().map(Project::new)
                                .filter(project -> null != project.getProjStatus() &&
                                        Constant.MIS_PROJ_STATUS_CLOSE.equals(project.getProjStatus()) ||
                                        //(3 == project.getProjStatus() || 4 == project.getProjStatus()) ||
                                        1 == project.getDelFlag())
                                .collect(Collectors.toList());
                    }
                    if (null == projects) {
                        conn.close();
                        routingContext.response().end(RespVo.failure(
                                "分摊的项目不存在，ids:" + projIds, null).toString());
                        return;
                    }
                    if (projects.size() > 0) {
                        conn.close();
                        StringBuilder sb = new StringBuilder("项目已经结束,项目为：");
                        int i = 0;
                        for (Project project : projects) {
                            sb.append(i).append(" : 名称 ").append(project.getName())
                                    .append(" , 编号:").append(project.getProjNo())
                                    .append(" id: ").append(project.getId()).append(" , 已经结束; \n");
                        }
                        routingContext.response().end(RespVo.failure(sb.toString(), null).toString());
                        return;
                    }
                    runnable.run();
                }));
    }

    public void insertBatch(RoutingContext routingContext, List<ProjectShare> list, DbOperation.Operate<SQLConnection> operate) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = ProjectShare.insertMany(list, params);
            logger.info(sql);
            conn.updateWithParams(sql, params, res -> {
                if (!res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("添加失败", res.cause()).toString());
                    return;
                }
                operate.operate(success, failed, conn);
            });
        });
    }

    public void insertManyPayProjectShare(RoutingContext routingContext, List<PayProjectShare> list, DbOperation.Operate<SQLConnection> operate) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = PayProjectShare.insertMany(list, params);
            logger.info(sql);
            conn.updateWithParams(sql, params, res -> {
                if (!res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("添加失败", res.cause()).toString());
                    return;
                }
                operate.operate(success, failed, conn);
            });
        });
    }


    private @Component.Autowired
    WebClient webClient;

    private @Component.Autowired
    String misHost;

    private @Component.Autowired
    Integer misPort;

    private @Component.Autowired
    String misProject;

    public void checkProjIsFinish(RoutingContext routingContext,
                                  List<String> projIds, Runnable runnable) {
        HttpRequest<Buffer> req = webClient.post(misPort, misHost, misProject);
        //HttpServerRequest request = routingContext.request();
        Buffer buffer = new JsonArray(projIds).toBuffer();
        req.sendBuffer(buffer, res -> {//.putHeaders(request.headers())
            HttpServerResponse response = routingContext.response();
            if (!res.succeeded()) {
                routingContext.response().end(RespVo.failure("访问mis项目失败", res.cause()).toString());
                return;
            }
            JsonArray projects = null;
            JsonObject jsonObject = null;
            try {
                jsonObject = res.result().bodyAsJsonObject();
                projects = jsonObject.getJsonObject("data").getJsonArray("data");
            } catch (Exception e) {
                response.end(RespVo.failure("操作失败", e).toString());
                return;
            }
            if (null == projects) {
                routingContext.response().end(RespVo.failure(
                        "分摊的项目不存在，ids:" + projIds, null).toString());
                return;
            }
            List<Project> list = new LinkedList<>();
            if (res.succeeded() && projects.size() > 0) {
                Project project = null;
                for (int i = 0; i < projects.size(); i++) {
                    project = new Project(projects.getJsonObject(i));
                    if (null != project.getProjStatus() &&
                            Constant.MIS_PROJ_STATUS_CLOSE.equals(project.getProjStatus()) ||
                            //(3 == project.getProjStatus() || 4 == project.getProjStatus()) ||
                            1 == project.getDelFlag()) {
                        list.add(project);
                    }
                }
            }
            if (list.size() > 0) {
                StringBuilder sb = new StringBuilder("项目已经结束,项目为：");
                int i = 0;
                for (Project project : list) {
                    sb.append(i).append(" : 名称 ").append(project.getName())
                            .append(" , 编号:").append(project.getProjNo())
                            .append(" id: ").append(project.getId()).append(" , 已经结束; \n");
                }
                routingContext.response().end(RespVo.failure(sb.toString(), null).toString());
                return;
            }
            runnable.run();
        });


    }

}
