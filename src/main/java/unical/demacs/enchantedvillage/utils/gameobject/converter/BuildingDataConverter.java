package unical.demacs.enchantedvillage.utils.gameobject.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import unical.demacs.enchantedvillage.utils.gameobject.BuildingData;

@Converter(autoApply = true)
public class BuildingDataConverter implements AttributeConverter<BuildingData, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BuildingData buildingData) {
        try {
            return objectMapper.writeValueAsString(buildingData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting BuildingData to JSON", e);
        }
    }

    @Override
    public BuildingData convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, BuildingData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to BuildingData", e);
        }
    }
}
