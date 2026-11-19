package co.edu.uptc.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;


public class PanelSelection extends JPanel{
     private DashBoard ventanaPrincipal;
    
    public PanelSelection(DashBoard ventana) {
        this.ventanaPrincipal = ventana;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(70, 130, 180));
        panelTitulo.setPreferredSize(new Dimension(900, 80));
        JLabel titulo = new JLabel("BIBLIOTECA VIRTUAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        panelTitulo.add(titulo);
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblBienvenida = new JLabel("Seleccione su tipo de acceso:");
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 18));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(lblBienvenida, gbc);
        
        JButton btnUsuario = crearBoton("USUARIO", new Color(100, 149, 237));
        btnUsuario.addActionListener(e -> ventanaPrincipal.showPanel("loginUsuario"));
        gbc.gridy = 1;
        gbc.ipadx = 150;
        gbc.ipady = 30;
        panelCentral.add(btnUsuario, gbc);
        
        JButton btnAdmin = crearBoton("ADMINISTRADOR", new Color(220, 20, 60));
        btnAdmin.addActionListener(e -> ventanaPrincipal.showPanel("loginAdmin"));
        gbc.gridy = 2;
        panelCentral.add(btnAdmin, gbc);
        
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
