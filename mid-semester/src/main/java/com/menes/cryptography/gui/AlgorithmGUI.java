package com.menes.cryptography.gui;

import javax.swing.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AlgorithmGUI {
    JPanel renderGUI();
    void doCipher() throws NoSuchAlgorithmException, InvalidKeyException;

}
