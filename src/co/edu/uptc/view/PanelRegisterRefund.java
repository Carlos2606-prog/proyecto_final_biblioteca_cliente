package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelRegisterRefund extends JPanel {
    private DashBoard dashBoard;
    private JTextField txtLoanId;
    private JTable tblLoans;
    private DefaultTableModel model;

    public PanelRegisterRefund(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard2) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("REGISTRAR DEVOLUCIÓN");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        // Panel de búsqueda
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSearch.setBackground(new Color(240, 248, 255));

        JLabel lblSearch = new JLabel("ID Préstamo:");
        lblSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSearch.add(lblSearch);

        txtLoanId = new JTextField(15);
        txtLoanId.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSearch.add(txtLoanId);

        JButton btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(70, 130, 180));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> searchLoan());
        panelSearch.add(btnSearch);

        JButton btnRegisterRefund = new JButton("Registrar Devolución");
        btnRegisterRefund.setBackground(new Color(34, 139, 34));
        btnRegisterRefund.setForeground(Color.WHITE);
        btnRegisterRefund.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegisterRefund.setFocusPainted(false);
        btnRegisterRefund.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegisterRefund.addActionListener(e -> registerRefund());
        panelSearch.add(btnRegisterRefund);

        String[] columns = { "ID Préstamo", "Usuario", "Libro", "Código Copia",
                "Fecha Préstamo", "Fecha Devolución", "Estado" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLoans = new JTable(model);
        tblLoans.setFont(new Font("Arial", Font.PLAIN, 12));
        tblLoans.setRowHeight(25);

        tblLoans.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblLoans.getSelectedRow();
                if (fila >= 0) {
                    txtLoanId.setText(model.getValueAt(fila, 0).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblLoans);


        add(panelTitle, BorderLayout.NORTH);
        add(panelSearch, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void searchLoan() {
        String searchId = txtLoanId.getText().trim();
        if (searchId.isEmpty()) {
            dashBoard.showMessage("Ingrese un ID de préstamo para buscar");
            return;
        }

        boolean isFound = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            String id = model.getValueAt(i, 0).toString();
            if (id.equalsIgnoreCase(searchId)) {
                tblLoans.setRowSelectionInterval(i, i);
                tblLoans.scrollRectToVisible(tblLoans.getCellRect(i, 0, true));
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            dashBoard.showMessage("Préstamo no encontrado con ID: " + searchId);
            tblLoans.clearSelection();
        }
    }

    private void registerRefund() {
        int fila = tblLoans.getSelectedRow();
        if (fila >= 0) {
            String idLoan = model.getValueAt(fila, 0).toString();
            String status = model.getValueAt(fila, 6).toString();
            String user = model.getValueAt(fila, 1).toString();
            String book = model.getValueAt(fila, 2).toString();

            if ("Devuelto".equals(status)) {
                dashBoard.showMessage("Este préstamo ya fue devuelto");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Confirmar devolución del préstamo?\n\n" +
                            "ID: " + idLoan + "\n" +
                            "Usuario: " + user + "\n" +
                            "Libro: " + book,
                    "Confirmar Devolución",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int loanId = Integer.parseInt(idLoan.substring(1));
                    dashBoard.getController().registerReturn(loanId);
                    txtLoanId.setText("");
                    tblLoans.clearSelection();
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    dashBoard.showMessage("Error al procesar el ID del préstamo: " + idLoan);
                }
            }
        } else {
            dashBoard.showMessage("Seleccione un préstamo de la tabla");
        }
    }

    public void updateTableData(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
        tblLoans.clearSelection();
        txtLoanId.setText("");
    }
}
