package com.bgi.business.controller;

import com.bgi.common.BaseController;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import com.bgi.common.BaseController;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Controller;
import com.bgi.vtx.annotation.RequestMapping;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.RoutingContext;

@Component(value = "aHelloController")
@Controller(value = AHelloController.class, name = "hello")
public class AHelloController extends BaseController {

    @RequestMapping(path="/hello", method=HttpMethod.GET, description = "hello, 没什么用")
    public Handler<RoutingContext> insertBatch() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            routingContext.response().end("hello");
        });
    }

    @RequestMapping(path="/push1", method=HttpMethod.GET, description = "hello, 没什么用")
    public Handler<RoutingContext> push1() {
        return this.handle(null, (routingContext, bodyBuf) -> {
            ServerWebSocket serverWebSocket = routingContext.request().upgrade();
            routingContext.response().end("hello");
            //VertxHandler vertxHandler = null;
            //RouterImpl
            //WebSocketRequestHandler
            //vertxHandler.hand
            //HttpHandlers
        });
    }
}
