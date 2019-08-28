package org.kimbs.licensingservice.events.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationChangeModel {

    private String type;

    private String action;

    private String organizationId;

    private String correlationId;
}
