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

package com.consol.citrus.config.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Bean definition parser for sync-message-channel-sender configuration.
 * 
 * @author Christoph Deppisch
 */
public class SyncMessageChannelSenderParser extends AbstractMessageChannelTemplateAwareParser {

    /**
     * @see com.consol.citrus.config.xml.AbstractMessageChannelTemplateAwareParser#doParseComponent(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected BeanDefinitionBuilder doParseComponent(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition("com.consol.citrus.channel.SyncMessageChannelSender");
        
        String replyTimeout = element.getAttribute("reply-timeout");
        
        if (StringUtils.hasText(replyTimeout)) {
            builder.addPropertyValue("replyTimeout", replyTimeout);
        }
        
        String replyHandler = element.getAttribute("reply-handler");
        
        if (StringUtils.hasText(replyHandler)) {
            builder.addPropertyReference("replyMessageHandler", replyHandler);
        }
        
        String replyMessageCorrelator = element.getAttribute("reply-message-correlator");
        
        if (StringUtils.hasText(replyMessageCorrelator)) {
            builder.addPropertyReference("correlator", replyMessageCorrelator);
        }
        
        return builder;
    }

}