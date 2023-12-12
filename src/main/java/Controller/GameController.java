package Controller;

import DAO.ClubDAO;
import DAO.GameDAO;
import DTO.Club;
import DTO.Game;
import com.google.gson.Gson;
import com.sun.xml.bind.v2.TODO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static spark.Spark.*;

public class GameController {
    private final Gson gson = new Gson();
    private final GameDAO gameDAO;
    private final ClubDAO clubDAO;

    public GameController(GameDAO gameDAO, ClubDAO clubDAO) {
        this.gameDAO = gameDAO;
        this.clubDAO = clubDAO;
        setupEndpoints();
    }

    private void setupEndpoints() {
        // Dodawanie nowego meczu
        post("/game", "application/json", (request, response) -> {
            response.type("application/json");
            Game newGame = gson.fromJson(request.body(), Game.class);
            gameDAO.createGame(newGame);
            return gson.toJson(newGame);
        });

        // Pobieranie wszystkich meczów
        get("/games", "application/json", (request, response) -> {
            response.type("application/json");
            return gson.toJson(gameDAO.getAllGames());
        });

        // Pobieranie meczu po ID
        get("/game/:id", "application/json", (request, response) -> {
            response.type("application/json");
            long gameId = Long.parseLong(request.params(":id"));
            Game game = gameDAO.getGameById(gameId);
            if (game != null) {
                return gson.toJson(game);
            } else {
                response.status(404);
                return "Game not found";
            }
        });

        // Symulacja meczu
        put("/game/:id/simulate", "application/json", (request, response) -> {
            response.type("application/json");
            long gameId = Long.parseLong(request.params(":id"));
            Game game = gameDAO.getGameById(gameId);

            if (game == null) {
                response.status(404);
                return "Game not found";
            }

            // Symulacja meczu
            simulateGame(game);

            return gson.toJson("Game has been simulated.");
        });

        // Generowanie meczów
        get("/generate", "application/json", (request, response) -> {
            response.type("application/json");
            generateGames();
            return gson.toJson("Games have been generated.");
        });

        // Symulacja wszystkich meczów
        put("/games/simulate", "application/json", (request, response) -> {
            response.type("application/json");
            simulateAllGames();
            return gson.toJson("All games have been simulated.");
        });

        // Resetowanie klubów
        put("/clubs/reset", "application/json", (request, response) -> {
            response.type("application/json");
            resetClubs();
            return gson.toJson("All clubs have been reset.");
        });
    }

    private void simulateGame(Game game) {
        if (!game.isPlayed()) {
            // Pobranie klubów z meczu
            Club club1 = clubDAO.getClubById(game.getClub1());
            Club club2 = clubDAO.getClubById(game.getClub2());

            // Implementacja symulacji meczu (analogiczna do kodu w Spring Boot)
            double powerClub1 = club1.getGrade() / 100.0;
            double powerClub2 = club2.getGrade() / 100.0;

            int goalsClub1 = generateGoals(powerClub1);
            int goalsClub2 = generateGoals(powerClub2);

            int shotsClub1 = generateShots(powerClub1);
            int shotsClub2 = generateShots(powerClub2);

            int possessionClub1 = generatePossession(powerClub1);
            int possessionClub2 = 100 - possessionClub1;

            int passesClub1 = generatePasses(powerClub1);
            int passesClub2 = generatePasses(powerClub2);

            // Aktualizacja wyniku i statystyk meczu
            game.setPlayed(true);
            game.setGoalsClub1(goalsClub1);
            game.setGoalsClub2(goalsClub2);
            game.setShotsClub1(shotsClub1);
            game.setShotsClub2(shotsClub2);
            game.setPossessionClub1(possessionClub1);
            game.setPossessionClub2(possessionClub2);
            game.setPassesClub1(passesClub1);
            game.setPassesClub2(passesClub2);

            gameDAO.updateGame(game);

            // Aktualizacja statystyk klubów
            updateClubStatistics(club1, club2, goalsClub1, goalsClub2);

            clubDAO.updateClub(club1);
            clubDAO.updateClub(club2);
        }
    }

//    private void generateGames() {
//        List<Club> clubs = clubDAO.getAllClubs();
//        Date startDate = new Date(); // Zainicjowanie daty na dzisiaj
//
//        for (int i = 0; i < clubs.size() - 1; i++) {
//            Club homeClub = clubs.get(i);
//
//            for (int j = i + 1; j < clubs.size(); j++) {
//                Club awayClub = clubs.get(j);
//
//                Game game = new Game();
//                game.setClub1(homeClub.getId());
//                game.setClub2(awayClub.getId());
//                game.setGameDate(startDate);
//
//                gameDAO.createGame(game);
//
//                // Dodanie 3 dni do daty
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(startDate);
//                calendar.add(Calendar.DAY_OF_YEAR, 3);
//                startDate = calendar.getTime();
//            }
//        }
//    }

