package unical.demacs.enchantedvillage.battle.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import unical.demacs.enchantedvillage.battle.BattleData;

@Converter(autoApply = true)
public class BattleDataConverter implements AttributeConverter<BattleData, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BattleData battleData) {
        try {
            return objectMapper.writeValueAsString(battleData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting BattleData to JSON", e);
        }
    }

    @Override
    public BattleData convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, BattleData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to BattleData", e);
        }
    }
}
