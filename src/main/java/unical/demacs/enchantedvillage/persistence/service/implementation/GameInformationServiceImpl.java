package unical.demacs.enchantedvillage.persistence.service.implementation;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;

@Service
@AllArgsConstructor
public class GameInformationServiceImpl {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private final ModelMapper modelMapper;
    private final RateLimiter rateLimiter;
}
