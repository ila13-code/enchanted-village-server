package unical.demacs.enchantedvillage.battle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unical.demacs.enchantedvillage.buildings.BuildingData;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BattleData {
    private List<TroopPlacement> troopPlacements;

    private List<BuildingData> buildingDestroyeds;
}