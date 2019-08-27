package org.kimbs.licensingservice.clients;

import org.kimbs.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OrganizationRestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

    public Optional<Organization> getOrganization(String organizationId) {
        ResponseEntity<Organization> responseEntity =
                restTemplate.exchange(
                          "http://ZUULSERVICE/api/organization/v1/organizations/{organizationId}"
                        , HttpMethod.GET
                        , null
                        , Organization.class
                        , organizationId);

        return Optional.ofNullable(responseEntity.getBody());
    }
}
