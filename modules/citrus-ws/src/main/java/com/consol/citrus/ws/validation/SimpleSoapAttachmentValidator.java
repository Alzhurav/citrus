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
package com.consol.citrus.ws.validation;

import com.consol.citrus.exceptions.CitrusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.consol.citrus.ws.message.SoapAttachment;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * Simple implementation of a {@link AbstractSoapAttachmentValidator}.
 *
 * Attachment content body is validated through simple string equals assertion.
 *
 * @author Christoph Deppisch
 */
public class SimpleSoapAttachmentValidator extends AbstractSoapAttachmentValidator {

    private boolean ignoreAllWhitespaces = false;

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(SimpleSoapAttachmentValidator.class);

    @Override
    protected void validateAttachmentContent(SoapAttachment receivedAttachment, SoapAttachment controlAttachment) {
        if (log.isDebugEnabled()) {
            log.debug("Validating SOAP attachment content ...");
            log.debug("Received attachment content: " + StringUtils.trimWhitespace(receivedAttachment.getContent()));
            log.debug("Control attachment content: " + StringUtils.trimWhitespace(controlAttachment.getContent()));
        }

        if (receivedAttachment.getContent() != null && controlAttachment.getContent() != null) {
            Assert.isTrue(controlAttachment.getContent() != null,
                    "Values not equal for attachment content '"
                    + controlAttachment.getContentId() + "', expected '"
                    + null + "' but was '"
                    + receivedAttachment.getContent().trim() + "'");

            String trimmedControlAttachment;
            String trimmedReceivedAttachment;

            if (ignoreAllWhitespaces) {
                trimmedControlAttachment = StringUtils.trimAllWhitespace(controlAttachment.getContent());
                trimmedReceivedAttachment = StringUtils.trimAllWhitespace(receivedAttachment.getContent());
            } else {
                trimmedControlAttachment = StringUtils.trimWhitespace(controlAttachment.getContent());
                trimmedReceivedAttachment = StringUtils.trimWhitespace(receivedAttachment.getContent());
            }

            Assert.isTrue(trimmedReceivedAttachment.equals(trimmedControlAttachment),
                    "Values not equal for attachment content '"
                    + controlAttachment.getContentId() + "', expected '"
                    + controlAttachment.getContent().trim() + "' but was '"
                    + receivedAttachment.getContent().trim() + "'");
        } else {
            // At least one of the attachments has binary data therefore do a binary validation
            try {
                Assert.isTrue(IOUtils.contentEquals(receivedAttachment.getInputStream(), controlAttachment.getInputStream()),
                    "Values not equal for binary attachment content '"
                    + controlAttachment.getContentId() + "'");
            }
            catch(IOException e) {
                throw new CitrusRuntimeException("Binary attachment validation failed", e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Validating attachment content: OK");
        }
    }

    public boolean isIgnoreAllWhitespaces() {
        return ignoreAllWhitespaces;
    }

    public void setIgnoreAllWhitespaces(boolean ignoreAllWhitespaces) {
        this.ignoreAllWhitespaces = ignoreAllWhitespaces;
    }
}
