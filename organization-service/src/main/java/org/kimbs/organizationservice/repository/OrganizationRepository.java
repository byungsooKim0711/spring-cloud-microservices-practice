package org.kimbs.organizationservice.repository;

import org.kimbs.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
