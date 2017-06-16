package org.lightfw.hello;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor {

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object msg) throws InterruptedException {
        if (msg == Msg.GREET) {
            System.out.println("GREET IS RECEIVED!");
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }

}