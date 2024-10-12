package unical.demacs.enchantedvillage.persistence.repository;

import jakarta.transaction.Transactional;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unical.demacs.enchantedvillage.persistence.entities.GameInformation;
import unical.demacs.enchantedvillage.persistence.entities.User;

import java.util.UUID;

@Repository
public interface GameInformationRepository extends JpaRepository<User, UUID> {

    //TODO: Implementare query per ottenere le informazioni di gioco di un utente
}
