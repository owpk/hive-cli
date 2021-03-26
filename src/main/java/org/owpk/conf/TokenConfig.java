package org.owpk.conf;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@NoArgsConstructor
@Data
public class TokenConfig {
    private String location;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String location;

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }
        public TokenConfig build() {
            TokenConfig teamAdmin = new TokenConfig();
            teamAdmin.location = location;
            return teamAdmin;
        }
    }

}
