package gui;

import model.Club;
import model.Datasource;
import model.PlayerToUpdate;

import javax.swing.*;
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
    private int idOfUpdatedPlayer;
    private List<Club> clubList;
    private PlayerToUpdate player;
    private Datasource datasource;
    public PlayerReader(Datasource datasource, PlayerToUpdate player, int idOfUpdatedPlayer) {
        this.player = player;
        this.idOfUpdatedPlayer = idOfUpdatedPlayer;
        setTitle("Player Reader");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        this.clubList = datasource.clubsList();
        this.datasource = datasource;
        initComponents(player);
    }

    private void initComponents(PlayerToUpdate player) {
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
                setVisible(false);
                dispose();
            }
        });


        if(player != null){
            string1Field.setText(player.getFirstName());
            string1Field.setEditable(false);
            string2Field.setText(player.getLastName());
            string2Field.setEditable(false);
            clubComboBox.setSelectedItem(player.getClub());
            clubComboBox.setEditable(false);
            positionComboBox.setSelectedItem(player.getPos());
            positionComboBox.setEditable(false);
            double1Field.setText(String.valueOf(player.getPts()));
            double2Field.setText(String.valueOf(player.getAst()));
            double3Field.setText(String.valueOf(player.getReb()));
        }

        panel.add(string1Label);
        panel.add(string1Field);
        panel.add(string2Label);
        panel.add(string2Field);
        panel.add(positionLabel);

        if(player == null){
            panel.add(positionComboBox);
        } else {
            JTextField positionField = new JTextField();
            positionField.setText(player.getPos());
            positionField.setEditable(false);
            panel.add(positionField);
        }

        panel.add(clubLabel);

        if(player == null){
            panel.add(clubComboBox);
        } else {
            JTextField clubField = new JTextField();
            clubField.setText(player.getClub());
            clubField.setEditable(false);
            panel.add(clubField);
        }

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
            if(player == null){
                datasource.addPlayer(string1Field.getText(), string2Field.getText(), positionComboBox.getSelectedItem().toString(), clubComboBox.getSelectedIndex() + 1);
                datasource.addStats(double1Field.getText(), double2Field.getText(), double3Field.getText());
            } else {
                datasource.updateStats(double1Field.getText(), double2Field.getText(), double3Field.getText(), idOfUpdatedPlayer);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
