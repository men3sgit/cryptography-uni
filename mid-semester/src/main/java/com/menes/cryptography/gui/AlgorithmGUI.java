package com.menes.cryptography.gui;

import javax.swing.*;

public interface AlgorithmGUI {
    JPanel renderGUI();
    void encrypt() throws Exception;
    void decrypt() throws Exception;

}
