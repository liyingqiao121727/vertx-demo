package com.bgi.business.controller;

import com.bgi.business.model.Notice;
import com.bgi.business.model.NoticeAdmin;
import com.bgi.business.model.NoticeProject;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.*;
import com.bgi.business.model.mis.Project;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.service.TipService;
import com.bgi.common.BaseController;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
import com.bgi.vtx.DbOperation;
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
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import io.vertx.reactivex.ext.sql.SQLClientHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component("tipController")
@Controller(value = TipController.class, name = "代办事项")
public class TipController extends BaseController {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TipController.class);

    private @Autowired
    DbOperation<SQLConnection> mysqlOperation;

    private @Autowired
    SQLClient extsqlClient;

    private @Autowired
    SQLClient sqlClient;

    private @Autowired
    io.vertx.reactivex.ext.sql.SQLClient rxSqlClientW;

    private @Autowired
    TipService tipService;

    private @Autowired
    String fontPath;

    private @Autowired
    String resourceLocation;

    @RequestMapping(path="/tip/insert", method=HttpMethod.PUT,
            request=Notice.class, response=RespVo.class, description = "新增通知信息")
    public Handler<RoutingContext> insertNotice() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Notice notice = new Notice(bodyBuf.toJsonObject());
            notice.setPdf(Html2PdfUtil.html2Pdf(notice.getMsg(), resourceLocation + fontPath, logger));
            tipService.insertOne(notice, routingContext);
        });
    }

    @RequestMapping(path="/tip/insert/many", method=HttpMethod.PUT, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "批量新增通知信息")
    public Handler<RoutingContext> insertManyNotice() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            tipService.insertMany(bodyBuf.toJsonObject().getJsonArray("list"), routingContext);
        });
    }

    @RequestMapping(path="/tip/update", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    },request=Notice.class, response=RespVo.class, description = "修改通知信息")
    public Handler<RoutingContext> updateNoticeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Notice notice = new Notice(bodyBuf.toJsonObject());
            notice.setPdf(Html2PdfUtil.html2Pdf(notice.getMsg(), resourceLocation + fontPath, logger));
            String delSql = TipUser.deleteByNoticeId();
            JsonArray delParams = new JsonArray();
            delParams.add(notice.getId());

            List<TipUser> list = notice.getTipUsers();
            JsonArray params = new JsonArray(new LinkedList());
            String sql = TipUser.insertMany(list, params);

            JsonArray paramsNotice = new JsonArray(new LinkedList());
            String updateSql = notice.updateById(paramsNotice);
            SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
                logger.info(delSql);
                return conn.rxUpdateWithParams(delSql, delParams).flatMap(updateResult -> {
                    if (list.isEmpty()) {
                        return Constant.SINGLE_UPSATE_RESULT;
                    }
                    logger.info(sql);
                    return conn.rxUpdateWithParams(sql, params);
                }).flatMap(updateResult -> {
                    logger.info(updateSql);
                    return conn.rxUpdateWithParams(updateSql, paramsNotice);
                }).doOnError(throwable -> {
                    conn.rxRollback().doOnError(throwable1 -> {
                        logger.error(throwable1);
                    }).subscribe();
                });
            }).subscribe(updateResult -> {
                routingContext.response().end(RespVo.success("修改成功").toString());
            }, throwable -> {
                logger.error(throwable);
                routingContext.response().end(RespVo.failure("修改失败", throwable).toString());
            });
            //this.updateOne(mysqlOperation, routingContext, notice);
        });
    }

    @RequestMapping(path="/tip/user/update/:userId/:noticeId/:read", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=RespVo.class, description = "修改已读状态")
    public Handler<RoutingContext> updateTipUserTypeByUserIdAndNoticeId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray param = new JsonArray(new ArrayList(3));
            String userId = routingContext.request().getParam("userId");
            String noticeId = routingContext.request().getParam("noticeId");
            Short read = Short.parseShort(routingContext.request().getParam("read"));
            String sql = TipUser.updateNoticeUserTypeByUserIdAndNoticeId(userId, noticeId, read, param);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, param);
        });
    }

    @RequestMapping(path="/tip/user/update/one", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER)
    }, request = TipUser.class, response=RespVo.class, description = "修改")
    public Handler<RoutingContext> updateTipUser() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonObject object = bodyBuf.toJsonObject();
            JsonArray param = new JsonArray(new LinkedList());
            TipUser tipUser = new TipUser(object);
            Integer single = object.getInteger(Notice.single);
            if (null == single || 0 != single) {
                String sql = tipUser.updateById(param);
                logger.info(sql);
                this.operate(mysqlOperation, routingContext, sql, param);
                return;
            }
            Notice notice = new Notice(null, false);
            notice.setId(tipUser.getNoticeId());
            notice.setSingleUserId(tipUser.getUserId());
            String sql = tipUser.updateTipUserByNoticeId(param);
            logger.info(sql);
            JsonArray params1 = new JsonArray(new LinkedList());
            String sql1 = notice.updateById(params1);
            logger.info(sql1);
            mysqlOperation.deal(routingContext, (success, failed, conn) -> {
                conn.updateWithParams(sql, param, res -> {
                    if (!res.succeeded()) {
                        failed.failed(conn, true, null);
                        routingContext.response().end(RespVo.failure("修改失败", res.cause()).toString());
                        return;
                    }
                    conn.updateWithParams(sql1, params1, result -> {
                        if (!res.succeeded()) {
                            failed.failed(conn, true, null);
                            routingContext.response().end(RespVo.failure("消息修改失败", result.cause()).toString());
                            return;
                        }
                        success.success(conn);
                        routingContext.response().end(RespVo.success("修改成功").toString());
                    });
                });
            });
        });
    }

    @RequestMapping(path="/tip/delete/:id", method=HttpMethod.DELETE, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER)
    }, response=RespVo.class, description = "删除通知信息")
    public Handler<RoutingContext> deleteNoticeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1))
                    .add(routingContext.request().getParam("id"));
            SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
                return conn.rxUpdateWithParams(NoticeProject.deleteByNoticeId(), params)
                        .flatMap(updateResult -> {
                            return conn.rxUpdateWithParams(TipUser.deleteByNoticeId(), params);
                        }).flatMap(updateResult -> {
                            return conn.rxUpdateWithParams(Notice.deleteById(), params);
                        }).doOnError(throwable -> {
                            conn.rxRollback().doOnError(throwable1 -> {
                                logger.error(throwable1);
                            }).subscribe();
                        });
            }).subscribe(updateResult -> {
                routingContext.response().end(RespVo.success("删除成功").toString());
            }, throwable -> {
                logger.error(throwable);
                routingContext.response().end(RespVo.failure("删除失败", throwable).toString());
            });
        });
    }

    @RequestMapping(path="/tip/query/page", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", in = SwaggerProperty.HEADER, type = String.class, value = "登录token"),
            @SwaggerProperty(fieldName = "page", in = SwaggerProperty.PARAMS, type = Integer.class, value = "起始页（从1开始）"),
            @SwaggerProperty(fieldName = "limit", in = SwaggerProperty.PARAMS, type = Integer.class, value = "每页数目"),
            @SwaggerProperty(fieldName = "orderBy", in = SwaggerProperty.PARAMS, type = String.class, value = "排序列（可选）"),
            @SwaggerProperty(fieldName = "sort", in = SwaggerProperty.PARAMS, type = String.class, value = "asc/desc（可选）")
    }, request=Notice.class, respParams = {
            @SwaggerProperty(fieldName = "total", type = Integer.class, value = "返回总数"),
            @SwaggerProperty(fieldName = "list", type = Notice.class,
                    collectionType = {@SwaggerProperty.SwaggerCollection(Notice.class)}, value = "[] json数组 查询结果")
    }, description = "分页查询信息")
    public Handler<RoutingContext> queryNoticePage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            NoticeTip notice = new NoticeTip(bodyBuf.toJsonObject());
            boolean customUserIdExist = null != notice.getCustomUserId() && !"".equals(notice.getCustomUserId().trim());
            this.queryPage(sqlClient, routingContext, notice, customUserIdExist ? 16 : 15);
        });
    }

    @RequestMapping(path="/tip/tip", method=HttpMethod.GET,
            response = RespVo.class, description = "(?userId=&type=&maxDays=&copyType=&state=)分页查询信息")
    public Handler<RoutingContext> queryNoticeTip() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            HttpServerRequest request = routingContext.request();
            JsonArray params = new JsonArray(new LinkedList());
            int maxdays = Integer.parseInt(request.getParam("maxDays"));
            LocalDate now = LocalDate.now();
            int type = Integer.parseInt(request.getParam("type"));
            String state = request.getParam("state");
            String copyType = request.getParam("copyType");
            String sql = NoticeTip.getByTimeLimit(request.getParam("userId"),
                    now.minusDays(maxdays).toString(), now.toString(), type,
                    null == state ? null : Integer.parseInt(state),
                    null == copyType ? null : Integer.parseInt(copyType), params);
            logger.info(sql);
            this.query(sqlClient, routingContext, sql, params, 16);
        });
    }

    @RequestMapping(path="/tip/query/admin/page", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", in = SwaggerProperty.HEADER, type = String.class, value = "登录token"),
            @SwaggerProperty(fieldName = "page", in = SwaggerProperty.PARAMS, type = Integer.class, value = "起始页（从1开始）"),
            @SwaggerProperty(fieldName = "limit", in = SwaggerProperty.PARAMS, type = Integer.class, value = "每页数目"),
            @SwaggerProperty(fieldName = "orderBy", in = SwaggerProperty.PARAMS, type = String.class, value = "排序列（可选）"),
            @SwaggerProperty(fieldName = "sort", in = SwaggerProperty.PARAMS, type = String.class, value = "asc/desc（可选）")
    }, request=Notice.class, respParams = {
            @SwaggerProperty(fieldName = "total", type = Integer.class, value = "返回总数"),
            @SwaggerProperty(fieldName = "list", type = Notice.class,
                    collectionType = {@SwaggerProperty.SwaggerCollection(Notice.class)}, value = "[] json数组 查询结果")
    }, description = "分页查询信息")
    public Handler<RoutingContext> queryNoticePageAdmin() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            NoticeAdmin notice = new NoticeAdmin(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, notice);
        });
    }

    @RequestMapping(path="/tip/server/push/only", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口(仅仅推送消息)")
    public Handler<RoutingContext> serverWebSocketDoPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            tipService.doPush(routingContext.request().getHeader("Authorization"),
                    bodyBuf.toJsonObject().getJsonArray("list"),
                    throwable -> routingContext.fail(500, throwable),
                    () -> routingContext.response().end(RespVo.success("推送成功").toString()));
        });
    }

    @RequestMapping(path="/tip/server/push", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口（推送并新增消息记录）")
    public Handler<RoutingContext> serverWebSocketPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            tipService.push(routingContext.request().getHeader("Authorization"),
                    bodyBuf.toJsonObject().getJsonArray("list"),
                    routingContext, throwable -> routingContext.fail(500, throwable),
                    () -> routingContext.response().end(RespVo.success("推送成功").toString()));
        });
    }

    private @Component.Autowired
    Map<String, ServerWebSocket> webSocketMap;

    @RequestMapping(path="/tip/push", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口(不对外提供)")
    public Handler<RoutingContext> webSocketPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray notices = bodyBuf.toJsonObject().getJsonArray("list");
            JsonArray userIds = null;
            JsonObject notice = null;
            for (int j = 0; j < notices.size(); j++) {
                notice = notices.getJsonObject(j);
                userIds = notice.getJsonArray(Notice.customUserIds);
                if (null != userIds) {
                    for (int i = 0; i < userIds.size(); i++) {
                        notice.put(Notice.customUserId, userIds.getValue(i));
                        ServerWebSocket socket = webSocketMap.get(userIds.getValue(i));
                        if (null != socket) {
                            String text = notice.toString();
                            socket.writeTextMessage(text);
                        }
                    }
                } else {
                    String text = notice.toString();
                    webSocketMap.entrySet().parallelStream().forEach(entry -> {
                        entry.getValue().writeTextMessage(text);
                    });
                }
            }

            routingContext.response().end(RespVo.success("推送成功").toString());
        });
    }

}
