package com.menes.cryptography;

import com.menes.cryptography.gui.panels.Application;

import javax.swing.SwingUtilities;


public class CryptographyToolLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application(Boolean.FALSE).setVisible(true));
    }
}
