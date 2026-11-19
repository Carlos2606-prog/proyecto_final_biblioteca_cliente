package co.edu.uptc.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerClient {

    private static final String DISCONNECTED = "__DISCONNECTED__";
    private static final int TIMEOUT_SECONDS = 15;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String host;
    private int port;
    private volatile boolean connected;
    private volatile Runnable notificationListener;
    private final BlockingQueue<String> responses = new LinkedBlockingQueue<>();

    public ServerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setNotificationListener(Runnable listener) {
        this.notificationListener = listener;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        connected = true;

        Thread reader = new Thread(this::readLoop, "server-reader");
        reader.setDaemon(true);
        reader.start();
    }

    private void readLoop() {
        try {
            while (true) {
                int length = in.readInt();
                byte[] data = new byte[length];
                in.readFully(data);
                String message = new String(data, StandardCharsets.UTF_8);

                if (message.startsWith("NOTIFICATION;")) {
                    Runnable listener = notificationListener;
                    if (listener != null) {
                        listener.run();
                    }
                } else {
                    responses.put(message);
                }
            }
        } catch (IOException | InterruptedException e) {

        } finally {
            connected = false;
            responses.offer(DISCONNECTED);
        }
    }

    public void disconnect() {
        connected = false;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public synchronized String sendRequest(String request) throws IOException {
        if (!isConnected()) {
            throw new IOException("El socket no está conectado.");
        }
        responses.clear();

        byte[] data = request.getBytes(StandardCharsets.UTF_8);
        out.writeInt(data.length);
        out.write(data);
        out.flush();

        try {
            String response = responses.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (response == null) {
                throw new IOException("El servidor no respondió a tiempo.");
            }
            if (DISCONNECTED.equals(response)) {
                throw new IOException("Se perdió la conexión con el servidor.");
            }
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Operación interrumpida.");
        }
    }

    public boolean isConnected() {
        return connected && socket != null && socket.isConnected() && !socket.isClosed();
    }
}
