package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.ResponseUtil;
import play.libs.Json;
import services.StatsService;

/**
 * StatsGlobalActor to fetch global stats
 * Subscribes to Supervisor Actor
 *
 * @author Bowen Cheng
 */
public class StatsGlobalActor extends AbstractActor {

    ActorRef websocket;
    StatsService statsService;
    String searchTerm;

    /**
     * Method call before Actor is started to subscribe to supervisor actor.
     *
     * @author Bowen Cheng
     */
    @Override
    public void preStart() {
        context().actorSelection("/user/supervisorActor/")
                .tell(new SupervisorActor.RegisterMsg(), self());
    }

    /**
     * StatsActor constructor
     *
     * @param websocket   Websocket Actor reference
     * @param statsService User Service reference
     * @author Bowen Cheng
     */
    public StatsGlobalActor(final ActorRef websocket, final StatsService statsService) {
        this.websocket = websocket;
        this.statsService = statsService;
    }

    /**
     * Method to get the Actor protocols and create the actor
     *
     * @param websocket   Websocket Actor reference
     * @param statsService User Service reference
     * @return Props
     * @author Bowen Cheng
     */
    public static Props props(final ActorRef websocket, final StatsService statsService) {
        return Props.create(StatsGlobalActor.class, websocket, statsService);
    }

    /**
     * The method called when message is received from other actor
     *
     * @return Receive
     * @author Bowen Cheng
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SupervisorActor.Data.class, this::sendNewData)
                .match(ObjectNode.class, o -> this.searchTerm = o.get("searchTerm").textValue())
                .build();
    }

    /**
     * Method to get global stats
     *
     * @author Bowen Cheng
     */
    private void sendNewData(SupervisorActor.Data data) {
        statsService.getGlobalStats(searchTerm)
                .thenAccept(response -> {
                    JsonNode jsonObject = Json.toJson(response);
                    websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
                });
    }

}
