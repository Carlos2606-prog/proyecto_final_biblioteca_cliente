package co.edu.uptc.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class MenuUser extends JPanel {
    private DashBoard daashBoard;

    public MenuUser(DashBoard dashBoard) {
        this.daashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(100, 149, 237));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("MENÚ USUARIO");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelOptions = new JPanel(new GridLayout(4, 1, 10, 10));
        panelOptions.setBackground(new Color(240, 248, 255));
        panelOptions.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnSearchBooks = createBtn("Buscar Libros");
        btnSearchBooks.addActionListener(e -> daashBoard.showPanel("buscarLibrosUsuario"));

        JButton btnMyLoans = createBtn("Mis Préstamos");
        btnMyLoans.addActionListener(e -> daashBoard.showPanel("misPrestamos"));

        JButton btnMyDetails = createBtn("Ver Mis Datos");
        btnMyDetails.addActionListener(e -> daashBoard.showPanel("misDatos"));

        JButton btnExit = createBtn("Cerrar Sesión");
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.addActionListener(e -> {
            daashBoard.getController().logout();
            daashBoard.showPanel("seleccion");
        });

        panelOptions.add(btnSearchBooks);
        panelOptions.add(btnMyLoans);
        panelOptions.add(btnMyDetails);
        panelOptions.add(btnExit);

        add(panelTitle, BorderLayout.NORTH);
        add(panelOptions, BorderLayout.CENTER);
    }

    private JButton createBtn(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}