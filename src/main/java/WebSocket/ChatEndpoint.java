package WebSocket;

import Models.EncodersAndDecoder.MembersEncoder;
import Models.EncodersAndDecoder.MessageDecoder;
import Models.EncodersAndDecoder.MessageEncoder;
import Models.JsonModel;
import Models.Members;
import Models.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class, MembersEncoder.class})
public class ChatEndpoint {

    private String username;
    private static ConcurrentHashMap<String, Session> ws_sessions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> ws_users = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {
        System.out.println("onOpen by " + username);
        this.username = username;
        //session.setMaxIdleTimeout(5*60*1000); //Таймаут сокета 5 мин

        if (ws_sessions.keySet().contains(username)) {
            String error = "Пользователь с именем " + username + " уже есть в чате";
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, error));
            throw new SocketException(error);
        }
        String old_username = ws_users.get(session.getId());
        if (old_username != null) {
            disconnectUser(old_username);
        }
        ws_sessions.put(username, session);
        ws_users.put(session.getId(), username);

        sendMessageToAll(new Message().setFrom(username).setContent("Connected!"));
        sendMembersToClient(session);
    }

    @OnMessage
    public void onMessage(Session session, JsonModel jsonModel) throws IOException {
        if (jsonModel instanceof Message) {
            Message message = (Message) jsonModel;
            message.setFrom(username);
            sendMessageToAll(message);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        disconnectUser(username);
        ws_users.remove(session.getId());
        session.close();
    }

    private void disconnectUser(String username) {
        ws_sessions.remove(username);
        sendMessageToAll(new Message().setFrom(username).setContent("Disconnected!"));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void sendMessageToAll(Message message) {

        for (String user : ws_sessions.keySet()) {
            try {
                Session session = ws_sessions.get(user);
                if (session != null)
                    session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMembersToClient(Session session) {
        try {
            session.getBasicRemote().sendObject(new Members().setList(new ArrayList<>(ws_sessions.keySet())));
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
    private static void sendMessageToClients(Message message, ArrayList<String> users) {
        ws_sessions.entrySet().stream().filter(session -> session.getKey().contains(users.iterator()))
    }
}