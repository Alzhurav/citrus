/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.validation;

import com.consol.citrus.validation.context.ValidationContext;


/**
 * Validation context providing a control message for message validation. 
 * The control message holds expected message content and header information. Message 
 * validators compare the actual message to the control message marking differences.
 * 
 * @author Christoph Deppisch
 */
public class ControlMessageValidationContext implements ValidationContext {
    /** The message type this context was built for */
    private final String messageType;

    /**
     * Default constructor using message type field.
     * @param messageType
     */
    public ControlMessageValidationContext(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }
}
