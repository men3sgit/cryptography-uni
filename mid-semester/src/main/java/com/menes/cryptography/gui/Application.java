package com.menes.cryptography.gui;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {
    public Application() {
        super("Cryptography App");
        init();
    }
    private void init(){
        getContentPane().add(new MainPanel());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }


}
