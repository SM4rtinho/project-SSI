package DTO;

import java.sql.Date;

public class User {
    private Integer id;
    private Date curr_date;
    private String email;
    private String name;
    private String password;
    private String role;
    private long club_id;

    public User() {
    }
    public User(Integer id, Date curr_date, String email, String name, String password, String role, long club_id) {
        this.id = id;
        this.curr_date = curr_date;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.club_id = club_id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCurr_date() {
        return curr_date;
    }

    public void setCurr_date(Date curr_date) {
        this.curr_date = curr_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getClub_id() {
        return club_id;
    }

    public void setClub_id(long club_id) {
        this.club_id = club_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", curr_date=" + curr_date +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", club_id=" + club_id +
                '}';
    }
}