package Controller;

import DAO.ClubDAO;
import DAO.GameDAO;
import DAO.UserDAO;
import DTO.Club;
import DTO.User;
import com.google.gson.Gson;
import static spark.Spark.*;

public class UserController {
    private final Gson gson = new Gson();
    private final UserDAO userDAO;
    private final ClubDAO clubDAO;
    private final GameDAO gameDAO;

    public UserController(UserDAO userDAO, ClubDAO clubDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.clubDAO = clubDAO;
        this.gameDAO = gameDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        // Dodawanie nowego użytkownika
        post("/user", "application/json", (request, response) -> {
            response.type("application/json");
            User newUser = gson.fromJson(request.body(), User.class);
            userDAO.createUser(newUser);
            return gson.toJson(newUser);
        });

        // Pobieranie wszystkich użytkowników
        get("/users", "application/json", (request, response) -> {
            response.type("application/json");
            return gson.toJson(userDAO.getAllUsers());
        });

        // Pobieranie użytkownika po ID
        get("/user/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long userId = Long.parseLong(request.params(":id"));
            User user = userDAO.getUserById(userId);
            if (user != null) {
                return gson.toJson(user);
            } else {
                response.status(404);
                return "User not found";
            }
        });

        // Aktualizacja użytkownika
        put("/user/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long userId = Long.parseLong(request.params(":id"));
            User updatedUser = gson.fromJson(request.body(), User.class);

            // Pobierz istniejącego użytkownika na podstawie identyfikatora
            User existingUser = userDAO.getUserById(userId);

            if (existingUser == null) {
                response.status(404);
                return "User not found";
            }

            // Pobierz klub na podstawie przekazanego identyfikatora
            Club club = clubDAO.getClubById(updatedUser.getClub_id());

            if (club == null) {
                response.status(404);
                return "Club not found";
            }

            // Zaktualizuj dane użytkownika
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());

            // Przypisz klub do użytkownika
            existingUser.setClub_id(club.getId());

            // Zapisz zaktualizowanego użytkownika w bazie danych
            userDAO.updateUser(existingUser);

            return gson.toJson(existingUser);
        });

        // Usunięcie użytkownika
        delete("/user/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long userId = Long.parseLong(request.params(":id"));
            if (userDAO.getUserById(userId) == null) {
                response.status(404);
                return "User not found";
            }
            userDAO.deleteUser(userId);
            return "User with id " + userId + " has been deleted.";
        });
    }
}
