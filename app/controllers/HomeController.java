package controllers;

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
import views.html.user;
import views.html.userproject;

import java.util.List;
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

        actorSystem.actorOf(SupervisorActor.getProps(),"supervisorActor");

    }

    /**
     * Rendering main page
     *
     * @return index.scala.html page
     * @author Mengqi Liu
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * Search latest ten projects by keyword and present result to view asynchronously.
     *
     * @param searchTerms search text input by user
     * @return Latest ten projects searched by the input terms through calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<Result> searchLatestTenProjects(String searchTerms) {
        CompletionStage<List<Project>> searchProjectsResponse = projectService.searchLatestTenProjects(searchTerms);
        return searchProjectsResponse.thenApplyAsync(projects -> ok(views.html.project.render(projects)), httpExecutionContext.current());
    }

    /**
     * Search latest ten projects by jobId and present result to view asynchronously.
     *
     * @param jobId id for the skill defined in Freelancer API
     * @return Latest ten projects searched by the skill id through calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<Result> findProjectsByJobId(long jobId) {
        CompletionStage<List<Project>> searchProjectsResponse = projectService.findProjectsByJobId(jobId);
        return searchProjectsResponse.thenApplyAsync(projects -> ok(views.html.project.render(projects)), httpExecutionContext.current());
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

    public WebSocket wsFindUserProjects() {
        return WebSocket.Json.accept(request -> ActorFlow.actorRef(ws -> UserProjectSearchActor.props(ws, projectService), actorSystem, materializer));
    }
}
