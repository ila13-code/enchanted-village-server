package unical.demacs.enchantedvillage.battle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unical.demacs.enchantedvillage.buildings.TroopsType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TroopPlacement {
    private TroopsType troopType;
    private int x;
    private int y;
}