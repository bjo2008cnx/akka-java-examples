package org.lightfw.watch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * 监听示例,WatchActor监听myWork
 */
public class Main {

  public static void main(String[] args) {
    //创建ActorSystem。一般来说，一个系统只需要一个ActorSystem。
    //参数1：系统名称。参数2：配置文件
    ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("akka.config"));
    ActorRef myWork = system.actorOf(Props.create(MyWork.class), "MyWork");
    system.actorOf(Props.create(WatchActor.class, myWork), "WatchActor"); //WatchActor监听myWork

    myWork.tell(MyWork.Msg.WORKING, ActorRef.noSender());
    myWork.tell(MyWork.Msg.DONE, ActorRef.noSender());

    //中断myWork
    myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());
  }
}