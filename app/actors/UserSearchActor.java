package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.ResponseUtil;
import play.libs.Json;
import services.UserService;

/**
 * UserSearchActor to fetch details for a user by making an API call every 10s
 * Subscribes to Supervisor Actor
 *
 * @author Yvonne Lee
 */
public class UserSearchActor extends AbstractActor {

    ActorRef websocket;
    UserService userService;

    /**
     * Method call before Actor is started to subscribe to supervisor actor.
     *
     * @author Yvonne Lee
     */
    @Override
    public void preStart() {
        context().actorSelection("/user/supervisorActor/")
                .tell(new SupervisorActor.RegisterMsg(), self());
    }

    /**
     * UserSearchActor constructor
     *
     * @param websocket   Websocket Actor reference
     * @param userService User Service reference
     * @author Yvonne Lee
     */
    public UserSearchActor(final ActorRef websocket, final UserService userService) {
        this.websocket = websocket;
        this.userService = userService;
    }

    /**
     * Method to get the Actor protocols and create the actor
     *
     * @param websocket   Websocket Actor reference
     * @param userService User Service reference
     * @return Props
     * @author Yvonne Lee
     */
    public static Props props(final ActorRef websocket, final UserService userService) {
        return Props.create(UserSearchActor.class, websocket, userService);
    }

    /**
     * The method called when message is received from other actor
     *
     * @return Receive
     * @author Yvonne Lee
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ObjectNode.class, o -> sendNewData(o.get("keyword").textValue()))
                .build();
    }

    /**
     * Method to fetch user details and send to UI.
     *
     * @param userId User ID
     * @author Yvonne Lee
     */
    private void sendNewData(String userId) {
        userService.findUserById(Long.parseLong(userId))
                .thenAccept(response -> {
                    //TODO: Add data to search history
                    JsonNode jsonObject = Json.toJson(response);
                    websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
                });
    }

}
