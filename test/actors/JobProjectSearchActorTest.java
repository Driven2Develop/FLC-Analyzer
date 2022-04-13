package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for JobProjectSearchActor class
 *
 * @author Mengqi Liu
 */
@RunWith(MockitoJUnitRunner.class)
public class JobProjectSearchActorTest {

    private static ActorSystem system;

    @Mock
    private ProjectService projectService;

    private static String DEFAULT_JOB_ID = "333";
    private static String DEFAULT_JOB_NAME = "JAVA";
    private static String DEFAULT_OWNER_ID = "1";
    private static String DEFAULT_TITLE = "Default Title";
    private static String DEFAULT_TYPE = "fixed";

    private static final CompletionStage<List<Project>> DEFAULT_PROJECTS = CompletableFuture.completedStage(buildDefaultProjects());

    /**
     * Setup JobProjectSearchActor initial state
     *
     * @author Mengqi Liu
     */
    @Before
    public void setup() {
        system = ActorSystem.create();
        when(projectService.findProjectsByJobId(Long.parseLong(DEFAULT_JOB_ID))).thenReturn(DEFAULT_PROJECTS);
    }

    /**
     * Main test method for JobProjectSearchActor
     *
     * @author Mengqi Liu
     */
    @Test
    public void testJobProjectSearchActor() {
        new TestKit(system) {
            {
                final Props props = JobProjectSearchActor.props(getTestActor(), projectService);
                final ActorRef subject = system.actorOf(props);
                within(
                        Duration.ofSeconds(10),
                        () -> {
                            ObjectNode testData = Json.newObject();
                            testData.put("jobId", DEFAULT_JOB_ID);
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
     * @author Mengqi Liu
     */
    @After
    public void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * static method to build object default <code>Project</code>
     *
     * @author Mengqi Liu
     */
    private static List<Project> buildDefaultProjects() {
        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setOwnerId(Long.parseLong(DEFAULT_OWNER_ID));
        project.setTitle(DEFAULT_TITLE);
        project.setType(DEFAULT_TYPE);
        project.setJobs(Arrays.asList(new Job(Long.parseLong(DEFAULT_JOB_ID), DEFAULT_JOB_NAME)));
        projects.add(project);
        return projects;
    }

    /**
     * validate received project data
     *
     * @author Mengqi Liu
     */
    private void validateData(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List projects = mapper.convertValue(node.get("data"), List.class);
        assertEquals(Long.parseLong(DEFAULT_OWNER_ID), ((LinkedHashMap) projects.get(0)).get("owner_id"));
        assertEquals(DEFAULT_TITLE, ((LinkedHashMap) projects.get(0)).get("title"));
        assertEquals(DEFAULT_TYPE, ((LinkedHashMap) projects.get(0)).get("type"));
    }
}
