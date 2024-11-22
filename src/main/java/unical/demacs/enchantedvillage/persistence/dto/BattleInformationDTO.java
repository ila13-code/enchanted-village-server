package unical.demacs.enchantedvillage.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import unical.demacs.enchantedvillage.battle.BattleData;
import unical.demacs.enchantedvillage.config.validators.ValidUUID;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
public class BattleInformationDTO {
    @NotBlank(message = "The ID cannot be empty")
    @ValidUUID
    private UUID id;

    //@NotBlank(message = "The User ID cannot be empty") // chi è l'utente che ha fatto la battaglia
    private String userId;

    @NotBlank(message = "The enemy email cannot be empty") // chi è il nemico
    private String enemyEmail;

    //@NotBlank(message = "The result victory cannot be empty") // true se l'utente ha vinto, false se ha perso
    private Boolean result;

    @NotBlank(message="The percentage destroyed cannot be empty") // percentuale di vittoria
    private int percentageDestroyed;

    //@NotBlank(message="The battle date cannot be empty") // dati della battaglia - truppe ed edifici distrutti con percentuale
    private BattleData battleData;

    //@NotBlank(message="The battle date cannot be empty") // data della battaglia
    private LocalDate battleDate;

    @NotBlank(message = "The elixir stolen cannot be empty")
    private int elixirStolen;

    @NotBlank(message = "The gold stolen cannot be empty")
    private int goldStolen;

}
