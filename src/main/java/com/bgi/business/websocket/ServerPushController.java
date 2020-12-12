package com.bgi.business.websocket;

import com.bgi.business.reqresp.response.RespVo;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.WebSocket;
import com.bgi.business.reqresp.response.RespVo;
import com.bgi.business.service.LoginService;
import com.bgi.vtx.annotation.Component;
import com.bgi.vtx.annotation.Component.Autowired;
import com.bgi.vtx.annotation.WebSocket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务端web socket
 *
 * @author 李英乔
 * @since 2019-08-12
 */
@Component("serverPushController")
@WebSocket
public class ServerPushController {

    /**
     * 用户id与web socket的映射关系
     */
    private @Autowired Map<String, ServerWebSocket> webSocketMap;

    /**
     * web socket id 与 user id 的绑定关系
     */
    private @Autowired Map<String, String> wsMapping;

    //private static final Map<String, Handler<ServerWebSocket>> handlerMap = new HashMap<>();

    private static InternalLogger logger = InternalLoggerFactory.getInstance(ServerPushController.class);

    /**
     * 统一的url前缀 配置文件中有
     */
    private @Autowired String urlPrefix;

    /**
     * 登录服务
     */
    private @Autowired LoginService loginService;

    public SimpleEntry<String, Handler<ServerWebSocket>> push() {
        return new SimpleEntry<>("/ws/push", serverWebSocket -> {
            // 获取每一个链接的ID
            String id = serverWebSocket.binaryHandlerID();
            serverWebSocket.frameHandler(frame -> {
                String text = frame.textData();
                text = null != text && "".equals(text.trim()) ? null : text.trim();
                if (null == text || text.length() == 1) {
                    return;
                }
                logger.info(text);
                JsonObject request = null;
                try {
                    request = (JsonObject) Json.decodeValue(text);
                } catch (Exception e) {
                    serverWebSocket.writeTextMessage(RespVo.failure("数据格式有误", e).toString());
                    return;
                }
                String token = request.getString("token");
                String userId = request.getString("userId");
                //访问该webSocket的token及权限校验
                loginService.authorization(token, userId,
                        e -> serverWebSocket.writeTextMessage(
                                RespVo.failure("岩云宝鉴权失败", null).toString()),
                        e -> serverWebSocket.writeTextMessage(
                                RespVo.failure("您不允许访问本系统，请联系管理员", null).toString()),
                        (user, bgiUser) -> {
                            //String uId = user.getId();
                            wsMapping.put(id, userId);
                            webSocketMap.put(userId, serverWebSocket);
                            serverWebSocket.writeTextMessage(RespVo.success("授权成功").toString());
                        });

            });
        });
    }
}
