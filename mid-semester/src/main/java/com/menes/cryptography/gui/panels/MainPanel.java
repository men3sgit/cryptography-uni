package com.menes.cryptography.gui.panels;

import com.menes.cryptography.utils.Common;
import com.menes.cryptography.utils.MarginFactory;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private ScreenPanel screenPanel;
    private RightTitlePanel rightTitlePanel;
    public MainPanel(){
        init();
        add(screenPanel = new ScreenPanel(),BorderLayout.CENTER);
        add(rightTitlePanel = new RightTitlePanel(screenPanel),BorderLayout.EAST);
        add(MarginFactory.marginLeft(30),BorderLayout.WEST);

    }

    private void init() {
        setPreferredSize(new Dimension(Common.Unit.MAIN_WIDTH,Common.Unit.MAIN_HEIGHT));
        setLayout(new BorderLayout());
    }
}
