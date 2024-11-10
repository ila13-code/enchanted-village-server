package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.buildings.BuildingData;
import unical.demacs.enchantedvillage.config.handler.exception.NoGameInformationFound;
import unical.demacs.enchantedvillage.config.handler.exception.NoUserFoundException;
import unical.demacs.enchantedvillage.config.handler.exception.TooManyRequestsException;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.repository.GameInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.interfaces.IGameInformationService;
import unical.demacs.enchantedvillage.utils.BuildingMergeValidator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class GameInformationServiceImpl implements IGameInformationService {

    private static final Duration SYNC_INTERVAL = Duration.ofMinutes(5);
    public static final Logger log = LoggerFactory.getLogger(GameInformationServiceImpl.class);

    private final UserRepository userRepository;
    private final GameInformationRepository gameInformationRepository;
    private final RateLimiter rateLimiter;
    private final BuildingMergeValidator buildingMergeValidator;

    @Override
    @Transactional
    public Optional<GameInformation> createGameInformation(@NotNull String email, GameInformationDTO gameInformationDTO) {
        log.info("Creating/Syncing game information for user {}", email);
        checkRateLimit();

        try {
            User user = findUserByEmail(email);

            return gameInformationRepository.findByUserId(user.getId())
                    .map(existingInfo -> updateExistingGameInformation(existingInfo, gameInformationDTO))
                    .or(() -> Optional.of(createNewGameInformation(user, gameInformationDTO)));
        } finally {
            log.info("Finished processing game information request");
        }
    }

    @Override
    @Transactional
    public Optional<GameInformation> updateGameInformation(String email, GameInformationDTO gameInformationDTO) {
        log.info("Updating game information for user {}", email);
        checkRateLimit();

        try {
            User user = findUserByEmail(email);

            return gameInformationRepository.findByUserId(user.getId())
                    .map(existing -> updateExistingGameInformation(existing, gameInformationDTO));
        } finally {
            log.info("Finished updating game information");
        }
    }

    @Override
    @Transactional
    public Optional<GameInformation> getGameInformation(String email) {
        log.info("Retrieving game information for user {}", email);
        checkRateLimit();

        try {
            User user = findUserByEmail(email);
            Optional<GameInformation> go=gameInformationRepository.findByUserId(user.getId());
            if(go.isPresent()) {
               log.info("Game information found for user {}", email);
               log.info(go.get().getBuildingData().get(0).getUniqueId());
            }
            return Optional.of(go)
                    .orElseThrow(() -> new NoGameInformationFound("Game information not found for user: " + email));
        } finally {
            log.info("Finished retrieving game information");
        }
    }

    @Override
    @Transactional
    public void deleteGameInformation(String email) {
        log.info("Deleting game information for user {}", email);
        checkRateLimit();

        try {
            User user = findUserByEmail(email);
            gameInformationRepository.findByUserId(user.getId())
                    .ifPresent(gameInformationRepository::delete);
        } finally {
            log.info("Finished deleting game information");
        }
    }

    private GameInformation createNewGameInformation(User user, GameInformationDTO dto) {
        GameInformation gameInformation = GameInformation.buildGameInformation()
                .id(UUID.randomUUID())
                .user(user)
                .creationDate(LocalDate.now())
                .lastUpdateDate(LocalDate.now())
                .lastSyncTimestamp(LocalDateTime.now())
                .buildingData(dto.getBuildings())
                .elixir(dto.getElixir())
                .gold(dto.getGold())
                .level(dto.getLevel())
                .experience(dto.getExperiencePoints())
                .version(0L)
                .build();

        return gameInformationRepository.save(gameInformation);
    }

    private GameInformation updateExistingGameInformation(GameInformation existing, GameInformationDTO dto) {
      /*  if (!existing.needsSync(SYNC_INTERVAL)) {
            log.debug("Skipping sync - within sync interval for user {}", existing.getUser().getEmail());
            return existing;
        }*/

        List<BuildingData> mergedBuildings = buildingMergeValidator.validateAndMergeBuildings(existing, dto);

        existing.setBuildingData(mergedBuildings);
        existing.setElixir(dto.getElixir());
        existing.setGold(dto.getGold());
        existing.setLevel(dto.getLevel());
        existing.setExperience(dto.getExperiencePoints());
        existing.setLastUpdateDate(LocalDate.now());
        existing.setLastSyncTimestamp(LocalDateTime.now());

        return gameInformationRepository.save(existing);
    }

    private List<BuildingData> mergeBuildings(List<BuildingData> serverBuildings, List<BuildingData> clientBuildings) {
        if (clientBuildings == null || clientBuildings.isEmpty()) {
            return serverBuildings != null ? serverBuildings : new ArrayList<>();
        }

        Map<String, BuildingData> buildingMap = new HashMap<>();

        if (serverBuildings != null) {
            serverBuildings.forEach(b -> buildingMap.put(b.getUniqueId(), b));
        }

        clientBuildings.forEach(clientBuilding -> {
            if (isValidBuildingUpdate(buildingMap.get(clientBuilding.getUniqueId()), clientBuilding)) {
                buildingMap.put(clientBuilding.getUniqueId(), clientBuilding);
            }
        });

        return new ArrayList<>(buildingMap.values());
    }



    private boolean isValidBuildingUpdate(BuildingData serverBuilding, BuildingData clientBuilding) {
        if (serverBuilding == null) return true;

        // Implementa qui la tua logica di validazione
        // Per esempio:
        // - Controlla che le coordinate siano valide
        // - Verifica che il tipo di edificio sia corretto
        // - Assicurati che gli aggiornamenti seguano le regole del gioco
        return true;
    }

    private void checkRateLimit() {
        if (!rateLimiter.tryAcquire()) {
            log.warn("Rate limit exceeded");
            throw new TooManyRequestsException();
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFoundException("User not found: " + email));
    }

}
