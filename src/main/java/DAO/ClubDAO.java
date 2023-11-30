package DAO;
import DTO.Club;
import DTO.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {
    private Connection connection;

    public ClubDAO(Connection connection) {
        this.connection = connection;
    }

    public void createClub(Club club) {
        String sql = "INSERT INTO club (name, matches_played, matches_won, matches_draw, matches_lost, points, user_id, grade, budget) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, club.getName());
            statement.setInt(2, club.getMatchesPlayed());
            statement.setInt(3, club.getMatchesWon());
            statement.setInt(4, club.getMatchesDraw());
            statement.setInt(5, club.getMatchesLost());
            statement.setInt(6, club.getPoints());
            statement.setInt(7, club.getUser());
            statement.setInt(8, club.getGrade());
            statement.setInt(9, club.getBudget());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClub(Club club) {
        String sql = "UPDATE club SET name=?, matches_played=?, matches_won=?, matches_draw=?, matches_lost=?, points=?, user_id=?, grade=?, budget=? WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, club.getName());
            statement.setInt(2, club.getMatchesPlayed());
            statement.setInt(3, club.getMatchesWon());
            statement.setInt(4, club.getMatchesDraw());
            statement.setInt(5, club.getMatchesLost());
            statement.setInt(6, club.getPoints());
            statement.setInt(7, club.getUser());
            statement.setInt(8, club.getGrade());
            statement.setInt(9, club.getBudget());
            statement.setLong(10, club.getId()); // używamy id do określenia, którego klubu zaktualizować
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClub(long id) {
        String sql = "DELETE FROM club WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Club getClubById(long id) {
        String sql = "SELECT * FROM club WHERE id = ?";
        Club club = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                club = mapResultSetToClub(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return club;
    }

    public List<Club> getAllClubs() {
        String sql = "SELECT * FROM club";
        List<Club> clubList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Club club = mapResultSetToClub(resultSet);
                clubList.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clubList;
    }

    private Club mapResultSetToClub(ResultSet resultSet) throws SQLException {
        Club club = new Club();
        club.setId(resultSet.getInt("id"));
        club.setName(resultSet.getString("name"));
        club.setMatchesPlayed(resultSet.getInt("matches_played"));
        club.setMatchesWon(resultSet.getInt("matches_won"));
        club.setMatchesDraw(resultSet.getInt("matches_draw"));
        club.setMatchesLost(resultSet.getInt("matches_lost"));
        club.setPoints(resultSet.getInt("points"));
        UserDAO userDao = new UserDAO(connection);
        User user = userDao.getUserById(resultSet.getLong("user_id"));
        club.setUser(user.getId());
        club.setGrade(resultSet.getInt("grade"));
        club.setBudget(resultSet.getInt("budget"));

        return club;
    }
}
