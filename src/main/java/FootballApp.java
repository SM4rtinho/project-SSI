import Controller.ClubController;
import Controller.GameController;
import Controller.PlayerController;
import Controller.UserController;
import DAO.ClubDAO;
import DAO.GameDAO;
import DAO.PlayerDAO;
import DAO.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;

import static spark.Spark.init;
import static spark.Spark.port;

public class FootballApp {

    public static void main(String[] args) {
        // Inicjalizacja Spark
        // W przypadku, gdybyś nie używał Spark, poniżej jest kod inicjalizujący połączenie z bazą danych
        // Ustaw odpowiednie parametry połączenia z bazą danych (url, użytkownik, hasło)
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

            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
