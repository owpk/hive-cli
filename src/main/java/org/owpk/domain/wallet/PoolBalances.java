package org.owpk.domain.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class PoolBalances {

   private String pool;
   private Balance balance;
}
