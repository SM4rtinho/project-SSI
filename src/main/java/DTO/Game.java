package DTO;

import java.sql.Date;

public class Game {
    private Integer id;
    private Date gameDate;
    private long club1;
    private long club2;
    private int goalsClub1;
    private int goalsClub2;
    private int shotsClub1;
    private int shotsClub2;
    private int possessionClub1;
    private int possessionClub2;
    private int passesClub1;
    private int passesClub2;
    private boolean isPlayed;

    public Game() {
    }

    public Game(Integer id, Date gameDate, Integer club1, Integer club2, int goalsClub1, int goalsClub2, int shotsClub1, int shotsClub2, int possessionClub1, int possessionClub2, int passesClub1, int passesClub2, boolean isPlayed) {
        this.id = id;
        this.gameDate = gameDate;
        this.club1 = club1;
        this.club2 = club2;
        this.goalsClub1 = goalsClub1;
        this.goalsClub2 = goalsClub2;
        this.shotsClub1 = shotsClub1;
        this.shotsClub2 = shotsClub2;
        this.possessionClub1 = possessionClub1;
        this.possessionClub2 = possessionClub2;
        this.passesClub1 = passesClub1;
        this.passesClub2 = passesClub2;
        this.isPlayed = isPlayed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public long getClub1() {
        return club1;
    }

    public void setClub1(Integer club1) {
        this.club1 = club1;
    }

    public long getClub2() {
        return club2;
    }

    public void setClub2(Integer club2) {
        this.club2 = club2;
    }

    public int getGoalsClub1() {
        return goalsClub1;
    }

    public void setGoalsClub1(int goalsClub1) {
        this.goalsClub1 = goalsClub1;
    }

    public int getGoalsClub2() {
        return goalsClub2;
    }

    public void setGoalsClub2(int goalsClub2) {
        this.goalsClub2 = goalsClub2;
    }

    public int getShotsClub1() {
        return shotsClub1;
    }

    public void setShotsClub1(int shotsClub1) {
        this.shotsClub1 = shotsClub1;
    }

    public int getShotsClub2() {
        return shotsClub2;
    }

    public void setShotsClub2(int shotsClub2) {
        this.shotsClub2 = shotsClub2;
    }

    public int getPossessionClub1() {
        return possessionClub1;
    }

    public void setPossessionClub1(int possessionClub1) {
        this.possessionClub1 = possessionClub1;
    }

    public int getPossessionClub2() {
        return possessionClub2;
    }

    public void setPossessionClub2(int possessionClub2) {
        this.possessionClub2 = possessionClub2;
    }

    public int getPassesClub1() {
        return passesClub1;
    }

    public void setPassesClub1(int passesClub1) {
        this.passesClub1 = passesClub1;
    }

    public int getPassesClub2() {
        return passesClub2;
    }

    public void setPassesClub2(int passesClub2) {
        this.passesClub2 = passesClub2;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameDate=" + gameDate +
                ", club1=" + club1 +
                ", club2=" + club2 +
                ", goalsClub1=" + goalsClub1 +
                ", goalsClub2=" + goalsClub2 +
                ", shotsClub1=" + shotsClub1 +
                ", shotsClub2=" + shotsClub2 +
                ", possessionClub1=" + possessionClub1 +
                ", possessionClub2=" + possessionClub2 +
                ", passesClub1=" + passesClub1 +
                ", passesClub2=" + passesClub2 +
                ", isPlayed=" + isPlayed +
                '}';
    }
}
