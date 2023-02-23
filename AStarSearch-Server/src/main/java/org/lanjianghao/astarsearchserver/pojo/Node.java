package org.lanjianghao.astarsearchserver.pojo;

public class Node implements Comparable<Node> {
    private Coord coord;
    private Coord parent;
    private double G;
    private double H;

    public Node(Coord coord, Coord parent, double g, double h) {
        this.coord = coord;
        this.parent = parent;
        G = g;
        H = h;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Coord getParent() {
        return parent;
    }

    public void setParent(Coord parent) {
        this.parent = parent;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    @Override
    public int compareTo(Node n) {
        if (n == null) return -1;
        if (G + H > n.G + n.H)
            return 1;
        else if (G + H < n.G + n.H) return -1;
        return 0;
    }
}
