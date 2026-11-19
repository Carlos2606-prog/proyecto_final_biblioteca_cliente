package co.edu.uptc.view;

import co.edu.uptc.controller.ClientController;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.CardLayout;

public class DashBoard extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ClientController controller;
    private String currentPanel = "seleccion";
    private PanelSelection panelSelection;
    private PanelLoginUser panelLoginUser;
    private PanelLoginAdmin panelLoginAdmin;
    private MenuUser menuUser;
    private MenuAdmin menuAdmin;
    private PanelManagementUsers panelManagementUsers;
    private PanelManagementBook panelManagementBook;
    private PanelRegisterLoan panelRegisterLoan;
    private PanelRegisterRefund panelRegisterRefund;
    private PanelSearchBooksAdmin panelSearchBooksAdmin;
    private PanelSearchBooksUser panelSearchBooksUser;
    private PanelMyLoans panelMyLoans;
    private PanelMyDetails panelMyDetails;
    private PanelStatistics panelStatistics;

    public DashBoard(ClientController controller) {
        this.controller = controller;
        controller.setView(this);
        setTitle("Biblioteca Virtual");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        add(mainPanel);
        showPanel("seleccion");
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        initPaneles();
    }

    private void initPaneles() {
        panelSelection = new PanelSelection(this);
        panelLoginUser = new PanelLoginUser(this);
        panelLoginAdmin = new PanelLoginAdmin(this);
        menuUser = new MenuUser(this);
        menuAdmin = new MenuAdmin(this);
        panelManagementUsers = new PanelManagementUsers(this);
        panelManagementBook = new PanelManagementBook(this);
        panelRegisterLoan = new PanelRegisterLoan(this);
        panelRegisterRefund = new PanelRegisterRefund(this);
        panelSearchBooksAdmin = new PanelSearchBooksAdmin(this);
        panelSearchBooksUser = new PanelSearchBooksUser(this);
        panelMyLoans = new PanelMyLoans(this);
        panelMyDetails = new PanelMyDetails(this);
        panelStatistics = new PanelStatistics(this);

        mainPanel.add(panelSelection, "seleccion");
        mainPanel.add(panelLoginUser, "loginUsuario");
        mainPanel.add(panelLoginAdmin, "loginAdmin");
        mainPanel.add(menuUser, "menuUsuario");
        mainPanel.add(menuAdmin, "menuAdmin");
        mainPanel.add(panelManagementUsers, "gestionUsuarios");
        mainPanel.add(panelManagementBook, "gestionLibros");
        mainPanel.add(panelRegisterLoan, "registrarPrestamo");
        mainPanel.add(panelRegisterRefund, "registrarDevolucion");
        mainPanel.add(panelSearchBooksAdmin, "buscarLibrosAdmin");
        mainPanel.add(panelSearchBooksUser, "buscarLibrosUsuario");
        mainPanel.add(panelMyLoans, "misPrestamos");
        mainPanel.add(panelMyDetails, "misDatos");
        mainPanel.add(panelStatistics, "estadisticas");
    }

    public void showPanel(String nombrePanel) {
        currentPanel = nombrePanel;
        loadPanelData(nombrePanel, true);
        cardLayout.show(mainPanel, nombrePanel);
    }

    /** Lo llama el controlador cuando el servidor avisa que los datos cambiaron. */
    public void refreshCurrentPanel() {
        loadPanelData(currentPanel, false);
    }

    private void loadPanelData(String nombrePanel, boolean firstTime) {
        switch (nombrePanel) {
            case "gestionUsuarios":
                controller.updateUsersTable();
                break;
            case "gestionLibros":
                controller.updateBooksTable();
                break;
            case "registrarPrestamo":
                // Al refrescar no se recarga para no borrar lo que el admin esta seleccionando
                if (firstTime) {
                    controller.loadBooksForLoan();
                }
                break;
            case "registrarDevolucion":
                controller.updateLoansTable();
                break;
            case "buscarLibrosAdmin":
                if (firstTime) {
                    controller.searchBooks("", "", true);
                } else {
                    controller.refreshSearch(true);
                }
                break;
            case "buscarLibrosUsuario":
                if (firstTime) {
                    controller.searchBooks("", "", false);
                } else {
                    controller.refreshSearch(false);
                }
                break;
            case "misPrestamos":
                controller.updateUserLoansTable();
                break;
            case "misDatos":
                controller.updateUserInfo();
                break;
            case "estadisticas":
                controller.updateStatistics();
                break;
            default:
                break;
        }
    }

    public PanelManagementUsers getPanelManagementUsers() {
        return panelManagementUsers;
    }

    public PanelManagementBook getPanelManagementBook() {
        return panelManagementBook;
    }

    public PanelRegisterLoan getPanelRegisterLoan() {
        return panelRegisterLoan;
    }

    public PanelRegisterRefund getPanelRegisterRefund() {
        return panelRegisterRefund;
    }

    public PanelSearchBooksAdmin getPanelSearchBooksAdmin() {
        return panelSearchBooksAdmin;
    }

    public PanelSearchBooksUser getPanelSearchBooksUser() {
        return panelSearchBooksUser;
    }

    public PanelMyLoans getPanelMyLoans() {
        return panelMyLoans;
    }

    public PanelMyDetails getPanelMyDetails() {
        return panelMyDetails;
    }

    public PanelStatistics getPanelStatistics() {
        return panelStatistics;
    }

    public ClientController getController() {
        return controller;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showLoginSuccess(boolean isUser) {
        if (isUser) {
            showPanel("menuUsuario");
        } else {
            showPanel("menuAdmin");
        }
    }

    public void showLoginError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error de Login", JOptionPane.ERROR_MESSAGE);
    }
}
