package org.kimbs.licensingservice.events.handlers;

import lombok.extern.slf4j.Slf4j;
import org.kimbs.licensingservice.events.CustomChannels;
import org.kimbs.licensingservice.events.models.OrganizationChangeModel;
import org.kimbs.licensingservice.redis.OrganizationRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannels.class)
@Slf4j
public class OrganizationChangeHandler {

    @Autowired
    private OrganizationRedisRepository organizationRedisRepository;

    @StreamListener("inBoundOrganizationChanges")
    public void loggerSink(OrganizationChangeModel organizationChangeModel) {
        log.info("Received a message of type " + organizationChangeModel.getType());

        switch(organizationChangeModel.getAction()){
            case "GET":
                log.info("Received a GET event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                break;
            case "SAVE":
                log.info("Received a SAVE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                break;
            case "UPDATE":
                log.info("Received a UPDATE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                organizationRedisRepository.deleteOrganization(organizationChangeModel.getOrganizationId());
                break;
            case "DELETE":
                log.info("Received a DELETE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                organizationRedisRepository.deleteOrganization(organizationChangeModel.getOrganizationId());
                break;
            default:
                log.error("Received an UNKNOWN event from the organization service of type {}", organizationChangeModel.getType());
                break;
        }
    }

}
