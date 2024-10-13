package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;

public interface IBattleInformationService {
    @Transactional
    void createBattleInformation(String email, BattleInformationDTO battleInformationDTO);
    @Transactional
    void updateBattleInformation(String userEmail, BattleInformationDTO battleInformationDTO);

    void getBattleInformationByUserEmail(String userEmail);

    void getBattleInformationByEnemyEmail(String enemyEmail);

    @Transactional
    void deleteBattleInformation(String userEmail);
}
