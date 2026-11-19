package co.edu.uptc.view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class PanelLoginAdmin extends JPanel {
    private DashBoard dashBoard;
    private JTextField txtUser;
    private JPasswordField txtPassword;

    public PanelLoginAdmin(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard){
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("ACCESO ADMINISTRADOR");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelForm.add(lblUser, gbc);

        txtUser = new JTextField(20);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panelForm.add(txtUser, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelButtons.setBackground(new Color(240, 248, 255));

        JButton btnEnter = new JButton("Ingresar");
        btnEnter.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnter.setBackground(new Color(34, 139, 34));
        btnEnter.setForeground(Color.WHITE);
        btnEnter.setFocusPainted(false);
        btnEnter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnter.addActionListener(e -> {
            dashBoard.getController().loginAdmin(
                txtUser.getText(), 
                new String(txtPassword.getPassword())
            );
        });

        JButton btnGoBack = new JButton("Volver");
        btnGoBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnGoBack.setBackground(new Color(128, 128, 128));
        btnGoBack.setForeground(Color.WHITE);
        btnGoBack.setFocusPainted(false);
        btnGoBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGoBack.addActionListener(e -> dashBoard.showPanel("seleccion"));

        panelButtons.add(btnEnter);
        panelButtons.add(btnGoBack);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        add(panelTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
    }
}