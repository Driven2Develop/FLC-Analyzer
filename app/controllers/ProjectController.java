package controllers;

import com.google.inject.Inject;
import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectService;

import java.util.List;

/**
 * Project controller for searching projects by skill id.
 *
 * @author Mengqi Liu
 */
public class ProjectController extends Controller {

    private ProjectService projectService;

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Search latest projects by skillId and present result to view
     *
     * @param skillId id for one skill defined in Freelancer API
     * @return Latest ten projects searched by the skill id calling Freelancer API
     * @throws Exception exception
     * @author Mengqi Liu
     */
    public Result findProjectsByJobId(long skillId) throws Exception {
        List<Project> projects = projectService.findProjectsByJobId(skillId);
        return ok(views.html.project.render(projects));
    }
}
