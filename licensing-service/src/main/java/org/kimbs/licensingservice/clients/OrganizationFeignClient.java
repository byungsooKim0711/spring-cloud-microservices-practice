package org.kimbs.licensingservice.clients;

import org.kimbs.licensingservice.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("organizationservice")
public interface OrganizationFeignClient {

    @GetMapping(value = "/v1/organizations/{organizationId}", consumes = "application/json")
    Optional<Organization> getOrganization(@PathVariable("organizationId") String organizationId);
}
