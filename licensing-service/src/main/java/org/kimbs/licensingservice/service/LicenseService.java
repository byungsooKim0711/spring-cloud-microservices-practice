package org.kimbs.licensingservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.kimbs.licensingservice.clients.OrganizationDiscoveryClient;
import org.kimbs.licensingservice.clients.OrganizationFeignClient;
import org.kimbs.licensingservice.clients.OrganizationRestTemplateClient;
import org.kimbs.licensingservice.config.ServiceConfig;
import org.kimbs.licensingservice.exception.ResourceNotFoundException;
import org.kimbs.licensingservice.model.License;
import org.kimbs.licensingservice.model.Organization;
import org.kimbs.licensingservice.repository.LicenseRepository;
import org.kimbs.licensingservice.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    OrganizationRestTemplateClient organizationRestTemplateClient;

    @HystrixCommand
    private Organization retrieveOrgInfo(String organizationId) {
        Optional<Organization> organization =  organizationRestTemplateClient.getOrganization(organizationId);

        return organization.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s]", organizationId)));
    }

    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s] And License ID: [%s]", organizationId, licenseId)));

        Organization organization = retrieveOrgInfo(organizationId);

        license.setOrganizationName(organization.getName());
        license.setContactName(organization.getContactName());
        license.setContactEmail(organization.getContactEmail());
        license.setContactPhone(organization.getContactPhone());
        license.setComment(serviceConfig.getExampleProperty());

        return license;
    }

    @HystrixCommand(
            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "findByOrganizationId",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")}
    )
    public List<License> findByOrganizationId(String organizationId) {
        log.info("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        License license = new License();
        license.setLicenseId("000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        return Arrays.asList(license);
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
