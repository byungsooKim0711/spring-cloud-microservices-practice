package org.kimbs.licensingservice.controller;

import org.kimbs.licensingservice.config.ServiceConfig;
import org.kimbs.licensingservice.model.License;
import org.kimbs.licensingservice.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;

    @GetMapping("/")
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        return licenseService.findByOrganizationId(organizationId);
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId) throws Exception {
        final License found = licenseService.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<License> saveLicense(@PathVariable("organizationId") String organizationId, @RequestBody final License license, final UriComponentsBuilder uriBuilder) {
        License created = licenseService.saveLicense(license);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/v1/organizations/{organizationId}/licenses/{licenseId}").buildAndExpand(organizationId, created.getLicenseId()).toUri());

        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);

    }

    @PutMapping("/{licenseId}")
    public String updateLicense(@PathVariable("licenseId") String licenseId) {
        return String.format("License ID [{0}] the put", licenseId);
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return new ResponseEntity<>(String.format("License ID [{0}] the delete", licenseId), HttpStatus.ACCEPTED);
    }
}
