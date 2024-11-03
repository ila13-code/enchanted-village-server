package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.battle.BattleData;
import unical.demacs.enchantedvillage.battle.BattleSimulationResult;
import unical.demacs.enchantedvillage.battle.BattleSimulator;
import unical.demacs.enchantedvillage.battle.TroopPlacement;
import unical.demacs.enchantedvillage.config.handler.exception.*;
import unical.demacs.enchantedvillage.persistence.dto.BattleInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.BattleInformation;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.repository.BattleInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.GameInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.interfaces.IBattleInformationService;
import unical.demacs.enchantedvillage.buildings.TroopsType;
import unical.demacs.enchantedvillage.buildings.BuildingData;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class BattleInformationServiceImpl implements IBattleInformationService {

    public static final Logger logger = LoggerFactory.getLogger(BattleInformationServiceImpl.class);

    private final UserRepository userRepository;
    private final GameInformationRepository gameInformationRepository;
    private final BattleInformationRepository battleInformationRepository;
    private final RateLimiter rateLimiter;


    @Override
    public Optional<BattleInformation> createBattleInformation(String userEmail, BattleInformationDTO battleInformationDTO) {
        logger.info("++++++START REQUEST createBattleInformation++++++");
        logger.info("Creating new battle information for user {}", userEmail);
        boolean result = rateLimiter.tryAcquire();
        if (!result) {
            logger.warn("Too many requests, try again later.");
            logger.info("******* END REQUEST *******");
            throw new TooManyRequestsException();
        }
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> {
                        logger.error("User {} not found", userEmail);
                        return new NoUserFoundException("User not found." + userEmail);
                    });

            User enemy = userRepository.findById(battleInformationDTO.getEnemyId())
                    .orElseThrow(() -> {
                        logger.error("Enemy {} not found", battleInformationDTO.getEnemyId());
                        return new NoEnemyFoundException("Enemy not found." + battleInformationDTO.getEnemyId());
                    });

            GameInformation gameInformationUser = gameInformationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("GameInformation for user {} not found", userEmail);
                        return new NoGameInformationFound("GameInformation not found." + userEmail);
                    });

            GameInformation gameInformationEnemy = gameInformationRepository.findByUserId(enemy.getId())
                    .orElseThrow(() -> {
                        logger.error("GameInformation for enemy {} not found", battleInformationDTO.getEnemyId());
                        return new NoGameInformationFound("GameInformation not found." + battleInformationDTO.getEnemyId());
                    });

            validateLevels(gameInformationUser, gameInformationEnemy);
            List<TroopsType> userTroops = gameInformationUser.getBuildingData().stream()
                    .flatMap(building -> Optional.ofNullable(building.getTroopsData())
                            .stream()
                            .flatMap(Collection::stream))
                    .map(troopsData -> TroopsType.values()[troopsData.getType()])
                    .collect(Collectors.toList());

            BattleData battleData = battleInformationDTO.getBattleData();

            validateBattleData(battleData, userTroops, gameInformationEnemy);

            BattleSimulator simulator = new BattleSimulator();
            BattleSimulationResult simulationResult = simulator.simulateBattle(
                    gameInformationEnemy.getBuildingData(),
                    battleData.getTroopPlacements()
            );

            int elixirStolen = calculateResourceStolen(gameInformationEnemy.getElixir(), simulationResult.getPercentageDestroyed());
            int goldStolen = calculateResourceStolen(gameInformationEnemy.getGold(), simulationResult.getPercentageDestroyed());


            BattleInformation battleInformation = BattleInformation.buildBattleInformation()
                    .id(UUID.randomUUID())
                    .user(user)
                    .enemy(enemy)
                    .result(simulationResult.isVictory())
                    .percentageDestroyed(simulationResult.getPercentageDestroyed())
                    .battleData(battleInformationDTO.getBattleData())
                    .battleDate(LocalDate.now())
                    .elixirStolen(elixirStolen)
                    .goldStolen(goldStolen)
                    .build();
            battleInformationRepository.save(battleInformation);
            logger.info("BattleInformation created successfully for user {}", userEmail);
            return Optional.of(battleInformation);

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Override
    public Optional<BattleInformation> getBattleInformationByUserEmail(String userEmail) {
        logger.info("++++++START REQUEST getBattleInformationByUserEmail++++++");
        logger.info("Get battle information for user {}", userEmail);
        boolean result = rateLimiter.tryAcquire();
        if (!result) {
            logger.warn("Too many requests, try again later.");
            logger.info("******* END REQUEST *******");
            throw new TooManyRequestsException();
        }
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> {
                        logger.error("User {} not found", userEmail);
                        return new NoUserFoundException("User not found." + userEmail);
                    });
            BattleInformation battleInformation = battleInformationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("BattleInformation for user {} not found", userEmail);
                        return new NoBattleInformationFoundException("BattleInformation not found." + userEmail);
                    });

            logger.info("BattleInformation found for user {}", userEmail);
            return Optional.of(battleInformation);

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Override
    public Optional<BattleInformation>  getBattleInformationByEnemyEmail(String enemyEmail) {
        logger.info("++++++START REQUEST getBattleInformationByEnemyEmail++++++");
        logger.info("Get battle information for enemy {}", enemyEmail);
        boolean result = rateLimiter.tryAcquire();
        if (!result) {
            logger.warn("Too many requests, try again later.");
            logger.info("******* END REQUEST *******");
            throw new TooManyRequestsException();
        }
        try {
            User user = userRepository.findByEmail(enemyEmail)
                    .orElseThrow(() -> {
                        logger.error("Enemy {} not found", enemyEmail);
                        return new NoUserFoundException("Enemy not found." + enemyEmail);
                    });
            BattleInformation battleInformation = battleInformationRepository.findByEnemyId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("BattleInformation for enemy {} not found", enemyEmail);
                        return new NoBattleInformationFoundException("BattleInformation not found." + enemyEmail);
                    });

            logger.info("BattleInformation found for enemy {}", enemyEmail);
            return Optional.of(battleInformation);

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    private int calculateResourceStolen(int totalResource, int destructionPercentage) {
        return (int) (totalResource * (destructionPercentage / 100.0) * 0.75); // 75% of destroyed resources can be stolen
    }

    private void validateLevels(GameInformation user, GameInformation enemy) {
        if (user.getLevel() < 3 || enemy.getLevel() < 3) {
            throw new LevelNotReachedException("Both players must be at least level 3 to engage in battle.");
        }
    }

    private void validateBattleData(BattleData battleData, List<TroopsType> userTroops, GameInformation enemyInfo) {
        Map<TroopsType, Long> troopCounts = battleData.getTroopPlacements().stream()
                .collect(Collectors.groupingBy(TroopPlacement::getTroopType, Collectors.counting()));

        for (Map.Entry<TroopsType, Long> entry : troopCounts.entrySet()) {
            long availableTroops = userTroops.stream().filter(t -> t == entry.getKey()).count();
            if (availableTroops < entry.getValue()) {
                throw new InvalidBattleDataException("User doesn't have enough troops of type " + entry.getKey());
            }
        }

        Set<String> enemyBuildingIds = enemyInfo.getBuildingData().stream()
                .map(BuildingData::getUniqueId)
                .collect(Collectors.toSet());

        for (BuildingData destruction : battleData.getBuildingDestroyeds()) {
            if (!enemyBuildingIds.contains(destruction.getUniqueId())) {
                throw new InvalidBattleDataException("Invalid building ID in destruction data: " + destruction.getUniqueId());
            }
        }
    }




}


