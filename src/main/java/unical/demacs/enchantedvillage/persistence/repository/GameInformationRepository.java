package unical.demacs.enchantedvillage.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameInformationRepository extends JpaRepository<GameInformation, UUID> {
    Optional<GameInformation> findByUserId(String userId);
    Optional<GameInformation> findByUserIdAndGameId(String userId, UUID gameId);

}
