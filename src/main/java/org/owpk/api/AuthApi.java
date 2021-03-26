package org.owpk.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import org.owpk.auth.Credentials;
import org.owpk.auth.Token;

@Client(value = "${hive.api.url}")
public interface AuthApi {

    @Post("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<Token> doAuth(@Body Credentials credentials);
}
