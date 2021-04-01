package org.owpk.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.svm.hosted.dashboard.ToJson;
import io.micronaut.context.annotation.Parameter;
import lombok.SneakyThrows;
import org.owpk.api.WalletsApi;
import org.owpk.domain.ApiResponse;
import org.owpk.domain.wallet.WalletsJ;
import org.owpk.jsondataextruder.DefinitionConfig;
import org.owpk.jsondataextruder.JsonFlatmap;
import picocli.CommandLine.Command;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    }

    @Command(name = "farm-info", description = "Get info about specific wallet tied to specific farm\n" +
            "Endpoint: /farms/{farmId}/wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId, @Parameter Long walletId) {
        var response = api.getWalletInfo(farmId, walletId);
    }

    @Command(name = "info", description = "Get info about specific wallet\n" +
            "Endpoint: /wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId) {
        var response = api.getWalletInfo(farmId);
        executeSingleResponseEntity(response, DefinitionConfig.DEFAULT);
    }

    @Override
    public void run() {
        var response = api.getWalletsList();

        var cfg = new DefinitionConfig(WalletsJ.class);
        var poolBalanceCfg = new DefinitionConfig("pool_balances");
        poolBalanceCfg.addFieldsToShow("status");
        cfg.setObjects(List.of(poolBalanceCfg));
        cfg.addFieldsToShow("id", "coin");
        executeListOfResponseEntities(response, cfg);
    }
}
