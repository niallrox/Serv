package Foundation;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Integer x; //Значение поля должно быть больше -310, Поле не может быть null
    private int y; //Значение поля должно быть больше -921

    public Coordinates(Integer x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public Integer getX() {
        return x;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
