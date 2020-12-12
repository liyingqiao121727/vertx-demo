package com.bgi.vtx;

import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.reqresp.response.RespVo;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class BGIWebSocket {

    private static final Map<String, Handler<ServerWebSocket>> handlerMap = new HashMap<>();

    private static InternalLogger logger = InternalLoggerFactory.getInstance(BGIWebSocket.class);

    public BGIWebSocket(HttpServer server, Map<String, ServerWebSocket> webSocketMap, Map<String, String> wsMapping) {
        server.websocketHandler(serverWebSocket -> {
            logger.info(serverWebSocket.subProtocol());
            String path = serverWebSocket.path();
            Handler<ServerWebSocket> handler = handlerMap.get(path);
            if (null != handler) {
                handler.handle(serverWebSocket);
            }
            //serverWebSocket.writeFinalTextFrame("123456789");
            //ServerWebSocketImpl sdf = (ServerWebSocketImpl) serverWebSocket;
            //Http1xServerConnection serverConnection = null;
            //serverWebSocket.headers().add("bgi", "sdjfgh");
            //serverWebSocket.writeFrame(new WebSocketFrameImpl("123456789"));
            logger.info(serverWebSocket.subProtocol());
            serverWebSocket.closeHandler(c -> {
                logger.info("webSocket 关闭了， 服务端剩余：" + webSocketMap.size());
                String id = serverWebSocket.binaryHandlerID();
                String userId = wsMapping.remove(id);
                if (null != userId) {
                    webSocketMap.remove(userId);
                }
            });

            serverWebSocket.endHandler(end -> {
                logger.info("webSocket 结束， 服务端剩余：" + webSocketMap.size());
            });

            serverWebSocket.exceptionHandler(ex -> {
                serverWebSocket.writeTextMessage(RespVo.failure("webSocket exception", ex).toString());
                logger.error("webSocket exception: ", ex);
            });
        });
    }

    public static void put(AbstractMap.SimpleEntry<String, Handler<ServerWebSocket>> entry) {
        handlerMap.put(entry.getKey(), entry.getValue());
    }
}
