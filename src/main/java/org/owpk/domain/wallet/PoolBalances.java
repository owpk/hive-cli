package org.owpk.domain.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import org.owpk.objectname.ObjectName;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
@ObjectName(name = "pool_balances")
public class PoolBalances {

   private String pool;
   private Balance balance;
   private String status;
}
