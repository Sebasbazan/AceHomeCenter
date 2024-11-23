package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.time.*;
import java.time.format.*;

public class Login extends JFrame {
    private JTextField operatorField;
    private JPasswordField passwordField;
    private JLabel dateTimeLabel;
    private JLabel statusLabel;
    private JLabel passwordLabel;
    private static final Color DARK_RED = new Color(128, 0, 0);
    private JTextField activeField = null;

    public Login() {
        setTitle("Sistema de Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new GridLayout(1, 2, 2, 0));
        getContentPane().setBackground(Color.WHITE);

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        add(leftPanel);
        add(rightPanel);

        initializeDateTime();

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 2));
        panel.setBackground(DARK_RED);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(0, 250));
        imagePanel.setBackground(DARK_RED);

        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/Maestro.jpg"));
            Image scaledImage = imageIcon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception ex) {
            System.err.println("Error al cargar la imagen Maestro.jpg: " + ex.getMessage());
        }

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setForeground(Color.WHITE);
        imagePanel.add(separator, BorderLayout.SOUTH);

        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        loginPanel.setBackground(DARK_RED);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel operatorLabel = new JLabel("ID de operador:");
        operatorLabel.setForeground(Color.WHITE);
        operatorField = new JTextField();
        operatorField.setEditable(true);  
        setupField(operatorField, 8);
        operatorField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                togglePasswordField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                togglePasswordField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                togglePasswordField();
            }
        });

        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setEditable(false); 
        setupField(passwordField, 8);

       
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);

        loginPanel.add(operatorLabel);
        loginPanel.add(operatorField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(DARK_RED);

        statusLabel = new JLabel("Registrar001", SwingConstants.RIGHT);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));

        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));

        footerPanel.add(dateTimeLabel, BorderLayout.WEST);
        footerPanel.add(statusLabel, BorderLayout.EAST);

        panel.add(imagePanel, BorderLayout.NORTH);
        panel.add(loginPanel, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 2));
        panel.setBackground(DARK_RED);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel keypadPanel = new JPanel(new GridLayout(4, 4, 2, 2));
        keypadPanel.setBackground(DARK_RED);
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton sampleButton = createButton("1");
        Dimension buttonSize = sampleButton.getPreferredSize();
        int buttonHeight = buttonSize.height;

        String[] buttons = {
            "7", "8", "9", "Cancelar",
            "4", "5", "6", "Borrar",
            "1", "2", "3", "Entrar",
            "", "0", "00", ""
        };

        for (String label : buttons) {
            if (!label.isEmpty()) {
                JButton button = createButton(label);
                button.setPreferredSize(new Dimension(buttonSize.width, buttonHeight));
                keypadPanel.add(button);
            } else {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setBackground(DARK_RED);
                keypadPanel.add(emptyPanel);
            }
        }

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.setBackground(DARK_RED);

        try {
            ImageIcon helpIcon = new ImageIcon(getClass().getResource("/img/ayuda.png"));
            Image scaledHelpImage = helpIcon.getImage().getScaledInstance(buttonSize.width, buttonHeight, Image.SCALE_SMOOTH);
            JButton helpButton = new JButton(new ImageIcon(scaledHelpImage));
            styleActionButton(helpButton, buttonSize.width, buttonHeight);
            helpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(Login.this, "        SISTEMA DE AYUDA");
                }
            });

            ImageIcon submitIcon = new ImageIcon(getClass().getResource("/img/ingresar.png"));
            Image scaledSubmitImage = submitIcon.getImage().getScaledInstance(buttonSize.width, buttonHeight, Image.SCALE_SMOOTH);
            JButton submitButton = new JButton(new ImageIcon(scaledSubmitImage));
            styleActionButton(submitButton, buttonSize.width, buttonHeight);
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    validateLogin();
                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
            buttonPanel.setBackground(DARK_RED);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(2, 50, 50, 50));
            buttonPanel.add(helpButton);
            buttonPanel.add(submitButton);

            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        } catch (Exception ex) {
            System.err.println("Error al cargar las imágenes de los botones: " + ex.getMessage());
            ex.printStackTrace();
        }

        panel.add(keypadPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void togglePasswordField() {
        boolean isNotEmpty = !operatorField.getText().trim().isEmpty();
        passwordLabel.setVisible(isNotEmpty);
        passwordField.setVisible(isNotEmpty);
    }

    private void setupField(JTextField field, int maxLength) {
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeField = field;
            }
        });

        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= maxLength) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.addActionListener(e -> handleButtonClick(text));
        return button;
    }

    private void styleActionButton(JButton button, int width, int height) {
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void handleButtonClick(String text) {
        if (activeField != null) {
            if (text.equals("Borrar")) {
                String currentText = activeField.getText();
                if (currentText.length() > 0) {
                    activeField.setText(currentText.substring(0, currentText.length() - 1));
                }
            } else if (text.equals("Cancelar")) {
                operatorField.setText("");
                passwordField.setText("");
            } else if (text.equals("Entrar")) {
                validateLogin();
            } else {
                activeField.setText(activeField.getText() + text);
            }
        }
    }

    private void validateLogin() {
        String operatorId = operatorField.getText();
        String password = new String(passwordField.getPassword());

        if (operatorId.equals("11111111") && password.equals("11111111")) {
            JOptionPane.showMessageDialog(this, "     BIENVENIDO MAESTRO", "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Documentación incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy                           HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dateTimeLabel.setText(now.format(formatter));

        Timer timer = new Timer(1000, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            dateTimeLabel.setText(currentTime.format(formatter));
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
