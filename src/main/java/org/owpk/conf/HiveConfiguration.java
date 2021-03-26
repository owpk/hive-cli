package org.owpk.conf;

import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ConfigurationProperties("hive")
@Introspected
public class HiveConfiguration {
    private String config;

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "token")
    protected TokenConfig.Builder builder = TokenConfig.builder();

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public TokenConfig.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(TokenConfig.Builder builder) {
        this.builder = builder;
    }
}
