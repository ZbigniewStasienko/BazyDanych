package gui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainApplication extends JFrame implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private Datasource datasource;
    private String refresh;
    private int clubsViewed = 0;
    JButton deleteButton = new JButton("Delete");
    JMenuBar menuBar = new JMenuBar();
    JMenu menuViews = new JMenu("View");
    JMenuItem menuPoints = new JMenuItem("Players sorted by points");
    JMenuItem menuRebounds = new JMenuItem("Players sorted by rebounds");
    JMenuItem menuAssists = new JMenuItem("Players sorted by assists");
    JMenuItem menuClubsList = new JMenuItem("List of Clubs");
    JButton openPlayerReaderButton = new JButton("Add Player");
    JButton openPlayerUpdateButton = new JButton("Update Player");
    JButton showRoster = new JButton("Show Roster");
    public static JButton refreshButton = new JButton("Refresh");
    JButton closeButton = new JButton("Close");

    //Constructor of the class initializes connection with database,
    //handles closing and initialize components of GUI window
    public MainApplication() {

        datasource = new Datasource();
        // Exiting the program due to failure to open the database connection
        if (!datasource.open()) {
            JOptionPane.showMessageDialog(this, "Could not open connection with database", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setTitle("Main Application");

        // Closing database connection when window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                datasource.close();
            }
        });

        setSize(800, 400);

        // Initializing GUI components
        initComponents();
    }

    // Method to initialize GUI components
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        setJMenuBar(menuBar);
        menuBar.add(menuViews);

        menuViews.add(menuPoints);
        menuViews.add(menuAssists);
        menuViews.add(menuRebounds);
        menuViews.add(menuClubsList);

        refreshButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
        openPlayerReaderButton.setFocusPainted(false);
        openPlayerUpdateButton.setFocusPainted(false);
        showRoster.setFocusPainted(false);
        closeButton.setFocusPainted(false);

        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);
        openPlayerReaderButton.addActionListener(this);
        openPlayerUpdateButton.addActionListener(this);
        showRoster.addActionListener(this);
        menuPoints.addActionListener(this);
        menuAssists.addActionListener(this);
        menuRebounds.addActionListener(this);
        menuClubsList.addActionListener(this);
        closeButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(openPlayerReaderButton);
        buttonPanel.add(openPlayerUpdateButton);
        buttonPanel.add(showRoster);
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        // Default view - players sorted by points
        viewPlayers("points_view");
        refresh = "points_view";
    }


    // Handling button events
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        try {
            if (source == deleteButton) {
                if (clubsViewed > 0) {
                    return;
                }
                int index = table.getSelectedRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(this, "No player is selected", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    List<View> currentView = datasource.queryView(refresh);
                    View playerToDel = currentView.get(index);
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to delete: " + playerToDel.getPlayerName() + "?","Warning",dialogButton);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        datasource.deleteStats(playerToDel.getId());
                        datasource.deletePlayer(playerToDel.getId());
                        JOptionPane.showMessageDialog(this, playerToDel.getPlayerName() + " is deleted from database \n Refresh view", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                        refreshButton.setBackground(Color.decode("#65B741"));
                    }
                }
            }

            if (source == refreshButton) {
                if (clubsViewed > 0) {
                    return;
                }
                if (refresh != null) {
                    if (refresh.equals("points_view")) {
                        viewPlayers("points_view");
                    } else if (refresh.equals("assist_view")) {
                        viewPlayers("assist_view");
                    } else if (refresh.equals("rebounds_view")) {
                        viewPlayers("rebounds_view");
                    }
                }
                refreshButton.setBackground(null);
            }

            if (source == openPlayerReaderButton) {
                openPlayerReaderWindow(null, -1);
            }

            if (source == openPlayerUpdateButton) {
                if (clubsViewed > 0) {
                    return;
                }
                int index = table.getSelectedRow();
                if (index < 0) {
                    JOptionPane.showMessageDialog(this, "No player is selected", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    List<View> currentView = datasource.queryView(refresh);
                    View playerToUpdate = currentView.get(index);
                    PlayerToUpdate player = datasource.playerUpdate(playerToUpdate.getId());
                    openPlayerReaderWindow(player, playerToUpdate.getId());
                }
            }

            if (source == showRoster) {
                if (clubsViewed == 1) {
                    int index = table.getSelectedRow();
                    if (index < 0) {
                        JOptionPane.showMessageDialog(this, "No team is selected", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        List<Club> clubs = datasource.clubsList();
                        Club clubToShow = clubs.get(index);
                        viewPlayersFromClub(clubToShow.getClubName());
                        clubsViewed = 2;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "View club first", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (source == menuPoints) {
                viewPlayers("points_view");
                refresh = "points_view";
                clubsViewed = 0;
            }

            if (source == menuAssists) {
                viewPlayers("assist_view");
                refresh = "assist_view";
                clubsViewed = 0;
            }

            if (source == menuRebounds) {
                viewPlayers("rebounds_view");
                refresh = "rebounds_view";
                clubsViewed = 0;
            }

            if (source == menuClubsList) {
                createClubsTable();
                clubsViewed = 1;
            }

            if (source == closeButton) {
                datasource.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to open PlayerReader window
    private void openPlayerReaderWindow(PlayerToUpdate player, int idPlayer) {
        SwingUtilities.invokeLater(() -> {
            PlayerReader playerReader = new PlayerReader(datasource, player, idPlayer);
            playerReader.setVisible(true);
        });
    }

    // Method to view players from a specific club
    private void viewPlayersFromClub(String clubName) {
        List<Player> players = datasource.clubPlayerList(clubName);
        Object[][] data = new Object[players.size()][5];
        int i = 0;
        String[] columnNames = {"Player Name", "Position", "Points", "Assists", "Rebounds"};
        for (Player player : players) {
            data[i] = new Object[]{player.getName(), player.getPos(), player.getPts(), player.getAst(), player.getReb()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
    }

    // Method to create table showing list of clubs
    private void createClubsTable() {

        List<Club> clubs = datasource.clubsList();
        Object[][] data = new Object[clubs.size()][3];
        int i = 0;
        String[] columnNames = {"City Name", "Club Name", "Division"};
        for (Club club : clubs) {
            data[i] = new Object[]{club.getCityName(), club.getClubName(), club.getDivision()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
    }

    // Method to view players based on what parameter user want to sort data by
    private void viewPlayers(String viewName) {
        List<View> views = datasource.queryView(viewName);
        Object[][] data = new Object[views.size()][6];
        int i = 0;
        String[] columnNames = {"ID", "Player Name", "Club Name", "Points", "Assists", "Rebounds"};
        for (View view : views) {
            data[i] = new Object[]{view.getId(), view.getPlayerName(), view.getClub(), view.getPts(), view.getAst(), view.getReb()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApplication().setVisible(true));
    }
}

