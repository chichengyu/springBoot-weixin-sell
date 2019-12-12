package cn.xiaochi.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket推送消息
 * 结果去网上查了一下，发现在@ServerEndPoint中是无法使用@Autowired注入Bean的。
 */
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    private final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // websocket 容器 ，用来存放 这些 session
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        logger.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        logger.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        logger.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message){
        for (WebSocket webSocket : webSocketSet){
            logger.info("【websocket消息】广播消息, message={}", message);
            try{
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
