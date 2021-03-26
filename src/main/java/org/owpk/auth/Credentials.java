package org.owpk.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Introspected
@Data
public class Credentials {
    private String login;
    private String password;
    private boolean remember;
    @JsonProperty(value = "twofa_code")
    private String twoFa;
}
