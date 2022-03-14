package controllers;

import com.google.inject.Inject;
import models.Project;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectService;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Home controller for rendering main page and project search results.
 *
 * @author Mengqi Liu
 */
public class HomeController extends Controller {

    private ProjectService projectService;
    private HttpExecutionContext httpExecutionContext;

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public HomeController(ProjectService projectService,
                          HttpExecutionContext httpExecutionContext) {
        this.projectService = projectService;
        this.httpExecutionContext = httpExecutionContext;
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
     * @@Ca
     * @author Mengqi Liu
     */
    public CompletionStage<Result> searchLatestTenProjects(String searchTerms) {
        CompletionStage<List<Project>> searchProjectsResponse = projectService.searchLatestTenProjects(searchTerms);
        return searchProjectsResponse.thenApplyAsync(projects -> ok(views.html.project.render(projects)), httpExecutionContext.current());
    }

    /**
     * Search latest ten projects by skillId and present result to view asynchronously.
     *
     * @param skillId id for the skill defined in Freelancer API
     * @return Latest ten projects searched by the skill id through calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<Result> findProjectsByJobId(long skillId) {
        CompletionStage<List<Project>> searchProjectsResponse = projectService.findProjectsByJobId(skillId);
        return searchProjectsResponse.thenApplyAsync(projects -> ok(views.html.project.render(projects)), httpExecutionContext.current());
    }
}