    private void generateGames() {
        List<Club> clubs = clubDAO.getAllClubs();
        java.util.Date utilStartDate = new java.util.Date(); // Zainicjowanie daty na dzisiaj

        for (int i = 0; i < clubs.size() - 1; i++) {
            Club homeClub = clubs.get(i);

            for (int j = i + 1; j < clubs.size(); j++) {
                Club awayClub = clubs.get(j);

                Game game = new Game();
                game.setClub1(homeClub.getId());
                game.setClub2(awayClub.getId());

                // Konwersja java.util.Date na java.sql.Date
                Date sqlStartDate = new Date(utilStartDate.getTime());
                game.setGameDate(sqlStartDate);

                gameDAO.createGame(game);

                // Dodanie 3 dni do daty
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(utilStartDate);
                calendar.add(Calendar.DAY_OF_YEAR, 3);
                utilStartDate = calendar.getTime();
            }
        }
    }

    private void simulateAllGames() {
        List<Game> games = gameDAO.getAllGames();

        for (Game game : games) {
            simulateGame(game);
        }
    }

    private void resetClubs() {
        List<Club> clubs = clubDAO.getAllClubs();
        List<Game> games = gameDAO.getAllGames();

        for (Club club : clubs) {
            club.setMatchesPlayed(0);
            club.setMatchesWon(0);
            club.setMatchesDraw(0);
            club.setMatchesLost(0);
            club.setPoints(0);
        }

        for (Game game : games) {
            game.setPlayed(false);
            game.setGoalsClub1(0);
            game.setGoalsClub2(0);
            game.setShotsClub1(0);
            game.setShotsClub2(0);
            game.setPossessionClub1(0);
            game.setPossessionClub2(0);
            game.setPassesClub1(0);
            game.setPassesClub2(0);

            gameDAO.updateGame(game);
        }

        clubDAO.updateAllClubs(clubs);
    }

    private void updateClubStatistics(Club club1, Club club2, int goalsClub1, int goalsClub2) {
        if (goalsClub1 > goalsClub2) {
            // Club 1 wygrał
            club1.setMatchesWon(club1.getMatchesWon() + 1);
            club1.setPoints(club1.getPoints() + 3);
            club2.setMatchesLost(club2.getMatchesLost() + 1);
            club1.setBudget(club1.getBudget() + 1000000);
            club2.setBudget(club2.getBudget() + 250000);
        } else if (goalsClub1 < goalsClub2) {
            // Club 2 wygrał
            club2.setMatchesWon(club2.getMatchesWon() + 1);
            club2.setPoints(club2.getPoints() + 3);
            club1.setMatchesLost(club1.getMatchesLost() + 1);
            club1.setBudget(club1.getBudget() + 250000);
            club2.setBudget(club2.getBudget() + 1000000);
        } else {
            // Remis
            club1.setMatchesDraw(club1.getMatchesDraw() + 1);
            club2.setMatchesDraw(club2.getMatchesDraw() + 1);
            club1.setPoints(club1.getPoints() + 1);
            club2.setPoints(club2.getPoints() + 1);
            club1.setBudget(club1.getBudget() + 500000);
            club2.setBudget(club2.getBudget() + 500000);
        }
    }


    private int generateGoals(double power) {
        // Wykorzystanie mocy klubu w generowaniu liczby goli
        int minGoals = (int) (power * 0); // Minimalna liczba goli (0)
        int maxGoals = (int) (power * 5); // Maksymalna liczba goli (power * 5)
        return generateRandomNumber(minGoals, maxGoals);
    }

    private int generateShots(double power) {
        // Wykorzystanie mocy klubu w generowaniu liczby strzałów
        int minShots = (int) (power * 5); // Minimalna liczba strzałów (power * 5)
        int maxShots = (int) (power * 15); // Maksymalna liczba strzałów (power * 15)
        return generateRandomNumber(minShots, maxShots);
    }

    private int generatePossession(double power) {
        // Wykorzystanie mocy klubu w generowaniu posiadania piłki
        int minPossession = (int) (power * 30); // Minimalne posiadanie piłki (power * 40%)
        int maxPossession = (int) (power * 70); // Maksymalne posiadanie piłki (power * 60%)
        return generateRandomNumber(minPossession, maxPossession);
    }

    private int generatePasses(double power) {
        // Wykorzystanie mocy klubu w generowaniu liczby podań
        int minPasses = (int) (power * 200); // Minimalna liczba podań (power * 200)
        int maxPasses = (int) (power * 500); // Maksymalna liczba podań (power * 400)
        return generateRandomNumber(minPasses, maxPasses);
    }

    private int generateRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
