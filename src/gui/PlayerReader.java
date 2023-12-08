package gui;

import model.Club;
import model.Datasource;
import model.Player;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlayerReader extends JFrame {
    private JComboBox<String> clubComboBox;
    private JComboBox<String> positionComboBox;
    private JTextField string1Field;
    private JTextField string2Field;
    private JTextField double1Field;
    private JTextField double2Field;
    private JTextField double3Field;
    private List<Club> clubList;
    private Datasource datasource;
    public PlayerReader(List<Club> clubList, Datasource datasource) {
        setTitle("Player Reader");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        this.clubList = clubList;
        this.datasource = datasource;
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel string1Label = new JLabel("Player first name:");
        string1Field = new JTextField();

        JLabel string2Label = new JLabel("Player last name:");
        string2Field = new JTextField();

        JLabel clubLabel = new JLabel("Select club:");
        String[] clubs = new String[clubList.size()];
        int i = 0;
        for(Club club : clubList){
            clubs[i] = club.getCityName() + " " + club.getClubName();
            i++;
        }
        clubComboBox = new JComboBox<>(clubs);

        JLabel positionLabel = new JLabel("Select position:");
        String[] positions = {"PG", "SG", "SF", "PF", "C"};
        positionComboBox = new JComboBox<>(positions);

        JLabel double1Label = new JLabel("Player points:");
        double1Field = new JTextField();

        JLabel double2Label = new JLabel("Player assist:");
        double2Field = new JTextField();

        JLabel double3Label = new JLabel("Player rebounds:");
        double3Field = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSubmit();
            }
        });


        panel.add(string1Label);
        panel.add(string1Field);
        panel.add(string2Label);
        panel.add(string2Field);
        panel.add(positionLabel);
        panel.add(positionComboBox);
        panel.add(clubLabel);
        panel.add(clubComboBox);
        panel.add(double1Label);
        panel.add(double1Field);
        panel.add(double2Label);
        panel.add(double2Field);
        panel.add(double3Label);
        panel.add(double3Field);
        panel.add(new JLabel());
        panel.add(submitButton);

        add(panel);
    }

    private void onSubmit() {
        try {
            datasource.addPlayer(string1Field.getText(), string2Field.getText(), positionComboBox.getSelectedItem().toString(), clubComboBox.getSelectedIndex() + 1);
            datasource.addStats(double1Field.getText(), double2Field.getText(), double3Field.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
