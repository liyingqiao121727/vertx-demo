package com.bgi.vtx;

import com.bgi.vtx.reqresp.response.RespVo;
import com.bgi.vtx.reqresp.response.RespVo;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * 对vertx框架内的Handler进行的封装；
 *
 * 封装原因：
 * 1.框架内的Handler没有对异常进行统一的处理
 * 2.Handler出现异常时，如果不进行处理是不会返回给前端的，在浏览器中的表现就是没有响应
 *   为了解决没有响应的问题，可根据需要对异常进行处理，或向前端返回错误
 *
 * @author 李英乔
 * @since 2019-06-17
 */
public class HandlerWrap<T> implements Handler<T> {

    //日志
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(HandlerWrap.class);

    /**
     * 被封装的Handler
     */
    private final Handler<T> handler;

    /**
     * 用于Handler出现异常时直接返回错误
     */
    private final RoutingContext routingContext;

    /**
     * Handler出现异常时的异常处理
     */
    private final Runnable failHandle;

    public HandlerWrap(Handler<T> handler) {
        this.handler = handler;
        this.routingContext = null;
        this.failHandle = null;
    }

    public HandlerWrap(RoutingContext routingContext, Handler<T> handler) {
        this.handler = handler;
        this.routingContext = routingContext;
        this.failHandle = null;
    }

    public HandlerWrap(RoutingContext routingContext, Runnable failHandle, Handler<T> handler) {
        this.handler = handler;
        this.routingContext = routingContext;
        this.failHandle = failHandle;
    }

    @Override
    public void handle(T t) {
        try {
            handler.handle(t);
        } catch (Exception e) {
            logger.error(e);
            if (null != failHandle) {
                failHandle.run();
            }
            if (null != routingContext && !routingContext.response().ended()) {
                routingContext.response().end(RespVo.failure("", e).toString());
            }
        }
    }
}
