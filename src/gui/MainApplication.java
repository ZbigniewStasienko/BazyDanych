package gui;

import model.Datasource;
import model.View;

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

        JButton openPlayerReaderButton = new JButton("Open PlayerReader");
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

        JButton createMonthsTableButton = new JButton("Create Months Table");
        createMonthsTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMonthsTable("points_view");
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
        buttonPanel.add(openPlayerReaderButton);
        buttonPanel.add(createTableButton);
        buttonPanel.add(createMonthsTableButton);
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    private void openPlayerReaderWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlayerReader playerReader = new PlayerReader();
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

    private void createMonthsTable(String viewName) {
        List<View> views = datasource.queryView(viewName);
        Object[][] data = new Object[views.size()][5];
        int i = 0;
        if(views == null) {
            return;
        }
        String[] columnNames = {"Player Name", "Club Name", "Points", "Assists", "Rebounds"};
        for(View view : views) {
            data[i] =  new Object[]{view.getPlayerName(), view.getClub(), view.getPts(), view.getAst(), view.getReb()};
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

