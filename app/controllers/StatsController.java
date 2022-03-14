package controllers;

import com.google.inject.Inject;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.StatsService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Stats controller for retrieving global and project level stats
 * @author Bowen Cheng
 */
public class StatsController extends Controller {

    private StatsService statsService;
    private HttpExecutionContext httpExecutionContext;


    /**
     * Constructor for DI
     *
     * @author Bowen Cheng
     */
    @Inject
    public StatsController(StatsService statsService, HttpExecutionContext httpExecutionContext) {
        this.statsService = statsService;
        this.httpExecutionContext = httpExecutionContext;
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

}
