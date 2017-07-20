package ws.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chatroomServerEndpoint", configurator = ChatroomServerConfigurator.class)
public class ChatroomServerEndpoint {

    static Set<Session> chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void handleOpen(EndpointConfig endpointConfig, Session userSession) {
        userSession.getUserProperties().put("username", endpointConfig.getUserProperties().get("username"));
        chatroomUsers.add(userSession);

    }

    @OnMessage
    public void handleMessage(String message, Session userSession) {
        String username = (String) userSession.getUserProperties().get("username");
        System.out.println("cse >> username " + username);
        System.out.println("cse >> usersess " + userSession);
        if (username != null) {
            chatroomUsers.stream().forEach((Session x) -> {
                try {
                    x.getBasicRemote().sendText(buildJsonData(username, message));
                } catch (IOException e) {
                }
            });
        }
    }

    @OnClose
    public void handleClose(Session userSession) {
        chatroomUsers.remove(userSession);
    }

    @OnError
    public void handleError(Throwable t) {
    }

    private String buildJsonData(String username, String message) {
        JsonObject jsonObject = Json.createObjectBuilder().add("message", username + ": " + message).build();
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWriter.toString();
    }

}
