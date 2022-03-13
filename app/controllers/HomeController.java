package controllers;

import com.google.inject.Inject;
import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectService;

import java.util.List;

/**
 * Home controller for rendering main page and project search results.
 *
 * @author Mengqi Liu
 */
public class HomeController extends Controller {

    private ProjectService projectService;

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
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
     * Search latest projects by keyword and present result to view
     *
     * @param searchTerms search text
     * @return Latest ten projects searched by the input terms calling Freelancer API
     * @throws Exception exception
     * @author Mengqi Liu
     */
    public Result searchLatestTenProjects(String searchTerms) throws Exception {
        List<Project> projects = projectService.searchLatestTenProjects(searchTerms.trim().replace(" ", "%20"));
        return ok(views.html.project.render(projects));
    }

}
