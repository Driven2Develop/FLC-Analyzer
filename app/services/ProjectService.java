package services;

import com.google.inject.Inject;
import models.Project;
import wsclient.MyWSClient;

import java.util.List;
import java.util.concurrent.CompletionStage;


/**
 * Service layer class for processing projects
 *
 * @author Mengqi Liu
 * @author Yvonne Lee
 */
public class ProjectService {

    private MyWSClient myWSClient;
    private static final String PROJECT_SEARCH_URL = "/projects/0.1/projects/all/?offset=0&limit=10&sort_field=time_submitted&job_details=true";
    private static final String PROJECT_LIST_URL = "/projects/0.1/projects?limit=10";

    /**
     * Constructor for DI
     *
     * @author Mengqi Liu
     */
    @Inject
    public ProjectService(MyWSClient myWSClient) {
        this.myWSClient = myWSClient;
    }

    /**
     * Search latest projects by search terms calling Freelancer API<br/>
     * Implemented using Stream API
     *
     * @param searchTerms the terms user inputs for searching related projects
     * @return Latest ten projects searched by the search terms calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<List<Project>> searchLatestTenProjects(String searchTerms) {
        return this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&query=" + searchTerms.trim().replace(" ", "%20"))
                .getListResults(Project.class, "projects");
    }

    /**
     * Search latest projects by jobId calling Freelancer API<br/>
     * Implemented using Stream API
     *
     * @param jobId id for one job defined in Freelancer API
     * @return Latest ten projects searched by the job id calling Freelancer API
     * @author Mengqi Liu
     */
    public CompletionStage<List<Project>> findProjectsByJobId(long jobId) {
        return this.myWSClient.initRequest(PROJECT_SEARCH_URL + "&jobs[]=" + jobId).getListResults(Project.class, "projects");
    }

    /**
     * Get projects by owner ID<br>
     * Implemented using Stream API
     *
     * @param ownerId owner ID to retrieve projects from
     * @return List of projects
     * @author Yvonne Lee
     */
    public CompletionStage<List<Project>> findProjectsByOwnerId(long ownerId) {
        return this.myWSClient.initRequest(PROJECT_LIST_URL + "&owners[]=" + ownerId).getListResults(Project.class, "projects");
    }
}
