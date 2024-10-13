package unical.demacs.enchantedvillage.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "battle_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildBattleInformation")
public class BattleInformation {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @NotNull(message = "The id cannot be null")
        private UUID id;

        @OneToOne(fetch = FetchType.LAZY)
        @Column(name = "fk_user", nullable = false)
        private User user;

        @OneToOne(fetch = FetchType.LAZY)
        @Column(name = "fk_enemy", nullable = false)
        private User enemy;

        @Column(name = "result", nullable = false)
        private boolean result;

        @Column(name="percentage_destroyed", nullable = false)
        private int percentageDestroyed;

        @Column(name="battle_data", nullable = false) //todo: capire come gestire il campo
        private String battleData;

        @Column(name = "battle_date", nullable = false)
        @NotNull(message = "The battleDate cannot be null")
        @PastOrPresent(message = "The battleDate cannot be in the future")
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        private LocalDate battleDate;

        @Column(name = "elixir_stolen", nullable = false)
        private int elixirStolen;

        @Column(name = "gold_stolen", nullable = false)
        private int goldStolen;

        @Override
        public String toString() {
            return "BattleInformation{" +
                    "id=" + id +
                    ", user=" + user +
                    ", enemy=" + enemy +
                    ", result=" + result +
                    ", percentageDestroyed=" + percentageDestroyed +
                    ", battleData=" + battleData +
                    ", battleDate=" + battleDate +
                    ", elixirStolen=" + elixirStolen +
                    ", goldStolen=" + goldStolen +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BattleInformation that = (BattleInformation) o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

}
