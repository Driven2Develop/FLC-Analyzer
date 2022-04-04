package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.ResponseUtil;
import models.User;
import play.libs.Json;
import services.UserService;

import java.util.concurrent.CompletionStage;

public class UserSearchActor extends AbstractActor {

    ActorRef websocket;
    UserService userService;
    String userId;


    static public class Data{

    }


    public UserSearchActor(final ActorRef websocket, final UserService userService) {
        this.websocket = websocket;
        this.userService = userService;
    }

    public static Props props(final ActorRef websocket, final UserService userService) {
        return Props.create(UserSearchActor.class, websocket, userService);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ObjectNode.class, o -> sendNewData(o.get("keyword").textValue()))
                .build();
    }

    private void sendNewData(String userId) {
        userService.findUserById(Long.parseLong(userId))
                .thenAccept(response ->  {
                    //TODO: Add data to search history
                    JsonNode jsonObject = Json.toJson(response);
                    websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
                });
    }

}
