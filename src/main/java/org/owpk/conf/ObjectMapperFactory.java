package org.owpk.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class ObjectMapperFactory {

    @Bean
    @Singleton
    public ObjectMapper getObjectMapperInstance() {
        return new ObjectMapper();
    }
}
