package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;

import java.util.Optional;

public interface IGameInformationService {
    @Transactional
    Optional<GameInformation> createGameInformation(String email, GameInformationDTO gameInformationDTO);
    @Transactional
    Optional<GameInformation> updateGameInformation(String userEmail, GameInformationDTO gameInformationDTO);

    Optional<GameInformation> getGameInformation(String userEmail);

    @Transactional
    void deleteGameInformation(String userEmail);

}
