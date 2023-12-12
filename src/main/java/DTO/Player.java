package DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Player {
    private Integer id;
    private String firstName;
    private String lastName;
    private int speed;
    private int shooting;
    private int defending;
    private int passing;
    private int price;
    private boolean isTaken;
    private boolean isFirstXI;
    private long club;

    public Player() {
    }

    public Player(Integer id, String firstName, String lastName, int speed, int shooting, int defending, int passing, int price, boolean isTaken, boolean isFirstXI, Integer club) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.speed = speed;
        this.shooting = shooting;
        this.defending = defending;
        this.passing = passing;
        this.price = price;
        this.isTaken = isTaken;
        this.isFirstXI = isFirstXI;
        this.club = club;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getDefending() {
        return defending;
    }

    public void setDefending(int defending) {
        this.defending = defending;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public boolean isFirstXI() {
        return isFirstXI;
    }

    public void setFirstXI(boolean firstXI) {
        isFirstXI = firstXI;
    }

    public long getClub() {
        return club;
    }

    public void setClub(Integer club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", speed=" + speed +
                ", shooting=" + shooting +
                ", defending=" + defending +
                ", passing=" + passing +
                ", price=" + price +
                ", isTaken=" + isTaken +
                ", isFirstXI=" + isFirstXI +
                ", club=" + club +
                '}';
    }
}
