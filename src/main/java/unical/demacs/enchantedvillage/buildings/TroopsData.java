package unical.demacs.enchantedvillage.buildings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TroopsData {
    @JsonProperty("_x")
    private float x;

    @JsonProperty("_y")
    private float y;

    @JsonProperty("_z")
    private float z;

    @JsonProperty("_type")
    private int type;

    @Override
    public String toString() {
        return "TroopsData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TroopsData that = (TroopsData) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = (int) x;
        result = (int) (31 * result + y);
        result = (int) (31 * result + z);
        result = 31 * result + type;
        return result;
    }

}
