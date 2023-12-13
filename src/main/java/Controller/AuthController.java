package Controller;

import DAO.ClubDAO;
import DAO.UserDAO;
import DTO.User;
import com.google.gson.Gson;


import static spark.Spark.*;

public class AuthController {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();
    private final UserDAO userDAO;
    private final ClubDAO clubDAO;

    public AuthController(UserDAO userDAO, ClubDAO clubDAO) {
        this.userDAO = userDAO;
        this.clubDAO = clubDAO;
        setupEndpoints();
    }

    public void setupEndpoints() {
        put("/register", "application/json", (request, response) -> {
            //response.type("application/json");
            String jsonUser = request.body();

            User userRegister = gson.fromJson(jsonUser, User.class);

            Boolean result = authService.register(userRegister.getEmail(), userRegister.getPassword(),
                    userRegister.getName(), "USER", userDAO, clubDAO);

            if (!result) {
                response.status(409); // Conflict - user already exists
                return "User already exists";
            }

            response.status(200);
            //return "User created";
            return gson.toJson(userRegister);

        });

        put("/login", "application/json", (request, response) -> {
            //response.type("application/json");
            String jsonUser = request.body();

            User userLogin = gson.fromJson(jsonUser, User.class);

            User user = authService.authenticate(userLogin.getEmail(), userLogin.getPassword(), userDAO);

            if (user == null) {
                response.status(401);
                return "Unauthorized";
            }

            response.status(200);
            //return "Logged in";
            return gson.toJson(user);
        });
    }
}
