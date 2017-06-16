package org.lightfw.hello;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class HelloWorld extends UntypedActor {

    @Override
    public void preStart() {
        // create the greeter actor
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        // tell it to perform the greeting
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        System.out.println("sender: "+getSender().toString());
        if (msg == Greeter.Msg.DONE) {
            System.out.println("DONE is received.");
            // when the greeter is done, stop this actor and with it the application
            getContext().stop(getSelf());
        } else {
            unhandled(msg);
        }
    }
}