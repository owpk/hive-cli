package org.owpk.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class Token {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @JsonIgnoreProperties
    private Long lastUpdated;
}
