package com.bgi.common;

import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.util.CommonUtil;
import com.bgi.vtx.BaseModel;
import com.bgi.vtx.BaseModel.ResultWrapper;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.HandlerWrap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.reactivex.functions.BiConsumer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class BaseController {

    protected <T extends BaseModel> void getById(SQLClient extsqlClient,
                                                 RoutingContext routingContext, ResultWrapper<T> wrapper) {
        extsqlClient.getConnection(new HandlerWrap<>(routingContext, sqlConnectionAsyncResult -> {
            SQLConnection conn = sqlConnectionAsyncResult.result();
            BaseModel.getById(conn, wrapper, (connect, result, res) -> {
                if (null == res || !res.succeeded()) {
                    conn.close();
                    routingContext.response().end(RespVo.failure("查询失败", null).toString());
                    return;
                }
                conn.close();
                routingContext.response().end(RespVo.success("查询成功").addContent("data", result.getEntries()).toString());
            });
        }));
    }

    protected void queryOne(SQLClient extsqlClient, RoutingContext routingContext, String sql, JsonArray params) {
        extsqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, resultSetAsyncResult -> {
            if (!resultSetAsyncResult.succeeded()) {
                routingContext.response().end(RespVo.failure("查询失败", resultSetAsyncResult.cause()).toString());
                return;
            }
            if (resultSetAsyncResult.result().getNumRows() < 1) {
                routingContext.response().end(RespVo.success("查询成功")
                        .addContent("entries", Constant.NULL_JSON_OBJECT).toString());
                return;
            }
            routingContext.response().end(RespVo.success("查询成功").addContent("entries",
                    resultSetAsyncResult.result().getRows().get(0)).toString());
        }));
    }

    public interface ReqResp {
        void handle(RoutingContext routingContext, Buffer bodyBuf);// throws Exception;
    }

    private static InternalLogger logger = InternalLoggerFactory.getInstance(BaseController.class);

    protected Handler<RoutingContext> handle(Handler<Void> endHandler, ReqResp reqResp) {
        return routingContext -> {
            routingContext.request().bodyHandler(body -> {
                HttpServerResponse response = routingContext.response();
                try {
                    CommonUtil.accessControlAllow(routingContext.response());
                    response.putHeader("content-type", "application/json");
                    reqResp.handle(routingContext, body);
                } catch (Exception e) {
                    logger.error(e);
                    routingContext.fail(500, e);
                    /*if (!response.ended()) {
                        response.end(e.getMessage());
                    }*/
                }
            });/*.endHandler(null != endHandler ? endHandler : event -> {
                routingContext.response().end();
            })*/;
        };
    }

    protected void insertOne(DbOperation<SQLConnection> mysqlOperation, RoutingContext routingContext, BaseModel model) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            model.insertOne(conn, (connect, res) -> {
                if (null == res || !res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("新增失败", null != res ? res.cause() : null).toString());
                    return;
                }
                success.success(conn);
                routingContext.response().end(RespVo.success("新增成功").toString());
            });
        });
    }

    /*protected void updateOne(DbOperation<SQLConnection> mysqlOperation, RoutingContext routingContext, BaseModel model) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            model.updateById(conn, (connect, res) -> {
                if (null == res || !res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("修改失败", null != res ? res.cause() : null).toString());
                    return;
                }
                success.success(conn);
                routingContext.response().end(RespVo.success("修改成功").toString());
            });
        });
    }*/

    protected void deleteOne(DbOperation<SQLConnection> mysqlOperation, RoutingContext routingContext, BaseModel model) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            model.deleteById(conn, (connect, res) -> {
                if (null == res || !res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("删除失败", null != res ? res.cause() : null).toString());
                    return;
                }
                success.success(conn);
                routingContext.response().end(RespVo.success("删除成功").toString());
            });
        });
    }

    protected void operate(DbOperation<SQLConnection> mysqlOperation,
                           RoutingContext routingContext, String sql, JsonArray params) {
        mysqlOperation.deal(routingContext, (success, failed, conn) -> {
            conn.updateWithParams(sql, params, new HandlerWrap<>(routingContext, res -> {
                if (!res.succeeded()) {
                    failed.failed(conn, true, null);
                    routingContext.response().end(RespVo.failure("操作失败", res.cause()).toString());
                    return;
                }
                success.success(conn);
                routingContext.response().end(RespVo.success("操作成功,影响行:" + res.result().getUpdated()).toString());
            }));
        });
    }

    protected void query(SQLClient sqlClient, RoutingContext routingContext, String sql,
                         JsonArray params, int... columns) {
        sqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, resultSetAsyncResult -> {
            if (!resultSetAsyncResult.succeeded()) {
                routingContext.response().end(RespVo.failure("查询失败", resultSetAsyncResult.cause()).toString());
                return;
            }

            routingContext.response().end(RespVo.success("查询成功").addContent("data",
                    SqlResult.getRows(resultSetAsyncResult.result(), columns)).toString());
        }));
    }

    protected void queryPage(SQLClient extsqlClient, RoutingContext routingContext, QueryPage entity, int... columns) {
        SQLpage sqLpage = new SQLpage(routingContext.request(), orderBy -> {
            int i = -1;
            String value = null;
            for (String order : orderBy) {
                ++i;
                value = entity.getMap().get(order);
                if (null == value) {
                    return false;
                }
                orderBy[i] = value;
            }
            return true;
        });
        JsonArray params = new JsonArray(new LinkedList());
        String sql = entity.queryPage(sqLpage, params);
        logger.info(sql);
        extsqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, res -> {
            if (!res.succeeded()) {
                routingContext.response().end(RespVo.failure("查询失败", res.cause()).toString());
                return;
            }
            sqLpage.setList(SqlResult.getRows(res.result(), columns));
            JsonArray queryParams = new JsonArray(new LinkedList());
            String query = entity.countPage(queryParams);
            logger.info(query);
            extsqlClient.queryWithParams(query, queryParams, new HandlerWrap<>(routingContext, r -> {
                sqLpage.setTotal(r.result().getResults().get(0).getLong(0));
                routingContext.response().end(sqLpage.resp());
            }));
        }));
    }

}
