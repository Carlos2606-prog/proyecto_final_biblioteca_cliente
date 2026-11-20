package co.edu.uptc.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SideMenuPanel extends JPanel {

    public static final String HOME = "inicio";

    private static final Color BACKGROUND = new Color(240, 248, 255);
    private static final int SIDEBAR_WIDTH = 220;

    protected final DashBoard dashBoard;
    private final Color btnColor;
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel panelContent = new JPanel(contentLayout);
    private final JPanel panelButtons = new JPanel(new GridLayout(0, 1, 0, 10));
    private final Map<String, JButton> buttons = new HashMap<>();
    private final Set<String> contentNames = new HashSet<>();
    private final Border defaultBorder = new JButton().getBorder();
    private final Border selectedBorder = BorderFactory.createLineBorder(Color.WHITE, 3);

    public SideMenuPanel(DashBoard dashBoard, String title, Color headerColor, Color btnColor, Color exitColor) {
        this.dashBoard = dashBoard;
        this.btnColor = btnColor;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        panelContent.setBackground(BACKGROUND);
        panelButtons.setBackground(BACKGROUND);

        add(buildSidebar(title, headerColor, exitColor), BorderLayout.WEST);
        add(panelContent, BorderLayout.CENTER);
        panelContent.add(buildHome(title), HOME);
    }

    private JPanel buildSidebar(String title, Color headerColor, Color exitColor) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(BACKGROUND);
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 215, 230)));

        JPanel panelTitle = new JPanel(new GridBagLayout());
        panelTitle.setBackground(headerColor);
        panelTitle.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 60));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        panelTitle.add(lblTitle);

        JPanel panelOptions = new JPanel(new BorderLayout());
        panelOptions.setBackground(BACKGROUND);
        panelOptions.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        panelOptions.add(panelButtons, BorderLayout.NORTH);

        JButton btnExit = createBtn("Cerrar Sesión", exitColor);
        btnExit.addActionListener(e -> {
            dashBoard.getController().logout();
            dashBoard.showPanel("seleccion");
        });
        panelOptions.add(btnExit, BorderLayout.SOUTH);

        sidebar.add(panelTitle, BorderLayout.NORTH);
        sidebar.add(panelOptions, BorderLayout.CENTER);
        return sidebar;
    }

    private JPanel buildHome(String title) {
        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblWelcome = new JLabel("BIBLIOTECA VIRTUAL", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(btnColor);
        gbc.gridy = 0;
        home.add(lblWelcome, gbc);

        JLabel lblHint = new JLabel("Seleccione una opción del menú de la izquierda", SwingConstants.CENTER);
        lblHint.setFont(new Font("Arial", Font.PLAIN, 16));
        lblHint.setForeground(Color.DARK_GRAY);
        gbc.gridy = 1;
        home.add(lblHint, gbc);
        return home;
    }

    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 42));
        return btn;
    }

    protected void addOption(String contentName, String text) {
        JButton btn = createBtn(text, btnColor);
        btn.addActionListener(e -> dashBoard.showPanel(contentName));
        buttons.put(contentName, btn);
        panelButtons.add(btn);
    }

    public void addContent(String contentName, JPanel panel) {
        contentNames.add(contentName);
        panelContent.add(panel, contentName);
    }

    public boolean hasContent(String contentName) {
        return contentNames.contains(contentName);
    }

    public void showContent(String contentName) {
        contentLayout.show(panelContent, contentName);
        for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
            entry.getValue().setBorder(entry.getKey().equals(contentName) ? selectedBorder : defaultBorder);
        }
    }
}
