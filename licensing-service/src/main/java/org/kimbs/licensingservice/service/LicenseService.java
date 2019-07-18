package org.kimbs.licensingservice.service;

import org.kimbs.licensingservice.config.ServiceConfig;
import org.kimbs.licensingservice.exception.ResourceNotFoundException;
import org.kimbs.licensingservice.model.License;
import org.kimbs.licensingservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig serviceConfig;

    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId) throws Exception {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s] And License ID: [%s]", organizationId, licenseId)));

        return license.builder().comment(serviceConfig.getExampleProperty()).build();
    }

    public List<License> findByOrganizationId(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public License saveLicense(License license) {
        return licenseRepository.save(license.builder().licenseId(UUID.randomUUID().toString()).build());
    }

    public License updateLicense(License license) {
        return licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }
}
