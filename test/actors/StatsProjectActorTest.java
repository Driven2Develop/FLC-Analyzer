package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Stats;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import services.StatsService;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit test for StatsProjectActor class
 *
 * @author Bowen Cheng
 */
@RunWith(MockitoJUnitRunner.class)
public class StatsProjectActorTest {

    private static ActorSystem system;

    @Mock
    private StatsService statsService;

    private static final List<Stats> TEST_STATS = List.of(
            new Stats("word1", 5),
            new Stats("word2", 6),
            new Stats("word3", 8)
    );

    /**
     * Setup StatsProjectActor initial state
     *
     * @author Bowen Cheng
     */
    @Before
    public void setup() {
        system = ActorSystem.create();
        when(statsService.getProjectStats(anyString())).thenReturn(TEST_STATS);
    }

    /**
     * Main test method for StatsProjectActor
     *
     * @author Bowen Cheng
     */
    @Test
    public void testGetProjectStats() {
        new TestKit(system) {
            {
                final Props props = StatsProjectActor.props(getTestActor(), statsService);
                final ActorRef subject = system.actorOf(props);
                within(
                        Duration.ofSeconds(10),
                        () -> {
                            ObjectNode testData = Json.newObject();
                            testData.put("projectDesc", "random text");
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
     * @author Bowen Cheng
     */
    @After
    public void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * validate received project data
     *
     * @author Bowen Cheng
     */
    private void validateData(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List stats = mapper.convertValue(node.get("data"), List.class);
        assertFalse(stats.isEmpty());
        assertEquals("word1", ((LinkedHashMap<?, ?>) stats.get(0)).get("word"));
        assertEquals(5, ((LinkedHashMap<?, ?>) stats.get(0)).get("occurrence"));
    }
}
