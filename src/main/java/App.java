import DAO.UserDAO;
import DTO.User;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static scala.Console.println;
import static spark.Spark.*;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    private static Map<String, User> users = new HashMap<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(2137);

        // Inicjalizacja połączenia z bazą danych SQLite
        Connection dbConnection = Database.getConnection();
        UserDAO userDao = new UserDAO(dbConnection);

        AuthService authService = new AuthService();

        post("/user", (request, response) -> {
            String jsonUser = request.body();

            User user = gson.fromJson(jsonUser, User.class);

            if (user == null) {
                response.status(400);
                return "Model.User not created";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date parsedDate = dateFormat.parse(LocalDate.now().toString());
            user.setCurr_date(new java.sql.Date(parsedDate.getTime()));
            userDao.createUser(user);

            response.status(200);
            return "Model.User created with ID";
        });

        get("/users", (request, response) -> {

            List<User> users = userDao.getAllUsers();

            return gson.toJson(users);
        });

        post("/authenticate", (request, response) -> {
            String jsonUser = request.body();

            User userLogin = gson.fromJson(jsonUser, User.class);

            User user = AuthService.authenticate(userLogin.getEmail(), userLogin.getPassword(), userDao);

            if (user == null) {
                response.status(401);
                return "Unauthorized";
            }
            response.status(200);
            return gson.toJson(userLogin.getEmail()+":"+userLogin.getPassword());
        });

        post("/register", (request, response) -> {
            String jsonUser = request.body();

            User userRegister = gson.fromJson(jsonUser, User.class);

            Boolean result = AuthService.register(userRegister.getEmail(), userRegister.getPassword(),
                    userRegister.getName(), userRegister.getRole(), userDao);

            if (!result) {
                response.status(409); // Conflict - user already exists
                return "User already exists";
            }

            response.status(200);
            return userRegister.getEmail()+":"+userRegister.getPassword();
        });

        get("/me", "application/json", (request, response) -> {
            String token = request.headers("Authorization").substring(8); //pobranie tokena
            User user = AuthService.getFromToken(token,userDao);
            println(user.toString());
            if (user != null) {
                return gson.toJson(user);
            } else {
                return null;
            }
        });

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    }
}
