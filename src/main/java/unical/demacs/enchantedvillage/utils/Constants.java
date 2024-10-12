package unical.demacs.enchantedvillage.utils;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {


    @Getter
    private static String keycloakKey;
    @Getter
    private static String tokenGeneratorKey;


    @Value("${keycloak.key}")
    public void setKeycloakKey(String keycloakKey) {
        Constants.keycloakKey = keycloakKey;
    }

    @Value("${token.generator.key}")
    public void setTokenGeneratorKey(String tokenGeneratorKey) {
        Constants.tokenGeneratorKey = tokenGeneratorKey;
    }


}
