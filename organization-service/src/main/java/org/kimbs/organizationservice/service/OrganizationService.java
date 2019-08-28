package org.kimbs.organizationservice.service;

import org.kimbs.organizationservice.events.source.SimpleSourceBean;
import org.kimbs.organizationservice.exception.ResourceNotFoundException;
import org.kimbs.organizationservice.model.Organization;
import org.kimbs.organizationservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SimpleSourceBean simpleSourceBean;

    public Organization findById(String organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With Org ID: [%s]", organizationId)));
    }

    public Organization save(Organization organization) {
        organization.setId(UUID.randomUUID().toString());

        simpleSourceBean.publishOrganizationChange("SAVE", organization.getId());

        return organizationRepository.saveAndFlush(organization);
    }

    public Organization update(String organizationId, Organization organization) {
        Organization updated = findById(organizationId);
        updated.setContactEmail(organization.getContactEmail());
        updated.setContactName(organization.getContactName());
        updated.setContactPhone(organization.getContactPhone());
        updated.setName(organization.getName());

        simpleSourceBean.publishOrganizationChange("UPDATE", organization.getId());

        return organizationRepository.saveAndFlush(updated);
    }

    public void delete(String organizationId) {
        Organization deleted = findById(organizationId);
        organizationRepository.deleteById(deleted.getId());

        simpleSourceBean.publishOrganizationChange("UPDATE", organizationId);
    }
}
