package unical.demacs.enchantedvillage.persistence.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unical.demacs.enchantedvillage.persistence.entities.User;
import unical.demacs.enchantedvillage.utils.Role;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findById(String id);
    Page<User> findAllByRole(Role role, Pageable pageable);
}