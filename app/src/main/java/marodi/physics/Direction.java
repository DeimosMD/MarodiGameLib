package marodi.physics;

public enum Direction {

    ALL,
    DOWN,
    UP,
    LEFT,
    RIGHT,
    VERTICAL,
    HORIZONTAL,
    NONE;

    boolean isAnyHorizontal() {
        return this == ALL || this == HORIZONTAL || this == RIGHT || this == LEFT;
    }

    boolean isAnyVertical() {
        return this == ALL || this == VERTICAL || this == UP || this == DOWN;
    }
}
