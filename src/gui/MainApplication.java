package gui;

import model.Club;
import model.Datasource;
import model.Player;
import model.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainApplication extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private Datasource datasource;
    private String refresh;

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

        JMenuBar menuBar = new JMenuBar();
        JMenu menuViews = new JMenu("View");
        JMenuItem menuClubs = new JMenuItem("Clubs");
        // JMenuItem menuClubsList = new JMenuItem("List of Clubs"); 
        JMenuItem menuPoints = new JMenuItem("Sort by points");
        JMenuItem menuRebounds = new JMenuItem("Sort by rebounds");
        JMenuItem menuAssists = new JMenuItem("Sort by assists");
    

        setJMenuBar(menuBar);
        menuBar.add(menuViews);
        menuBar.add(menuClubs);

        menuViews.add(menuPoints);
        menuViews.add(menuAssists);
        menuViews.add(menuRebounds);
        // menuClubs.add(menuClubsList);

        JButton refreshButton = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("../image/refresh.png"));
            Image newimage = img.getScaledInstance(24, 22, DO_NOTHING_ON_CLOSE);
            refreshButton.setIcon(new ImageIcon(newimage));
            
        } catch (Exception e) {
            System.out.println(e);
        }

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("usuniecie zawodnika");
            }
            
        });



        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("test refresh");
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
                System.out.println("refresh test");
                
            }
        });

        JButton openPlayerReaderButton = new JButton("Add Player");
        openPlayerReaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlayerReaderWindow();
            }
        });

        JButton createTableButton = new JButton("Create Example Table");
        createTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createExampleTable();
            }
        });

        menuPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMonthsTable("points_view");
                refresh = "points_view";
            }
        });

        menuAssists.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMonthsTable("assist_view");
                refresh = "assist_view";
            }
        });

        menuRebounds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMonthsTable("rebounds_view");
                refresh = "rebounds_view";
            }
        });

        menuClubs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createClubsTable();
            }
            
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(openPlayerReaderButton);
        buttonPanel.add(createTableButton);
        buttonPanel.add(closeButton);
        
        
        
        panel.add(buttonPanel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    private void MouseCheck(Object[][] data){
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                System.out.println(row);
                System.out.println(col);
                System.out.println(data[row][col]);
            }
        });
    }

    private void openPlayerReaderWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlayerReader playerReader = new PlayerReader(datasource.clubsList(), datasource);
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
        Object[][] data = new Object[views.size()][5];
        int i = 0;
        String[] columnNames = {"Player Name", "Club Name", "Points", "Assists", "Rebounds"};
        for(View view : views) {
            data[i] =  new Object[]{view.getPlayerName(), view.getClub(), view.getPts(), view.getAst(), view.getReb()};
            i++;
        }
        tableModel.setDataVector(data, columnNames);
        this.MouseCheck(data);
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

