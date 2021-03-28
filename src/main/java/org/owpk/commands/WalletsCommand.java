package org.owpk.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Parameter;
import lombok.SneakyThrows;
import org.owpk.api.WalletsApi;
import org.owpk.domain.ApiResponse;
import org.owpk.domain.wallet.WalletsJ;
import picocli.CommandLine.Command;

import javax.inject.Inject;

@Command(name = "wal",
        description = "Get info about all wallets tied to account\n\tDefault api endpoint: /wallets")
public class WalletsCommand extends BaseCommand implements Runnable {
    private WalletsApi api;

    @Inject
    public WalletsCommand(WalletsApi api) {
        this.api = api;
    }

    @Command(name = "farm-wallets", description = "Get info about all wallets tied to specific farm\n" +
            "Endpoint: /farms/{farmId}/wallets",
            subcommands = {BaseCommand.class})
    public void getWalletsFarmList(@Parameter Long farmId) throws Exception {
        var response = api.getWalletsList(farmId);
        executeResult(response, ApiResponse.class);
    }

    @Command(name = "farm-info", description = "Get info about specific wallet tied to specific farm\n" +
            "Endpoint: /farms/{farmId}/wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId, @Parameter Long walletId) {
        var response = api.getWalletInfo(farmId, walletId);
        executeResult(response, WalletsJ.class);
    }

    @Command(name = "info", description = "Get info about specific wallet\n" +
            "Endpoint: /wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId) {
        var response = api.getWalletInfo(farmId);
        executeResult(response, WalletsJ.class);
    }

    @SneakyThrows
    @Override
    public void run() {
        var response = api.getWalletsList();
        executeResult(response, ApiResponse.class);
    }
}
