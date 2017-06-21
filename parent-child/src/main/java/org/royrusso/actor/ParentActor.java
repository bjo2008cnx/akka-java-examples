
package org.royrusso.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.royrusso.command.Command;
import org.royrusso.event.Event;

import java.util.UUID;

/**
 * 转发消息给child
 *
 * @author royrusso
 */
public class ParentActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef childActor;
    private final ActorRef childActor2;

    public ParentActor() {
        childActor = getContext().actorOf(Props.create(ChildActor.class), "child-actor");
        childActor2 = getContext().actorOf(Props.create(Child2Actor.class), "child-actor-2");
    }

    @Override
    public void onReceive(Object msg) throws Exception {

        log.info("父::收到消息: " + msg);

        if (msg instanceof Command) {
            Command command = (Command) msg;
            final String data = command.getData();
            final Event event = new Event(data, UUID.randomUUID().toString());

            if (command.getType() == 1) {
                childActor.tell(event, getSelf());
            } else if (command.getType() == 2) {
                childActor2.tell(event, getSelf());
            }
        } else if (msg.equals("echo")) {
            log.info("ECHO!");
        }
    }
}
