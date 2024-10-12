package unical.demacs.enchantedvillage.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GameInformationDTO {
    @NotBlank(message = "The ID cannot be empty")
    private String id;

    @NotBlank(message = "The User ID cannot be empty")
    private String userId;

    @NotBlank(message = "The buildingData cannot be empty")
    private String buildingData;

    @NotBlank(message = "The elixir cannot be empty")
    private String elixir;

    @NotBlank(message = "The gold cannot be empty")
    private String gold;

    @NotBlank(message = "The level cannot be empty")
    private String level;

}
