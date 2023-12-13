package Controller;

import DAO.ClubDAO;
import DTO.Club;
import DTO.User;
import org.apache.commons.codec.digest.DigestUtils;
import DAO.UserDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AuthService {
    public User authenticate(String email, String password, UserDAO userDao) {
        User user = userDao.getUserByEmail(email);

        if (user != null && isPasswordCorrect(password, user)) {
//            user.setName(name);
//            user.setRole(role);
            return user;
        }
        return null;
    }

    public Boolean register(String email, String password, String name, String role, UserDAO userDao, ClubDAO clubDAO) {
        if (userDao.getUserByEmail(email) != null) {
            // User already exists
            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setPassword(hashPassword(password));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(LocalDate.now().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.setCurr_date(new java.sql.Date(parsedDate.getTime()));

        List<Club> clubs = clubDAO.getAllClubs();

        // Sprawdź, który klub nie jest zajęty
        for (Club club : clubs) {
            if (!club.isOccupied()) {
                user.setClub_id(club.getId());
                userDao.createUser(user);
                // Oznacz klub jako zajęty
                club.setOccupied(true);
                clubDAO.updateClub(club);
                return true;
            }
        }

//        userDao.createUser(user);
//        return true;
        return false;
    }

    private Boolean isPasswordCorrect(String providedPassword, User user) {
        return hashPassword(providedPassword).equals(user.getPassword());
    }

    private String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

}