package com.menes.cryptography.gui.panels;

import javax.swing.*;

public class Application extends JFrame {
    private final boolean isFileMode;

    public Application(boolean isFileMode) {
        super("Holocaust");
        this.isFileMode = isFileMode;
        init();
    }

    private void init() {
        getContentPane().add(new MainPanel(isFileMode));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFrameMenuItem = new JMenuItem("Cipher with File");
        newFrameMenuItem.addActionListener(action -> {
            SwingUtilities.invokeLater(() -> new Application(Boolean.TRUE).setVisible(true));
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(action -> System.exit(0));
        fileMenu.add(newFrameMenuItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }


}
