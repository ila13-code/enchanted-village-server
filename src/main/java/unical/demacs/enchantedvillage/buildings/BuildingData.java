package unical.demacs.enchantedvillage.buildings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingData {
    private String uniqueId;
    private int prefabIndex;
    private int x;
    private int y;
    private List<TroopsData> troopsData;
    private int health;
    private boolean isDefensive;
    private int attackRange;
    private int attackDamage;

    @Override
    public String toString() {
        return "BuildingData{" +
                "uniqueId='" + uniqueId + '\'' +
                ", prefabIndex=" + prefabIndex +
                ", x=" + x +
                ", y=" + y +
                ", troopsData=" + troopsData +
                ", health=" + health +
                ", isDefensive=" + isDefensive +
                ", attackRange=" + attackRange +
                ", attackDamage=" + attackDamage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingData that = (BuildingData) o;

        if (prefabIndex != that.prefabIndex) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (health != that.health) return false;
        if (isDefensive != that.isDefensive) return false;
        if (attackRange != that.attackRange) return false;
        if (attackDamage != that.attackDamage) return false;
        if (uniqueId != null ? !uniqueId.equals(that.uniqueId) : that.uniqueId != null) return false;
        return troopsData != null ? troopsData.equals(that.troopsData) : that.troopsData == null;
    }

    @Override
    public int hashCode() {
        int result = uniqueId != null ? uniqueId.hashCode() : 0;
        result = 31 * result + prefabIndex;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + (troopsData != null ? troopsData.hashCode() : 0);
        result = 31 * result + health;
        result = 31 * result + (isDefensive ? 1 : 0);
        result = 31 * result + attackRange;
        result = 31 * result + attackDamage;
        return result;
    }

}
