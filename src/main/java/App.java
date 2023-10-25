import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    private static Map<String, User> users = new HashMap<>();
    private static Gson gson = new Gson();

    public static void main(String[] args){

        port(8081);

        post("/user", (request, response) -> {
            String jsonUser = request.body();

            User user = gson.fromJson(jsonUser, User.class);

            if (user == null){
                response.status(400);

                return "User not created";
            }
            String id = UUID.randomUUID().toString();

            user.setId(id);

            users.put(id, user);

            response.status(200);

            return  "User created with ID " + id;
        });

        get("/users", (request, response) ->
            gson.toJson(users.values()));

        get("/test", (request, response) ->
                {
                    String message = "xd";
                    try{
                        Class.forName("org.sqlite.JDBC");
                        String jdbcUrl = "jdbc:sqlite:D:\\Nowy folder\\intelJ\\FB123\\src\\main\\java\\com\\example\\fb123\\football2.db";
                        Connection connection = DriverManager.getConnection(jdbcUrl);
                        String sql = "SELECT * from club";

                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery(sql);

                        while (result.next()){
                            String id = result.getString("id");
                            String name = result.getString("name");

                            message = id + " | " + name + "\n";
                        }

                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    return message;
                });

    }
}
