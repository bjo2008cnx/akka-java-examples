package org.lightfw.hello;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

  public static void main(String[] args) {
    akka.Main.main(new String[] { HelloWorld.class.getName() });
  }

  public static void main2(String[] args) {
    ActorSystem system = ActorSystem.create("Hello");
    ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
    System.out.println(a.path());
  }
}