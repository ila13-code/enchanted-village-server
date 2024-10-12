package unical.demacs.enchantedvillage.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import unical.demacs.enchantedvillage.utils.Role;

import java.util.Objects;

@Entity
@Table(name = "game_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildGameInformation")
public class GameInformation {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "fk_user", nullable = false)
    private User user;

    @Lob
    @Column(name = "building_data", nullable = false, columnDefinition = "CLOB")
    private String buildingData;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "elixir", nullable = false)
    private int elixir;

    @Column(name = "gold", nullable = false)
    private int gold;

    @Override
    public String toString() {
        return "GameInformation{" +
                "id=" + id +
                ", user=" + user +
                ", buildingData='" + buildingData + '\'' +
                ", level=" + level +
                ", elixir=" + elixir +
                ", gold=" + gold +
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

