/**
  * (C) Copyright 2014 Roy Russo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  */

package org.royrusso.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.persistence.Persistent;
import org.royrusso.actor.BaseProcessor;
import org.royrusso.actor.Receiver;
import org.royrusso.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示场景：处理器发送命令，接收者接收到消息后进行处理并且对发送方发送消息确认表明已经成功收到消息。
 * 如果没有发送确认则表明该消息没有被接收并正确处理。失败消息会到达死信箱，系统下次启动时后继续发送死信箱中的发送失败的消息。
 */
public class System {

    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("channel-system");

        Thread.sleep(2000);

        final ActorRef receiver = actorSystem.actorOf(Props.create(Receiver.class));
        final ActorRef processor = actorSystem.actorOf(Props.create(BaseProcessor.class, receiver), "channel-processor");

        //发送消息
        for (int i = 0; i < 10; i++) {
            processor.tell(Persistent.create(new Command("CMD " + i)), null);
        }

        Thread.sleep(2000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
