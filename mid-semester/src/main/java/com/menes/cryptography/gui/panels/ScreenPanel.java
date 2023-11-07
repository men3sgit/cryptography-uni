package com.menes.cryptography.gui.panels;

import com.menes.cryptography.gui.*;
import com.menes.cryptography.utils.Common;
import com.menes.cryptography.utils.MarginFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;


public class ScreenPanel extends JPanel {
    private JLabel title = new JLabel("Message Digest");
    private JTextArea input = new JTextArea(8, 52), result = new JTextArea(8, 30);
    private AlgorithmGUI algorithmGUI;
    private JButton copyBtn, clearBtn = new JButton("Clear"), encryptBtn = new JButton("Encrypt"), decryptBtn = new JButton("Decrypt");
    private JPanel algoPanel;
    private JScrollPane inputScrollPane, resultScrollPane;


    public ScreenPanel() {
        renderComponents();
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void renderComponents() {

        renderTitle();
        add(MarginFactory.marginTop(10));
        renderAlgorithm();
        add(MarginFactory.marginTop(10));
        renderInput();
        add(MarginFactory.marginTop(10));
        renderButtons();
        add(MarginFactory.marginTop(10));
        renderResult();
        add(MarginFactory.marginTop(20));
        renderCopyButton();
        add(MarginFactory.marginTop(20));

    }

    private void renderAlgorithm() {
        add(algoPanel = new JPanel());
        algoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        renderAlgorithmGUI(title.getText());
    }

    public void renderAlgorithmGUI(String algo) {
        title.setText(algo);
        clearData();
        displayButton(Boolean.TRUE);
        algoPanel.removeAll();
        revalidate();
        if (algo.equalsIgnoreCase("Message Digest")) {
            algorithmGUI = new MessageDigestGUI(input, result);
            displayButton(Boolean.FALSE);
        } else if (algo.equalsIgnoreCase("HMAC")) {
            algorithmGUI = new HMACGUI(input, result);
        } else if (algo.equalsIgnoreCase("RSA")) {
            algorithmGUI = new RSAGUI(input, result);
        } else {
            algorithmGUI = new SymmetricEncryptionGUI(input, result);
        }
        algoPanel.add((algorithmGUI.renderGUI()));
        revalidate();
    }

    private void clearData() {
        input.setText("");
        result.setText("");
    }

    private void renderTitle() {
        JPanel titlePanel = new JPanel();
        title.setPreferredSize(new Dimension(Common.Unit.MAIN_WIDTH, 40));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBackground(Common.Color.THEME);
        title.setOpaque(true);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);
        add(titlePanel);
    }

    private void renderInput() {
        inputScrollPane = new JScrollPane(input);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1),"Input"));
        input.setLineWrap(true);
        input.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        input.setFont(new Font("Monospaced", Font.TYPE1_FONT, Common.Unit.INPUT_TEXT_SIZE));
        inputScrollPane.setVerticalScrollBar(new ScrollBar());
        focusPanel(input);
        add(inputScrollPane);

        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                copyBtn.setVisible(true);
                try {
                    if (title.getText().equalsIgnoreCase("Message Digest"))
                        algorithmGUI.encrypt();
                } catch (NoSuchAlgorithmException ex) {
                    result.setText("No support");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (input.getText().isBlank()) {
                    result.setText("");
                    copyBtn.setVisible(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }


    private void renderResult() {
        resultScrollPane = new JScrollPane(result);
        resultScrollPane.setVerticalScrollBar(new ScrollBar());
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1),"Result"));
        result.setEditable(false);
        result.setLineWrap(true);
        result.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        result.setFont(new Font("Monospaced", Font.TRUETYPE_FONT, Common.Unit.INPUT_TEXT_SIZE));
        focusPanel(result);
        add(resultScrollPane);
    }

    MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == copyBtn) {
                copyBtn.setText("Copied");
                copyToClipboard(result.getText());
            } else if (e.getSource() == clearBtn) {
                input.setText("");
                result.setText("");
            } else if (e.getSource() == encryptBtn) {
                try {
                    algorithmGUI.encrypt();
                    result.setForeground(Color.BLACK);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    result.setText(ex.getMessage());
                    result.setForeground(Color.RED);
                }
            } else if (e.getSource() == decryptBtn) {
                try {
                    algorithmGUI.decrypt();
                } catch (Exception ex) {
                    throw new RuntimeException();
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if (source == copyBtn) {
                copyBtn.setText("Copy");
            }
        }
    };

    private void renderCopyButton() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        copyBtn = new JButton("Copy");
        copyBtn.setBackground(Common.Color.COPIED);
        copyBtn.setPreferredSize(new Dimension(80, 30));
        copyBtn.setForeground(Color.WHITE);
        copyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        copyBtn.setFocusable(false);
        copyBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        copyBtn.addMouseListener(mouseListener);
        copyBtn.setVisible(false);

        panel.add(copyBtn);
        add(panel);
    }

    private void renderButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initButton(encryptBtn, panel);
        initButton(decryptBtn, panel);
        initButton(clearBtn, panel);
        add(panel);
    }

    private void displayButton(boolean flag) {
        encryptBtn.setVisible(flag);
        decryptBtn.setVisible(flag);
    }

    private void initButton(JButton button, JPanel panel) {
        button.setBackground(Common.Color.THEME);
        button.setPreferredSize(new Dimension(90, 40));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusable(false);
        button.addMouseListener(mouseListener);
        panel.add(button);
    }

    public void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, null);
    }


    public void focusPanel(JComponent component) {
        component.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getSource() == input) {
                    inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3),"Input"));
                } else if (e.getSource() == result)
                    resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3),"Result"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() == input) {
                    inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1),"Input"));
                } else if (e.getSource() == result)
                    resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1),"Result"));
            }
        });
    }

}
/*
 * TODO 1: change name title from right title
 * TODO 1.1: change jcomboBox
 *
 * TODO 2: Block Cipher
 *
 */
