package Controller;

import DAO.ClubDAO;
import DTO.Club;
import com.google.gson.Gson;
import static spark.Spark.*;

public class ClubController {
    private final Gson gson = new Gson();
    private final ClubDAO clubDAO;

    public ClubController(ClubDAO clubDAO) {
        this.clubDAO = clubDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        // Dodawanie nowego klubu
        post("/club", "application/json", (request, response) -> {
            response.type("application/json");
            Club newClub = gson.fromJson(request.body(), Club.class);
            clubDAO.createClub(newClub);
            return gson.toJson(newClub);
        });

        // Pobieranie wszystkich klubów
        get("/clubs", "application/json", (request, response) -> {
            response.type("application/json");
            return gson.toJson(clubDAO.getAllClubs());
        });

        // Pobieranie klubu po ID
        get("/club/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long clubId = Long.parseLong(request.params(":id"));
            Club club = clubDAO.getClubById(clubId);
            if (club != null) {
                return gson.toJson(club);
            } else {
                response.status(404);
                return "Club not found";
            }
        });

        // Aktualizacja klubu
        put("/club/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long clubId = Long.parseLong(request.params(":id"));
            Club updatedClub = gson.fromJson(request.body(), Club.class);

            // Pobierz istniejący klub na podstawie identyfikatora
            Club existingClub = clubDAO.getClubById(clubId);

            if (existingClub == null) {
                response.status(404);
                return "Club not found";
            }

            // Zaktualizuj dane klubu
            existingClub.setName(updatedClub.getName());
            existingClub.setMatchesPlayed(updatedClub.getMatchesPlayed());
            existingClub.setMatchesWon(updatedClub.getMatchesWon());
            existingClub.setMatchesLost(updatedClub.getMatchesLost());
            existingClub.setMatchesDraw(updatedClub.getMatchesDraw());
            existingClub.setPoints(updatedClub.getPoints());

            // Zapisz zaktualizowany klub w bazie danych
            clubDAO.updateClub(existingClub);

            return gson.toJson(existingClub);
        });

        // Edycja klubu
        put("/clubedit/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long clubId = Long.parseLong(request.params(":id"));
            Club updatedClub = gson.fromJson(request.body(), Club.class);

            // Pobierz istniejący klub na podstawie identyfikatora
            Club existingClub = clubDAO.getClubById(clubId);

            if (existingClub == null) {
                response.status(404);
                return "Club not found";
            }

            // Zaktualizuj dane klubu
            existingClub.setName(updatedClub.getName());
            existingClub.setGrade(updatedClub.getGrade());
            existingClub.setBudget(updatedClub.getBudget());

            // Zapisz zaktualizowany klub w bazie danych
            clubDAO.updateClub(existingClub);

            return gson.toJson(existingClub);
        });

        // Upgrade klubu
        put("/clubup/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long clubId = Long.parseLong(request.params(":id"));

            // Pobierz istniejący klub na podstawie identyfikatora
            Club existingClub = clubDAO.getClubById(clubId);

            if (existingClub == null) {
                response.status(404);
                return "Club not found";
            }

            // Zaktualizuj poziom klubu
            int temp = (existingClub.getGrade() < 99) ? existingClub.getGrade() + 1 : 99;
            existingClub.setGrade(temp);

            // Zapisz zaktualizowany klub w bazie danych
            clubDAO.updateClub(existingClub);

            return gson.toJson(existingClub);
        });
    }
}
