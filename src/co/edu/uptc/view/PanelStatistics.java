package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

public class PanelStatistics extends JPanel {
    private DashBoard dashBoard;
    private JLabel lblUsers, lblBooks, lblActiveLoans, lblLateLoans, lblAvailable, lblCopies;

    public PanelStatistics(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(220, 20, 60));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("ESTADÍSTICAS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 20, 20));
        panelCentral.setBackground(new Color(240, 248, 255));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        lblUsers = new JLabel("0");
        panelCentral.add(createPanelStatistics("Total de Usuarios", lblUsers, new Color(100, 149, 237)));

        lblBooks = new JLabel("0");
        panelCentral.add(createPanelStatistics("Total de Libros", lblBooks, new Color(34, 139, 34)));

        lblActiveLoans = new JLabel("0");
        panelCentral.add(createPanelStatistics("Préstamos Activos", lblActiveLoans, new Color(255, 165, 0)));

        lblLateLoans = new JLabel("0");
        panelCentral.add(createPanelStatistics("Préstamos Atrasados", lblLateLoans, new Color(220, 20, 60)));

        lblAvailable = new JLabel("0");
        panelCentral.add(createPanelStatistics("Libros Disponibles", lblAvailable, new Color(70, 130, 180)));

        lblCopies = new JLabel("0");
        panelCentral.add(createPanelStatistics("Total Copias", lblCopies, new Color(138, 43, 226)));


        add(panelTitle, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel createPanelStatistics(String name, JLabel lblValue, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JLabel lblName = new JLabel(name, SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setForeground(Color.WHITE);
        lblName.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 36));
        lblValue.setForeground(Color.WHITE);
        lblValue.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        panel.add(lblName, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    public void updateStatistics(int[] stats) {
        lblUsers.setText(String.valueOf(stats[0]));
        lblBooks.setText(String.valueOf(stats[1]));
        lblActiveLoans.setText(String.valueOf(stats[2]));
        lblLateLoans.setText(String.valueOf(stats[3]));
        lblAvailable.setText(String.valueOf(stats[4]));
        lblCopies.setText(String.valueOf(stats[5]));
    }
}
