package co.edu.uptc.view;

import java.awt.Color;

public class MenuUser extends SideMenuPanel {

    public MenuUser(DashBoard dashBoard) {
        super(dashBoard, "MENÚ USUARIO",
                new Color(100, 149, 237),  // encabezado
                new Color(70, 130, 180),   // botones
                new Color(220, 20, 60));   // cerrar sesión

        addOption("buscarLibrosUsuario", "Buscar Libros");
        addOption("misPrestamos", "Mis Préstamos");
        addOption("misDatos", "Ver Mis Datos");
    }
}
