/*
 * Copyright 2006-2014 the original author or authors.
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

package com.consol.citrus.camel.endpoint;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.ActionTimeoutException;
import com.consol.citrus.message.*;
import com.consol.citrus.message.correlation.*;
import com.consol.citrus.messaging.ReplyConsumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous producer creates synchronous Camel exchange for sending message and receiving synchronous reply.
 * Reply message is correlated and stored in correlation manager. This way test cases are able to receive synchronous
 * message asynchronously at later time.
 *
 * @author Christoph Deppisch
 * @since 1.4.1
 */
public class CamelSyncProducer extends CamelProducer implements ReplyConsumer {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(CamelSyncProducer.class);

    /** Store of reply messages */
    private CorrelationManager<Message> correlationManager;

    /** Endpoint configuration */
    private final CamelSyncEndpointConfiguration endpointConfiguration;

    /**
     * Constructor using endpoint configuration and fields.
     *
     * @param name
     * @param endpointConfiguration
     */
    public CamelSyncProducer(String name, CamelSyncEndpointConfiguration endpointConfiguration) {
        super(name, endpointConfiguration);
        this.endpointConfiguration = endpointConfiguration;

        this.correlationManager = new PollingCorrelationManager(endpointConfiguration, "Reply message did not arrive yet");
    }

    @Override
    public void send(final Message message, TestContext context) {
        log.info("Sending message to camel endpoint: '" + endpointConfiguration.getEndpointUri() + "'");

        String correlationKey = endpointConfiguration.getCorrelator().getCorrelationKey(message);
        context.saveCorrelationKey(correlationKey, this);

        context.onOutboundMessage(message);

        log.info("Message was successfully sent to camel endpoint: '" + endpointConfiguration.getEndpointUri() + "'");

        Exchange response = endpointConfiguration.getCamelContext().createProducerTemplate()
                .request(endpointConfiguration.getEndpointUri(), new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        endpointConfiguration.getMessageConverter().convertOutbound(exchange, message, endpointConfiguration);
                    }
                });


        log.info("Received synchronous response message on camel endpoint: '" + endpointConfiguration.getEndpointUri() + "'");
        Message replyMessage = endpointConfiguration.getMessageConverter().convertInbound(response, endpointConfiguration);
        context.onInboundMessage(replyMessage);
        correlationManager.store(correlationKey, replyMessage);
    }

    @Override
    public Message receive(TestContext context) {
        return receive(context.getCorrelationKey(this), context);
    }

    @Override
    public Message receive(String selector, TestContext context) {
        return receive(selector, context, endpointConfiguration.getTimeout());
    }

    @Override
    public Message receive(TestContext context, long timeout) {
        return receive(context.getCorrelationKey(this), context, timeout);
    }

    @Override
    public Message receive(String selector, TestContext context, long timeout) {
        Message message = correlationManager.find(selector, timeout);

        if (message == null) {
            throw new ActionTimeoutException("Action timeout while receiving synchronous reply message on camel exchange");
        }

        return message;
    }

    /**
     * Sets the correlation manager.
     * @param correlationManager
     */
    public void setCorrelationManager(CorrelationManager<Message> correlationManager) {
        this.correlationManager = correlationManager;
    }
}
