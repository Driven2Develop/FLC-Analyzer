package services;

import models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static helpers.DateUtil.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for class <code>ProjectService</code>
 *
 * @author Mengqi Liu
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private MyWSClient myWSClient;

    private static final long DEFAULT_USER_ID = 1L;
    private static final String TEST_TITLE_1 = "Test 1";
    private static final String TEST_TITLE_2 = "Test 2";
    private static final List<Job> TEST_JOBS_1 = List.of(new Job(7, "Java"), new Job(8, "JavaScript"));
    private static final List<Job> TEST_JOBS_2 = List.of(new Job(9, "Java"), new Job(10, "JavaScript"));
    private static final long TEST_SUBMITTIME_1 = 1647003600L;
    private static final long TEST_SUBMITTIME_2 = 1646139600L;
    private static final String SEARCH_TERMS_JAVA = "Java";
    private static final long SEARCH_JOB_ID_JAVA = 7L;
    private static final String DEFAULT_PROJECT_TYPE = "Default Project Type";
    private static final CompletionStage<List<Project>> FUTURE_PROJECTS = CompletableFuture.completedStage(List.of(
            buildProject(TEST_TITLE_1, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_1, TEST_SUBMITTIME_1),
            buildProject(TEST_TITLE_2, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_2, TEST_SUBMITTIME_2)
    ));

    /**
     * Setup data for tests
     *
     * @author Yvonne Lee
     */
    @Before
    public void setup() {
        prepareData();
    }

    /**
     * test method <code>searchLatestTenProjects</code> in class <code>ProjectService</code>
     *
     * @author Mengqi Liu
     */
    @Test
    public void testSearchLatestTenProjects() throws Exception {
        List<Project> projects = projectService.searchLatestTenProjects(SEARCH_TERMS_JAVA).toCompletableFuture().get();
        validateData(projects);
    }

    /**
     * test method <code>findProjectsByJobId</code> in class <code>ProjectService</code>
     *
     * @author Mengqi Liu
     */
    @Test
    public void testFindProjectsByJobId() throws Exception {
        List<Project> projects = projectService.findProjectsByJobId(SEARCH_JOB_ID_JAVA).toCompletableFuture().get();
        validateData(projects);
    }

    /**
     * test method <code>findProjectsByOwnerId</code> in class <code>ProjectService</code>
     *
     * @author Yvonne Lee
     */
    @Test
    public void findProjectsByOwnerId() throws Exception {
        List<Project> projects = projectService.findProjectsByOwnerId(DEFAULT_USER_ID).toCompletableFuture().get();
        validateData(projects);
    }

    /**
     * static method to build object <code>Project</code>
     *
     * @author Mengqi Liu
     */
    private static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs, long time) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        project.setTimeSubmitted(time);
        return project;
    }

    /**
     * prepare data and mock objects before calling ProjectService methods
     *
     * @author Mengqi Liu
     */
    private void prepareData() {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.getListResults(Project.class, "projects")).thenReturn(FUTURE_PROJECTS);
    }

    /**
     * validate the data returned from ProjectService methods
     *
     * @author Mengqi Liu
     */
    private void validateData(List<Project> projects) {
        assertThat(projects)
                .isNotNull()
                .hasSize(2)
                .extracting(Project::getTitle, Project::getOwnerId, Project::getType, Project::getJobs, Project::getTimeSubmitted)
                .containsExactlyInAnyOrder(
                        tuple(TEST_TITLE_1, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_1, parseDate(TEST_SUBMITTIME_1)),
                        tuple(TEST_TITLE_2, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, TEST_JOBS_2, parseDate(TEST_SUBMITTIME_2))
                );
    }
}
