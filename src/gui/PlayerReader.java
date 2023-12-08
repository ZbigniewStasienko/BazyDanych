package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerReader extends JFrame {
    private JComboBox<String> clubComboBox;
    private JComboBox<String> positionComboBox;
    private JTextField string1Field;
    private JTextField string2Field;
    private JTextField double1Field;
    private JTextField double2Field;
    private JTextField double3Field;

    public PlayerReader() {
        setTitle("Player Reader");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);

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
        String[] clubs = {"chi", "det", "cle"};
        clubComboBox = new JComboBox<>(clubs);

        JLabel positionLabel = new JLabel("Select position:");
        String[] positions = {"1", "2", "3", "4", "5"};
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
            int intValue = Integer.parseInt(positionComboBox.getSelectedItem().toString());
            String string1Value = string1Field.getText();
            String string2Value = string2Field.getText();
            double double1Value = Double.parseDouble(double1Field.getText());
            double double2Value = Double.parseDouble(double2Field.getText());
            double double3Value = Double.parseDouble(double3Field.getText());
            String a = clubComboBox.getSelectedItem().toString();

            System.out.println("Pos: " + intValue);
            System.out.println("String 1: " + string1Value);
            System.out.println("String 2: " + string2Value);
            System.out.println("Double 1: " + double1Value);
            System.out.println("Double 2: " + double2Value);
            System.out.println("Double 3: " + double3Value);
            System.out.println("EE: " + a);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
