package Man;

public class player {
    double x, y;
    int score;

    public int getScore() {
        return score;
    }

    public player(double x, double y) {
        this.x = x;
        this.y = y;
        score = 0;
    }
    public void incrementScore() {
        score++;
    }
    public void set(double nx, double ny) {
        x = nx;
        y = ny;
    }
}
