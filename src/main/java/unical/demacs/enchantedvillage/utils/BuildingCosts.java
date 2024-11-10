package unical.demacs.enchantedvillage.utils;

import java.util.Arrays;

public enum BuildingCosts {
    CANNON(0, 100, 0),           // ItemType.cannon
    TOWER(1, 200, 0),            // ItemType.tower
    WALL(2, 20, 0),              // ItemType.wall
    BARRACK(3, 150, 0),          // ItemType.barrack
    TRAINING_BASE(4, 200, 0),    // ItemType.trainingBase
    BOAT(5, 100, 0),             // ItemType.boat
    CAMP(6, 100, 0),             // ItemType.camp
    FLAG(7, 50, 0),              // ItemType.flag
    TREE(8, 20, 0),              // ItemType.tree
    ELIXIR_STORAGE(9, 100, 0),   // ItemType.elisirStorage
    ELIXIR_COLLECTOR(10, 100, 0),// ItemType.elisirCollector
    GOLD_STORAGE(11, 100, 0),    // ItemType.goldStorage
    GOLD_COLLECTOR(12, 100, 0);  // ItemType.goldCollector

    private final int prefabIndex;
    private final int goldCost;
    private final int elixirCost;

    BuildingCosts(int prefabIndex, int goldCost, int elixirCost) {
        this.prefabIndex = prefabIndex;
        this.goldCost = goldCost;
        this.elixirCost = elixirCost;
    }

    public static BuildingCosts fromPrefabIndex(int prefabIndex) {
        return Arrays.stream(BuildingCosts.values())
                .filter(type -> type.prefabIndex == prefabIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid prefab index: " + prefabIndex));
    }

    public int getGoldCost() { return goldCost; }
    public int getElixirCost() { return elixirCost; }
    public int getPrefabIndex() { return prefabIndex; }
}