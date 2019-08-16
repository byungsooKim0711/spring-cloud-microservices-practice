package org.kimbs.licensingservice.clients;

import org.kimbs.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OrganizationRestTemplateClient {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    public Optional<Organization> getOrganization(String organizationId) {
        ResponseEntity<Organization> responseEntity =
                restTemplate.exchange(
                          //"http://ORGANIZATIONSERVICE/v1/organizations/{organizationId}" // Eureka 에 등록된 서비스 아이디로는 왜 못하냐구요........ 네??? 뭐가문제예요?ㅠㅠㅠㅠ
                          //"http://ZUULSERVICE/v1/organizations/{organizationId}"
                          "http://localhost:5555/api/organization/v1/organizations/{organizationId}"
                          //"http://localhost:8080/v1/organizations/{organizationId}"
                        , HttpMethod.GET
                        , null
                        , Organization.class
                        , organizationId);

        return Optional.ofNullable(responseEntity.getBody());
    }
}
