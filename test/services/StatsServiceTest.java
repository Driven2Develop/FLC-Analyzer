package services;

import models.Project;
import models.Stats;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for stats service
 *
 * @author Bowen Cheng
 */
@RunWith(MockitoJUnitRunner.class)
public class StatsServiceTest {

    @InjectMocks
    private StatsService statsService;

    @Mock
    private MyWSClient myWSClient;


    private static final String SEARCH_TERM = "project";
    private static final String PROJECT_DESC_A = "This is a description of project A";
    private static final String PROJECT_DESC_B = "This is a description of project B";

    private static final CompletionStage<List<Project>> TEST_PROJECTS = CompletableFuture.completedStage(List.of(
                    buildProject(PROJECT_DESC_A),
                    buildProject(PROJECT_DESC_B)
            ));

    @Before
    public void setup() {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.getListResults(new HashMap<>(), SEARCH_TERM, Project.class, "projects")).thenReturn(TEST_PROJECTS);
    }

    @Test
    public void testGetGlobalStats() throws Exception {
        List<Stats> projects = statsService.getGlobalStats("project").toCompletableFuture().get();
        validateData(projects);
    }

    @Test
    public void testGetProjectStats() {
        List<Stats> stats = statsService.getProjectStats(PROJECT_DESC_A + " " + PROJECT_DESC_B);
        validateData(stats);
    }

    private static Project buildProject(String description) {
        Project project = new Project();
        project.setPreviewDescription(description);
        return project;
    }

    private void validateData(List<Stats> stats) {
        assertThat(stats)
                .isNotNull()
                .isNotEmpty()
                .hasSize(8)
                .extracting(Stats::getWord, Stats::getOccurrence)
                .containsExactlyInAnyOrder(
                        tuple("This", 2),
                        tuple("is", 2),
                        tuple("a", 2),
                        tuple("description", 2),
                        tuple("of", 2),
                        tuple("project", 2),
                        tuple("A", 1),
                        tuple("B", 1)
                );
    }
}
