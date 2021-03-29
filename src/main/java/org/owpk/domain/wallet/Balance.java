package org.owpk.domain.wallet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import org.owpk.objectname.ObjectName;

@JsonAutoDetect
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
@ObjectName(name = "balance")
public class Balance {

   @JsonProperty
   private String status;
   private Double value;

   @JsonProperty("value_fiat")
   private Double valueFiat;

}
