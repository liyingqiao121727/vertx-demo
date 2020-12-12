package com.bgi.business.service;

import com.bgi.business.model.Notice;
import com.bgi.business.model.NoticeProject;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
import com.bgi.vtx.annotation.Component;
import com.bgi.business.model.Notice;
import com.bgi.business.model.NoticeProject;
import com.bgi.business.model.NoticeUser;
import com.bgi.business.model.TipUser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.constant.Constant;
import com.bgi.util.Html2PdfUtil;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.reactivex.ext.sql.SQLClientHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * 消息代办 Service
 *
 * @author 李英乔
 * @since 2019-08-02
 */
@Component("tipService")
public class TipService {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(TipService.class);

    /**
     * 公共的url 前缀 配置文件中有
     */
    private @Autowired String urlPrefix;

    /**
     * web 请求客户端
     */
    private @Autowired WebClient webClient;

    /**
     * web socket 服务端推送的url 配置文件中有
     */
    private @Autowired JsonArray webSocketPushUrls;

    /**
     * reactive 数据库写入客户端
     */
    private @Autowired io.vertx.reactivex.ext.sql.SQLClient rxSqlClientW;

    /**
     * 字体文件路径 配置文件中有
     */
    private @Autowired String fontPath;

    /**
     * 资源文件根目录
     */
    private @Autowired String resourceLocation;

    /**
     * 调用 web socket 消息推送接口， 完成消息推送
     *
     * @param token
     * @param body
     * @param failure
     * @param success
     */
    public void doPush(String token, JsonArray body, Handler<Throwable> failure, Runnable success) {
        JsonObject jsonObject = null;
        for (int i = 0; i < webSocketPushUrls.size(); i++) {
            jsonObject = webSocketPushUrls.getJsonObject(i);
            webClient.post(jsonObject.getInteger("port"), jsonObject.getString("host"),
                    urlPrefix + jsonObject.getString("url"))
                    .putHeader("Authorization", token).sendBuffer(body.toBuffer(), resp -> {
                if (resp.succeeded()) {
                    success.run();
                    return;
                }
                failure.handle(resp.cause());
            });
        }
    }

    /**
     * 新增消息并 推送消息
     *
     * @param token
     * @param notices
     * @param routingContext
     * @param fail
     * @param success
     */
    public void push(String token, JsonArray notices, RoutingContext routingContext,
                     Handler<Throwable> fail, Runnable success) {
        this.insertMany(notices, routingContext);
        this.doPush(token, notices, fail, success);
    }

    /**
     * 新增消息
     *
     * @param notice
     * @param routingContext
     */
    public void insertOne(Notice notice, RoutingContext routingContext) {
        String projId = notice.getProjId();
        if (null == notice.getState()) {
            notice.setState(0);
        }
        List<TipUser> users = notice.getTipUsers();
        //JsonArray userIds = notice.getCustomUserIds();
        JsonArray params = new JsonArray(new LinkedList());
        //新增消息
        String sql = notice.insertOne(params);

        SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
            return conn.rxUpdateWithParams(sql, params).flatMap(updateResult -> {
                if (null != projId) {
                    //消息与工程绑定
                    NoticeProject project = new NoticeProject();
                    project.setNoticeId(notice.getId());
                    project.setProjId(projId);
                    project.setBigAgreeId(notice.getBigAgreeId());
                    params.clear();
                    String projSql = project.insertOne(params);
                    logger.info(projSql);
                    return conn.rxUpdateWithParams(projSql, params);
                }
                return Constant.SINGLE_UPSATE_RESULT;
            }).flatMap(updateResult -> {
                if (users.size() > 0) {
                    params.clear();
                    String userSql = TipUser.insertMany(users, params);
                    logger.info(userSql);
                    return conn.rxUpdateWithParams(userSql, params);
                }
                return Constant.SINGLE_UPSATE_RESULT;
            }).doOnError(throwable -> {
                conn.rxRollback().doOnError(throwable1 -> { logger.error(throwable1); }).subscribe();
            });
        }).subscribe(updateResult -> {
            routingContext.response().end(RespVo.success("新增成功").toString());
        }, throwable -> {
            logger.error(throwable);
            routingContext.response().end(RespVo.failure("新增失败", throwable).toString());
        });
    }

    /**
     * 新增多条消息
     *
     * @param notices
     * @param routingContext
     */
    public void insertMany(JsonArray notices, RoutingContext routingContext) {
        List<Notice> list = new LinkedList<>();
        List<NoticeProject> listProjList = new LinkedList<>();
        List<TipUser> noticeUserList = new LinkedList<>();
        Notice notice = null;
        List<TipUser> users = null;
        TipUser user = null;
        NoticeProject project = null;
        for (int i = 0; i < notices.size(); i++) {
            notice = new Notice(notices.getJsonObject(i));
            //将消息转pdf
            notice.setPdf(Html2PdfUtil.html2Pdf(notice.getMsg(), resourceLocation + fontPath, logger));
            if (null == notice.getState()) {
                notice.setState(0);
            }
            if (null != notice.getProjId()) {
                //消息与工程绑定
                project = new NoticeProject();
                project.setProjId(notice.getProjId());
                project.setNoticeId(notice.getId());
                project.setBigAgreeId(notice.getBigAgreeId());
                listProjList.add(project);
            }
            list.add(notice);
            users = notice.getTipUsers();
            if (users.isEmpty()) {
                continue;
            }
            //新增消息 通知用户
            noticeUserList.addAll(users);
        }
        JsonArray params = new JsonArray(new LinkedList());
        JsonArray paramsProj = new JsonArray(new LinkedList());
        JsonArray paramsUser = new JsonArray(new LinkedList());

        //新增多条消息
        String sql = Notice.insertMany(list, params);

        SQLClientHelper.inTransactionSingle(rxSqlClientW, conn -> {
            return conn.rxUpdateWithParams(sql, params).flatMap(updateResult -> {
                if (listProjList.size() > 0) {
                    String sqlProj = NoticeProject.insertMany(listProjList, paramsProj);
                    logger.info(sqlProj);
                    return conn.rxUpdateWithParams(sqlProj, paramsProj);
                }
                return Constant.SINGLE_UPSATE_RESULT;
            }).flatMap(updateResult -> {
                if (noticeUserList.size() > 0) {
                    String sqlUser = TipUser.insertMany(noticeUserList, paramsUser);
                    logger.info(sqlUser);
                    return conn.rxUpdateWithParams(sqlUser, paramsUser);
                }
                return Constant.SINGLE_UPSATE_RESULT;
            }).doOnError(throwable -> {
                conn.rxRollback().doOnError(throwable1 -> { logger.error(throwable1); }).subscribe();
            });
        }).subscribe(updateResult -> {
            routingContext.response().end(RespVo.success("新增成功").toString());
        }, throwable -> {
            logger.error(throwable);
            routingContext.response().end(RespVo.failure("新增失败", throwable).toString());
        });
    }

}
