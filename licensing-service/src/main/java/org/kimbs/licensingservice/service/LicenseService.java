package org.kimbs.licensingservice.service;

import org.kimbs.licensingservice.clients.OrganizationDiscoveryClient;
import org.kimbs.licensingservice.clients.OrganizationFeignClient;
import org.kimbs.licensingservice.clients.OrganizationRestTemplateClient;
import org.kimbs.licensingservice.config.ServiceConfig;
import org.kimbs.licensingservice.exception.ResourceNotFoundException;
import org.kimbs.licensingservice.model.License;
import org.kimbs.licensingservice.model.Organization;
import org.kimbs.licensingservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestTemplateClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    private Organization retrieveOrgInfo(String organizationId, String clientType) {
        Optional<Organization> organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestTemplateClient.getOrganization(organizationId);
        }

        return organization.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s]", organizationId)));
    }

    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s] And License ID: [%s]", organizationId, licenseId)));

        Organization organization = retrieveOrgInfo(organizationId, clientType);

        license.setOrganizationName(organization.getName());
        license.setContactName(organization.getContactName());
        license.setContactEmail(organization.getContactEmail());
        license.setContactPhone(organization.getContactPhone());
        license.setComment(serviceConfig.getExampleProperty());

        return license;
    }

    public List<License> findByOrganizationId(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public License saveLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        return licenseRepository.save(license);
    }

    public License updateLicense(String licenseId, License license) {
        license.setLicenseId(licenseId);
        return licenseRepository.save(license);
    }

    public void deleteLicense(String licenseId) {
        licenseRepository.deleteById(licenseId);
    }
}
