package unical.demacs.enchantedvillage.buildings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BuildingData {
    @JsonProperty("_uniqueId")
    private String uniqueId;

    @JsonProperty("_prefabIndex")
    private int prefabIndex;

    @JsonProperty("_x")
    private int x;

    @JsonProperty("_y")
    private int y;

    @JsonProperty("_troopsData")
    private List<TroopsData> troopsData;

    private int health;

    private int attackDamage;

    private boolean defensive;

    private int attackRange;

    public String toString() {
        return "BuildingData(uniqueId=" + this.getUniqueId() + ", prefabIndex=" + this.getPrefabIndex() + ", x=" + this.getX() + ", y=" + this.getY() + ", troopsData=" + this.getTroopsData().toString() + ", health=" + this.getHealth() + ", attackDamage=" + this.getAttackDamage() + ", defensive=" + this.isDefensive() + ", attackRange=" + this.getAttackRange() + ")";
    }
}