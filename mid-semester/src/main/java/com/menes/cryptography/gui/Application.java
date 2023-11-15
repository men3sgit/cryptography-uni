package com.menes.cryptography.gui;

import com.menes.cryptography.gui.panels.MainPanel;

import javax.swing.*;

public class Application extends JFrame {
    public Application() {
        super("Cryptography App");
        init();
    }

    private void init() {
        getContentPane().add(new MainPanel());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");

        // Create the "New JFrame" menu item
        JMenuItem newFrameMenuItem = new JMenuItem("Cipher with File");
        newFrameMenuItem.addActionListener(action -> {

        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(action -> {

        });
        exitItem.addActionListener(action -> System.exit(0));
        fileMenu.add(newFrameMenuItem);
        fileMenu.add(exitItem);

        // Add the "File" menu to the menu bar
        menuBar.add(fileMenu);
        // Set the menu bar for the main frame
        setJMenuBar(menuBar);
    }


}
