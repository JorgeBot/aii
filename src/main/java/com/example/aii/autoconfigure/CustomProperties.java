package com.example.aii.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aii")
public class CustomProperties {
    private String allowOrigin;

    private Integer HttpClientTimeout;

    public String getAllowOrigin() {
        return allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    public Integer getHttpClientTimeout() {
        return HttpClientTimeout;
    }

    public void setHttpClientTimeout(Integer httpClientTimeout) {
        HttpClientTimeout = httpClientTimeout;
    }
}
