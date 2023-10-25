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
        UserDao userDao = new UserDao(dbConnection);

        post("/user", (request, response) -> {
            String jsonUser = request.body();

            User user = gson.fromJson(jsonUser, User.class);

            if (user == null) {
                response.status(400);
                return "User not created";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date parsedDate = dateFormat.parse(LocalDate.now().toString());
            user.setCurr_date(new java.sql.Date(parsedDate.getTime()));
            userDao.createUser(user);

            response.status(200);
            return "User created with ID";
        });

        get("/users", (request, response) -> {

            List<User> users = userDao.getAllUsers();

            return gson.toJson(users);
        });
    }
}
