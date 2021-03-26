package org.owpk.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Parameter;
import org.owpk.jsondataextruder.DefinitionConfig;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "configure",
        description = "Define specific parameters you want to show from api response json object")
public class DefinitionConfigParser implements Callable<DefinitionConfig> {

    @Parameters
    Map<String, String> definitionMap;

    @Parameter
    String raw;

    @Option(names = {"-f", "--file"})
    private String file;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public DefinitionConfig call() throws Exception {
        DefinitionConfig definitionConfig = DefinitionConfig.DEFAULT;
        if (raw != null && !raw.isBlank()) {
            definitionConfig = objectMapper.convertValue(raw, DefinitionConfig.class);
        } else if (!definitionMap.isEmpty()){
            String[] fields = definitionMap.get("fields").split(",");
            String[] filter = definitionMap.get("filter").split(",");
            for (String field : fields) {
                definitionConfig.addFieldsToShow(field);
            }
            for (String f : filter) {
                definitionConfig.addFilterByFields(f);
            }
        } else if (file != null && !file.isBlank()) {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            definitionConfig = objectMapper.convertValue(new String(bytes), DefinitionConfig.class);
        }
        return definitionConfig;
    }
}
