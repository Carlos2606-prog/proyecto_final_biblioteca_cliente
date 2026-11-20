package co.edu.uptc.view;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PanelRegisterLoan extends JPanel {
    private DashBoard dashBoard;
    private JComboBox<String> cmbUsers;
    private JComboBox<String> cmbBooks;
    private JComboBox<String> cmbCopies;
    private JDateChooser dateChooser;
    private JTextArea txtAreaInfo;

    public PanelRegisterLoan(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard2) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("REGISTRAR PRÉSTAMO");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        panelForm.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbUsers = new JComboBox<>();
        cmbUsers.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbUsers.setPreferredSize(new Dimension(400, 25));
        panelForm.add(cmbUsers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblBook = new JLabel("Libro:");
        lblBook.setFont(new Font("Arial", Font.PLAIN, 14));
        panelForm.add(lblBook, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbBooks = new JComboBox<>();
        cmbBooks.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbBooks.setPreferredSize(new Dimension(400, 25));
        cmbBooks.addActionListener(e -> loadAvailableCopies());
        panelForm.add(cmbBooks, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCopy = new JLabel("Copia Disponible:");
        lblCopy.setFont(new Font("Arial", Font.PLAIN, 14));
        panelForm.add(lblCopy, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbCopies = new JComboBox<>();
        cmbCopies.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbCopies.setPreferredSize(new Dimension(400, 25));
        panelForm.add(cmbCopies, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblDate = new JLabel("Fecha Devolución:");
        lblDate.setFont(new Font("Arial", Font.PLAIN, 14));
        panelForm.add(lblDate, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
        dateChooser.setPreferredSize(new Dimension(400, 25));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMinSelectableDate(new java.util.Date());
        panelForm.add(dateChooser, gbc);

        JButton btnRegister = new JButton("Registrar Préstamo");
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(e -> registerLoan());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelForm.add(btnRegister, gbc);

        txtAreaInfo = new JTextArea(10, 50);
        txtAreaInfo.setEditable(false);
        txtAreaInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        txtAreaInfo.setBorder(BorderFactory.createTitledBorder("Información del Préstamo"));
        JScrollPane scrollInfo = new JScrollPane(txtAreaInfo);


        add(panelTitle, BorderLayout.NORTH);
        add(panelForm, BorderLayout.WEST);
        add(scrollInfo, BorderLayout.CENTER);
    }

    private void loadAvailableCopies() {
        String selection = (String) cmbBooks.getSelectedItem();
        if (selection == null || selection.startsWith("--")) {
            return;
        }

        String bookCode = selection.split(" - ")[0];
        dashBoard.getController().loadAvailableCopies(bookCode);
    }

    private void registerLoan() {
        if (cmbUsers.getSelectedIndex() <= 0) {
            dashBoard.showMessage("Seleccione un usuario");
            return;
        }

        if (cmbBooks.getSelectedIndex() <= 0) {
            dashBoard.showMessage("Seleccione un libro");
            return;
        }

        if (cmbCopies.getSelectedIndex() <= 0 || cmbCopies.getItemCount() == 0) {
            dashBoard.showMessage("Seleccione una copia disponible");
            return;
        }

        if (dateChooser.getDate() == null) {
            dashBoard.showMessage("Seleccione la fecha de devolución");
            return;
        }

        String selectedUser = (String) cmbUsers.getSelectedItem();
        if (selectedUser.startsWith("--")) {
            dashBoard.showMessage("Seleccione un usuario válido");
            return;
        }

        String selectedCopy = (String) cmbCopies.getSelectedItem();
        if (selectedCopy.startsWith("--")) {
            dashBoard.showMessage("Seleccione una copia válida");
            return;
        }

        String userId = selectedUser.split(" - ")[0].trim();
        String copyIdStr;
        try {
            int startIndex = selectedCopy.indexOf("(ID:") + 4;
            int endIndex = selectedCopy.indexOf(")", startIndex);
            copyIdStr = selectedCopy.substring(startIndex, endIndex).trim();
        } catch (Exception e) {
            dashBoard.showMessage("Error al extraer el ID de la copia");
            return;
        }

        java.util.Date date = dateChooser.getDate();
        LocalDate refundDate = new java.sql.Date(date.getTime()).toLocalDate();
        String dateStr = refundDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        dashBoard.getController().registerLoan(userId, copyIdStr, dateStr);
    }

    public void updateUsersList(String[] users) {
        cmbUsers.removeAllItems();
        for (String user : users) {
            cmbUsers.addItem(user);
        }
    }

    public void updateBooksList(String[] books) {
        cmbBooks.removeAllItems();
        for (String book : books) {
            cmbBooks.addItem(book);
        }
    }

    public void updateCopiesList(String[] copies) {
        cmbCopies.removeAllItems();
        for (String copy : copies) {
            cmbCopies.addItem(copy);
        }
    }

    public void showLoanInfo(String info) {
        txtAreaInfo.setText(info);
    }

    public void clearForm() {
        cmbUsers.setSelectedIndex(0);
        cmbBooks.setSelectedIndex(0);
        cmbCopies.removeAllItems();
        dateChooser.setDate(null);
        txtAreaInfo.setText("");
    }
}
