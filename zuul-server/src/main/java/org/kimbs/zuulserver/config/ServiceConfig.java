package org.kimbs.zuulserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig {

    @Value("${signing.key}")
    private String jwtStringKey = "";

    public String getJwtStringKey() {
        return jwtStringKey;
    }
}
