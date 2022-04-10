package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Job;
import models.Project;
import models.Readability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import services.ProjectService;
import services.TestData;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static services.TestData.*;
import static services.TestData.TEST_JOBS_2;

/**
 * Unit test for DescriptionReadabilityActor class
 *
 * @author Iymen Abdella
 */
@RunWith(MockitoJUnitRunner.class)

public class DescriptionReadabilityActorTest {


    private static ActorSystem system;

    // import data from TestData
    @Mock
    private ProjectService projectService;
    private static String DEFAULT_USER_ID = "1";
    private static final String DEFAULT_SEARCH_TERM = "Java";
    public static final Project TEST_PROJECT_1 = buildProject(TEST_TITLE_1, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE, TEST_JOBS_1, TestData.DEFAULT_READABILITY_1, TestData.DEFAULT_TEST_PREVIEW_DESCRIPTION_1);
    public static final Project TEST_PROJECT_2 = buildProject(TEST_TITLE_2, Long.parseLong(DEFAULT_USER_ID), DEFAULT_PROJECT_TYPE, TEST_JOBS_2, TestData.DEFAULT_READABILITY_2, TestData.DEFAULT_TEST_PREVIEW_DESCRIPTION_2);
    public static final CompletionStage<List<Project>> DEFAULT_PROJECTS = CompletableFuture.completedStage(List.of(TEST_PROJECT_1, TEST_PROJECT_2));

    /**
     * Setup AverageReadabilityActor startup state
     *
     * @author Iymen Abdella
     */
    @Before
    public void setup() {
        system = ActorSystem.create();
        doReturn(TEST_PROJECT_1.getReadability().getScore()).when(projectService.computeProjectReadability(TEST_PROJECT_1.getPreviewDescription()));
    }

    /**
     * Main test method for AverageReadabilityActor
     *
     * @author Iymen Abdella
     */
    @Test
    public void testAverageReadabilityActor() {
        new TestKit(system) {
            {
                final Props props = AverageReadabilityActor.props(getTestActor(), projectService);
                final ActorRef subject = system.actorOf(props);
                within(
                        Duration.ofSeconds(10),
                        () -> {
                            ObjectNode testData = Json.newObject();
                            testData.put("keyword", DEFAULT_SEARCH_TERM);
                            subject.tell(testData, getRef());
                            subject.tell(new SupervisorActor.Data(), getRef());

                            ObjectNode node = expectMsgClass(ObjectNode.class);
                            assertNotNull(node);
                            return null;
                        });
            }

        };

    }

    /**
     * shut down actor system after tests complete
     *
     * @author Iymen Abdella
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
    public static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs, Readability r, String s) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        project.setReadability(r);
        project.setPreviewDescription(s);
        return project;
    }
}
