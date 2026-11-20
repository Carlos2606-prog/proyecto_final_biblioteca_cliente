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
        setSize(1150, 650);
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
        menuAdmin.addContent("gestionUsuarios", panelManagementUsers);
        menuAdmin.addContent("gestionLibros", panelManagementBook);
        menuAdmin.addContent("registrarPrestamo", panelRegisterLoan);
        menuAdmin.addContent("registrarDevolucion", panelRegisterRefund);
        menuAdmin.addContent("buscarLibrosAdmin", panelSearchBooksAdmin);
        menuAdmin.addContent("estadisticas", panelStatistics);
        menuUser.addContent("buscarLibrosUsuario", panelSearchBooksUser);
        menuUser.addContent("misPrestamos", panelMyLoans);
        menuUser.addContent("misDatos", panelMyDetails);
    }

    public void showPanel(String nombrePanel) {
        currentPanel = nombrePanel;
        loadPanelData(nombrePanel, true);

        if ("menuAdmin".equals(nombrePanel)) {
            menuAdmin.showContent(SideMenuPanel.HOME);
        } else if ("menuUsuario".equals(nombrePanel)) {
            menuUser.showContent(SideMenuPanel.HOME);
        } else if (menuAdmin.hasContent(nombrePanel)) {
            menuAdmin.showContent(nombrePanel);
            cardLayout.show(mainPanel, "menuAdmin");
            return;
        } else if (menuUser.hasContent(nombrePanel)) {
            menuUser.showContent(nombrePanel);
            cardLayout.show(mainPanel, "menuUsuario");
            return;
        }
        cardLayout.show(mainPanel, nombrePanel);
    }

    public void refreshCurrentPanel() {
        loadPanelData(currentPanel, false);
    }

    private void loadPanelData(String panelName, boolean firstTime) {
        switch (panelName) {
            case "gestionUsuarios":
                controller.updateUsersTable();
                break;
            case "gestionLibros":
                controller.updateBooksTable();
                break;
            case "registrarPrestamo":
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
