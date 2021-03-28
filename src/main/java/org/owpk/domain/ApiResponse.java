package org.owpk.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class ApiResponse<T> {
    private List<T> data = Collections.emptyList();
}
