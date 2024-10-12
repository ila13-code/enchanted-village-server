package unical.demacs.enchantedvillage.config.security.tokenvalidator;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.persistence.repository.UserRepository;
import unical.demacs.enchantedvillage.persistence.service.implementation.UserServiceImpl;
import unical.demacs.enchantedvillage.utils.Constants;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.*;

@Service
@AllArgsConstructor
public class TokenValidatorService {

    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    public static final Logger logger = LoggerFactory.getLogger(TokenValidatorService.class);

    public Map<String, Object> isValid(String accessToken) throws ParseException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(accessToken);
        byte[] publicKeyBytes = Base64.getDecoder().decode(Constants.getKeycloakKey());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        JWSVerifier jwsVerifier = new RSASSAVerifier(publicKey);
        if (signedJWT.verify(jwsVerifier) &&
                new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {

            return signedJWT.getPayload().toJSONObject();
        }
        return null;
    }

    public Optional<?> getUser(String token, boolean signUp) throws ParseException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        logger.info("*********START REQUEST VALIDATE TOKEN*********");
        Map<String, Object> jsonJWT= this.isValid(token);
        if (jsonJWT == null ||
                !(jsonJWT.get("resource_access") instanceof Map<?, ?>) ||
                !(((Map<?, ?>) jsonJWT.get("resource_access")).get("enchanted-village") instanceof Map<?, ?>) ||
                !(((Map<?, ?>) ((Map<?, ?>) jsonJWT.get("resource_access")).get("enchanted-village")).get("roles") instanceof List<?>) ||
                !(((List<?>) ((Map<?, ?>) ((Map<?, ?>) jsonJWT.get("resource_access")).get("enchanted-village")).get("roles")).stream().allMatch(role -> role instanceof String))) {
            logger.error("Token not valid.");
            logger.info("*********END REQUEST VALIDATE TOKEN********");
            return Optional.empty();
        }
        else {
            Map<String, Object> resourceAccess = (Map<String, Object>) jsonJWT.get("resource_access");
            Map<String, Object> kittifyWeb = (Map<String, Object>) resourceAccess.get("enchanted-village");
            List<String> roles = (List<String>) kittifyWeb.get("roles");
            String role = roles.get(0);
            logger.info("Token valid. User: " + jsonJWT.get("sub") + " Role: " + role);
            String userId = (String) jsonJWT.get("sub");
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                logger.info("User found for id.");
                logger.info("*********END REQUEST VALIDATE TOKEN********");
                return Optional.of(user);
            } else {
                if (!signUp) {
                    logger.error("User not found for id.");
                    logger.info("*********END REQUEST VALIDATE TOKEN********");
                    return Optional.empty();
                } else {
                    logger.info("User not found for id. Creating user.");
                    logger.info("*********END REQUEST VALIDATE TOKEN********");
                    return Optional.of(userServiceImpl.createUser(userId, (String) jsonJWT.get("given_name"), (String) jsonJWT.get("family_name"), (String) jsonJWT.get("email"), role, (String) jsonJWT.get("preferred_username")));
                }
            }
        }
    }

}