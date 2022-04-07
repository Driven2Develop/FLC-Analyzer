package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import scala.concurrent.duration.Duration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Supervisor Actor class is a timer which ticks every 10s and notifies all its clients
 *
 * @author Yvonne Lee
 */
public class SupervisorActor extends AbstractActorWithTimers {
    private Set<ActorRef> userActors;

    /**
     * Method Call before creation of Actor and it starts sending Tick message
     * every 10 seconds
     *
     * @author Yvonne Lee
     */
    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Tick(), Duration.create(10, TimeUnit.SECONDS));
    }

    /**
     * Class to send Tick messages to other actors
     *
     * @author Yvonne Lee
     */
    public static final class Tick {
    }

    /**
     * Class used by other actors to register with Supervisor Actor
     *
     * @author Yvonne Lee
     */
    public static class RegisterMsg {
    }

    /**
     * Class used by other actors to de-register with Supervisor Actor
     *
     * @author Yvonne Lee
     */
    public static class DeRegister {
    }

    /**
     * Class to send messages to other Actors
     *
     * @author Yvonne Lee
     */
    public static class Data {
    }

    /**
     * Method called to create Supervisor Actor
     *
     * @return Props
     * @author Yvonne Lee
     */
    static public Props getProps() {
        return Props.create(SupervisorActor.class, SupervisorActor::new);
    }

    /**
     * Supervisor Actor constructor
     *
     * @author Yvonne Lee
     */
    private SupervisorActor() {
        this.userActors = new HashSet<>();
    }

    /**
     * The method called when message is received from other actors
     * and adds the registered actor into set of actors
     *
     * @return Receive
     * @author Yvonne Lee
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RegisterMsg.class, msg -> userActors.add(sender()))
                .match(Tick.class, msg -> notifyClients())
                .match(DeRegister.class, msg -> userActors.remove(sender()))
                .build();
    }

    /**
     * The method to notify all the registered Actors
     *
     * @author Yvonne Lee
     */
    private void notifyClients() {
        userActors.forEach(ar -> ar.tell(new Data(), self()));
    }

}
