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
 * Project controller for searching projects by skill id.
 *
 * @author Mengqi Liu
 */
public class ProjectController extends Controller {

    private ProjectService projectService;
    private HttpExecutionContext httpExecutionContext;

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public ProjectController(ProjectService projectService,
                             HttpExecutionContext httpExecutionContext) {
        this.projectService = projectService;
        this.httpExecutionContext = httpExecutionContext;
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
