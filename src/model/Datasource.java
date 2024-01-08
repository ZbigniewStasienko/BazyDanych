package model;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Datasource extends Component {
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

    private static final String PLAYER_TO_UPDATE = "SELECT p.first_name AS 'Name', p.last_name AS 'Surname', p.position AS 'Position', c.city_name || ' ' || c.club_name AS 'Club', s.points AS 'Points', s.assists AS 'Assists', s.rebounds AS 'Rebounds'\n" +
            "FROM players AS p\n" +
            "INNER JOIN stats AS s\n" +
            "ON p.player_id = s.player_id\n" +
            "INNER JOIN clubs AS c\n" +
            "ON c.club_id = p.club_id\n" +
            "WHERE p.player_id = %d;";

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }

        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean addingTransaction(String name, String surname, String pos, int club_id, String pts, String ast, String reb) {
        boolean result = false;
        try {
            int affectedRows = addPlayer(name, surname, pos, club_id);
            if(affectedRows == 1) {
                int statsRow = addStats(pts, ast, reb);
                if(statsRow == 1){
                    result = true;
                } else {
                    List<View> view = queryView("points_view");
                    int id = view.size();
                    deletePlayer(id+1);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Adding player failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public int addPlayer(String name, String surname, String pos, int club_id) {
        String command = String.format(ADD_PLAYER, name, surname, pos, club_id);
        try (Statement statement = conn.createStatement()) {
            int affectedRows = statement.executeUpdate(command);
            return affectedRows;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public int addStats(String pts, String ast, String reb) {
        String[] checkedData = checkData(pts, ast, reb);
        if(Objects.equals(checkedData[0], "t")){
            String command = String.format(ADD_STATS, checkedData[1],checkedData[2], checkedData[3]);
            try (Statement statement = conn.createStatement()) {
                int affectedRows = statement.executeUpdate(command);
                return affectedRows;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Adding stats failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }
    public void deletePlayer(int idPlayer) {
        String command = String.format(DELETE_PLAYER, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteStats(int idPlayer) {
        String command = String.format(DELETE_STATS, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void updateStats(String pts, String ast, String reb, int idPlayer) {
        String command = String.format(UPDATE_STATS, pts, ast, reb, idPlayer);
        try(Statement statement = conn.createStatement()) {
            statement.executeUpdate(command);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public PlayerToUpdate playerUpdate(int idOfPlayer) {
        String command = String.format(PLAYER_TO_UPDATE, idOfPlayer);
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(command)) {
            PlayerToUpdate player = new PlayerToUpdate();
            player.setFirstName(results.getString(1));
            player.setLastName(results.getString(2));
            player.setPos(results.getString(3));
            player.setClub(results.getString(4));
            player.setPts(results.getDouble(5));
            player.setAst(results.getDouble(6));
            player.setReb(results.getDouble(7));
            return player;

        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public String[] checkData(String pts, String ast, String reb){
        String[] output = new String[4];
        output[0] = "f";
        pts = pts.replace(',', '.');
        ast = ast.replace(',', '.');
        reb = reb.replace(',', '.');
        try {
            Double.parseDouble(pts);
            Double.parseDouble(ast);
            Double.parseDouble(reb);
            output[0] = "t";
            output[1] = pts;
            output[2] = ast;
            output[3] = reb;
        } catch (NumberFormatException e) {
            output[0] = "f";
        }
        return output;
    }
}
