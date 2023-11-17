package com.menes.cryptography.gui.panels;

import com.menes.cryptography.utils.Common;
import com.menes.cryptography.gui.custom.MarginFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class RightTitlePanel extends JPanel {
    private List<JLabel> titlePanels;
    private ScreenPanel screenPanel;

    public RightTitlePanel(ScreenPanel screenPanel) {
        this.screenPanel = screenPanel;
        setPreferredSize(new Dimension(230, 0));
        add(MarginFactory.marginTop(200));
        renderComponents();

    }

    public void renderComponents() {
        renderButtons();
    }


    private void renderButtons() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 20, 5)); // 0 rows means any number of rows, 1 column
        panel.setBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3));
        titlePanels = new ArrayList<>();
        titlePanels.add(new JLabel("Message Digest"));
        titlePanels.add(new JLabel("HMAC-SHA256"));
        titlePanels.add(new JLabel("Block Ciphers"));
        if (screenPanel.isFileMode()) titlePanels.add(new JLabel("Electronic Signature"));
        if (!screenPanel.isFileMode()) titlePanels.add(new JLabel("RSA"));

        titlePanels.forEach(label -> {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    label.setBackground(Common.Color.THEME);
                    label.setForeground(Color.WHITE);
                    screenPanel.renderAlgorithmGUI(label.getText());

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    label.setBackground(Common.Color.ENTERED);
                    label.setForeground(Color.DARK_GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    label.setBackground(getBackground());
                    label.setForeground(getForeground());
                }

            });
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.setOpaque(true);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(180, 50));
            panel.add(new JPanel().add(label));
        });
        add(panel);


    }


}
