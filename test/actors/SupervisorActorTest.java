package actors;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import actors.SupervisorActor.Data;
import actors.SupervisorActor.Tick;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;

/**
 * Unit test for SupervisorActor class
 *
 * @author Yvonne Lee
 */
public class SupervisorActorTest {

    public static ActorSystem system;

    @Before
    public void setup() {
        system = ActorSystem.create();
    }

    @Test
    public void testSupervisorActor() {
        new TestKit(system) {
            {
                final Props props = SupervisorActor.getProps();
                final ActorRef subject = system.actorOf(props);

                within(
                        Duration.ofSeconds(10),
                        () -> {
                            subject.tell(new SupervisorActor.RegisterMsg(), getRef());
                            expectNoMsg();

                            subject.tell(new SupervisorActor.Tick(), getRef());
                            expectMsgClass(Data.class);

                            subject.tell(new SupervisorActor.DeRegister(), getRef());
                            expectNoMsg();

                            return null;
                        });
            }
        };
    }

    @After
    public void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

}
