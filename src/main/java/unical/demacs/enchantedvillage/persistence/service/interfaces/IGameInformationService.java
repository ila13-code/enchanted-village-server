package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;

public class IGameInformationService {
    @Transactional
    public void createGameInformation(String id, String userEmail, String buildingData, String elixir, String gold, String level) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public void updateGameInformation(String userEmail, String buildingData, String elixir, String gold, String level) {
        throw new UnsupportedOperationException();
    }

    public void getGameInformation(String userEmail) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public void deleteGameInformation(String userEmail) {
        throw new UnsupportedOperationException();
    }

}
