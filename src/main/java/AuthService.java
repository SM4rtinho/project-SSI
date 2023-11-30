import DTO.User;
import org.apache.commons.codec.digest.DigestUtils;
import DAO.UserDAO;

public class AuthService {
    public User authenticate(String email, String password, UserDAO userDao) {
        User user = userDao.getUserByEmail(email);

        if (user != null && isPasswordCorrect(password, user)) {
            return user;
        }

        return null;
    }

    public Boolean register(String email, String password, String name, String role, UserDAO userDao) {
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

    private Boolean isPasswordCorrect(String providedPassword, User user) {
        return hashPassword(providedPassword).equals(user.getPassword());
    }

    private String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
}