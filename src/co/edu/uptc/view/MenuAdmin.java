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

public class MenuAdmin extends JPanel {

    private DashBoard dashBoard;
    
    public MenuAdmin(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard){
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("MENÚ ADMINISTRADOR");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);
        
        JPanel panelOptions = new JPanel(new GridLayout(7, 1, 10, 10));
        panelOptions.setBackground(new Color(240, 248, 255));
        panelOptions.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        
        JButton btnUsersManagement = createBtn("Gestión de Usuarios");
        btnUsersManagement.addActionListener(e -> dashBoard.showPanel("gestionUsuarios"));
        
        JButton btnBooksManagement = createBtn("Gestión de Libros");
        btnBooksManagement.addActionListener(e -> dashBoard.showPanel("gestionLibros"));
        
        JButton btnRegisterLoan = createBtn("Registrar Préstamo");
        btnRegisterLoan.addActionListener(e -> dashBoard.showPanel("registrarPrestamo"));
        
        JButton btnRegisterRefund = createBtn("Registrar Devolución");
        btnRegisterRefund.addActionListener(e -> dashBoard.showPanel("registrarDevolucion"));
        
        JButton btnSearchBooks = createBtn("Buscar Libros");
        btnSearchBooks.addActionListener(e -> dashBoard.showPanel("buscarLibrosAdmin"));
        
        JButton btnViewStatistics = createBtn("Ver Estadísticas");
        btnViewStatistics.addActionListener(e -> dashBoard.showPanel("estadisticas"));
        
        JButton btnExit = createBtn("Cerrar Sesión");
        btnExit.setBackground(new Color(139, 0, 0));
        btnExit.addActionListener(e -> {
            dashBoard.getController().logout();
            dashBoard.showPanel("seleccion");
        });
        
        panelOptions.add(btnUsersManagement);
        panelOptions.add(btnBooksManagement);
        panelOptions.add(btnRegisterLoan);
        panelOptions.add(btnRegisterRefund);
        panelOptions.add(btnSearchBooks);
        panelOptions.add(btnViewStatistics);
        panelOptions.add(btnExit);
        
        add(panelTitle, BorderLayout.NORTH);
        add(panelOptions, BorderLayout.CENTER);       
    }
    
    private JButton createBtn(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(178, 34, 34));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
