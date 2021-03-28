package org.owpk.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import org.owpk.domain.ApiResponse;
import org.owpk.domain.wallet.WalletsJ;

@Client(value = "${hive.api.url}")
@Requires(property = "hive.token")
//@Produces(MediaType.APPLICATION_JSON)
public interface WalletsApi {

    @Get("/farms/{farmId}/wallets")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<JsonNode> getWalletsList(@PathVariable("farmId") Long farmId);

    @Get("/farms/{farmId}/wallets/{walletId}")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<JsonNode> getWalletInfo(@PathVariable("farmId") Long farmId,
                                                    @PathVariable("walletId") Long walletId);

    @Get("/wallets/{walletId}")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<JsonNode> getWalletInfo(@PathVariable("walletId") Long walletId);

    @Get("/wallets")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<JsonNode> getWalletsList();
}
