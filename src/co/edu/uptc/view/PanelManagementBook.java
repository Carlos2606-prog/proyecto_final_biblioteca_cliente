package co.edu.uptc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelManagementBook extends JPanel {
    private DashBoard dashBoard;
    private JTable tblBooks;
    private JTextField txtCode, txtTitle, txtAuthor, txtKind, txtYear, txtCopies;
    private DefaultTableModel model;

    public PanelManagementBook(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("GESTIÓN DE LIBROS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelForm = new JPanel(new GridLayout(2, 1, 5, 5));
        panelForm.setBackground(new Color(240, 248, 255));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.setBackground(new Color(240, 248, 255));
        row1.add(new JLabel("Código:"));
        txtCode = new JTextField(8);
        row1.add(txtCode);
        row1.add(new JLabel("Título:"));
        txtTitle = new JTextField(20);
        row1.add(txtTitle);
        row1.add(new JLabel("Autor:"));
        txtAuthor = new JTextField(15);
        row1.add(txtAuthor);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setBackground(new Color(240, 248, 255));
        row2.add(new JLabel("Género:"));
        txtKind = new JTextField(12);
        row2.add(txtKind);
        row2.add(new JLabel("Año:"));
        txtYear = new JTextField(6);
        row2.add(txtYear);
        row2.add(new JLabel("Copias:"));
        txtCopies = new JTextField(5);
        row2.add(txtCopies);

        JButton btnAdd = new JButton("Agregar");
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> addBook());
        row2.add(btnAdd);

        JButton btnEdit = new JButton("Editar");
        btnEdit.setBackground(new Color(255, 165, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> editBook());
        row2.add(btnEdit);

        JButton btnDelete = new JButton("Eliminar");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteBook());
        row2.add(btnDelete);

        panelForm.add(row1);
        panelForm.add(row2);

        String[] columns = { "Código", "Título", "Autor", "Género", "Año", "Copias" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBooks = new JTable(model);
        tblBooks.setFont(new Font("Arial", Font.PLAIN, 13));
        tblBooks.setRowHeight(25);

        tblBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblBooks.getSelectedRow();
                if (fila >= 0) {
                    txtCode.setText(model.getValueAt(fila, 0).toString());
                    txtTitle.setText(model.getValueAt(fila, 1).toString());
                    txtAuthor.setText(model.getValueAt(fila, 2).toString());
                    txtKind.setText(model.getValueAt(fila, 3).toString());
                    txtYear.setText(model.getValueAt(fila, 4).toString());
                    txtCopies.setText(model.getValueAt(fila, 5).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblBooks);

        add(panelTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addBook() {
        if (!txtCode.getText().trim().isEmpty() && !txtTitle.getText().trim().isEmpty()) {
            dashBoard.getController().addBook(
                    txtCode.getText().trim(),
                    txtTitle.getText().trim(),
                    txtAuthor.getText().trim(),
                    txtKind.getText().trim(),
                    txtYear.getText().trim(),
                    txtCopies.getText().trim());
            cleanFields();
        } else {
            dashBoard.showMessage("Complete al menos el código y el título del libro");
        }
    }

    private void editBook() {
        int fila = tblBooks.getSelectedRow();
        if (fila >= 0) {
            if (!txtCode.getText().trim().isEmpty() && !txtTitle.getText().trim().isEmpty()) {
                dashBoard.getController().editBook(
                        txtCode.getText().trim(),
                        txtTitle.getText().trim(),
                        txtAuthor.getText().trim(),
                        txtKind.getText().trim(),
                        txtYear.getText().trim(),
                        txtCopies.getText().trim());
                cleanFields();
            } else {
                dashBoard.showMessage("Complete al menos el código y el título del libro");
            }
        } else {
            dashBoard.showMessage("Seleccione un libro de la tabla");
        }
    }

    private void deleteBook() {
        int fila = tblBooks.getSelectedRow();
        if (fila >= 0) {
            String code = model.getValueAt(fila, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar el libro '" + model.getValueAt(fila, 1) + "'?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                dashBoard.getController().removeBook(code);
                cleanFields();
            }
        } else {
            dashBoard.showMessage("Seleccione un libro de la tabla");
        }
    }

    private void cleanFields() {
        txtCode.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtKind.setText("");
        txtYear.setText("");
        txtCopies.setText("");
        tblBooks.clearSelection();
    }

    public void updateTableData(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}
