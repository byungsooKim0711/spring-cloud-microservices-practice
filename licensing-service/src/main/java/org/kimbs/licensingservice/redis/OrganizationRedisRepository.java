package org.kimbs.licensingservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.kimbs.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
@Slf4j
public class OrganizationRedisRepository {

    private static final String HASH_NAME = "organization";

    private RedisTemplate<String, Organization> redisTemplate;

    private HashOperations hashOperations;

    public OrganizationRedisRepository() {
        super();
    }

    @Autowired
    private OrganizationRedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void saveOrganization(Organization organization) {
        log.info("saveOrganization");
        this.hashOperations.put(HASH_NAME, organization.getId(), organization);
    }

    public void updateOrganization(Organization organization) {
        log.info("updateOrganization");
        this.hashOperations.put(HASH_NAME, organization.getId(), organization);
    }

    public void deleteOrganization(String organizationId) {
        log.info("deleteOrganization");
        this.hashOperations.delete(HASH_NAME, organizationId);
    }

    public Organization findOrganization(String organizationId) {
        log.info("findOrganization");
        return (Organization) this.hashOperations.get(HASH_NAME, organizationId);
    }
}
