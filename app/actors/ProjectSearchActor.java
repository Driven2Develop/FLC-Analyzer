package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.ResponseUtil;
import play.libs.Json;
import services.ProjectService;

/**
 * ProjectSearchActor to search by making an API call every 10s
 * Subscribes to Supervisor Actor
 *
 * @author Yvonne Lee
 */
public class ProjectSearchActor extends AbstractActor {

    ActorRef websocket;
    ProjectService projectService;
    String searchTerm;

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
     * @param websocket      Websocket Actor reference
     * @param projectService Project Service reference
     * @author Yvonne Lee
     */
    public ProjectSearchActor(final ActorRef websocket, final ProjectService projectService) {
        this.websocket = websocket;
        this.projectService = projectService;
    }


    /**
     * Method to get the Actor protocols and create the actor
     *
     * @param websocket      Websocket Actor reference
     * @param projectService Project Service reference
     * @return Props
     * @author Yvonne Lee
     */
    public static Props props(final ActorRef websocket, final ProjectService projectService) {
        return Props.create(ProjectSearchActor.class, websocket, projectService);
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
                .match(SupervisorActor.Data.class, this::send)
                .match(ObjectNode.class, o -> this.searchTerm = o.get("keyword").textValue())
                .build();
    }

    /**
     * Method to search projects send to UI.
     *
     * @param data Data
     * @author Yvonne Lee
     */
    private void send(SupervisorActor.Data data) {
        projectService.searchLatestTenProjects(searchTerm)
                .thenAccept(response -> {
                    JsonNode jsonObject = Json.toJson(response);
                    websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
                });
    }
}
