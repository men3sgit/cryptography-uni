package com.menes.cryptography.gui;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;

public interface AlgorithmGUI {
    JPanel renderGUI();
    void encrypt() throws Exception;
    void decrypt() throws Exception;

}
