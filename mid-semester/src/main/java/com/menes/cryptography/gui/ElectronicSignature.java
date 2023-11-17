package com.menes.cryptography.gui;

import com.menes.cryptography.algorithms.MessageDigest;
import com.menes.cryptography.gui.custom.MarginFactory;
import com.menes.cryptography.gui.panels.ScreenPanel;
import com.menes.cryptography.utils.Common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

public class ElectronicSignature implements AlgorithmGUI {
    private ScreenPanel screenPanel;
    ButtonGroup buttonGroup = new ButtonGroup();
    JPanel main = new JPanel();
    private JTextField textField = new JTextField();

    public ElectronicSignature(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
    }

    @Override
    public JPanel renderGUI() {
        JPanel grid = new JPanel();
        renderButtons(grid);
        grid.setLayout(new GridLayout(3, 4));
        grid.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 2), "Hashes"));
        main.add(grid);
        main.add(MarginFactory.marginBottom(20));
        textField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME), "Hash"));
        textField.setPreferredSize(new Dimension(360, 40));
        main.add(textField);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        return main;
    }

    private void renderButtons(JPanel panel) {
        ActionListener actionListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            updateButtonBackground(source);
            System.out.println("Selected: " + source.getText());
        };
        Arrays.stream(Common.Data.hashes)
                .forEach(s -> {
                    JRadioButton radioButton = new JRadioButton(s);
                    radioButton.setPreferredSize(new Dimension(130, 36));
                    radioButton.setActionCommand(s);
                    radioButton.setCursor(Common.Cursor.HAND_CURSOR);
                    radioButton.setFocusable(false);
                    radioButton.addActionListener(actionListener);
                    radioButton.setBorderPainted(true);
                    buttonGroup.add(radioButton);
                    panel.add(radioButton);
                });

    }

    @Override
    public void encrypt() throws Exception {
        try {
            String hash = textField.getText();
            if (hash.trim().isEmpty()) return;
            if (Objects.isNull(screenPanel.getFileChooser())) return;
            String hashed = MessageDigest.hash(screenPanel.getFileChooser().getSelectedFile(), buttonGroup.getSelection().getActionCommand());
            boolean isSuccess = hash.equals(hashed);
            if (isSuccess) {
                screenPanel.getResult().setForeground(Color.GREEN);
                screenPanel.getResult().setText("Verification Success");
            } else {
                screenPanel.getResult().setForeground(Color.RED);
                screenPanel.getResult().setText("Verification Failure");
            }
        } catch (Exception e) {
            screenPanel.getResult().setForeground(Color.RED);
            screenPanel.getResult().setText(e.getMessage());
        }

    }

    @Override
    public void decrypt() throws Exception {

    }

    private void updateButtonBackground(JRadioButton selectedButton) {
        // Reset background color for all buttons
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            button.setBackground(null);
            button.setForeground(Color.BLACK);
        }

        // Set the background color for the selected button
        selectedButton.setBackground(Common.Color.THEME); // You can customize the color as needed
        selectedButton.setForeground(Color.WHITE);
    }
}
