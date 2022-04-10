package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.ResponseUtil;
import models.Readability;
import play.libs.Json;
import services.ProjectService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DescriptionReadabilityActor extends AbstractActor {

    ActorRef websocket;
    ProjectService projectService;
    String previewDescription;

    /**
     * Method call before Actor is started to subscribe to supervisor actor.
     *
     * @author Iymen Abdella
     */
    @Override
    public void preStart() {
        context().actorSelection("/user/supervisorActor/")
                .tell(new SupervisorActor.RegisterMsg(), self());
    }

    /**
     * AverageReadabilityActor constructor
     *
     * @param websocket   Websocket Actor reference
     * @param projectService Project Service reference
     * @author Iymen Abdella
     */
    public DescriptionReadabilityActor(final ActorRef websocket, final ProjectService projectService) {
        this.websocket = websocket;
        this.projectService = projectService;
    }

    /**
     * Creating actors, follows factory design pattern
     *
     * @param websocket   Websocket Actor reference
     * @param projectService Project Service reference
     * @return Props
     * @author Iymen Abdella
     */
    public static Props props(final ActorRef websocket, final ProjectService projectService) {
        return Props.create(DescriptionReadabilityActor.class, websocket, projectService);
    }

    /**
     * The method called when message is received from other actor
     *
     * @return Receive
     * @author Iymen Abdella
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(SupervisorActor.Data.class, this::send)
                .match(ObjectNode.class, o -> this.previewDescription = o.get("keyword").textValue())
                .build();
    }

    /**
     * Method to fetch get average readability based on search terms
     *
     * Makes asynchronous call before rendering UI
     *
     * @param data data
     * @author Iymen Abdella
     */
    private void send(SupervisorActor.Data data) {

        CompletableFuture<List<Readability>> results = CompletableFuture.supplyAsync(() -> projectService.computeProjectReadability(previewDescription));
        results.thenAccept(response -> {
            JsonNode jsonObject = Json.toJson(response);
            websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
        });
    }
}
