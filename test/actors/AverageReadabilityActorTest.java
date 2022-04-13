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
import models.Readability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import services.ProjectService;
import services.TestData;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static services.TestData.*;

/**
 * Unit test for AverageReadabilityActor class
 *
 * @author Iymen Abdella
 */
@RunWith(MockitoJUnitRunner.class)
public class AverageReadabilityActorTest {

    private static ActorSystem system;

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
        //when(projectService.getAverageReadability(DEFAULT_SEARCH_TERM)).thenReturn(DEFAULT_PROJECTS);
        Mockito.when(projectService.getAverageReadability(DEFAULT_SEARCH_TERM)).thenAnswer(invocationOnMock -> DEFAULT_PROJECTS);
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
                            validateData(node);
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
     *
     * @author Iymen Abdella
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

    /**
     * validate received project data
     *
     * @author Iymen Abdella
     */
    private void validateData(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List readability = mapper.convertValue(node.get("data"), List.class);
        Map map = (Map) readability.get(1);
        assertFalse(readability.isEmpty());
        assertEquals(29L, ((LinkedHashMap<?, ?>) map.get("readability")).get("score"));
        assertEquals("College Graduate", ((LinkedHashMap<?, ?>) map.get("readability")).get("education_level"));
    }
}
