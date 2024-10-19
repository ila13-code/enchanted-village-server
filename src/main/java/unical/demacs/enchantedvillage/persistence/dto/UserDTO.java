package unical.demacs.enchantedvillage.persistence.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import unical.demacs.enchantedvillage.utils.Role;


@Data
@ToString
public class UserDTO {
    @NotBlank(message = "The ID cannot be empty")
    private String id;

    @NotBlank(message = "The name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Name can only contain letters, spaces, hyphens, and apostrophes")
    private String name;

    @NotBlank(message = "The surname cannot be blank")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Surname can only contain letters, spaces, hyphens, and apostrophes")
    private String surname;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Provide a valid email address")
    private String email;

    @NotNull(message = "The role cannot be null and void")
    private Role role;

    @NotBlank(message = "The username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username can only contain letters and numbers")
    private String username;


}