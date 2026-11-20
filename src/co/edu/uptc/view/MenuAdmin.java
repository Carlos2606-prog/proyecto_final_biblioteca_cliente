package co.edu.uptc.view;

import java.awt.Color;

public class MenuAdmin extends SideMenuPanel {

    public MenuAdmin(DashBoard dashBoard) {
        super(dashBoard, "MENÚ ADMINISTRADOR",
                new Color(220, 20, 60),
                new Color(178, 34, 34),
                new Color(139, 0, 0));

        addOption("gestionUsuarios", "Gestión de Usuarios");
        addOption("gestionLibros", "Gestión de Libros");
        addOption("registrarPrestamo", "Registrar Préstamo");
        addOption("registrarDevolucion", "Registrar Devolución");
        addOption("buscarLibrosAdmin", "Buscar Libros");
    }
}
