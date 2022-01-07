package com.liangzhicheng.modules.controller;

import com.liangzhicheng.common.utils.PrintUtil;
import com.liangzhicheng.modules.service.ISysRoleUserService;
import io.swagger.annotations.Api;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket管理相关类
 * @author liangzhicheng
 */
@Api(value = "WebSocketManager", tags = {"WebSocket"})
@ServerEndpoint(value = "/webSocket/{connectId}")
@Component
public class WebSocketManager {

    /**
     * 处理无法注入的关键
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketManager.applicationContext = applicationContext;
    }

    /**
     * 需要注入的service或者dao
     */
    private ISysRoleUserService roleUserService;

    /**
     * 用于存放所有在线客户端
     */
    public static Map<String, Session> clients = new ConcurrentHashMap<>();
    /**
     * 用于记录当前在线连接数，应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * 与某个客户端的连接会话，需要通过此来给客户端发送数据
     */
    private Session session;
    /**
     * 建立连接id
     */
    private String connectId;

    @OnOpen
    public void onOpen(@PathParam("connectId") String connectId, Session session) throws IOException {
        this.connectId = connectId;
        this.session = session;
        addOnlineCount();
        clients.put(connectId, session);
        if(connectId.contains("accountId:")){
            roleUserService = applicationContext.getBean(ISysRoleUserService.class);
            roleUserService.testOnlineRoleUser(connectId);
        }
        PrintUtil.info("[websocket服务] 客户端上线，客户端id：{}", connectId);
    }

    @OnMessage
    public void onMessage(String param, Session session) throws IOException{
        PrintUtil.info("[websocket服务] 接收到客户端{}发来的消息", param);
    }

    @OnClose
    public void onClose(Session session) throws IOException{
        subOnlineCount();
        clients.remove(connectId);
        PrintUtil.info("[websocket服务] 客户端下线，客户端id：{}", connectId);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        PrintUtil.error("[websocket服务] 发生异常，异常信息为：{}", throwable.getMessage());
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketManager.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketManager.onlineCount--;
    }

    public static synchronized Map<String, Session> getClients() {
        return clients;
    }

}
