package com.okta.dev.oktar2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@ConfigurationProperties(prefix = "r2dbc")
public class R2DBCConfigProperties {
    private String url;
}
