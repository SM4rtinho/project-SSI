package DAO;

import DTO.Club;
import DTO.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    private Connection connection;

    public GameDAO(Connection connection) {
        this.connection = connection;
    }

    public void createGame(Game game) {
        String sql = "INSERT INTO game (game_date, club_1_id, club_2_id, goals_club1, goals_club2, " +
                "shots_club1, shots_club2, possession_club1, possession_club2, " +
                "passes_club1, passes_club2, is_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, game.getGameDate());
            statement.setLong(2, game.getClub1());
            statement.setLong(3, game.getClub2());
            statement.setInt(4, game.getGoalsClub1());
            statement.setInt(5, game.getGoalsClub2());
            statement.setInt(6, game.getShotsClub1());
            statement.setInt(7, game.getShotsClub2());
            statement.setInt(8, game.getPossessionClub1());
            statement.setInt(9, game.getPossessionClub2());
            statement.setInt(10, game.getPassesClub1());
            statement.setInt(11, game.getPassesClub2());
            statement.setBoolean(12, game.isPlayed());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGame(Game game) {
        String sql = "UPDATE game SET game_date=?, club_1_id=?, club_2_id=?, goals_club1=?, goals_club2=?, " +
                "shots_club1=?, shots_club2=?, possession_club1=?, possession_club2=?, " +
                "passes_club1=?, passes_club2=?, is_played=? WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, game.getGameDate());
            statement.setLong(2, game.getClub1());
            statement.setLong(3, game.getClub2());
            statement.setInt(4, game.getGoalsClub1());
            statement.setInt(5, game.getGoalsClub2());
            statement.setInt(6, game.getShotsClub1());
            statement.setInt(7, game.getShotsClub2());
            statement.setInt(8, game.getPossessionClub1());
            statement.setInt(9, game.getPossessionClub2());
            statement.setInt(10, game.getPassesClub1());
            statement.setInt(11, game.getPassesClub2());
            statement.setBoolean(12, game.isPlayed());
            statement.setLong(13, game.getId()); // używamy id do określenia, którą grę zaktualizować
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGame(long id) {
        String sql = "DELETE FROM game WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Game getGameById(long id) {
        String sql = "SELECT * FROM game WHERE id = ?";
        Game game = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                game = mapResultSetToGame(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    public List<Game> getAllGames() {
        String sql = "SELECT * FROM game";
        List<Game> gameList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Game game = mapResultSetToGame(resultSet);
                gameList.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameList;
    }

    private Game mapResultSetToGame(ResultSet resultSet) throws SQLException {
        Game game = new Game();
        game.setId(resultSet.getInt("id"));
        game.setGameDate(resultSet.getDate("game_date"));
//        ClubDAO clubDAO = new ClubDAO(connection);
//        Club club1 = clubDAO.getClubById(resultSet.getLong("club_1_id"));
//        Club club2 = clubDAO.getClubById(resultSet.getLong("club_2_id"));
//        game.setClub1(club1.getId());
//        game.setClub2(club2.getId());

        // Sprawdź, czy club1 nie jest null przed wywołaniem getId()
        if (resultSet.getObject("club_1_id") != null) {
            Club club1 = new Club();
            club1.setId(resultSet.getInt("club_1_id"));
            // Ustawienia dla Club...
            game.setClub1(club1.getId());
        }

        // Sprawdź, czy club2 nie jest null przed wywołaniem getId()
        if (resultSet.getObject("club_2_id") != null) {
            Club club2 = new Club();
            club2.setId(resultSet.getInt("club_2_id"));
            // Ustawienia dla Club...
            game.setClub2(club2.getId());
        }

        game.setGoalsClub1(resultSet.getInt("goals_club1"));
        game.setGoalsClub2(resultSet.getInt("goals_club2"));
        game.setShotsClub1(resultSet.getInt("shots_club1"));
        game.setShotsClub2(resultSet.getInt("shots_club2"));
        game.setPossessionClub1(resultSet.getInt("possession_club1"));
        game.setPossessionClub2(resultSet.getInt("possession_club2"));
        game.setPassesClub1(resultSet.getInt("passes_club1"));
        game.setPassesClub2(resultSet.getInt("passes_club2"));
        game.setPlayed(resultSet.getBoolean("is_played"));

        return game;
    }
}
