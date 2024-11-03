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
}