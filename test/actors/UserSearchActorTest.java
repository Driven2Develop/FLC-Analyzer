package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.libs.Json;
import services.UserService;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for UserSearchActor class
 *
 * @author Yvonne Lee
 */
@RunWith(MockitoJUnitRunner.class)
public class UserSearchActorTest {

    private static ActorSystem system;

    @Mock
    private UserService userService;

    private static String DEFAULT_USER_ID = "1";
    private static String DEFAULT_USERNAME = "john_doe";
    private static String DEFAULT_USER_DISPLAY_NAME = "John Doe";

    private static final CompletionStage<User> DEFAULT_USER = CompletableFuture.completedStage(buildDefaultUser());

    /**
     * Setup UserSearchActor initial state
     *
     * @author Yvonne Lee
     */
    @Before
    public void setup() {
        system = ActorSystem.create();
        when(userService.findUserById(Long.parseLong(DEFAULT_USER_ID))).thenReturn(DEFAULT_USER);
    }

    /**
     * Main test method for UserSearchActor
     *
     * @author Yvonne Lee
     */
    @Test
    public void testUserSearchActor() {
        new TestKit(system) {
            {
                final Props props = UserSearchActor.props(getTestActor(), userService);
                final ActorRef subject = system.actorOf(props);
                within(
                        Duration.ofSeconds(10),
                        () -> {
                            subject.tell(new SupervisorActor.Data(), getRef());
                            expectNoMsg();

                            ObjectNode testData = Json.newObject();
                            testData.put("keyword", DEFAULT_USER_ID);
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
     * static method to build object default <code>User</code>
     *
     * @author Yvonne Lee
     */
    private static User buildDefaultUser() {
        User user = new User();
        user.setId(Long.parseLong(DEFAULT_USER_ID));
        user.setUsername(DEFAULT_USERNAME);
        user.setDisplayName(DEFAULT_USER_DISPLAY_NAME);

        return user;
    }

    /**
     * validate received user data
     *
     * @author Yvonne Lee
     */
    private void validateData(ObjectNode node) {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(node.get("data"), User.class);
        assertEquals(Long.parseLong(DEFAULT_USER_ID), user.getId());
        assertEquals(DEFAULT_USERNAME, user.getUsername());
        assertEquals(DEFAULT_USER_DISPLAY_NAME, user.getDisplayName());
    }
}
