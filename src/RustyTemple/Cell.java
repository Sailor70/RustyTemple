package RustyTemple;

public class Cell {

    private boolean occupated = false;
    private boolean left = false;
    private boolean right = false;
    private boolean top = false;
    private boolean bottom = false;
    private boolean predator = false;
    private boolean feeder = false;

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public boolean ocupated() {
        return occupated;
    }

    public void visit() {
        occupated = true;
    }

    public void leave() {
        occupated = false;
    }

    public void makeWall(String step) {

        switch (step) {

            case "left":
                left = true;
                break;
            case "right":
                right = true;
                break;
            case "top":
                top = true;
                break;
            case "bottom":
                bottom = true;
                break;

        }
    }

    public boolean getPredator() {
        return this.predator;
    }

    public void setPredator(boolean state) {
        predator = state;
    }

    public void setFeeder(boolean state) {
        feeder = state;
    }

    public boolean getFeeder() {
        return this.feeder;
    }
}
