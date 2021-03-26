package org.owpk.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Parameter;
import org.owpk.api.WalletsApi;
import org.owpk.jsondataextruder.DefinitionConfig;
import org.owpk.jsondataextruder.JsonFlatmap;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import java.util.Objects;

@Command(name = "wal",
        description = "Get info about all wallets tied to account\n\tDefault api endpoint: /wallets")
public class WalletsCommand implements Runnable{
    private WalletsApi api;
    private ObjectMapper objectMapper;

    @Option(names = {"-r", "--raw"}) private boolean raw;

    @Inject
    public WalletsCommand(WalletsApi api, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.api = api;
    }

    @Command(name = "farm-wallets", description = "Get info about all wallets tied to specific farm\n" +
            "Endpoint: /farms/{farmId}/wallets",
            subcommands = {DefinitionConfigParser.class})
    public void getWalletsFarmList(@Parameter Long farmId) throws Exception {
        DefinitionConfig defConfig = new CommandLine(DefinitionConfigParser.class).getExecutionResult();
        var response = api.getWalletsList(farmId);
        var multimap = JsonFlatmap.flatmap(response.body().getData(), defConfig);
        if (raw) {
            JsonNode jsonNode = objectMapper.convertValue(response.body().getData(), JsonNode.class);
            System.out.println(jsonNode.toPrettyString());
        } else
            multimap.forEach((k,v) -> System.out.println(k + " : " + v));
    }

    @Command(name = "farm-info", description = "Get info about specific wallet tied to specific farm\n" +
            "Endpoint: /farms/{farmId}/wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId, @Parameter Long walletId) {
        DefinitionConfig defConfig = new CommandLine(DefinitionConfigParser.class).getExecutionResult();
        var response = api.getWalletInfo(farmId, walletId);
        JsonFlatmap.flatmap(response.body(), DefinitionConfig.DEFAULT);
        System.out.println(response);
    }

    @Command(name = "info", description = "Get info about specific wallet\n" +
            "Endpoint: /wallets/{walletId}")
    public void getWalletInfo(@Parameter Long farmId) {
        DefinitionConfig defConfig = new CommandLine(DefinitionConfigParser.class).getExecutionResult();
        var response = api.getWalletInfo(farmId);
        System.out.println(Objects.requireNonNull(response.body()).toPrettyString());
    }

    private void executeIfRaw() {

    }

    @Override
    public void run() {
        DefinitionConfig defConfig = new CommandLine(DefinitionConfigParser.class).getExecutionResult();
        var response = api.getWalletsList();
        System.out.println(Objects.requireNonNull(response.body()).toPrettyString());
    }
}
