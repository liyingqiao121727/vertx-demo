package com.bgi.business.controller;

import com.bgi.business.model.*;
import com.bgi.business.model.mis.Project;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.common.BaseController;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.vtx.annotation.SwaggerProperty;
import com.bgi.business.model.*;
import com.bgi.business.model.mis.Project;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.service.NoticeService;
import com.bgi.common.BaseController;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import io.vertx.reactivex.ext.sql.SQLClientHelper;

import java.util.*;

@Component("messageController")
@Controller(value = NoticeController.class, name = "通知公告")
public class NoticeController extends BaseController {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(NoticeController.class);

    private @Component.Autowired
    DbOperation<SQLConnection> mysqlOperation;

    private @Component.Autowired
    SQLClient extsqlClient;

    private @Component.Autowired
    SQLClient sqlClient;

    private @Component.Autowired
    io.vertx.reactivex.ext.sql.SQLClient rxSqlClientW;

    private @Component.Autowired
    NoticeService noticeService;

    private @Component.Autowired
    String fontPath;

    private @Component.Autowired
    String resourceLocation;

    @RequestMapping(path="/notice/insert", method=HttpMethod.PUT,
            request=Notice.class, response=RespVo.class, description = "新增通知信息")
    public Handler<RoutingContext> insertNotice() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Notice notice = new Notice(bodyBuf.toJsonObject());
            notice.setPdf(Html2PdfUtil.html2Pdf(notice.getMsg(), resourceLocation + fontPath, logger));
            noticeService.insertOne(notice, routingContext);
        });
    }

    @RequestMapping(path="/notice/insert/many", method=HttpMethod.PUT, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "批量新增通知信息")
    public Handler<RoutingContext> insertManyNotice() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            noticeService.insertMany(bodyBuf.toJsonObject().getJsonArray("list"), routingContext);
        });
    }

    @RequestMapping(path="/notice/update", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    },request=Notice.class, response=RespVo.class, description = "修改通知信息")
    public Handler<RoutingContext> updateNoticeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            Notice notice = new Notice(bodyBuf.toJsonObject());
            notice.setPdf(Html2PdfUtil.html2Pdf(notice.getMsg(), resourceLocation + fontPath, logger));
            String delSql = NoticeUser.deleteByNoticeId();
            JsonArray delParams = new JsonArray();
            delParams.add(notice.getId());
            JsonArray customUserIds = notice.getCustomUserIds();

            List<NoticeUser> list = new ArrayList<>(customUserIds.size());
            NoticeUser user = null;
            for (int i = 0; i < customUserIds.size(); i++) {
                user = new NoticeUser();
                user.setNoticeId(notice.getId());
                user.setRead(Constant.NOTICE_UNREAD);
                user.setUserId(customUserIds.getString(i));
                list.add(user);
            }
            JsonArray params = new JsonArray(new LinkedList());
            String sql = NoticeUser.insertBatch(list, params);

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

    @RequestMapping(path="/notice/user/update/:userId/:noticeId/:read", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=RespVo.class, description = "修改已读状态")
    public Handler<RoutingContext> updateNoticeUserTypeByUserIdAndNoticeId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray param = new JsonArray(new ArrayList(3));
            String userId = routingContext.request().getParam("userId");
            String noticeId = routingContext.request().getParam("noticeId");
            Short read = Short.parseShort(routingContext.request().getParam("read"));
            String sql = NoticeUser.updateNoticeUserTypeByUserIdAndNoticeId(userId, noticeId, read, param);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, param);
        });
    }

    @RequestMapping(path="/notice/delete/:id", method=HttpMethod.DELETE, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=RespVo.class, description = "删除通知信息")
    public Handler<RoutingContext> deleteNoticeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1))
                    .add(routingContext.request().getParam("id"));
            SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
                return conn.rxUpdateWithParams(NoticeProject.deleteByNoticeId(), params)
                        .flatMap(updateResult -> {
                            return conn.rxUpdateWithParams(NoticeUser.deleteByNoticeId(), params);
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

    @RequestMapping(path="/notice/query/:id", method=HttpMethod.GET, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=Notice.class, description = "查询通知信息")
    public Handler<RoutingContext> queryNoticeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1));
            String sql = Notice.getById(routingContext.request().getParam("id"), params);
            this.query(sqlClient, routingContext, sql, params, 15);
        });
    }

    @RequestMapping(path="/notice/query/pdf/:id", method=HttpMethod.GET, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=Notice.class, description = "查询通知信息")
    public Handler<RoutingContext> queryNoticePdfById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String id = routingContext.request().getParam("id");
            if (id.endsWith(".pdf")) {
                id = id.substring(0, id.length() - 4);
            }
            JsonArray params = new JsonArray(new ArrayList(1));
            String sql = Notice.getById(id, params);
            extsqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, res -> {
                if (res.succeeded() && res.result().getNumRows() > 0) {
                    Notice notice = new Notice(res.result().getRows().get(0));
                    String pdf = notice.getPdf();
                    if (null == pdf) {
                        routingContext.fail(500, new Exception("该消息未生成pdf文件"));
                        return;
                    }
                    HttpServerResponse response = routingContext.response();
                    Buffer buffer = Buffer.buffer();
                    buffer.appendBytes(Base64.getDecoder().decode(pdf));

                    response.putHeader("Pragma", "no-cache");
                    response.putHeader("Cache-Control", "no-cache");
                    response.putHeader("Expires", "0");
                    response.putHeader("content-type", "application/pdf");
                    //routingContext.response().setStatusCode(304);
                    routingContext.response().end(buffer);
                    return;
                }
                routingContext.fail(500, new Exception("该消息不存在或未生成pdf文件"));
            }));
        });
    }

    @RequestMapping(path="/notice/query/user/:userId", method=HttpMethod.GET, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=Notice.class, description = "根据用户id查询通知信息")
    public Handler<RoutingContext> queryNoticeByUserId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String sql = Notice.getByUserIdJoin();
            logger.info(sql);
            JsonArray params = new JsonArray(new ArrayList(1)).add(
                    routingContext.request().getParam("userId"));
            this.query(sqlClient, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/notice/user/query/:noticeId", method=HttpMethod.GET, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=Notice.class, description = "根据消息id查询消息和用户")
    public Handler<RoutingContext> queryNoticeUserByNoticeId() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String sql = NoticeUser.getByNoticeId();
            JsonArray params = new JsonArray(new ArrayList(1)).add(
                    routingContext.request().getParam("noticeId"));
            logger.info(sql);
            this.query(sqlClient, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/notice/query/page", method=HttpMethod.POST, reqParams = {
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
            Notice notice = new Notice(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, notice);
        });
    }

    @RequestMapping(path="/notice/query/admin/page", method=HttpMethod.POST, reqParams = {
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

    @RequestMapping(path="/notice/type/insert", method=HttpMethod.PUT, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, request=NoticeType.class, response=RespVo.class, description = "新增栏目信息")
    public Handler<RoutingContext> insertNoticeType() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = new NoticeType(bodyBuf.toJsonObject()).insertOne(params);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, params);
        });
    }

    @RequestMapping(path="/notice/type/update", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, request=NoticeType.class, response=RespVo.class, description = "修改栏目信息")
    public Handler<RoutingContext> updateNoticeTypeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new LinkedList());
            String sql = new NoticeType(bodyBuf.toJsonObject()).updateById(params);
            logger.info(sql);
            this.operate(mysqlOperation, routingContext, sql, params);
            //this.updateOne(mysqlOperation, routingContext, new NoticeType(bodyBuf.toJsonObject()));
        });
    }

    @RequestMapping(path="/notice/type/delete/:id", method=HttpMethod.DELETE, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=RespVo.class, description = "删除栏目信息")
    public Handler<RoutingContext> deleteNoticeTypeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            String id = routingContext.request().getParam("id");
            JsonArray params = new JsonArray(new ArrayList(1)).add(id);
            String sql = Notice.countByNoticeTypeId();
            extsqlClient.queryWithParams(sql, params, resultSetAsyncResult -> {
                if (resultSetAsyncResult.succeeded() && resultSetAsyncResult.result().getNumRows() > 0
                        && resultSetAsyncResult.result().getResults().get(0).getInteger(0) > 0) {
                    routingContext.response().end(RespVo.failure(
                            "本栏目下还有通知消息，请先将本栏目下的所有通知消息删除", null).toString());
                    return;
                }
                NoticeType noticeType = new NoticeType();
                noticeType.setId(id);
                this.deleteOne(mysqlOperation, routingContext, noticeType);
            });
        });
    }

    @RequestMapping(path="/notice/type/query/:id", method=HttpMethod.GET, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
    }, response=Project.class, description = "查询栏目信息")
    public Handler<RoutingContext> queryNoticeTypeById() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            this.getById(extsqlClient, routingContext, json -> {
                NoticeType noticeType = new NoticeType();
                noticeType.setId(routingContext.request().getParam("id"));
                return noticeType;
            });
        });
    }

    @RequestMapping(path="/notice/type/query/page", method=HttpMethod.POST,reqParams = {
            @SwaggerProperty(fieldName = "Authorization", in = SwaggerProperty.HEADER, type = String.class, value = "登录token"),
            @SwaggerProperty(fieldName = "page", in = SwaggerProperty.PARAMS, type = Integer.class, value = "起始页（从1开始）"),
            @SwaggerProperty(fieldName = "limit", in = SwaggerProperty.PARAMS, type = Integer.class, value = "每页数目"),
            @SwaggerProperty(fieldName = "orderBy", in = SwaggerProperty.PARAMS, type = String.class, value = "排序列（可选）"),
            @SwaggerProperty(fieldName = "sort", in = SwaggerProperty.PARAMS, type = String.class, value = "asc/desc（可选）")
    }, request=Notice.class, respParams = {
            @SwaggerProperty(fieldName = "total", type = Integer.class, value = "返回总数"),
            @SwaggerProperty(fieldName = "list", type = NoticeType.class,
                    collectionType = {@SwaggerProperty.SwaggerCollection(NoticeType.class)}, value = "[] json数组 查询结果")
    }, description = "分页查询栏目信息")
    public Handler<RoutingContext> queryNoticeTypePage() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            NoticeType noticeType = new NoticeType(bodyBuf.toJsonObject());
            this.queryPage(sqlClient, routingContext, noticeType);
        });
    }

    @RequestMapping(path="/notice/server/push/only", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口(仅仅推送消息)")
    public Handler<RoutingContext> serverWebSocketDoPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            noticeService.doPush(routingContext.request().getHeader("Authorization"),
                    bodyBuf.toJsonObject().getJsonArray("list"),
                    throwable -> routingContext.fail(500, throwable),
                    () -> routingContext.response().end(RespVo.success("推送成功").toString()));
        });
    }

    @RequestMapping(path="/notice/server/push", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口（推送并新增消息记录）")
    public Handler<RoutingContext> serverWebSocketPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            noticeService.push(routingContext.request().getHeader("Authorization"),
                    bodyBuf.toJsonObject().getJsonArray("list"),
                    routingContext, throwable -> routingContext.fail(500, throwable),
                    () -> routingContext.response().end(RespVo.success("推送成功").toString()));
        });
    }

    private @Component.Autowired
    Map<String, ServerWebSocket> webSocketMap;

    @RequestMapping(path="/notice/push", method=HttpMethod.POST, reqParams = {
            @SwaggerProperty(fieldName = "Authorization", type = String.class, in = SwaggerProperty.HEADER),
            @SwaggerProperty(fieldName = "list", type = Notice.class, collectionType = @SwaggerProperty.SwaggerCollection(Notice.class))
    }, response=RespVo.class, description = "服务端推送接口(不对外提供)")
    public Handler<RoutingContext> webSocketPush() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray notices = bodyBuf.toJsonArray();
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
