package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Job;
import models.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import services.ProjectService;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;
import static services.TestData.*;

/**
 * Unit test for ProjectSearchActor class
 *
 * @author Yvonne Lee
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectSearchActorTest {

    private static ActorSystem system;

    @Mock
    private ProjectService projectService;

    private static String DEFAULT_USER_ID = "1";
    private static final String DEFAULT_SEARCH_TERM = "Java";

    public static final Project TEST_PROJECT_1 = buildProject(TEST_TITLE_1, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE, TEST_JOBS_1);
    public static final Project TEST_PROJECT_2 = buildProject(TEST_TITLE_2, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE, TEST_JOBS_2);
    public static final CompletionStage<List<Project>> DEFAULT_PROJECTS = CompletableFuture.completedStage(List.of(TEST_PROJECT_1, TEST_PROJECT_2));

    /**
     * Setup ProjectSearchActor initial state
     *
     * @author Yvonne Lee
     */
    @Before
    public void setup() {
        system = ActorSystem.create();
        when(projectService.searchLatestTenProjects(DEFAULT_SEARCH_TERM)).thenReturn(DEFAULT_PROJECTS);
    }

    /**
     * Main test method for ProjectSearchActor
     *
     * @author Yvonne Lee
     */
    @Test
    public void testProjectSearchActor() {
        new TestKit(system) {
            {
                final Props props = ProjectSearchActor.props(getTestActor(), projectService);
                final ActorRef subject = system.actorOf(props);
                within(
                        Duration.ofSeconds(10),
                        () -> {
                            ObjectNode testData = Json.newObject();
                            testData.put("keyword", DEFAULT_SEARCH_TERM);
                            subject.tell(testData, getRef());
                            subject.tell(new SupervisorActor.Data(), getRef());

                            ObjectNode node = expectMsgClass(ObjectNode.class);
                            validateData(node);
                            return null;
                        });
            }

        };

    }

    /**
     * shut down actor system after tests complete
     *
     * @author Yvonne Lee
     */
    @After
    public void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * static method to build object <code>Project</code>
     *
     * @author Yvonne
     */
    public static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        return project;
    }

    /**
     * validate received project data
     *
     * @author Yvonne Lee
     */
    private void validateData(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List<Project> projects = mapper.convertValue(node.get("data"), new TypeReference<>() {});
        assertThat(projects)
                .isNotNull()
                .hasSize(2)
                .extracting(Project::getTitle, Project::getOwnerId, Project::getType)
                .containsExactlyInAnyOrder(
                        tuple(TEST_TITLE_1, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE),
                        tuple(TEST_TITLE_2, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE)
                );
        List<Long> jobIdList = List.of(7L, 8L, 9L, 10L);
        assertThat(projects).extracting(Project::getJobs).anyMatch(jobs -> jobs.stream().anyMatch(job -> jobIdList.contains(job.getId())));
        List<String> jobNameList = List.of("Java", "JavaScript");
        assertThat(projects).extracting(Project::getJobs).anyMatch(jobs -> jobs.stream().anyMatch(job -> jobNameList.contains(job.getName())));
    }
}
