package controllers;

import com.google.inject.Inject;
import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectService;

import java.util.List;

public class ProjectController extends Controller {

    private ProjectService projectService;

    @Inject
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Result findProjectsByJobId(long skillId) throws Exception {
        List<Project> projects = projectService.findProjectsByJobId(skillId);
        return ok(views.html.project.render(projects));
    }
}
