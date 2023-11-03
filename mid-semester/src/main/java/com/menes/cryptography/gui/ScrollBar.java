package com.menes.cryptography.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollBar extends JScrollBar {

    public ScrollBar() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(180, 180, 180));
        setBackground(Color.WHITE);
        setUnitIncrement(20);
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected Dimension getMinimumThumbSize() {
            int thumbSize = 60;
            return scrollbar.getOrientation() == JScrollBar.VERTICAL ? new Dimension(8, thumbSize) : new Dimension(thumbSize, 8);
        }

        @Override
        protected JButton createIncreaseButton(int i) {
            return new ScrollBarButton();
        }

        @Override
        protected JButton createDecreaseButton(int i) {
            return new ScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent jc, Rectangle rect) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent jc, Rectangle rect) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = rect.x;
            int y = rect.y;
            int width = rect.width - 4;
            int height = rect.height;
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                y += 2;
                height -= 4;
            } else {
                x += 2;
                width -= 4;
            }
            g2.setColor(scrollbar.getForeground());
            g2.fillRoundRect(x + 2, y, width, height, 10, 10);
        }
    }

    private static class ScrollBarButton extends JButton {

        public ScrollBarButton() {
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void paint(Graphics g) {
        }
    }
}