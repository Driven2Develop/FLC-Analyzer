package controllers;

import actors.JobProjectSearchActor;
import actors.ProjectSearchActor;
import actors.SupervisorActor;
import actors.UserProjectSearchActor;
import actors.UserSearchActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.google.inject.Inject;
import models.Project;
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
import views.html.index;
import views.html.projectsearch;
import views.html.user;
import views.html.userproject;
import views.html.project;

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
     * @param searchTerms search text
     * @return list of stats
     * @author Bowen Cheng
     */
    public CompletionStage<Result> getGlobalStats(String searchTerms) {
        return statsService.getGlobalStats(searchTerms)
                .thenApplyAsync(stats -> ok(views.html.Stats.render(stats)), httpExecutionContext.current());
    }

    /**
     * Gets global stats and presents result to view
     *
     * @param projectDesc project description to get stats from
     * @return list of stats
     * @author Bowen Cheng
     */
    public CompletionStage<Result> getProjectStats(String projectDesc) {
        return CompletableFuture
                .supplyAsync(() -> statsService.getProjectStats(projectDesc))
                .thenApplyAsync((stats) -> ok(views.html.Stats.render(stats)));
    }

    /**
     * Gets readability and presents result to view
     *
     * @param projectDesc project description to get stats from
     * @return readability obejct based on project description
     * @author Iymen Abdella
     */
    public CompletionStage<Result> computeProjectReadability(String projectDesc) {

        return CompletableFuture
                .supplyAsync(() -> projectService.computeProjectReadability(projectDesc))
                .thenApplyAsync(readability -> ok(views.html.Readability.render(readability)), httpExecutionContext.current());
    }
    /**
     * Gets average Flesch Readability index and presents result to view
     *
     * @param searchTerms text being searched
     * @return list of readability objects averaged out
     * @author Iymen Abdella
     */
//    public CompletionStage<Result> getAverageReadability(String searchTerms) {
//        return CompletableFuture
//                .supplyAsync(() -> projectService.getAverageReadability(searchTerms))
//                .thenApplyAsync(readability -> ok(views.html.Readability.render(readability.join())), httpExecutionContext.current());
//    }

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
