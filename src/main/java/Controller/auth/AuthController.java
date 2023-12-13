package Controller.auth;

import DAO.UserDAO;
import DTO.User;
import Controller.auth.AuthService;
import com.google.gson.Gson;

import static spark.Spark.post;

public class AuthController {

    private final Gson gson = new Gson();

    private final UserDAO userDAO;

    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
        setupEndpoints();

    }

    private void setupEndpoints() {
        post("/autheticate", "application/json", (request, response) -> {
            String email = request.attribute("email");
            String password = request.attribute("passowrd");
            User user = AuthService.authenticate(email,password,userDAO);
            if(user != null){
                return gson.toJson(email+":"+password);
            }
            return null;
        });
    }
}
