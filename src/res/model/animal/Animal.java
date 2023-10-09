package res.model.animal;

import res.model.map.Case;

import java.util.Objects;

public class Animal {
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
        this.xDir = xDir;
    }

    public int getyDir() {
        return yDir;
    }

    public void setyDir(int yDir) {
        this.yDir = yDir;
    }

    public void move() {
        setX(getX() + getxDir());
        setY(getY() + getyDir());
    }

    public void goToCase(Case meilleureEchapatoire) {
        if (Objects.nonNull(meilleureEchapatoire)) {
            boolean isRight = meilleureEchapatoire.getX() > getX();
            boolean isLeft = meilleureEchapatoire.getX() < getX();

            boolean isUp = meilleureEchapatoire.getY() < getY();
            boolean isDown = meilleureEchapatoire.getY() > getY();

            int nextXDir = 0;

            if (isRight) {
                nextXDir = 1;

            } else if (isLeft) {
                nextXDir = -1;

            }

            int nextYDir = 0;

            if (isUp) {
                nextXDir = -1;

            } else if (isDown) {
                nextXDir = 1;

            }

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
