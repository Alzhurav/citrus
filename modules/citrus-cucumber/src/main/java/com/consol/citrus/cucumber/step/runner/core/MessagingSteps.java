/*
 * Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.cucumber.step.runner.core;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.cucumber.message.MessageCreators;
import com.consol.citrus.dsl.builder.*;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.message.*;
import cucumber.api.java.en.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
public class MessagingSteps {

    @CitrusResource
    private TestRunner runner;

    /** Available message creator POJO objects */
    private MessageCreators messageCreators = new MessageCreators();

    /** Messages defined by id */
    private Map<String, Message> messages = new HashMap<>();

    @Given("^message creator ([^\\s]+)$")
    public void messageCreator(String type) {
        messageCreators.addType(type);
    }

    @Given("^message ([^\\s]+)$")
    public void message(String messageId) {
        messages.put(messageId, new DefaultMessage());
    }

    @When("^<([^>]*)> sends message <([^>]*)>$")
    public void sendMessage(final String endpoint, final String messageId) {
        if (messages.containsKey(messageId)) {
            runner.send(new BuilderSupport<SendMessageBuilder>() {
                @Override
                public void configure(SendMessageBuilder builder) {
                    builder.endpoint(endpoint)
                            .message(new DefaultMessage(messages.get(messageId)));
                }
            });
        } else {
            runner.send(new BuilderSupport<SendMessageBuilder>() {
                @Override
                public void configure(SendMessageBuilder builder) {
                    builder.endpoint(endpoint)
                            .message(messageCreators.createMessage(messageId));
                }
            });
        }
    }

    @Then("^<([^>]*)> should send message <([^>]*)>$")
    public void shouldSendMessage(String endpoint, String messageName) {
        sendMessage(endpoint, messageName);
    }

    @When("^<([^>]*)> sends$")
    public void send(final String endpoint, final String payload) {
        runner.send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint(endpoint)
                        .payload(payload);
            }
        });
    }

    @When("^<([^>]*)> sends \"([^\"]*)\"$")
    public void sendPayload(String endpoint, String payload) {
        send(endpoint, payload);
    }

    @Then("^<([^>]*)> should send \"([^\"]*)\"$")
    public void shouldSend(String endpoint, String payload) {
        send(endpoint, payload);
    }

    @Then("^<([^>]*)> should send$")
    public void shouldSendPayload(String endpoint, String payload) {
        send(endpoint, payload);
    }

    @When("^<([^>]*)> receives message <([^>]*)>$")
    public void receiveXmlMessage(final String endpoint, final String messageName) {
        receiveMessage(endpoint, MessageType.XML.name(), messageName);
    }

    @When("^<([^>]*)> receives ([^\\s]+) message <([^>]*)>$")
    public void receiveMessage(final String endpoint, final String type, final String messageId) {
        if (messages.containsKey(messageId)) {
            runner.receive(new BuilderSupport<ReceiveMessageBuilder>() {
                @Override
                public void configure(ReceiveMessageBuilder builder) {
                    builder.endpoint(endpoint)
                            .messageType(type)
                            .message(new DefaultMessage(messages.get(messageId)));
                }
            });
        } else {
            runner.receive(new BuilderSupport<ReceiveMessageBuilder>() {
                @Override
                public void configure(ReceiveMessageBuilder builder) {
                    builder.endpoint(endpoint)
                            .messageType(type)
                            .message(messageCreators.createMessage(messageId));
                }
            });
        }
    }

    @Then("^<([^>]*)> should receive message <([^>]*)>$")
    public void shouldReceiveXmlMessage(String endpoint, String messageName) {
        receiveMessage(endpoint, MessageType.XML.name(), messageName);
    }

    @Then("^<([^>]*)> should receive ([^\\s]+) message <([^>]*)>$")
    public void shouldReceiveMessage(String endpoint, String type, String messageName) {
        receiveMessage(endpoint, type, messageName);
    }

    @When("^<([^>]*)> receives ([^\\s]+) \"([^\"]*)\"$")
    public void receive(final String endpoint, final String type, final String payload) {
        runner.receive(new BuilderSupport<ReceiveMessageBuilder>() {
            @Override
            public void configure(ReceiveMessageBuilder builder) {
                builder.endpoint(endpoint)
                .messageType(type)
                .payload(payload);
            }
        });
    }

    @When("^<([^>]*)> receives \"([^\"]*)\"$")
    public void receiveXml(String endpoint, String payload) {
        receive(endpoint, MessageType.XML.name(), payload);
    }

    @When("^<([^>]*)> receives$")
    public void receiveXmlPayload(String endpoint, String payload) {
        receive(endpoint, MessageType.XML.name(), payload);
    }

    @When("^<([^>]*)> receives ([^\\s\"]+)$")
    public void receivePayload(String endpoint, String type, String payload) {
        receive(endpoint, type, payload);
    }

    @Then("^<([^>]*)> should receive ([^\\s]+) \"([^\"]*)\"$")
    public void shouldReceive(String endpoint, String type, String payload) {
        receive(endpoint, type, payload);
    }

    @Then("^<([^>]*)> should receive \"([^\"]*)\"$")
    public void shouldReceiveXml(String endpoint, String payload) {
        receive(endpoint, MessageType.XML.name(), payload);
    }

    @Then("^<([^>]*)> should receive$")
    public void shouldReceiveXmlPayload(String endpoint, String payload) {
        receive(endpoint, MessageType.XML.name(), payload);
    }

    @Then("^<([^>]*)> should receive ([^\\s\"]+)$")
    public void shouldReceivePayload(String endpoint, String type, String payload) {
        receive(endpoint, type, payload);
    }

    @And("^<([^>]*)> header ([^\\s]+) is \"([^\"]*)\"$")
    public void addHeader(String messageId, String name, String value) {
        if (!messages.containsKey(messageId)) {
            throw new CitrusRuntimeException(String.format("Unknown message '%s'", messageId));
        }

        messages.get(messageId).setHeader(name, value);
    }

    @And("^<([^>]*)> payload is \"([^\"]*)\"$")
    public void addPayload(String messageId, String payload) {
        if (!messages.containsKey(messageId)) {
            throw new CitrusRuntimeException(String.format("Unknown message '%s'", messageId));
        }

        messages.get(messageId).setPayload(payload);
    }

    @And("^<([^>]*)> payload is$")
    public void addPayloadMultiline(String messageId, String payload) {
        addPayload(messageId, payload);
    }
}
