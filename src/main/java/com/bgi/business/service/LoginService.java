package com.bgi.business.service;

import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.vtx.annotation.Component;
import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.reactivex.functions.BiConsumer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

/**
 * 用户登录 Service
 *
 * @author 李英乔
 * @since 2019-06-05
 */
@Component("loginService")
public class LoginService {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoginService.class);

    /**
     * 发起web请求的客户端
     */
    private @Autowired WebClient webClient;

    /**
     * bgi服务的主机地址
     */
    private @Autowired String bgiHost;

    /**
     * bgi服务的主机端口
     */
    private @Autowired Integer bgiPort;

    /**
     * bgi服务的用户信息接口
     */
    private @Autowired String bgiUserInfo;

    /**
     * bgi服务的登录接口
     */
    private @Autowired String bgiLogin;

    /**
     * token 及用户的权限校验
     * @param token
     * @param userId
     * @param fail
     * @param failure
     * @param consumer
     */
    public void authorization(String token, String userId, Handler<Throwable> fail, Handler<Throwable> failure,
                              BiConsumer<EarthEnergyUser, JsonObject> consumer) {
        webClient.get(bgiPort, bgiHost, bgiUserInfo + userId)
                .putHeader("Authorization", token).send(resp -> {
            if (!resp.succeeded()) {
                fail.handle(resp.cause());
                return;
            }
            try {
                consumer.accept(null, null);
            } catch (Exception e) {
                failure.handle(e);
            }
        });
    }

    /**
     * 用户登录
     *
     * @param routingContext
     */
    public void login(RoutingContext routingContext) {
        HttpRequest<Buffer> req = webClient.post(bgiPort, bgiHost, bgiLogin);
        HttpServerRequest request = routingContext.request();
        //访问登录接口
        req.putHeaders(request.headers()).sendForm(request.params(), httpResponseAsyncResult -> {
            HttpServerResponse response = routingContext.response();
            if (null == httpResponseAsyncResult.result() || !httpResponseAsyncResult.succeeded()) {
                //登录接口访问失败
                response.end(RespVo.failure("登录失败, 可能原因：用户名或密码错误", httpResponseAsyncResult.cause()).toString());
                return;
            }
            JsonObject jsonObject = null;
            //登录接口访问成功
            try {
                jsonObject = httpResponseAsyncResult.result().bodyAsJsonObject();
            } catch (Exception e) {
                response.end(httpResponseAsyncResult.result().bodyAsBuffer());
                return;
            }
            JsonObject resp = jsonObject;
            if (null == resp) {
                //登录失败
                Buffer buffer = httpResponseAsyncResult.result().bodyAsBuffer();
                if (null == buffer) {
                    response.end(RespVo.failure("登录失败，请检查用户名或密码是否有误", httpResponseAsyncResult.cause()).toString());
                    return;
                }
                response.end(buffer);
                return;
            }
            //登录成功
            response.end(resp.toString());
            return;
        });
    }
}
