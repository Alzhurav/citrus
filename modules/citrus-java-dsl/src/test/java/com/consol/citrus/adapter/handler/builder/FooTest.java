/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.adapter.handler.builder;

import com.consol.citrus.dsl.CitrusTestBuilder;
import com.consol.citrus.message.MessageType;
import org.springframework.stereotype.Component;

@Component("FooTest")
public class FooTest extends CitrusTestBuilder {

    @Override
    public void configure() {
        receive("inboundRequestReceiver")
                .messageType(MessageType.PLAINTEXT)
                .payload("<Test name=\"FooTest\"></Test>");

        send("outboundResponseSender")
                .payload("<Test name=\"FooTest\">OK</Test>");

        echo("Foo Test OK!");
    }
}
