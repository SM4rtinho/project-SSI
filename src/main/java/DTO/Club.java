package DTO;
import java.util.List;

public class Club {
    private Integer id;
    private String name;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesDraw;
    private int matchesLost;
    private int points;
    private List<Player> players;
    private int grade;
    private int budget;

    public Club() {
    }

    public Club(Integer id, String name, int matchesPlayed, int matchesWon, int matchesDraw, int matchesLost, int points, List<Player> players, int grade, int budget) {
        this.id = id;
        this.name = name;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDraw = matchesDraw;
        this.matchesLost = matchesLost;
        this.points = points;
        this.players = players;
        this.grade = grade;
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getMatchesDraw() {
        return matchesDraw;
    }

    public void setMatchesDraw(int matchesDraw) {
        this.matchesDraw = matchesDraw;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", matchesPlayed=" + matchesPlayed +
                ", matchesWon=" + matchesWon +
                ", matchesDraw=" + matchesDraw +
                ", matchesLost=" + matchesLost +
                ", points=" + points +
                ", players=" + players +
                ", grade=" + grade +
                ", budget=" + budget +
                '}';
    }
}
