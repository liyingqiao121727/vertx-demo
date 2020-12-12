package com.bgi.business.controller;

import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.business.model.EarthEnergyUser;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.service.LoginService;
import com.bgi.common.BaseController;
import com.bgi.vtx.HandlerWrap;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.util.ArrayList;

@Component("loginController")
@Controller(value = LoginController.class, name = "登录相关")
public class LoginController extends BaseController {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoginController.class);

    private @Component.Autowired
    WebClient webClient;

    private @Component.Autowired
    String bgiHost;

    private @Component.Autowired
    Integer bgiPort;

    private @Component.Autowired
    String bgiLogin;

    private @Component.Autowired
    SQLClient sqlClient;

    private @Component.Autowired
    LoginService loginService;

    @RequestMapping(path="/auth/login", method=HttpMethod.POST, description = "登录接口")
    public Handler<RoutingContext> authLogin() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            JsonArray params = new JsonArray(new ArrayList(1));
            String username = routingContext.request().getParam("username");
            String refreshToken = routingContext.request().getParam("refresh_token");
            if (null == username && null != refreshToken) {
                loginService.login(routingContext);
                return;
            }
            String sql = EarthEnergyUser.getVisitByUserName(username, params);
            logger.info(sql);
            sqlClient.queryWithParams(sql, params, new HandlerWrap<>(routingContext, r -> {
                if (!r.succeeded() || r.result().getRows().size() < 1) {
                    routingContext.fail(401, new Exception("该用户名不存在，或者您不允许访问本系统"));
                    return;
                }

                loginService.login(routingContext);

                /*HttpRequest<Buffer> req = webClient.post(bgiPort, bgiHost, bgiLogin);
                HttpServerRequest request = routingContext.request();
                req.putHeaders(request.headers()).sendForm(request.params(), httpResponseAsyncResult -> {
                    HttpServerResponse response = routingContext.response();
                    if (!httpResponseAsyncResult.succeeded()) {
                        response.end(RespVo.failure("登录失败", httpResponseAsyncResult.cause()).toString());
                        return;
                    }
                    String accessToken = null;
                    JsonObject jsonObject = null;
                    try {
                        jsonObject = httpResponseAsyncResult.result().bodyAsJsonObject();
                        accessToken = jsonObject.getString("access_token");
                    } catch (Exception e) {
                        response.end(httpResponseAsyncResult.result().bodyAsBuffer());
                        return;
                    }
                    JsonObject resp = jsonObject;
                    //if (null == accessToken) {
                    response.end(resp.toString());
                    return;*/
                    //}
                /*String token = "Bearer " + accessToken;
                loginService.authorization(token, e -> routingContext.fail(401, new Exception("岩云宝鉴权失败")),
                        e -> routingContext.fail(401, new Exception("您不允许访问本系统，请联系管理员")),
                        (user, bgiUser) -> response.end(resp.toString()));*/

                //});

            }));

        });
    }/*

    @RequestMapping(path="/auth/mobile/login", method=HttpMethod.POST, description = "验证码，返回图片（响应头中有sid，登录接口需要）")
    public Handler<RoutingContext> authMobileLogin() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            HttpRequest<Buffer> req = webClient.post(bgiPort, bgiHost, bgiLogin);
            HttpServerRequest request = routingContext.request();
            req.putHeaders(request.headers()).sendForm(request.params(), httpResponseAsyncResult -> {
                HttpServerResponse response = routingContext.response();
                if (!httpResponseAsyncResult.succeeded()) {
                    response.end();
                    return;
                }
                String accessToken = null;
                JsonObject jsonObject = null;
                try {
                    jsonObject = httpResponseAsyncResult.result().bodyAsJsonObject();
                    accessToken = jsonObject.getString("access_token");
                } catch (Exception e) {
                    response.end(httpResponseAsyncResult.result().bodyAsBuffer());
                    return;
                }
                JsonObject resp = jsonObject;
                if (null == accessToken) {
                    response.end(resp.toString());
                    return;
                }
                String token = "Bearer " + accessToken;
                loginService.authorization(token, e -> routingContext.fail(401, new Exception("岩云宝鉴权失败")),
                        e -> routingContext.fail(401, new Exception("您不允许访问本系统，请联系管理员")),
                        (user, bgiUser) -> response.end(resp.toString()));

            });
        });
    }*/

}
