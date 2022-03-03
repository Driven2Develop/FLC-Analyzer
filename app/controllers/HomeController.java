package controllers;

import com.google.inject.Inject;
import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectService;

import java.util.List;

public class HomeController extends Controller {

    private ProjectService projectService;

    @Inject
    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result searchLatestTenProjects(String searchTerms) throws Exception {
        List<Project> projects = projectService.searchLatestTenProjects(searchTerms.trim().replace(" ", "%20"));
        return ok(views.html.project.render(projects));
    }

}
