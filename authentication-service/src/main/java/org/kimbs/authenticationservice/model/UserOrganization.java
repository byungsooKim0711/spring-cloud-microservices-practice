package org.kimbs.authenticationservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_org")
@Data
public class UserOrganization implements Serializable {

    @Id
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "organization_id", nullable = false)
    private String organizationId;

}
