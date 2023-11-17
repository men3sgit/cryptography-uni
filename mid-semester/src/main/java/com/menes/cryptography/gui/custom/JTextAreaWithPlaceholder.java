package com.menes.cryptography.gui.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextAreaWithPlaceholder extends JTextArea {
    private String placeholder;

    public JTextAreaWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setLineWrap(true);
        setWrapStyleWord(true);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK); // Change text color to normal
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY); // Change text color to placeholder color
                }
            }
        });
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(Color.GRAY); // Set initial text color to placeholder color
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawString(placeholder, getInsets().left, g.getFontMetrics().getAscent() + getInsets().top);
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JTextArea with Placeholder Example");
        JTextAreaWithPlaceholder textArea = new JTextAreaWithPlaceholder("Enter your text here");
        textArea.setPreferredSize(new Dimension(300, 200));
        frame.add(textArea);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
