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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.NoSuchAlgorithmException;

import static com.menes.cryptography.gui.GUIUtils.focusTextArea;

public class ScreenPanel extends JPanel {
    private JLabel title = new JLabel("Message Digest");
    private JTextArea input = new JTextArea(), result = new JTextArea();
    private AlgorithmGUI algorithmGUI;
    private JButton copyBtn, clearBtn = new JButton("Clear"), encryptBtn = new JButton("Encrypt"), decryptBtn = new JButton("Decrypt");
    private JPanel algoPanel;


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
        add(MarginFactory.marginTop(50));
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
        input.setPreferredSize(new Dimension(Common.Unit.INPUT_WIDTH, Common.Unit.INPUT_HEIGHT));
        input.setFont(new Font("Courier", Font.PLAIN, Common.Unit.INPUT_TEXT_SIZE));
        input.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Input"));
        input.setLineWrap(true);
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
        focusTextArea(input, "Input");
        add(input);
    }


    private void renderResult() {
        result.setPreferredSize(new Dimension(Common.Unit.INPUT_WIDTH, Common.Unit.INPUT_HEIGHT));
        result.setEditable(false);
        result.setLineWrap(true);
        result.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Result"));
        result.setFont(new Font("Monospaced", Font.TRUETYPE_FONT, Common.Unit.INPUT_TEXT_SIZE));

        focusTextArea(result, "Result");
        add(result);
    }

    MouseListener mouseListener = new MouseListener() {
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
                } catch (Exception ex) {
                    result.setText(ex.getMessage() + "\nSecret key (16, 24, or 32 characters)");
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
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

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

    public String getTitle() {
        return title.getText();
    }

    public JTextArea getInput() {
        return input;
    }

    public JTextArea getResult() {
        return result;
    }

    public JButton getCopyBtn() {
        return copyBtn;
    }

    public JButton getClearBtn() {
        return clearBtn;
    }

    public void setTitle(String title) {
        this.title.setText(title);

    }

    public void setInput(JTextArea input) {
        this.input = input;
    }

    public void setResult(JTextArea result) {
        this.result = result;
    }

    public void setCopyBtn(JButton copyBtn) {
        this.copyBtn = copyBtn;
    }

    public void setClearBtn(JButton clearBtn) {
        this.clearBtn = clearBtn;
    }
}

/*
 * TODO 1: change name title from right title
 * TODO 1.1: change jcomboBox
 *
 * TODO 2: Block Cipher
 *
 */
