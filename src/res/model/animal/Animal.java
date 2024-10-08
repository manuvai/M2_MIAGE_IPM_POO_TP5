package res.model.animal;

import res.model.map.Case;

import java.util.Objects;

public class Animal {

    protected static final int MAX_FORWARD = 1;
    protected static final int MIN_BACKWARD = -1;
    protected int x;
    protected int y;
    protected int xDir;
    protected int yDir;

    public Animal(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxDir() {
        return xDir;
    }

    public void setxDir(int xDir) {
        xDir = Math.max(MIN_BACKWARD, Math.min(MAX_FORWARD, xDir));
        this.xDir = xDir;
    }

    public int getyDir() {
        return yDir;
    }

    public void setyDir(int yDir) {
        yDir = Math.max(MIN_BACKWARD, Math.min(MAX_FORWARD, yDir));
        this.yDir = yDir;
    }

    public void move() {
        setX(getX() + getxDir());
        setY(getY() + getyDir());
    }

    public void goToCase(Case meilleureEchapatoire) {
        if (Objects.nonNull(meilleureEchapatoire)) {
            int nextXDir = meilleureEchapatoire.getX() - getX();
            int nextYDir = meilleureEchapatoire.getY() - getY();

            setxDir(nextXDir);
            setyDir(nextYDir);
        }
    }

    public Direction getDirection() {
        Direction direction = Direction.IDLE;

        if (getxDir() > 0) {
            direction = Direction.RIGHT;

        } else if (getxDir() < 0) {
            direction = Direction.LEFT;

        } else if (getyDir() > 0) {
            direction = Direction.DOWN;

        } else if (getyDir() < 0) {
            direction = Direction.UP;
        }

        return direction;
    }

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN,
        IDLE
    }
}
