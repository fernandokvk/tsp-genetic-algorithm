package models;

public class City implements Comparable<City>{
    private final int n;
    private final int x;
    private final int y;

    public City() {
        this.n = -1;
        this.x = -1;
        this.y = -1;
    }

    public City(int n, int x, int y) {
        this.n = n;
        this.x = x;
        this.y = y;
    }

    public int getN() {
        return n;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(City o) {
        return Integer.compare(this.n, o.n);
    }
}
