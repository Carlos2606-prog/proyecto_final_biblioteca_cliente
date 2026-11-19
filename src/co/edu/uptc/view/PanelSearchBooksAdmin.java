package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelSearchBooksAdmin extends JPanel {
    private DashBoard dashBoard;
    private JTextField txtSearch;
    private JComboBox<String> cmbCriterion;
    private JTable tblResults;
    private DefaultTableModel model;

    public PanelSearchBooksAdmin(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel paneltitle = new JPanel();
        paneltitle.setBackground(new Color(220, 20, 60));
        paneltitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("BUSCAR LIBROS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        paneltitle.add(title);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSearch.setBackground(new Color(240, 248, 255));

        panelSearch.add(new JLabel("Buscar por:"));
        cmbCriterion = new JComboBox<>(new String[] { "Título", "Autor", "Género", "Código" });
        panelSearch.add(cmbCriterion);

        txtSearch = new JTextField(25);
        panelSearch.add(txtSearch);

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(34, 139, 34));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> performSearch());
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
        btnGoBack.addActionListener(e -> dashBoard.showPanel("menuAdmin"));
        panelInferior.add(btnGoBack);

        add(paneltitle, BorderLayout.NORTH);
        add(panelSearch, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void performSearch() {
        dashBoard.getController().searchBooks(
                cmbCriterion.getSelectedItem().toString(),
                txtSearch.getText(),
                true);
    }

    public void updateSearchResults(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}