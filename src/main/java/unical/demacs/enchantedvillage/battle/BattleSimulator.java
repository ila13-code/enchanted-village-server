package unical.demacs.enchantedvillage.battle;

import unical.demacs.enchantedvillage.buildings.BuildingData;


import java.util.*;
import java.util.stream.Collectors;

public class BattleSimulator {
    private static final int GRID_SIZE = 50;
    private static final int SIMULATION_STEPS = 1000;

    public BattleSimulationResult simulateBattle(List<BuildingData> defenderBuildings, List<TroopPlacement> attackerTroops) {
        BattleGrid grid = new BattleGrid(GRID_SIZE, defenderBuildings);
        List<MovingTroop> troops = initializeTroops(attackerTroops);

        int totalDestruction = 0;
        for (int step = 0; step < SIMULATION_STEPS; step++) {
            moveTroops(troops, grid);
            totalDestruction += performAttacks(troops, grid);
            performDefensiveAttacks(troops, grid);

            if (totalDestruction == defenderBuildings.size() * 100 || allTroopsDead(troops)) {
                break;
            }
        }

        int percentageDestroyed = calculateDestructionPercentage(totalDestruction, defenderBuildings.size());
        boolean victory = percentageDestroyed > 50;

        return new BattleSimulationResult(victory, percentageDestroyed);
    }

    private List<MovingTroop> initializeTroops(List<TroopPlacement> placements) {
        return placements.stream()
                .map(p -> new MovingTroop(p.getTroopType(), p.getX(), p.getY()))
                .collect(Collectors.toList());
    }

    private void moveTroops(List<MovingTroop> troops, BattleGrid grid) {
        for (MovingTroop troop : troops) {
            if (!troop.isAlive()) continue;
            BuildingData target = findNearestBuilding(troop, grid);
            if (target != null) {
                moveTroopTowardsTarget(troop, target);
            }
        }
    }

    private BuildingData findNearestBuilding(MovingTroop troop, BattleGrid grid) {
        return grid.getBuildings().stream()
                .filter(b -> b.getHealth() > 0)
                .min(Comparator.comparingDouble(b ->
                        calculateDistance(troop.getX(), troop.getY(), b.getX(), b.getY())))
                .orElse(null);
    }

    private void moveTroopTowardsTarget(MovingTroop troop, BuildingData target) {
        int dx = Integer.compare(target.getX() - troop.getX(), 0);
        int dy = Integer.compare(target.getY() - troop.getY(), 0);
        troop.move(dx, dy);
    }

    private int performAttacks(List<MovingTroop> troops, BattleGrid grid) {
        int totalDamage = 0;
        for (MovingTroop troop : troops) {
            if (!troop.isAlive()) continue;
            BuildingData target = findBuildingInRange(troop, grid);
            if (target != null) {
                int damage = troop.attack();
                totalDamage += grid.damageBuilding(target, damage);
            }
        }
        return totalDamage;
    }

    private BuildingData findBuildingInRange(MovingTroop troop, BattleGrid grid) {
        return grid.getBuildings().stream()
                .filter(b -> b.getHealth() > 0 &&
                        calculateDistance(troop.getX(), troop.getY(), b.getX(), b.getY()) <= troop.getAttackRange())
                .findFirst()
                .orElse(null);
    }

    private void performDefensiveAttacks(List<MovingTroop> troops, BattleGrid grid) {
        for (BuildingData building : grid.getBuildings()) {
            if (building.getHealth() > 0 && building.isDefensive()) {
                MovingTroop target = findNearestTroop(building, troops);
                if (target != null && isInRange(building, target)) {
                    int damage = building.getAttackDamage();
                    target.receiveDamage(damage);
                }
            }
        }
    }

    private MovingTroop findNearestTroop(BuildingData building, List<MovingTroop> troops) {
        return troops.stream()
                .filter(MovingTroop::isAlive)
                .min(Comparator.comparingDouble(t ->
                        calculateDistance(building.getX(), building.getY(), t.getX(), t.getY())))
                .orElse(null);
    }

    private boolean isInRange(BuildingData building, MovingTroop troop) {
        double distance = calculateDistance(building.getX(), building.getY(), troop.getX(), troop.getY());
        return distance <= building.getAttackRange();
    }

    private boolean allTroopsDead(List<MovingTroop> troops) {
        return troops.stream().noneMatch(MovingTroop::isAlive);
    }

    private int calculateDestructionPercentage(int totalDestruction, int totalBuildings) {
        return (totalDestruction * 100) / (totalBuildings * 100);
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}