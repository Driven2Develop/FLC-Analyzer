package services;

import models.Job;
import models.Project;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * helper class to provide data for other unit tests
 *
 * @author Mengqi Liu
 */
public class TestData {

    public static final long DEFAULT_USER_ID = 1L;
    public static final String TEST_TITLE_1 = "Test 1";
    public static final String TEST_TITLE_2 = "Test 2";
    public static final List<Job> TEST_JOBS_1 = List.of(new Job(7, "Java"), new Job(8, "JavaScript"));
    public static final List<Job> TEST_JOBS_2 = List.of(new Job(9, "Java"), new Job(10, "JavaScript"));
    public static final long TEST_SUBMITTIME_1 = 1647003600L;
    public static final long TEST_SUBMITTIME_2 = 1646139600L;
    public static final String DEFAULT_PROJECT_TYPE = "Default Project Type";
    public static final CompletionStage<List<Project>> FUTURE_PROJECTS = CompletableFuture.completedStage(List.of(
            buildProject(TEST_TITLE_1, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_1, TEST_SUBMITTIME_1),
            buildProject(TEST_TITLE_2, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_2, TEST_SUBMITTIME_2)
    ));

    /**
     * static method to build object <code>Project</code>
     *
     * @author Mengqi Liu
     */
    public static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs, long time) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        project.setTimeSubmitted(time);
        return project;
    }
}
