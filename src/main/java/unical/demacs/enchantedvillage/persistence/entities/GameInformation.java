package unical.demacs.enchantedvillage.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import unical.demacs.enchantedvillage.buildings.BuildingData;
import unical.demacs.enchantedvillage.buildings.converter.BuildingDataConverter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    @JoinColumn(name = "fk_user", nullable = false)
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

    @Column(name = "building_data", columnDefinition = "TEXT")
    @Convert(converter = BuildingDataConverter.class)
    private List<BuildingData> buildingData;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "elixir", nullable = false)
    private int elixir;

    @Column(name = "gold", nullable = false)
    private int gold;

    @Column(name = "experience", nullable = false)
    private int experience;

    @Version
    @Column(name = "version")
    private Long version = 0L;

    @Column(name = "last_sync_timestamp")
    private LocalDateTime lastSyncTimestamp;


    public boolean needsSync(Duration maxSyncInterval) {
        if (lastSyncTimestamp == null) return true;
        return LocalDateTime.now().minus(maxSyncInterval).isAfter(lastSyncTimestamp);
    }


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

