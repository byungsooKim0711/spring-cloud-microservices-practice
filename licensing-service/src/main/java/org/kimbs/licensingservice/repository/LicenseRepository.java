package org.kimbs.licensingservice.repository;

import org.kimbs.licensingservice.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {
    public List<License> findByOrganizationId(String organizationId);

    public Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
