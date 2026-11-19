import co.edu.uptc.controller.ClientController;
import co.edu.uptc.view.DashBoard;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {

        String ip = recoveryIP();

        if (ip == null || ip.trim().isEmpty()) {
            System.exit(0);
        }

        ClientController controller = new ClientController(ip);

        DashBoard dashBoard = new DashBoard(controller);
        dashBoard.setVisible(true);
    }

    public static String recoveryIP() {
        return JOptionPane.showInputDialog(
                null,
                "Digite la IP del servidor:",
                "Conexión al Servidor",
                JOptionPane.QUESTION_MESSAGE);
    }
}