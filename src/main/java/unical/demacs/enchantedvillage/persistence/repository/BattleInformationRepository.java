package unical.demacs.enchantedvillage.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;

import java.util.UUID;

@Repository
public interface BattleInformationRepository  extends JpaRepository<BattleInformation, UUID> {
    BattleInformation findByUserId(String userId);
    BattleInformation findByEnemyId(String enemyId);

    BattleInformation updateBattleInformationById(UUID id, BattleInformation battleInformation);
}
