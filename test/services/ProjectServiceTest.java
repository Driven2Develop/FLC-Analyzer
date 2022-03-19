package services;

import models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.util.HashMap;
import java.util.List;

import static helpers.DateUtil.parseDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static services.TestData.DEFAULT_PROJECT_TYPE;
import static services.TestData.DEFAULT_USER_ID;
import static services.TestData.FUTURE_PROJECTS;
import static services.TestData.TEST_JOBS_1;
import static services.TestData.TEST_JOBS_2;
import static services.TestData.TEST_SUBMITTIME_1;
import static services.TestData.TEST_SUBMITTIME_2;
import static services.TestData.TEST_TITLE_1;
import static services.TestData.TEST_TITLE_2;

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

    private static final String SEARCH_TERMS_JAVA = "Java";
    private static final long SEARCH_JOB_ID_JAVA = 7L;

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
        when(myWSClient.getListResults(new HashMap(), SEARCH_TERMS_JAVA, Project.class, "projects")).thenReturn(FUTURE_PROJECTS);
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
        when(myWSClient.getListResults(new HashMap(), SEARCH_JOB_ID_JAVA, Project.class, "projects")).thenReturn(FUTURE_PROJECTS);
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
        when(myWSClient.getListResults(new HashMap(), DEFAULT_USER_ID, Project.class, "projects")).thenReturn(FUTURE_PROJECTS);
        List<Project> projects = projectService.findProjectsByOwnerId(DEFAULT_USER_ID).toCompletableFuture().get();
        validateData(projects);
    }

    /**
     * prepare data and mock objects before calling ProjectService methods
     *
     * @author Mengqi Liu
     */
    private void prepareData() {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
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
        List<Long> jobIdList = List.of(7L, 8L, 9L, 10L);
        assertThat(projects).extracting(Project::getJobs).anyMatch(jobs -> jobs.stream().filter(job -> jobIdList.contains(job.getId())).findAny().isPresent());
        List<String> jobNameList = List.of("Java", "JavaScript");
        assertThat(projects).extracting(Project::getJobs).anyMatch(jobs -> jobs.stream().filter(job -> jobNameList.contains(job.getName())).findAny().isPresent());
    }
}
