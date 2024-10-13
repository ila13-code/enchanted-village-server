package unical.demacs.enchantedvillage.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import unical.demacs.enchantedvillage.utils.gameobject.BuildingData;
import unical.demacs.enchantedvillage.utils.gameobject.converter.BuildingDataConverter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "game_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildGameInformation")
public class GameInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull(message = "The id cannot be null")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "fk_user", nullable = false)
    private User user;

    @Column
    @NotNull(message = "The creationDate cannot be null")
    @PastOrPresent(message = "The creationDate cannot be in the future")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    @Column
    @NotNull(message = "The lastUpdateDate cannot be null")
    @PastOrPresent(message = "The lastUpdateDate cannot be in the future")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastUpdateDate;


    @Lob
    @Column(name = "building_data", nullable = false, columnDefinition = "CLOB")
    @Convert(converter = BuildingDataConverter.class)
    private BuildingData buildingData;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "elixir", nullable = false)
    private int elixir;

    @Column(name = "gold", nullable = false)
    private int gold;

    @Column(name = "experience", nullable = false)
    private int experience;

    @Override
    public String toString() {
        return "GameInformation{" +
                "id=" + id +
                ", user=" + user +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", buildingData='" + buildingData + '\'' +
                ", level=" + level +
                ", elixir=" + elixir +
                ", gold=" + gold +
                ", experience=" + experience +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameInformation gameInformation)) return false;
        return Objects.equals(getId(), gameInformation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}

