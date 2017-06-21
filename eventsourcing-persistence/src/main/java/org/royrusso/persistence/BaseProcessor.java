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

package org.royrusso.persistence;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedEventsourcedProcessor;
import org.royrusso.event.Command;
import org.royrusso.event.Event;

import java.util.UUID;

/**
 * 接收消息，新建事件并持久化 并更新processor的状态,允许从日志和snapshot中进行恢复.
 *
 * @author royrusso
 */
public class BaseProcessor extends UntypedEventsourcedProcessor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * 状态
     */
    private ProcessorState processorState = new ProcessorState();

    /**
     * 重启时被调用. 加载snapshot 并重放
     *
     * @param msg
     */
    public void onReceiveRecover(Object msg) {
        log.info("接收到恢复指令: " + msg);
        if (msg instanceof Event) {
            log.info("msg is Event");
            processorState.update((Event) msg);

        } else if (msg instanceof SnapshotOffer) {
            log.info("msg is SnapshotOffer");
            processorState = (ProcessorState) ((SnapshotOffer) msg).snapshot();
        }
    }

    /**
     * Called on Command dispatch
     *
     * @param msg
     */
    public void onReceiveCommand(Object msg) {
        log.info("接收到消息: " + msg);
        if (msg instanceof Command) {
            final String data = ((Command) msg).getData();

            final Event event = new Event(data, UUID.randomUUID().toString());

            // 持久化并更新状态
            persist(event, new Procedure<Event>() {
                public void apply(Event evt) throws Exception {
                    processorState.update(evt);
                    // broadcast event on eventstream
                    getContext().system().eventStream().publish(evt);
                }
            });
        } else if (msg.equals("snapshot")) {
            log.info("保存快照 ");
            saveSnapshot(processorState.copy());
        } else if (msg.equals("printstate")) {
            log.info("打印状态");
            log.info(processorState.toString());
        }
    }
}
