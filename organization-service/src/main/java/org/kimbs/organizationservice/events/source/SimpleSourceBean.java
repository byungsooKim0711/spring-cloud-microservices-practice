package org.kimbs.organizationservice.events.source;

import lombok.extern.slf4j.Slf4j;
import org.kimbs.organizationservice.events.models.OrganizationChangeModel;
import org.kimbs.organizationservice.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleSourceBean {

    @Autowired
    private Source source;

    public void publishOrganizationChange(String action, String organizationId) {
        log.info("Sending Kafka message {} for Organization Id {}", action, organizationId);

        OrganizationChangeModel change = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(), action, organizationId, UserContext.getCorrelationId());

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}