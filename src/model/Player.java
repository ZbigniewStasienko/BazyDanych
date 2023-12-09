package model;

public class Player {
    private String name;
    private String pos;
    private double pts;
    private double ast;
    private double reb;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public double getPts() {
        return pts;
    }

    public void setPts(double pts) {
        this.pts = pts;
    }

    public double getAst() {
        return ast;
    }

    public void setAst(double ast) {
        this.ast = ast;
    }

    public double getReb() {
        return reb;
    }

    public void setReb(double reb) {
        this.reb = reb;
    }
}
