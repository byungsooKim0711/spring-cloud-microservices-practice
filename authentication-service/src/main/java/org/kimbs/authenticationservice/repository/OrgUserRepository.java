package org.kimbs.authenticationservice.repository;

import org.kimbs.authenticationservice.model.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgUserRepository extends JpaRepository<UserOrganization, String> {

    public Optional<UserOrganization> findByUserName(String username);
}
