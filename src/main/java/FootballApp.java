import Controller.*;
import DAO.ClubDAO;
import DAO.GameDAO;
import DAO.PlayerDAO;
import DAO.UserDAO;
import com.google.gson.Gson;

import java.sql.Connection;

import static spark.Spark.*;
import static spark.Spark.post;

public class FootballApp {
    private static Gson gson = new Gson();
    public static void main(String[] args) {
        Connection connection = Database.getConnection();

        try {
            port(8081);

            ClubDAO clubDAO = new ClubDAO(connection);
            GameDAO gameDAO = new GameDAO(connection);
            PlayerDAO playerDAO = new PlayerDAO(connection);
            UserDAO userDAO = new UserDAO(connection);


            ClubController clubController = new ClubController(clubDAO);
            GameController gameController = new GameController(gameDAO, clubDAO);
            PlayerController playerController = new PlayerController(playerDAO, userDAO, clubDAO);
            UserController userController = new UserController(userDAO, clubDAO, gameDAO);
            AuthController authController = new AuthController(userDAO, clubDAO);

            // Konfiguracja CORS
            options("/*", (request, response) -> {
                String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                if (accessControlRequestHeaders != null) {
                    response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                }

                String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                if (accessControlRequestMethod != null) {
                    response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                }

                return "OK";
            });

            before((request, response) -> {
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Request-Method", "GET, POST, PUT, DELETE, OPTIONS");
                response.header("Access-Control-Allow-Headers", "*");
                // response.header("Access-Control-Allow-Credentials", "true"); // Opcjonalnie, jeśli używasz ciasteczek, dodaj to
            });

            init();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
