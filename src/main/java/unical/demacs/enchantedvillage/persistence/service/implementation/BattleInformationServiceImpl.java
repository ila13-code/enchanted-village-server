package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;
import unical.demacs.enchantedvillage.persistence.repository.BattleInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.GameInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.interfaces.IBattleInformationService;

@Service
@AllArgsConstructor
public class BattleInformationServiceImpl implements IBattleInformationService {

    public static final Logger logger = LoggerFactory.getLogger(BattleInformationServiceImpl.class);

    private final UserRepository userRepository;
    private final GameInformationRepository gameInformationRepository;
    private final BattleInformationRepository battleInformationRepository;
    private final ModelMapper modelMapper;
    private final RateLimiter rateLimiter;


    @Override
    public void createBattleInformation(String email, BattleInformationDTO battleInformationDTO) {

    }

    @Override
    public void updateBattleInformation(String userEmail, BattleInformationDTO battleInformationDTO) {

    }

    @Override
    public void getBattleInformationByUserEmail(String userEmail) {

    }

    @Override
    public void getBattleInformationByEnemyEmail(String enemyEmail) {

    }

    @Override
    public void deleteBattleInformation(String userEmail) {

    }
}


