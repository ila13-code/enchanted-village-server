package unical.demacs.enchantedvillage.utils.gameobject;

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


    @Override
    public String toString() {
        return "BuildingData{" +
                "uniqueId='" + uniqueId + '\'' +
                ", prefabIndex=" + prefabIndex +
                ", x=" + x +
                ", y=" + y +
                ", troopsData=" + troopsData +
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
        return result;
    }

}
