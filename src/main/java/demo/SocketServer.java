package demo;

import javax.jws.WebService;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
public class SocketServer {
    //储存所有和 WebSocket 连接的对象
    private static CopyOnWriteArraySet<SocketServer> webSocketSet = new CopyOnWriteArraySet<SocketServer>();

    //和 WebSocket 建立连接的对象
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("add new session");
        webSocketSet.add(this);
    }


    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }


    //接收到服务2的信息后，发送信息给每个对象
    @OnMessage
    public void onMessage(String message, Session session) {
        for (SocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    //发送信息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}