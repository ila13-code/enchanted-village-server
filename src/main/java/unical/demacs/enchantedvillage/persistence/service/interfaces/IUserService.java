package unical.demacs.enchantedvillage.persistence.service.interfaces;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import unical.demacs.enchantedvillage.persistence.dto.UserDTO;
import unical.demacs.enchantedvillage.persistence.entities.User;

public interface IUserService {
    @Transactional
    public UserDTO createUser(String id, String name, String surname, String email, String role);

    public User getUserByEmail(String email);
    @Transactional
    boolean updateUser(UserDTO userDTO);

    Page<UserDTO> getAllUsers(int page, int size);
}