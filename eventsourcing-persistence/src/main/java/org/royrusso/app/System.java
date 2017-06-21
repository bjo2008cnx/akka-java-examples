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
import org.royrusso.event.Command;
import org.royrusso.event.Event;
import org.royrusso.event.EventHandler;
import org.royrusso.persistence.BaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class System {


    public static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("actor-server");

        final ActorRef handler = actorSystem.actorOf(Props.create(EventHandler.class));
        actorSystem.eventStream().subscribe(handler, Event.class);

        Thread.sleep(5000);

        final ActorRef processor = actorSystem.actorOf(Props.create(BaseProcessor.class), "eventsourcing-processor");

        processor.tell(new Command("CMD 1"), null);
        processor.tell(new Command("CMD 2"), null);
        processor.tell(new Command("CMD 3"), null);

        processor.tell("snapshot", null);

        processor.tell(new Command("CMD 4"), null);
        processor.tell(new Command("CMD 5"), null);

        processor.tell("printstate", null);

        Thread.sleep(5000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
