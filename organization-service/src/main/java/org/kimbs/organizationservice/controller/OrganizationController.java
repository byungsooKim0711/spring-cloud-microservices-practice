package org.kimbs.organizationservice.controller;

import org.kimbs.organizationservice.model.Organization;
import org.kimbs.organizationservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organization> findById(@PathVariable("organizationId") String organizationId) {
        Organization found = organizationService.findById(organizationId);

        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Organization> save(@RequestBody final Organization organization, final UriComponentsBuilder uriBuilder) {
        Organization created = organizationService.save(organization);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/v1/organizations/{organizationId}").buildAndExpand(organization.getId()).toUri());

        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<Organization> update(@PathVariable("organizationId") String organizationId, @RequestBody Organization organization) {
        Organization updated = organizationService.update(organizationId, organization);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<?> delete(@PathVariable("organizationId") String organizationId) {
        organizationService.delete(organizationId);

        return ResponseEntity.accepted().build();
    }
}
