package org.kimbs.licensingservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServiceConfig {

    @Value("${example.property}")
    private String exampleProperty="";

    @Value("${signing.key}")
    private String jwtSigningKey="";

    @Value("${redis.server}")
    private String redisServer = "";

    @Value("${redis.port}")
    private String redisPort = "";

    public Integer getRedisPort() {
        return Integer.parseInt(this.redisPort);
    }
}