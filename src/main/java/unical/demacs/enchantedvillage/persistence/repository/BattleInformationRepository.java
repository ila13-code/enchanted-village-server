package unical.demacs.enchantedvillage.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BattleInformationRepository  extends JpaRepository<BattleInformation, UUID> {
    Optional<BattleInformation> findByUserId(String userId);
    Optional<BattleInformation> findByEnemyId(String enemyId);

    Optional<BattleInformation> findTopByUserIdOrderByBattleDateDesc(String userId);

}
