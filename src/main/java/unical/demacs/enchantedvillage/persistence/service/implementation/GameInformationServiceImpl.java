package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.config.handler.exception.NoGameInformationFound;
import unical.demacs.enchantedvillage.config.handler.exception.NoUserFoundException;
import unical.demacs.enchantedvillage.config.handler.exception.TooManyRequestsException;
import unical.demacs.enchantedvillage.persistence.dto.GameInformationDTO;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.repository.GameInformationRepository;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.interfaces.IGameInformationService;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameInformationServiceImpl implements IGameInformationService {

    public static final Logger logger = LoggerFactory.getLogger(GameInformationServiceImpl.class);

    private final UserRepository userRepository;
    private final GameInformationRepository gameInformationRepository;
    private final ModelMapper modelMapper;
    private final RateLimiter rateLimiter;

    @Transactional
    @Override
    public void createGameInformation(@NotNull String email, GameInformationDTO gameInformationDTO) {
        logger.info("++++++START REQUEST createGameInformation++++++");
        logger.info("Creating new gameInformation for user {}", email);
        boolean result = rateLimiter.tryAcquire();
        if (!result) {
            logger.warn("Too many requests, try again later.");
            logger.info("******* END REQUEST *******");
            throw new TooManyRequestsException();
        }
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("User {} not found", email);
                        return new NoUserFoundException("User not found." + email);
                    });

            gameInformationRepository.findByUserId(user.getId())
                    .ifPresentOrElse(gameInformation -> {
                        logger.info("GameInformation already exists for user {}", email);
                    }, () -> {
                        GameInformation gameInformation = GameInformation.buildGameInformation()
                                .id(UUID.randomUUID())
                                .user(user)
                                .creationDate(LocalDate.now())
                                .lastUpdateDate(LocalDate.now())
                                .buildingData(gameInformationDTO.getBuildingData())
                                .elixir(gameInformationDTO.getElixir())
                                .gold(gameInformationDTO.getGold())
                                .level(gameInformationDTO.getLevel())
                                .experience(gameInformationDTO.getExperience())
                                .build();
                        gameInformationRepository.save(gameInformation);
                        logger.info("GameInformation created successfully for user {}", email);
                    });

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Transactional
    @Override
    public void updateGameInformation(String userEmail, GameInformationDTO gameInformationDTO) {
        logger.info("++++++START REQUEST updateGameInformation++++++");
        logger.info("Updating gameInformation for user {}", userEmail);
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

            GameInformation gameInformation = gameInformationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("GameInformation not found for user {}", userEmail);
                        return new NoUserFoundException("GameInformation not found." + userEmail);
                    });

            gameInformation.setBuildingData(gameInformationDTO.getBuildingData());
            gameInformation.setElixir(gameInformationDTO.getElixir());
            gameInformation.setGold(gameInformationDTO.getGold());
            gameInformation.setLevel(gameInformationDTO.getLevel());
            gameInformation.setExperience(gameInformationDTO.getExperience());
            gameInformation.setLastUpdateDate(LocalDate.now());
            gameInformationRepository.save(gameInformation);
            logger.info("GameInformation updated successfully for user {}", userEmail);

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Transactional
    @Override
    public void getGameInformation(String userEmail) {
        logger.info("++++++START REQUEST getGameInformation++++++");
        logger.info("Get gameInformation for user {}", userEmail);
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

            GameInformation gameInformation = gameInformationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("GameInformation not found for user {}", userEmail);
                        return new NoGameInformationFound("GameInformation not found." + userEmail);
                    });

            logger.info("GameInformation found for user {}", userEmail);
            logger.info(gameInformation.toString());

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Transactional
    @Override
    public void deleteGameInformation(String userEmail) {
        logger.info("++++++START REQUEST deleteGameInformation++++++");
        logger.info("Delete gameInformation for user {}", userEmail);
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

            GameInformation gameInformation = gameInformationRepository.findByUserId(user.getId())
                    .orElseThrow(() -> {
                        logger.error("GameInformation not found for user {}", userEmail);
                        return new NoGameInformationFound("GameInformation not found." + userEmail);
                    });

            gameInformationRepository.delete(gameInformation);
            logger.info("GameInformation deleted successfully for user {}", userEmail);

        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }



}
