package dev.highright96.server.loginUserDetails.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
@Data
public class AppProperties {
    private String tokenSecret;
    private Long tokenExpirationMsec;
}
