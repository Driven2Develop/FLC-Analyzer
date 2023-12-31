package controllers;

import actors.*;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.google.inject.Inject;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.streams.ActorFlow;
import play.libs.ws.WSBodyReadables;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import services.ProjectService;
import services.StatsService;
import services.UserService;
import views.html.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Home controller for rendering main page and project search results.
 *
 * @author Mengqi Liu
 * @author Bowen Cheng
 * @author Yvonne Lee
 */
public class HomeController extends Controller implements WSBodyReadables {
    private ProjectService projectService;
    private StatsService statsService;
    private UserService userService;
    private HttpExecutionContext httpExecutionContext;
    private ActorSystem actorSystem;
    private Materializer materializer;

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     * @author Yvonne Lee
     */
    @Inject
    public HomeController(ProjectService projectService,
                          StatsService statsService,
                          UserService userService,
                          HttpExecutionContext httpExecutionContext,
                          ActorSystem actorSystem,
                          Materializer materializer) {
        this.projectService = projectService;
        this.statsService = statsService;
        this.userService = userService;
        this.httpExecutionContext = httpExecutionContext;
        this.actorSystem = actorSystem;
        this.materializer = materializer;

        actorSystem.actorOf(SupervisorActor.getProps(), "supervisorActor");

    }

    /**
     * Rendering main page
     *
     * @return index.scala.html page
     * @author Mengqi Liu
     * @author Yvonne Lee
     */
    public Result index(Http.Request request) {
        return ok(index.render(request));
    }

    /**
     * Search latest ten projects by keyword and present result to view asynchronously.
     *
     * @param searchTerms search text input by user
     * @return Latest ten projects searched by the input terms through calling Freelancer API
     * @author Mengqi Liu
     * @author Yvonne Lee
     */
    public Result searchLatestTenProjects(String searchTerms, Http.Request request) {
        return ok(projectsearch.render(request));
    }

    /**
     * Websocket to search latest 10 projects
     *
     * @return Websocket object
     * @author Yvonne Lee
     */
    public WebSocket wsSearchProjects() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> ProjectSearchActor.props(ws, projectService), actorSystem, materializer));
    }


    /**
     * Search latest ten projects by jobId and present result to view asynchronously.
     *
     * @param jobId id for the skill defined in Freelancer API
     * @return Latest ten projects searched by the skill id through calling Freelancer API
     * @author Mengqi Liu
     */
    public Result findProjectsByJobId(long jobId, Http.Request request) {
        return ok(project.render(request));
    }

    /**
     * Web socket to get projects by job id
     *
     * @return WebSocket object
     * @author Mengqi Liu
     */
    public WebSocket wsFindProjectsByJobId() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> JobProjectSearchActor.props(ws, projectService), actorSystem, materializer));
    }

    /**
     * Gets global stats and presents result to view
     *
     * @param searchTerm search text
     * @return list of stats
     * @author Bowen Cheng
     */
    public Result getGlobalStats(String searchTerm, Http.Request request) {
        return ok(statsglobal.render(request));
    }

    /**
     * Web socket to get global stats
     *
     * @return Websocket object
     * @author Bowen Cheng
     */
    public WebSocket wsGetGlobalStats() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> StatsGlobalActor.props(ws, statsService), actorSystem, materializer));
    }

    /**
     * Gets project stats and presents result to view
     *
     * @param projectDesc project description to get stats from
     * @return list of stats
     * @author Bowen Cheng
     */
    public Result getProjectStats(String projectDesc, Http.Request request) {
        return ok(statsproject.render(request));

    }

    /**
     * Web socket to get project stats
     *
     * @return Websocket object
     * @author Bowen Cheng
     */
    public WebSocket wsGetProjectStats() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> StatsProjectActor.props(ws, statsService), actorSystem, materializer));
    }

    /**
     * Gets readability and presents result to view
     *
     * @return readability object based on project description
     * @author Iymen Abdella
     */

    public Result computeProjectReadability(String searchTerms, Http.Request request) {
        return ok(Readability.render(request));
    }

    public WebSocket wsComputeProjectReadability() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> DescriptionReadabilityActor.props(ws, projectService), actorSystem, materializer));
    }

    /**
     * Gets average Flesch Readability index and presents result to view
     *
     * @return list of readability objects averaged out
     * @author Iymen Abdella
     */
    public Result getAverageReadability(String searchTerms, Http.Request request) {
        return ok(AverageReadability.render(request));
    }

    public WebSocket wsAverageReadability() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> AverageReadabilityActor.props(ws, projectService), actorSystem, materializer));
    }

    /**
     * Gets user by ID and presents result to view
     *
     * @param userId user ID
     * @return user User
     * @author Yvonne Lee
     */
    public Result userProfile(String userId, Http.Request request) {
        return ok(user.render(request));
    }

    /**
     * Web socket to get user
     *
     * @return WebSocket object
     * @author Yvonne Lee
     */
    public WebSocket wsFindUser() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> UserSearchActor.props(ws, userService), actorSystem, materializer));
    }

    /**
     * Gets user projects by ID and presents result to view
     *
     * @param userId user ID
     * @return user User
     * @author Yvonne Lee
     */
    public Result findUserProjectsById(String userId, Http.Request request) {
        return ok(userproject.render(request));
    }

    /**
     * Web socket to find projects by user
     *
     * @return WebSocket object
     * @author Yvonne Lee
     */
    public WebSocket wsFindUserProjects() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> UserProjectSearchActor.props(ws, projectService), actorSystem, materializer));
    }
}
