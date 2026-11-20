package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;

public class PanelMyDetails extends JPanel {
    private DashBoard dashBoard;
    private JTextArea txtDetailsArea;

    public PanelMyDetails(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        initComponents(dashBoard);
    }

    private void initComponents(DashBoard dashBoard) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(new Color(100, 149, 237));
        panelTitle.setPreferredSize(new Dimension(900, 60));
        JLabel title = new JLabel("MIS DATOS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panelTitle.add(title);

        txtDetailsArea = new JTextArea(15, 40);
        txtDetailsArea.setEditable(false);
        txtDetailsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDetailsArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(txtDetailsArea);


        add(panelTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateUserData(String info) {
        txtDetailsArea.setText(info);
    }
}
