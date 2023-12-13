import Controller.AuthService;
import DAO.UserDAO;
import DTO.User;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static spark.Spark.*;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    private static Map<String, User> users = new HashMap<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(8081);

        // Inicjalizacja połączenia z bazą danych SQLite
        Connection dbConnection = Database.getConnection();
        UserDAO userDao = new UserDAO(dbConnection);



    }
}
