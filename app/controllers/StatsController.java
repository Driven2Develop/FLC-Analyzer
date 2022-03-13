package controllers;

import com.google.inject.Inject;
import models.Stats;
import play.mvc.Controller;
import play.mvc.Result;
import services.StatsService;

import java.util.List;

/**
 * Stats controller for retrieving global and project level stats
 */
public class StatsController extends Controller {

    private StatsService statsService;

    /**
     * Constructor for DI
     *
     * @author Bowen Cheng
     */
    @Inject
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Gets global stats and presents result to view
     *
     * @param searchTerms search text
     * @return list of stats
     * @throws Exception exception
     * @author Bowen Cheng
     */
    public Result getGlobalStats(String searchTerms) throws Exception {
        List<Stats> stats = statsService.getGlobalStats(searchTerms.trim().replace(" ", "%20"));
        return ok(views.html.Stats.render(stats));
    }

    /**
     * Gets global stats and presents result to view
     *
     * @param projectDesc project description to get stats from
     * @return list of stats
     * @author Bowen Cheng
     */
    public Result getProjectStats(String projectDesc) {
        List<Stats> stats = statsService.getProjectStats(projectDesc);
        return ok(views.html.Stats.render(stats));
    }

}
