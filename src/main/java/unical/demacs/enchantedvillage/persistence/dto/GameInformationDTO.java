package unical.demacs.enchantedvillage.persistence.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import unical.demacs.enchantedvillage.config.validators.ValidUUID;
import unical.demacs.enchantedvillage.buildings.BuildingData;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@ToString
public class GameInformationDTO {
    @ValidUUID
    private UUID id;

    private LocalDate creationDate;

    private LocalDate lastUpdateDate;

    private List<BuildingData> buildings;

    @Min(0)
    private int elixir;

    @Min(0)
    private int gold;

    @Min(1)
    private int level;

    @Min(0)
    private int experiencePoints;
}