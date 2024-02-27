package ar.com.api.assetPlatform.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coins")
@Getter
@Setter
public class ApiServiceConfig {

    private String baseUrl;
    private String healthAPI;
    private String urlAssertPlatform;

}
