package ru.extremism.extrmismserver.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.extremism.extrmismserver.model.AnalyzedText;
import ru.extremism.extrmismserver.model.AnalyzingText;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JsonTextParser {
    private final Set<String> removeKeys = Set.of("id", "weight", "part");
    private ObjectMapper om = new ObjectMapper();
    private ObjectWriter ow = om.writer().withDefaultPrettyPrinter();

    @SneakyThrows
    public String parseToJson(AnalyzingText[] text) {
        return ow.writeValueAsString(text);
    }

    @SneakyThrows
    public String parseToJson(AnalyzingText text) {
        return ow.writeValueAsString(new AnalyzingText[]{text});
    }

    public AnalyzedText parseFromJson(String json) {
        AnalyzedText text = new AnalyzedText();
        json = json.replaceAll("'", "\"");
        try (JsonParser jsonParser = om.createParser(json)) {
            Map<String, Object> map = om.readValue(jsonParser, Map.class);
            text.setId(map.get("id").toString());
            text.setWeight(Double.parseDouble(map.get("weight").toString()));
            text.setSignatures(map.entrySet()
                    .stream()
                    .filter(entry -> !removeKeys.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .map(Object::toString)
                    .toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return text;
    }
}
