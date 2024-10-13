package unical.demacs.enchantedvillage.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import unical.demacs.enchantedvillage.config.validators.ValidUUID;
import unical.demacs.enchantedvillage.utils.gameobject.BuildingData;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@ToString
public class GameInformationDTO {
    @NotBlank(message = "The ID cannot be empty")
    @ValidUUID
    private UUID id;

    @NotBlank(message = "The User ID cannot be empty")
    private String userId;

    private LocalDate creationDate;

    private LocalDate lastUpdateDate;

    @NotBlank(message = "The buildingData cannot be empty")
    private BuildingData buildingData;

    @NotBlank(message = "The elixir cannot be empty")
    private int elixir;

    @NotBlank(message = "The gold cannot be empty")
    private int gold;

    @NotBlank(message = "The level cannot be empty")
    private int level;

    @NotBlank(message = "The experience cannot be empty")
    private int experience;

}
