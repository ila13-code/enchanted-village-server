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
        if (battleDestroyeds == null) {
            return null;  // Ritorna null invece di "[]"
        }
        try {
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("version", "1.0");
            wrapper.put("destroyeds", battleDestroyeds);
            wrapper.put("timestamp", LocalDateTime.now());

            return objectMapper.writeValueAsString(wrapper);
        } catch (JsonProcessingException e) {
            log.error("Error converting BattleDestroyed list to JSON", e);
            return null;  // Ritorna null invece di "[]"
        }
    }

    @Override
    public List<BattleDestroyed> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;  // Ritorna null invece di List.of()
        }

        try {
            log.debug("Converting from database column. Data length: {}", dbData.length());

            if (dbData.matches("\\d+")) {
                log.error("Database data appears to be a number instead of JSON: {}", dbData);
                return null;  // Ritorna null invece di List.of()
            }

            try {
                List<BattleDestroyed> result = objectMapper.readValue(
                        dbData,
                        new TypeReference<List<BattleDestroyed>>() {}
                );
                return result;
            } catch (JsonProcessingException e) {
                JsonNode root = objectMapper.readTree(dbData);
                JsonNode destroyedsNode = root.path("destroyeds");

                if (destroyedsNode.isMissingNode() || destroyedsNode.isNull()) {
                    return null;  // Ritorna null invece di List.of()
                }

                return objectMapper.readValue(
                        destroyedsNode.toString(),
                        new TypeReference<List<BattleDestroyed>>() {}
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to BattleDestroyed list. Data: {}", dbData, e);
            return null;  // Ritorna null invece di List.of()
        }
    }
}