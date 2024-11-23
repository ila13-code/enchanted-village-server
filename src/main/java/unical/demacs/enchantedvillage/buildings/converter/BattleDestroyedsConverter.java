package unical.demacs.enchantedvillage.buildings.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unical.demacs.enchantedvillage.battle.BattleDestroyed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Converter
public class BattleDestroyedsConverter implements AttributeConverter<List<BattleDestroyed>, String> {

    private static final Logger log = LoggerFactory.getLogger(BattleDestroyedsConverter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public String convertToDatabaseColumn(List<BattleDestroyed> battleDestroyeds) {
        try {
            if (battleDestroyeds == null) {
                return "[]";
            }
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("version", "1.0");
            wrapper.put("destroyeds", battleDestroyeds);
            wrapper.put("timestamp", LocalDateTime.now());

            String json = objectMapper.writeValueAsString(wrapper);
            log.debug("Converting to database column. JSON length: {}", json.length());
            log.debug("JSON content: {}", json);

            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting BattleDestroyed list to JSON", e);
            return "[]";
        }
    }

    @Override
    public List<BattleDestroyed> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return List.of();
            }
            log.debug("Converting from database column. Data length: {}", dbData.length());

            // Verifica se il dbData è un numero
            if (dbData.matches("\\d+")) {
                log.error("Database data appears to be a number instead of JSON: {}", dbData);
                return List.of();
            }

            // Prova prima a interpretare direttamente come array
            try {
                List<BattleDestroyed> result = objectMapper.readValue(
                        dbData,
                        new TypeReference<List<BattleDestroyed>>() {}
                );
                log.debug("Successfully converted {} battle destroyeds from direct array", result.size());
                return result;
            } catch (JsonProcessingException e) {
                // Se fallisce, prova con la vecchia struttura con "destroyeds"
                JsonNode root = objectMapper.readTree(dbData);
                JsonNode destroyedsNode = root.path("destroyeds");

                if (destroyedsNode.isMissingNode() || destroyedsNode.isNull()) {
                    log.error("Destroyeds node is missing or null in JSON: {}", dbData);
                    return List.of();
                }

                List<BattleDestroyed> result = objectMapper.readValue(
                        destroyedsNode.toString(),
                        new TypeReference<List<BattleDestroyed>>() {}
                );

                log.debug("Successfully converted {} battle destroyeds from wrapped object", result.size());
                return result;
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to BattleDestroyed list. Data: {}", dbData, e);
            return List.of();
        }

    }
}