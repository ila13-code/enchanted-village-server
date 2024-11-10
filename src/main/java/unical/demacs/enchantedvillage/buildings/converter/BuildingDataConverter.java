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
import org.springframework.stereotype.Component;
import unical.demacs.enchantedvillage.buildings.BuildingData;
import unical.demacs.enchantedvillage.persistence.service.implementation.GameInformationServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Converter
public class BuildingDataConverter implements AttributeConverter<List<BuildingData>, String> {

    public static final Logger log = LoggerFactory.getLogger(BuildingDataConverter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);  // Per debug più leggibile

    @Override
    public  String convertToDatabaseColumn(List<BuildingData> buildingDataList) {
        try {
            if (buildingDataList == null) {
                return "[]";
            }
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("version", "1.0");
            wrapper.put("buildings", buildingDataList);
            wrapper.put("timestamp", LocalDateTime.now());

            String json = objectMapper.writeValueAsString(wrapper);
            log.debug("Converting to database column. JSON length: {}", json.length());
            log.debug("JSON content: {}", json);

            if (json.length() > 65535) {
                log.warn("JSON content exceeds TEXT limit. Length: {}", json.length());
            }

            return json;
        } catch (JsonProcessingException e) {
            log.error("Error converting BuildingData list to JSON", e);
            throw new RuntimeException("Error converting BuildingData list to JSON", e);
        }
    }

    @Override
    public List<BuildingData> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return List.of();
            }
            log.debug("Converting from database column. Data length: {}", dbData.length());

            // Verifica se il dbData è un numero (caso problematico)
            if (dbData.matches("\\d+")) {
                log.error("Database data appears to be a number instead of JSON: {}", dbData);
                return List.of();
            }

            JsonNode root = objectMapper.readTree(dbData);
            String version = root.path("version").asText("1.0");
            JsonNode buildingsNode = root.path("buildings");

            if (buildingsNode.isMissingNode() || buildingsNode.isNull()) {
                log.error("Buildings node is missing or null in JSON: {}", dbData);
                return List.of();
            }

            List<BuildingData> result = objectMapper.readValue(
                    buildingsNode.toString(),
                    new TypeReference<List<BuildingData>>() {}
            );

            log.debug("Successfully converted {} buildings", result.size());
            return result;
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to BuildingData list. Data: {}", dbData, e);
            return List.of();
        }
    }
}
