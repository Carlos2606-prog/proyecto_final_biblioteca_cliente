package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelSearchBooksUser extends JPanel {
    private DashBoard dashBoard;
    private JTextField txtSearch;
    private JComboBox<String> cmbCriterion;
    private JTable tblResults;
    private DefaultTableModel model;

    public PanelSearchBooksUser(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(100, 149, 237));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("BUSCAR LIBROS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSearch.setBackground(new Color(240, 248, 255));

        JLabel lblCriterion = new JLabel("Buscar por:");
        lblCriterion.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbCriterion = new JComboBox<>(new String[] { "Título", "Autor", "Género" });
        cmbCriterion.setFont(new Font("Arial", Font.PLAIN, 14));

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(34, 139, 34));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> performSearch());

        panelSearch.add(lblCriterion);
        panelSearch.add(cmbCriterion);
        panelSearch.add(txtSearch);
        panelSearch.add(btnSearch);

        String[] columns = { "Código", "Título", "Autor", "Género", "Año", "Total Copias", "Disponibles" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblResults = new JTable(model);
        tblResults.setFont(new Font("Arial", Font.PLAIN, 13));
        tblResults.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblResults);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelInferior.setBackground(new Color(240, 248, 255));
        JButton btnGoBack = new JButton("Volver al Menú");
        btnGoBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnGoBack.setBackground(new Color(128, 128, 128));
        btnGoBack.setForeground(Color.WHITE);
        btnGoBack.setFocusPainted(false);
        btnGoBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGoBack.addActionListener(e -> dashBoard.showPanel("menuUsuario"));
        panelInferior.add(btnGoBack);

        add(panelTitle, BorderLayout.NORTH);
        add(panelSearch, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void performSearch() {
        dashBoard.getController().searchBooks(
                cmbCriterion.getSelectedItem().toString(),
                txtSearch.getText(),
                false);
    }

    // Método llamado por el Presenter
    public void updateSearchResults(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}