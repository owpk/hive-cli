package org.owpk.commands;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.owpk.domain.ApiResponse;
import org.owpk.jsondataextruder.DefinitionConfig;
import org.owpk.jsondataextruder.JsonFlatmap;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "configure",
        description = "Define specific parameters you want to show from api response")
@Slf4j
public class BaseCommand implements Callable<DefinitionConfig> {

    @Parameters
    protected Map<String, String> definitionMap;

    @Parameter
    protected String raw;

    @Option(names = {"-f", "--file"})
    private String file;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public DefinitionConfig call() {
        DefinitionConfig definitionConfig = DefinitionConfig.DEFAULT;
        if (raw != null && !raw.isBlank()) {
            definitionConfig = objectMapper.convertValue(raw, DefinitionConfig.class);
        } else if (!definitionMap.isEmpty()) {
            var fields = parse("fields");
            var filter = parse("filter");
            for (String field : fields)
                definitionConfig.addFieldsToShow(field);
            for (String f : filter)
                definitionConfig.addFilterByFields(f);
        } else if (file != null && !file.isBlank()) {
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(Paths.get(file));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            definitionConfig = objectMapper.convertValue(new String(bytes), DefinitionConfig.class);
        }
        return definitionConfig;
    }

    private String[] parse(String configName) {
        var empty = new String[0];
        var fields_ = definitionMap.get(configName);
        return fields_ != null ? fields_.split(",") : empty;
    }

    @SuppressWarnings("unchecked")
    protected <T> void executeResult(HttpResponse<JsonNode> response, Class<T> clazz) {
        if (raw != null && !raw.isBlank()) {
        } else if (definitionMap != null) {
            DefinitionConfig defConfig = call();
            var object = objectMapper.convertValue(response.body(), clazz);
            Multimap<String, String> result;
            if (object instanceof ApiResponse) {
                var list = ((ApiResponse<T>) object).getData();
                result = JsonFlatmap.flatmap(list, defConfig);
            } else
                result = JsonFlatmap.flatmap(object, defConfig);
            System.out.println(result.toString());
        } else {
            System.out.println(response.body().toPrettyString());
        }
    }
}
