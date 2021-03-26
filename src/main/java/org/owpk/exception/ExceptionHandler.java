package org.owpk.exception;

import io.micronaut.core.exceptions.ExceptionHandler;
import io.micronaut.http.annotation.Produces;

import javax.inject.Singleton;

@Produces
@Singleton
class NotFoundExceptionHandler implements ExceptionHandler<RuntimeException> {

    @Override
    public void handle(RuntimeException exception) {

    }
}