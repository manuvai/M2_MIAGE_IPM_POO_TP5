package res.model.animal;

import res.vue.observer.Observateur;

public class Animal implements Observateur {
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

    @Override
    public void update() {
        setX(getX() + getxDir());
        setY(getY() + getyDir());
    }
}
