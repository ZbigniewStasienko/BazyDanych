package model;
// Class that maps data from database

public class View {
    private String playerName;
    private String club;
    private double pts;
    private double ast;
    private double reb;
    private String pos;
    private int id;

    public View() {
        this.playerName = null;
        this.club = null;
        this.pts = -1;
        this.ast = -1;
        this.reb = -1;
        this.pos = null;
        this.id = -1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
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
