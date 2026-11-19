package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelManagementUsers extends JPanel {
    private DashBoard dashBoard;
    private JTable tblUsers;
    private JTextField txtUserId, txtName;
    private JComboBox<String> cmbType;
    private DefaultTableModel model;

    public PanelManagementUsers(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("GESTIÓN DE USUARIOS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelForm.setBackground(new Color(240, 248, 255));

        panelForm.add(new JLabel("ID:"));
        txtUserId = new JTextField(10);
        panelForm.add(txtUserId);

        panelForm.add(new JLabel("Nombre:"));
        txtName = new JTextField(20);
        panelForm.add(txtName);

        panelForm.add(new JLabel("Tipo:"));
        cmbType = new JComboBox<>(new String[] { "Estudiante", "Profesor", "Externo" });
        panelForm.add(cmbType);

        JButton btnAdd = new JButton("Agregar");
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> addUser());
        panelForm.add(btnAdd);

        JButton btnEdit = new JButton("Editar");
        btnEdit.setBackground(new Color(255, 165, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> editUser());
        panelForm.add(btnEdit);

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteUser());
        panelForm.add(btnDelete);

        String[] columns = { "ID", "Nombre", "Tipo", "Préstamos Activos" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(model);
        tblUsers.setFont(new Font("Arial", Font.PLAIN, 13));
        tblUsers.setRowHeight(25);
        tblUsers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblUsers.getSelectedRow();
                if (row >= 0) {
                    txtUserId.setText(model.getValueAt(row, 0).toString());
                    txtName.setText(model.getValueAt(row, 1).toString());
                    cmbType.setSelectedItem(model.getValueAt(row, 2));
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tblUsers);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelInferior.setBackground(new Color(240, 248, 255));
        JButton btnGoBack = new JButton("Volver al Menú");
        btnGoBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnGoBack.setBackground(new Color(128, 128, 128));
        btnGoBack.setForeground(Color.WHITE);
        btnGoBack.setFocusPainted(false);
        btnGoBack.addActionListener(e -> dashBoard.showPanel("menuAdmin"));
        panelInferior.add(btnGoBack);

        add(panelTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void addUser() {
        if (!txtUserId.getText().isEmpty() && !txtName.getText().isEmpty()) {
            dashBoard.getController().addUser(
                    txtUserId.getText(),
                    txtName.getText(),
                    cmbType.getSelectedItem().toString());
            cleanFields();
        }
    }

    private void editUser() {
        int fila = tblUsers.getSelectedRow();
        if (fila >= 0) {
            dashBoard.getController().editUser(
                    txtUserId.getText(),
                    txtName.getText(),
                    cmbType.getSelectedItem().toString());
            cleanFields();
        } else {
            dashBoard.showMessage("Seleccione un usuario de la tabla");
        }
    }

    private void deleteUser() {
        int fila = tblUsers.getSelectedRow();
        if (fila >= 0) {
            String userId = model.getValueAt(fila, 0).toString();
            dashBoard.getController().removeUser(userId);
            cleanFields();
        } else {
            dashBoard.showMessage("Seleccione un usuario de la tabla");
        }
    }

    private void cleanFields() {
        txtUserId.setText("");
        txtName.setText("");
        cmbType.setSelectedIndex(0);
    }

    public void updateTableData(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}
