/**
 * (C) Copyright 2014 Roy Russo
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *      http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.royrusso.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.ConfirmablePersistent;
import org.royrusso.command.ChannelReply;
import org.royrusso.command.Command;

/**
 * Receives commands that are sent via the listened-to channel and then replies with a sort of echo statement.
 *
 * @author royrusso
 */
public class Receiver extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ConfirmablePersistent) {
            log.info("接收到ConfirmablePersistent消息");
            ConfirmablePersistent confirmablePersistent = (ConfirmablePersistent) msg;

            log.info("负载： " + confirmablePersistent.payload() + " #: " + confirmablePersistent.sequenceNr());

            getSender().tell(new ChannelReply((Command) confirmablePersistent.payload(), confirmablePersistent.sequenceNr()), null);
            confirmablePersistent.confirm();
        }
    }
}
