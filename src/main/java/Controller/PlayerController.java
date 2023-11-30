package Controller;

import DAO.ClubDAO;
import DAO.PlayerDAO;
import DAO.UserDAO;
import DTO.Club;
import DTO.Player;
import DTO.User;
import com.google.gson.Gson;

import java.util.Random;

import static spark.Spark.*;

public class PlayerController {
    private final Gson gson = new Gson();
    private final PlayerDAO playerDAO;
    private final UserDAO userDAO;
    private final ClubDAO clubDAO;

    public PlayerController(PlayerDAO playerDAO, UserDAO userDAO, ClubDAO clubDAO) {
        this.playerDAO = playerDAO;
        this.userDAO = userDAO;
        this.clubDAO = clubDAO;
        setupEndpoints();
    }

    private Void trainPlayer(long playerId, String type) throws Exception {
        Player player = playerDAO.getPlayerById(playerId);

        if (player != null) {
            switch (type) {
                case "shooting":
                    player.setShooting(Math.min(player.getShooting() + 1, 99));
                    break;
                case "passing":
                    player.setPassing(Math.min(player.getPassing() + 1, 99));
                    break;
                case "defending":
                    player.setDefending(Math.min(player.getDefending() + 1, 99));
                    break;
                case "speed":
                    player.setSpeed(Math.min(player.getSpeed() + 1, 99));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid training type.");
            }

            playerDAO.updatePlayer(player);
        }

        throw new Exception("Invalid player ID.");
    }

    private void setupEndpoints() {
        // Dodawanie nowego gracza
        post("/player", "application/json", (request, response) -> {
            response.type("application/json");
            Player newPlayer = gson.fromJson(request.body(), Player.class);
            playerDAO.createPlayer(newPlayer);
            return gson.toJson(newPlayer);
        });

        // Dodawanie nowego gracza z losowymi statystykami
        post("/add-player", "application/json", (request, response) -> {
            response.type("application/json");
            Player newPlayer = gson.fromJson(request.body(), Player.class);
            // Ustawianie losowych statystyk gracza
            Random rand = new Random();
            newPlayer.setPassing(rand.nextInt(89) + 10);
            newPlayer.setShooting(rand.nextInt(89) + 10);
            newPlayer.setDefending(rand.nextInt(89) + 10);
            newPlayer.setSpeed(rand.nextInt(89) + 10);
            newPlayer.setPrice(rand.nextInt(9000000) + 1000000);
            newPlayer.setClub(null);
            newPlayer.setTaken(false);
            newPlayer.setFirstXI(false);
            playerDAO.createPlayer(newPlayer);
            return gson.toJson(newPlayer);
        });

        // Pobieranie wszystkich graczy
        get("/players", "application/json", (request, response) -> {
            response.type("application/json");
            return gson.toJson(playerDAO.getAllPlayers());
        });

        // Pobieranie gracza po ID
        get("/player/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long playerId = Long.parseLong(request.params(":id"));
            Player player = playerDAO.getPlayerById(playerId);
            if (player != null) {
                return gson.toJson(player);
            } else {
                response.status(404);
                return "Player not found";
            }
        });

        // Kupowanie gracza
        put("/players/:Ids/buy", "application/json", (request, response) -> {
            response.type("application/json");
            Long[] Ids = gson.fromJson(request.params(":Ids"), Long[].class);

            // Pobierz gracza na podstawie ID
            Player player = playerDAO.getPlayerById(Ids[0]);

            if (player != null && !player.isTaken()) {
                User user = userDAO.getUserById(Ids[1]);

                if (user != null) {
                    //Club club = user.getClub_id();
                    Club club = clubDAO.getClubById(user.getClub_id());

                    if (player.getPrice() <= club.getBudget()) {
                        player.setTaken(true);
                        club.setBudget(club.getBudget() - player.getPrice());
                        club.getPlayers().add(player);
                        player.setClub(club.getId());
                        playerDAO.updatePlayer(player);
                        userDAO.updateUser(user);

                        return gson.toJson("Player has been bought.");
                    } else {
                        response.status(400);
                        return gson.toJson("Not enough budget to buy this player.");
                    }
                }
            }

            response.status(404);
            return gson.toJson("Player not found or already taken.");
        });

        // Sprzedaż gracza
        put("/players/:Ids/sell", "application/json", (request, response) -> {
            response.type("application/json");
            Long[] Ids = gson.fromJson(request.params(":Ids"), Long[].class);

            // Pobierz gracza na podstawie ID
            Player player = playerDAO.getPlayerById(Ids[0]);

            if (player != null && player.isTaken()) {
                User user = userDAO.getUserById(Ids[1]);

                if (user != null) {
                    //Club club = user.getClub();
                    Club club = clubDAO.getClubById(user.getClub_id());

                    player.setTaken(false);
                    player.setFirstXI(false);
                    club.setBudget((int) (club.getBudget() + player.getPrice() * 0.8));
                    club.getPlayers().remove(player);
                    player.setClub(null);
                    playerDAO.updatePlayer(player);
                    userDAO.updateUser(user);

                    return gson.toJson("Player has been sold.");
                }
            }

            response.status(404);
            return gson.toJson("Player not found or not taken.");
        });

        // Usunięcie gracza z pierwszego składu
        put("/players/:id/remove-from-xi", "application/json", (request, response) -> {
            response.type("application/json");
            long playerId = Long.parseLong(request.params(":id"));
            Player player = playerDAO.getPlayerById(playerId);

            if (player != null && player.isFirstXI()) {
                player.setFirstXI(false);
                playerDAO.updatePlayer(player);

                return gson.toJson("Player has been removed from the first XI.");
            }

            response.status(400);
            return gson.toJson("Player is not in the first XI.");
        });

        // Przeniesienie gracza do pierwszego składu
        put("/player/:id/moveToXI", "application/json", (request, response) -> {
            response.type("application/json");
            long playerId = Long.parseLong(request.params(":id"));
            Player player = playerDAO.getPlayerById(playerId);

            if (player != null) {
                player.setFirstXI(true);
                playerDAO.updatePlayer(player);

                return gson.toJson("Player moved to XI successfully.");
            }

            response.status(404);
            return gson.toJson("Player not found.");
        });

        // Trening gracza
        put("/playerstat/:id/:type", "application/json", (request, response) -> {
            response.type("application/json");
            long playerId = Long.parseLong(request.params(":id"));
            String type = request.params(":type");

            try {
                Player trainedPlayer = null;
                trainPlayer(playerId, type);
                return gson.toJson(trainedPlayer);
            } catch (Exception e) {
                response.status(400);
                return gson.toJson("Invalid player ID.");
            }
        });

    }
}
