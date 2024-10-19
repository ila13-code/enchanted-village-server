package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;

import java.util.Optional;

public interface IBattleInformationService {
    @Transactional
    Optional<BattleInformation> createBattleInformation(String email, BattleInformationDTO battleInformationDTO);

    Optional<BattleInformation>  getBattleInformationByUserEmail(String userEmail);

    Optional<BattleInformation>  getBattleInformationByEnemyEmail(String enemyEmail);

}
