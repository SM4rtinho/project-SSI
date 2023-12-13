package Controller.auth;

import DTO.User;
import org.apache.commons.codec.digest.DigestUtils;
import DAO.UserDAO;

public class AuthService {
    public static User authenticate(String email, String password, UserDAO userDao) {
        User user = userDao.getUserByEmail(email);

        if (user != null && isPasswordCorrect(password, user)) {
            return user;
        }

        return null;
    }

    public static User getFromToken(String token, UserDAO userDAO){
        String[] credentials = extractCredentials(token);
        User user = authenticate(credentials[0],credentials[1],userDAO);
        return user;
    }

    public static String[] extractCredentials(String encodedHeader){
        if (encodedHeader != null) {
            return encodedHeader.split(":");
        } else {
            return null;
        }
    }

    public static Boolean register(String email, String password, String name, String role, UserDAO userDao) {
        if (userDao.getUserByEmail(email) != null) {
            // User already exists
            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setPassword(hashPassword(password));

        userDao.createUser(user);
        return true;
    }

    private static Boolean isPasswordCorrect(String providedPassword, User user) {
        return hashPassword(providedPassword).equals(user.getPassword());
    }

    private static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
}