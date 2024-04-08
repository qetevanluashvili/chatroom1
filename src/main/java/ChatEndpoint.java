import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat")
public class ChatEndpoint {

    private static final Set<Session> activeSessions = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        activeSessions.add(session);
        broadcastMessage("Server", "User joined the chat");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            Message receivedMessage = objectMapper.readValue(message, Message.class);
            broadcastMessage(receivedMessage.getSender(), receivedMessage.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        activeSessions.remove(session);
        broadcastMessage("Server", "User left the chat");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void broadcastMessage(String sender, String content) {
        try {
            Message message = new Message(sender, content);
            String jsonMessage = objectMapper.writeValueAsString(message);
            synchronized (activeSessions) {
                for (Session s : activeSessions) {
                    if (s.isOpen()) {
                        s.getBasicRemote().sendText(jsonMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


