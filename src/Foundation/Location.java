package Foundation;

import java.io.Serializable;

public class Location implements Serializable {
    private float x;
    private double y;
    private int z;
    private String name; //Поле не может быть null

    public Location(float x, double y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name=" + name +
                '}';
    }
}
