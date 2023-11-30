package DAO;

import DTO.Club;
import DTO.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private Connection connection;

    public PlayerDAO(Connection connection) {
        this.connection = connection;
    }

    public void createPlayer(Player player) {
        String sql = "INSERT INTO player (first_name, last_name, speed, shooting, defending, passing, " +
                "price, is_taken, is_first_xi, club_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getFirstName());
            statement.setString(2, player.getLastName());
            statement.setInt(3, player.getSpeed());
            statement.setInt(4, player.getShooting());
            statement.setInt(5, player.getDefending());
            statement.setInt(6, player.getPassing());
            statement.setInt(7, player.getPrice());
            statement.setBoolean(8, player.isTaken());
            statement.setBoolean(9, player.isFirstXI());
            statement.setInt(10, player.getClub());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(Player player) {
        String sql = "UPDATE player SET first_name=?, last_name=?, speed=?, shooting=?, defending=?, " +
                "passing=?, price=?, is_taken=?, is_first_xi=?, club_id=? WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getFirstName());
            statement.setString(2, player.getLastName());
            statement.setInt(3, player.getSpeed());
            statement.setInt(4, player.getShooting());
            statement.setInt(5, player.getDefending());
            statement.setInt(6, player.getPassing());
            statement.setInt(7, player.getPrice());
            statement.setBoolean(8, player.isTaken());
            statement.setBoolean(9, player.isFirstXI());
            statement.setInt(10, player.getClub());
            statement.setLong(11, player.getId()); // używamy id do określenia, którego zawodnika zaktualizować
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayer(long id) {
        String sql = "DELETE FROM player WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayerById(long id) {
        String sql = "SELECT * FROM player WHERE id = ?";
        Player player = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                player = mapResultSetToPlayer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return player;
    }

    public List<Player> getAllPlayers() {
        String sql = "SELECT * FROM player";
        List<Player> playerList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Player player = mapResultSetToPlayer(resultSet);
                playerList.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playerList;
    }

    private Player mapResultSetToPlayer(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        player.setId(resultSet.getInt("id"));
        player.setFirstName(resultSet.getString("first_name"));
        player.setLastName(resultSet.getString("last_name"));
        player.setSpeed(resultSet.getInt("speed"));
        player.setShooting(resultSet.getInt("shooting"));
        player.setDefending(resultSet.getInt("defending"));
        player.setPassing(resultSet.getInt("passing"));
        player.setPrice(resultSet.getInt("price"));
        player.setTaken(resultSet.getBoolean("is_taken"));
        player.setFirstXI(resultSet.getBoolean("is_first_xi"));
        ClubDAO clubDao = new ClubDAO(connection);
        Club club = clubDao.getClubById(resultSet.getLong("club_id"));
        player.setClub(club.getId());

        return player;
    }
}
