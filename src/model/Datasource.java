package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "database.db";
    // public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\zbysz\\Documents\\Studia\\Semestr V\\BD\\repo\\BazyDanych\\" + DB_NAME;
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:..\\BazyDanych\\" + DB_NAME;
    public static final String VIEW = "SELECT * FROM %s;";
    public static final String ADD_PLAYER = "INSERT INTO players (first_name, last_name, position, club_id) VALUES ('%s', '%s', '%s', %d);";
    public static final String ADD_STATS = "INSERT INTO stats (points, assists, rebounds) VALUES (%s, %s, %s);";
    public static final String DELETE_PLAYER = "DELETE FROM players WHERE player_id = %d";
    public static final String DELETE_STATS = "DELETE FROM stats WHERE player_id = %d;";
    public static final String UPDATE_STATS = "UPDATE stats SET points = %s, assists = %s, rebounds = %s WHERE player_id = %d;";
    public static final String CLUB_LIST = "SELECT city_name AS 'City', club_name AS 'Club', division AS 'Division' FROM clubs;";

    public static final String ClUB_PLAYERS = """
            SELECT p.first_name || ' ' || p.last_name AS 'Player',p.position AS 'Position', s.points AS 'Points', s.assists AS 'Assists', s.rebounds AS 'Rebounds'
            FROM players AS p
            INNER JOIN stats AS s
            ON p.player_id = s.player_id
            INNER JOIN clubs AS c
            ON c.club_id = p.club_id
            WHERE c.club_name = '%s'
            ORDER BY p.position;""";

    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }

        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
    public List<View> queryView(String viewName) {
        String command = String.format(VIEW, viewName);
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(command)) {

            List<View> viewElements = new ArrayList<>();
            while(results.next()) {
                View view = new View();
                view.setId(results.getInt(1));
                view.setPlayerName(results.getString(2));
                view.setClub(results.getString(3));
                view.setPts(results.getDouble(4));
                view.setAst(results.getDouble(5));
                view.setReb(results.getDouble(6));
                viewElements.add(view);
            }
            return viewElements;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }

    public List<Club> clubsList() {
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(CLUB_LIST)) {

            List<Club> clubs = new ArrayList<>();
            while(results.next()) {
                Club club = new Club();
                club.setCityName(results.getString(1));
                club.setClubName(results.getString(2));
                club.setDivision(results.getString(3));
                clubs.add(club);
            }
            return clubs;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public void addPlayer(String name, String surname, String pos, int club_id) {
        String command = String.format(ADD_PLAYER, name, surname, pos, club_id);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    public void addStats(String pts, String ast, String reb) {
        String command = String.format(ADD_STATS, pts,ast, reb);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    public void deletePlayer(int idPlayer) {
        String command = String.format(DELETE_PLAYER, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    public void deleteStats(int idPlayer) {
        String command = String.format(DELETE_STATS, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    public void updateStats(String pts, String ast, String reb, int idPlayer) {
        String command = String.format(UPDATE_STATS, pts, ast, reb, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    public List<Player> clubPlayerList(String clubName) {
        String command = String.format(ClUB_PLAYERS, clubName);
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(command)) {

            List<Player> players = new ArrayList<>();
            while(results.next()) {
                Player player = new Player();
                player.setName(results.getString(1));
                player.setPos(results.getString(2));
                player.setPts(results.getDouble(3));
                player.setAst(results.getDouble(4));
                player.setReb(results.getDouble(5));
                players.add(player);
            }
            return players;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}
