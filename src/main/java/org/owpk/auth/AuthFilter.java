package org.owpk.auth;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import org.reactivestreams.Publisher;

import javax.inject.Inject;

@Filter(value = {"/api/v2/farms/**", "/api/v2/wallets/**", "/api/v2/farms", "/api/v2/wallets"})
public class AuthFilter implements HttpClientFilter {
    private TokenManagerImpl tokenManager;

    @Inject
    public AuthFilter(TokenManagerImpl tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        String accessToken = tokenManager.getToken().getAccessToken();
        if (accessToken == null || accessToken.isBlank() || accessToken.equals("unauthenticated"))
            throw new RuntimeException("Null token");
        tokenManager.checkIfTokenExpired();
        return chain.proceed(request.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
    }

}
