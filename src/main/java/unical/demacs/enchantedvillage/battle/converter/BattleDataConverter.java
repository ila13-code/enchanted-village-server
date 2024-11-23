package unical.demacs.enchantedvillage.battle.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unical.demacs.enchantedvillage.battle.BattleData;

@Converter(autoApply = true)
public class BattleDataConverter implements AttributeConverter<BattleData, String> {

    private static final Logger log = LoggerFactory.getLogger(BattleDataConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BattleData battleData) {
        if (battleData == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(battleData);
        } catch (JsonProcessingException e) {
            log.error("Error converting BattleData to JSON", e);
            return null;
        }
    }

    @Override
    public BattleData convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(dbData, BattleData.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to BattleData: {}", dbData, e);
            return null;
        }
    }
}