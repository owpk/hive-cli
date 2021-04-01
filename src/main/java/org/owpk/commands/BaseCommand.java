package org.owpk.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import io.micronaut.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.owpk.domain.wallet.WalletsJ;
import org.owpk.exception.ApplicationException;
import org.owpk.jsondataextruder.DefinitionConfig;
import org.owpk.jsondataextruder.JsonFlatmap;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static picocli.CommandLine.*;

@Command(name = "configure",
        description = "Define specific parameters you want to show from api response")
@Slf4j
public class BaseCommand {
    private static final String FIELD = "field";
    private static final String FILTER = "filter";

    @Parameters
    protected Map<String, String> definitionMap;

    @Option(names = {"-s", "--show"})
    protected String show;

    @Option(names = {"-f", "--file"})
    protected String file;

    @Option(names = {"-F", "--format"}, description = "Formatting output by specific regex (default %s : %s\\n")
    protected String format = "%s : %s\n";

    @Inject
    ObjectMapper objectMapper;

    @SuppressWarnings("ConstantConditions")
    protected void checkForSuccessHttpStatus(HttpResponse<JsonNode> response) {
        if (response == null || response.body() == null || response.body().isEmpty()) {
            var msg = "No response from server";
            log.error(msg + " : response %s", response);
            throw new ApplicationException(msg);
        }
    }

    protected void printResult(Multimap<String,String> map) {
        map.entries().forEach((k) -> System.out.printf(format, k.getKey(), k.getValue()));
    }

    public DefinitionConfig parseConfig(DefinitionConfig config) {
        checkForInput();
        if (show != null && !show.isBlank()) {
            config = tryToConvert(show);
        } else if (!definitionMap.isEmpty()) {
            parseRequestedFields(config);
            parseRequestedFilter(config);
        } else if (file != null && !file.isBlank()) {
            try {
                var bytes = Files.readAllBytes(Paths.get(file));
                var rawConfig = new String(bytes);
                config = tryToConvert(rawConfig);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ApplicationException(e.getLocalizedMessage());
            }
        }
        return config;
    }

    public void executeListOfResponseEntities(HttpResponse<JsonNode> response, DefinitionConfig cfg) {
        var node = getJsonNodeFromHttpBody(response, "data");
        List<WalletsJ> list = convertNodeToEntitiesList(WalletsJ.class, node);
        var result = JsonFlatmap.flatmap(list, cfg);
        printResult(result);
    }

    public void executeSingleResponseEntity(HttpResponse<JsonNode> response, DefinitionConfig cfg) {

    }

    private void checkForInput() {
        var isEmpty = definitionMap.keySet().stream().filter(x -> x.equals(FIELD) || x.equals(FILTER)).findAny().isEmpty();
        if (isEmpty) {
            System.out.println("Unknown options: " + definitionMap.keySet());
            System.exit(1);
        }
    }

    private <T> DefinitionConfig tryToConvert(T val) {
        var config = DefinitionConfig.DEFAULT;
        try {
            config = objectMapper.convertValue(val, DefinitionConfig.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException("Cannot read configuration, error occurred : " + e.getLocalizedMessage());
        }
        return config;
    }

    private void parseRequestedFields(DefinitionConfig config) {
        var definition = definitionMap.get(FIELD);
        if (definition != null) {
            var fields = definition.split(",");
            for (var field : fields) {
                var inheritFields = field.split("\\.");
                if (inheritFields.length > 1) {
                    var inheritObject = new DefinitionConfig(inheritFields[inheritFields.length - 2]);
                    config.addEntitiesToShow(inheritObject);
                    inheritObject.addFieldsToShow(inheritFields[inheritFields.length - 1]);
                } else config.addFieldsToShow(field);
            }
        }
    }

    private void parseRequestedFilter(DefinitionConfig config) {
        var empty = new String[0];
        var fields_ = definitionMap.get("filter");
    }

    protected JsonNode getJsonNodeFromHttpBody(HttpResponse<JsonNode> response, String fieldName) {
        checkForSuccessHttpStatus(response);
        return response.body().get(fieldName);
    }

    protected <T> List<T> convertNodeToEntitiesList(Class<T> clazz, JsonNode node) {
        List<T> list = new ArrayList<>();
        var iterator = node.elements();
        while (iterator.hasNext()) {
            var val = objectMapper.convertValue(iterator.next(), clazz);
            list.add(val);
        }
        return list;
    }
}
