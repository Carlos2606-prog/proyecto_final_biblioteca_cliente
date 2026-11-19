package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelMyLoans extends JPanel {
    private DashBoard dashBoard;
    private JTable tblLoans;
    private DefaultTableModel model;

    public PanelMyLoans(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard2) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(100, 149, 237));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("MIS PRÉSTAMOS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        String[] columns = { "ID Préstamo", "Título", "Código Ejemplar", "Fecha Préstamo", "Fecha Devolución",
                "Estado" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLoans = new JTable(model);
        tblLoans.setFont(new Font("Arial", Font.PLAIN, 13));
        tblLoans.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblLoans);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelInferior.setBackground(new Color(240, 248, 255));
        JButton btnGoBack = new JButton("Volver al Menú");
        btnGoBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnGoBack.setBackground(new Color(128, 128, 128));
        btnGoBack.setForeground(Color.WHITE);
        btnGoBack.setFocusPainted(false);
        btnGoBack.addActionListener(e -> dashBoard.showPanel("menuUsuario"));
        panelInferior.add(btnGoBack);

        add(panelTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public void updateTableData(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}
