package com.menes.cryptography.gui.panels;

import com.menes.cryptography.gui.*;
import com.menes.cryptography.gui.custom.ScrollBar;
import com.menes.cryptography.utils.Common;
import com.menes.cryptography.gui.custom.MarginFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;


public class ScreenPanel extends JPanel {
    private JLabel title = new JLabel("Block Ciphers");
    private JTextArea input = new JTextArea(8, 52), result = new JTextArea(8, 30);
    private AlgorithmGUI algorithmGUI;
    private JButton copyBtn, clearBtn = new JButton("Clear"), encryptBtn = new JButton("Encrypt"), decryptBtn = new JButton("Decrypt"), verify = new JButton("Verify");
    private JPanel algoPanel;
    private JScrollPane inputScrollPane, resultScrollPane = new JScrollPane(result);
    private boolean isFileMode;
    private JTextField fileTextField = new JTextField("No file chosen");
    private JFileChooser fileChooser;

    public ScreenPanel(boolean isFileMode) {
        this.isFileMode = isFileMode;
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
        ESMode(false);
        HMACMode(false);
        revalidate();
        if (algo.equalsIgnoreCase("Message Digest")) {
            algorithmGUI = new MessageDigestGUI(this);
            displayButton(Boolean.FALSE);
        } else if (algo.equalsIgnoreCase("HMAC-SHA256")) {
            algorithmGUI = new HMACGUI(input, result);
            HMACMode(true);
        } else if (algo.equalsIgnoreCase("RSA")) {
            algorithmGUI = new RSAGUI(this);
        } else if (algo.equalsIgnoreCase("Electronic Signature")) {
            algorithmGUI = new ElectronicSignature(this);
            ESMode(true);
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
        if (isFileMode) {
            JButton openButton = new JButton("Choose File");
            openButton.setForeground(Color.WHITE);
            openButton.setBackground(Common.Color.THEME);
            openButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            openButton.setFocusable(false);
            openButton.setPreferredSize(new Dimension(100, 30));
            openButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            openButton.addActionListener(action -> {

                fileChooser = new JFileChooser();
                int ans = fileChooser.showOpenDialog(this);
                // Check if a file was selected:
                if (ans == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file:
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    fileTextField.setText(selectedFile.getAbsolutePath());
                    try {
                        result.setForeground(Color.BLACK);
                        if (title.getText().equalsIgnoreCase("Message Digest")) {
                            algorithmGUI.encrypt();
                            copyBtn.setVisible(true);
                        }
                    } catch (Exception e) {
                        result.setForeground(Color.RED);
                        result.setText(e.getMessage());
                    }
                }
            });

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(openButton);
            fileTextField.setEditable(false);
            fileTextField.setForeground(Common.Color.THEME);
            fileTextField.setPreferredSize(new Dimension(300, 30));
            panel.add(fileTextField);
            add(panel);
            return;
        }

        inputScrollPane = new JScrollPane(input);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1), "Input"));
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
                if (input.getText().trim().isEmpty()) {
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

        resultScrollPane.setVerticalScrollBar(new ScrollBar());
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1), "Result"));
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
            if (e.getSource() == verify) {
                try {
                    algorithmGUI.encrypt();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource() == copyBtn) {
                copyBtn.setText("Copied");
                copyToClipboard(result.getText());
            } else if (e.getSource() == clearBtn) {
                if (isFileMode) {
                    fileTextField.setText("No file chosen");
                    copyBtn.setVisible(false);
                } else {
                    input.setText("");
                }
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
                    ex.printStackTrace();
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
        initButton(verify, panel);
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
                    inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3), "Input"));
                } else if (e.getSource() == result)
                    resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 3), "Result"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getSource() == input) {
                    inputScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1), "Input"));
                } else if (e.getSource() == result)
                    resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Common.Color.THEME, 1), "Result"));
            }
        });
    }

    public JTextArea getInput() {
        return input;
    }

    public JTextArea getResult() {
        return result;
    }

    public AlgorithmGUI getAlgorithmGUI() {
        return algorithmGUI;
    }

    public JButton getCopyBtn() {
        return copyBtn;
    }

    public JButton getClearBtn() {
        return clearBtn;
    }

    public JButton getEncryptBtn() {
        return encryptBtn;
    }

    public JButton getDecryptBtn() {
        return decryptBtn;
    }

    public JPanel getAlgoPanel() {
        return algoPanel;
    }

    public JScrollPane getInputScrollPane() {
        return inputScrollPane;
    }

    public JScrollPane getResultScrollPane() {
        return resultScrollPane;
    }

    public boolean isFileMode() {
        return isFileMode;
    }

    public JTextField getFileTextField() {
        return fileTextField;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public MouseAdapter getMouseListener() {
        return mouseListener;
    }

    public void ESMode(boolean is) {
        this.clearBtn.setVisible(!is);
        this.encryptBtn.setVisible(!is);
        this.decryptBtn.setVisible(!is);
        this.verify.setVisible(is);
        revalidate();
    }
    public void HMACMode(boolean is) {
        this.clearBtn.setVisible(!is);
        this.decryptBtn.setVisible(!is);
        revalidate();
    }
}

