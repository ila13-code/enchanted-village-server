package unical.demacs.enchantedvillage.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(GameInformation.class, GameInformationDTO.class)
                .setPostConverter(context -> {
                    GameInformation source = context.getSource();
                    GameInformationDTO destination = context.getDestination();

                    destination.setId(source.getId());

                    destination.setCreationDate(source.getCreationDate());
                    destination.setLastUpdateDate(source.getLastUpdateDate());
                    destination.setBuildings(source.getBuildingData());
                    destination.setElixir(source.getElixir());
                    destination.setGold(source.getGold());
                    destination.setLevel(source.getLevel());
                    destination.setExperiencePoints(source.getExperience());

                    return destination;
                });

        modelMapper.createTypeMap(BattleInformation.class, BattleInformationDTO.class)
                .setPostConverter(context -> {
                    BattleInformation source = context.getSource();
                    BattleInformationDTO destination = context.getDestination();

                    destination.setId(source.getId());
                    destination.setUserId(source.getUser().getEmail());
                    destination.setEnemyEmail(source.getEnemy().getEmail());
                    destination.setResult(source.isResult());
                    destination.setRewardExp(source.getRewardExp());
                    destination.setPercentageDestroyed(source.getPercentageDestroyed());
                    destination.setBattleData(source.getBattleData());
                    destination.setBattleDestroyeds(source.getBattleDestroyeds());
                    destination.setBattleDate(source.getBattleDate());
                    destination.setElixirStolen(source.getElixirStolen());
                    destination.setGoldStolen(source.getGoldStolen());

                    return destination;
                });

        return modelMapper;
    }


}