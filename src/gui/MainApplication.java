package gui;

import model.Club;
import model.Datasource;
import model.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainApplication extends JFrame implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private Datasource datasource;
    private String refresh;
    JButton deleteButton = new JButton("Delete");
    JMenuBar menuBar = new JMenuBar();
    JMenu menuViews = new JMenu("View");
    JMenuItem menuPoints = new JMenuItem("Players sorted by points");
    JMenuItem menuRebounds = new JMenuItem("Players sorted by rebounds");
    JMenuItem menuAssists = new JMenuItem("Players sorted by assists");
    JMenuItem menuClubsList = new JMenuItem("List of Clubs");
    JButton openPlayerReaderButton = new JButton("Add Player");
    JButton createTableButton = new JButton("Create Example Table");
    JButton refreshButton = new JButton();
    JButton closeButton = new JButton("Close");


    public MainApplication() {

        datasource = new Datasource();
        if(!datasource.open()) {
            JOptionPane.showMessageDialog(this, "Could not open connection with database", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        setTitle("Main Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        setJMenuBar(menuBar);
        menuBar.add(menuViews);

        menuViews.add(menuPoints);
        menuViews.add(menuAssists);
        menuViews.add(menuRebounds);
        menuViews.add(menuClubsList);

        try {
            Image img = ImageIO.read(getClass().getResource("../image/refresh.png"));
            Image newimage = img.getScaledInstance(24, 22, DO_NOTHING_ON_CLOSE);
            refreshButton.setIcon(new ImageIcon(newimage));
            
        } catch (Exception e) {
            System.out.println(e);
        }

        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);
        openPlayerReaderButton.addActionListener(this);
        createTableButton.addActionListener(this);
        menuPoints.addActionListener(this);
        menuAssists.addActionListener(this);
        menuRebounds.addActionListener(this);
        menuClubsList.addActionListener(this);
        closeButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(openPlayerReaderButton);
        buttonPanel.add(createTableButton);
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        try {
            if (source == deleteButton) {
                int index = table.getSelectedRow();
                if (index<0) {
                    JOptionPane.showMessageDialog(this, "No player is selected", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    List <View> currentView = datasource.queryView(refresh);
                    View playerToDel = currentView.get(index);
                    datasource.deleteStats(playerToDel.getId());
                    datasource.deletePlayer(playerToDel.getId());
                }
            }

            if (source == refreshButton){
                if(refresh != null){
                    if (refresh == "points_view"){
                        createMonthsTable("points_view");
                    }
                    else if(refresh == "assist_view"){
                        createMonthsTable("assist_view");
                    }
                    else if(refresh == "rebounds_view"){
                        createMonthsTable("rebounds_view");
                    }
                }
            }

            if (source == openPlayerReaderButton) {
                openPlayerReaderWindow();
            }

            if (source == createTableButton) {
                createExampleTable();
            }

            if (source == menuPoints) {
                createMonthsTable("points_view");
                refresh = "points_view";
            }

            if (source == menuAssists) {
                createMonthsTable("assist_view");
                refresh = "assist_view";
            }

            if (source == menuRebounds) {
                createMonthsTable("rebounds_view");
                refresh = "rebounds_view";
            }

            if (source == menuClubsList) {
                createClubsTable();
            }

            if (source == closeButton) {
                System.exit(0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPlayerReaderWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlayerReader playerReader = new PlayerReader(datasource);
                playerReader.setVisible(true);
            }
        });
    }

    private void createExampleTable() {
        String[] columnNames = {"Day"};
        String[][] data = {
                {"Monday"},
                {"Tuesday"},
                {"Wednesday"},
                {"Thursday"},
                {"Friday"},
                {"Saturday"},
                {"Sunday"}
        };

        tableModel.setDataVector(data, columnNames);
    }

    private void createClubsTable() {
        
        List<Club> clubs = datasource.clubsList();
        Object[][] data = new Object[clubs.size()][3];
        int i = 0;
        String[] columnNames = {"City Name", "Club Name", "Division"};
        for(Club club : clubs) {
            data[i] =  new Object[]{club.getCityName(), club.getClubName(), club.getDivision()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
    }

    private void createMonthsTable(String viewName) {
        List<View> views = datasource.queryView(viewName);
        Object[][] data = new Object[views.size()][6];
        int i = 0;
        String[] columnNames = {"ID", "Player Name", "Club Name", "Points", "Assists", "Rebounds"};
        for(View view : views) {
            data[i] =  new Object[]{view.getId(), view.getPlayerName(), view.getClub(), view.getPts(), view.getAst(), view.getReb()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApplication().setVisible(true);
            }
        });
    }
}

