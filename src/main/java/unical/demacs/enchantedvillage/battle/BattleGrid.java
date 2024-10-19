package unical.demacs.enchantedvillage.battle;

import unical.demacs.enchantedvillage.buildings.BuildingData;
import java.util.ArrayList;
import java.util.List;

public class BattleGrid {
    private List<BuildingData> buildings;

    public BattleGrid(int size, List<BuildingData> buildings) {
        this.buildings = new ArrayList<>(buildings);
    }

    public List<BuildingData> getBuildings() {
        return buildings;
    }

    public int damageBuilding(BuildingData building, int damage) {
        int actualDamage = Math.min(building.getHealth(), damage);
        building.setHealth(building.getHealth() - actualDamage);
        return actualDamage;
    }
}