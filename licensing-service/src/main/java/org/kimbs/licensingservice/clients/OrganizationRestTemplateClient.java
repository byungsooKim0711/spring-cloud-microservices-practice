package org.kimbs.licensingservice.clients;

import lombok.extern.slf4j.Slf4j;
import org.kimbs.licensingservice.model.Organization;
import org.kimbs.licensingservice.redis.OrganizationRedisRepository;
import org.kimbs.licensingservice.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class OrganizationRestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrganizationRedisRepository organizationRedisRepository;

    public Organization getOrganization(String organizationId) {
        log.info("In Licensing Service.getOrganization:{}", UserContext.getCorrelationId());

        Organization organization = this.checkRedisCache(organizationId).orElseGet(() -> {
            log.info("Unable to locate organization from the redis cache: {}.", organizationId);

            ResponseEntity<Organization> responseEntity = restTemplate.exchange(
                      "http://ZUULSERVICE/api/organization/v1/organizations/{organizationId}"
                    , HttpMethod.GET
                    , null
                    , Organization.class
                    , organizationId);
            return responseEntity.getBody();
        });

        if (organization != null) {
            this.cacheOrganizationObject(organization);
        }
        return organization;
    }

    private Optional<Organization> checkRedisCache(String organizationId) {
        try {
            return Optional.ofNullable(organizationRedisRepository.findOrganization(organizationId));
        } catch (Exception exception) {
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}", organizationId, exception.getCause());
            return Optional.empty();
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            organizationRedisRepository.saveOrganization(organization);
        } catch (Exception exception) {
            log.error("Unable to cache organization {} in Redis. Exception {}.", organization.getId(), exception.getCause());
        }
    }
}