package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;

public interface IGameInformationService {
    @Transactional
    void createGameInformation(String email, GameInformationDTO gameInformationDTO);
    @Transactional
    void updateGameInformation(String userEmail, GameInformationDTO gameInformationDTO);

    void getGameInformation(String userEmail);

    @Transactional
    void deleteGameInformation(String userEmail);

}
