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
 * JobProjectSearchActor to fetch projects for a job id by making an API call every 10s
 * Subscribes to Supervisor Actor
 *
 * @author Mengqi Liu
 */
public class JobProjectSearchActor extends AbstractActor {

    ActorRef websocket;
    ProjectService projectService;
    long jobId;

    /**
     * Method call before Actor is started to subscribe to supervisor actor.
     *
     * @author Mengqi Liu
     */
    @Override
    public void preStart() {
        context().actorSelection("/user/supervisorActor/")
                .tell(new SupervisorActor.RegisterMsg(), self());
    }

    /**
     * JobProjectSearchActor constructor
     *
     * @param websocket   Websocket Actor reference
     * @param projectService Project Service reference
     * @author Mengqi Liu
     */
    public JobProjectSearchActor(final ActorRef websocket, final ProjectService projectService) {
        this.websocket = websocket;
        this.projectService = projectService;
    }

    /**
     * Method to get the Actor protocols and create the actor
     *
     * @param websocket   Websocket Actor reference
     * @param projectService Project Service reference
     * @return Props
     * @author Mengqi Liu
     */
    public static Props props(final ActorRef websocket, final ProjectService projectService) {
        return Props.create(JobProjectSearchActor.class, websocket, projectService);
    }

    /**
     * The method called when message is received from other actor
     *
     * @return Receive
     * @author Mengqi Liu
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SupervisorActor.Data.class, this::sendNewData)
                .match(ObjectNode.class, o -> this.jobId = Long.parseLong(o.get("jobId").textValue()))
                .build();
    }

    /**
     * Method to fetch projects by job id and send to UI.
     *
     * @param data data
     * @author Mengqi Liu
     */
    private void sendNewData(SupervisorActor.Data data) {
        projectService.findProjectsByJobId(jobId)
                .thenAccept(response -> {
                    JsonNode jsonObject = Json.toJson(response);
                    websocket.tell(ResponseUtil.createResponse(jsonObject, true), self());
                });
    }

}
