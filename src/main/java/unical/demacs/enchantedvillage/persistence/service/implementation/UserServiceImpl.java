package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.config.handler.exception.TooManyRequestsException;
import unical.demacs.enchantedvillage.config.handler.exception.UserException;
import unical.demacs.enchantedvillage.persistence.dto.UserDTO;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.interfaces.IUserService;
import unical.demacs.enchantedvillage.utils.Role;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    //private EmailService emailService;
    private final ModelMapper modelMapper;
    private final RateLimiter rateLimiter;

    @Transactional
    @Override
    public User createUser(String id, String name, String surname, String email, String role, String username) {
        logger.info("++++++START REQUEST++++++");
        logger.info("Attempting to create user with email: {}", email);
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                logger.info("User with id {} already exists", id);
                return user;
            } else {
                user = new User(id, name, surname, email, Role.valueOf(role), username, null);
                userRepository.save(user);
                //emailService.sendEmailRegistration(email, name);
                logger.info("User created successfully: {}", email);
                return user;
            }
        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("++++++START REQUEST++++++");
        logger.info("Attempting to get user by email: {}", email);
        try {
            if (!rateLimiter.tryAcquire()) {
                logger.warn("Rate limit exceeded for getUserByEmail");
                throw new TooManyRequestsException();
            }

            return userRepository.findByEmail(email)
                    .map(user -> {
                        logger.info("User found: {}", user);
                        return user;
                    })
                    .orElseThrow(() -> {
                        logger.warn("User not found for email: {}", email);
                        return new UserException("No user found");
                    });
        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }



    @Override
    public boolean updateUser(UserDTO userDTO) {
        logger.info("++++++START REQUEST++++++");
        logger.info("Attempting to update user: {}", userDTO.getEmail());
        try {
            if (!rateLimiter.tryAcquire()) {
                logger.warn("Rate limit exceeded for updateUser");
                throw new TooManyRequestsException();
            }

            return userRepository.findByEmail(userDTO.getEmail())
                    .map(existingUser -> {
                        User updatedUser = modelMapper.map(userDTO, User.class);
                        userRepository.save(updatedUser);
                        logger.info("User updated successfully: {}", updatedUser.getEmail());
                        return true;
                    })
                    .orElseThrow(() -> {
                        logger.warn("No user found for update: {}", userDTO.getEmail());
                        return new UserException("No user found");
                    });
        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("++++++START REQUEST++++++");
        logger.info("Attempting to check if user exists by email: {}", email);
        try {
            if (!rateLimiter.tryAcquire()) {
                logger.warn("Rate limit exceeded for existsByEmail");
                throw new TooManyRequestsException();
            }

            boolean exists = userRepository.existsByEmail(email);
            logger.info("User exists: {}", exists);
            return exists;
        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        logger.info("++++++START REQUEST++++++");
        logger.info("Attempting to get all users. Page: {}, Size: {}", page, size);
        try {
            if (!rateLimiter.tryAcquire()) {
                logger.warn("Rate limit exceeded for getAllUsers");
                throw new TooManyRequestsException();
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<User> usersPage = userRepository.findAllByRole(Role.USER, pageable);
            logger.info("Users found: {}", usersPage.getTotalElements());
            return usersPage;
        } finally {
            logger.info("++++++END REQUEST++++++");
        }
    }
}