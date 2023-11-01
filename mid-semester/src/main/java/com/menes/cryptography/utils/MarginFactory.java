package com.menes.cryptography.utils;

import javax.swing.*;
import java.awt.*;

public class MarginFactory {
    public static JPanel marginTop(int top) {
        JPanel marginPanel = new JPanel();
        marginPanel.setPreferredSize(new Dimension(0, top));
        return marginPanel;
    }

    public static JPanel marginBottom(int bottom) {
        JPanel marginPanel = new JPanel();
        marginPanel.setPreferredSize(new Dimension(0, bottom));
        return marginPanel;
    }

    public static JPanel marginLeft(int left) {
        JPanel marginPanel = new JPanel();
        marginPanel.setPreferredSize(new Dimension(left, 0));
        return marginPanel;
    }

    public static JPanel marginRight(int right) {
        JPanel marginPanel = new JPanel();
        marginPanel.setPreferredSize(new Dimension(right, 0));
        return marginPanel;
    }
}
