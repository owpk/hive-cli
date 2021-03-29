package org.owpk.domain.wallet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.ToString;
import org.owpk.objectname.ObjectName;

import java.util.List;

@JsonAutoDetect
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
@ToString
@ObjectName(name = "wallet")
public class WalletsJ {

    private Long id;

    @JsonProperty(value = "user_id")
    private Long userId;

    private String name;
    private String source;
    private String coin;
    private String wal;

    @JsonProperty(value = "fetch_balance")
    private boolean fetchBalance;

    @JsonProperty(value = "api_key_id")
    private Integer apiKeyId;

    private Balance balance;

    @ObjectName(name = "pool_balances")
    @JsonProperty(value = "pool_balances")
    private List<PoolBalances> poolBalances;

    @JsonProperty(value = "fs_count")
    private Integer fsCount;

    @JsonProperty(value = "workers_count")
    private Integer workersCount;

    @JsonProperty(value = "farm_id")
    private Integer farmId;
}

