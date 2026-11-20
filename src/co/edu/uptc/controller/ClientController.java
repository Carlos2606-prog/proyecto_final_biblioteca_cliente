package co.edu.uptc.controller;

import co.edu.uptc.view.DashBoard;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import javax.swing.SwingUtilities;

public class ClientController {

    private static final int PORT = 1234;

    private ServerClient client;
    private DashBoard dashBoard;
    private boolean connected;
    private String lastCriterion = "";
    private String lastSearch = "";

    public ClientController(String serverIp) {
        this.client = new ServerClient(serverIp, PORT);
        client.setNotificationListener(this::onServerNotification);
        try {
            client.connect();
            connected = true;
            System.out.println("Conectado al servidor exitosamente en " + serverIp);
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setView(DashBoard dashBoard) {
        this.dashBoard = dashBoard;
    }

    private void onServerNotification() {
        SwingUtilities.invokeLater(() -> {
            if (dashBoard != null) {
                dashBoard.refreshCurrentPanel();
            }
        });
    }

    private boolean isOk(String response) {
        return response != null && response.startsWith("SUCCESS");
    }

    private String errorOf(String response) {
        return response != null && response.startsWith("ERROR;") ? response.substring(6) : String.valueOf(response);
    }

    private String payload(String response) {
        int i = response.indexOf(';');
        return i < 0 ? "" : response.substring(i + 1);
    }

    private JsonArray parseArray(String response) {
        return new JsonParser().parse(payload(response)).getAsJsonArray();
    }

    private boolean checkOrShow(String response) {
        if (isOk(response)) {
            return true;
        }
        dashBoard.showMessage(errorOf(response));
        return false;
    }

    private void showError(Exception e) {
        dashBoard.showMessage("Error: " + e.getMessage());
    }

    private String clean(String s) {
        return s == null ? "" : s.replace(";", ",").trim();
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String str(JsonObject o, String key) {
        JsonElement e = o.get(key);
        return (e == null || e.isJsonNull()) ? "" : e.getAsString();
    }

    private Object[][] toRows(JsonArray array, String... keys) {
        Object[][] rows = new Object[array.size()][keys.length];
        for (int i = 0; i < array.size(); i++) {
            JsonObject o = array.get(i).getAsJsonObject();
            for (int j = 0; j < keys.length; j++) {
                rows[i][j] = str(o, keys[j]);
            }
        }
        return rows;
    }

    private void prefixLoanIds(Object[][] rows) {
        for (Object[] row : rows) {
            row[0] = "L" + row[0];
        }
    }


    public void loginAdmin(String user, String password) {
        try {
            String response = client.sendRequest("LOGIN_ADMIN;" + clean(user) + ";" + password.replace(";", ","));
            if (isOk(response)) {
                dashBoard.showLoginSuccess(false);
            } else {
                dashBoard.showLoginError(errorOf(response));
            }
        } catch (IOException e) {
            dashBoard.showLoginError("Error de conexión con el servidor.");
        }
    }

    public void loginUser(String userId, String name) {
        try {
            String response = client.sendRequest("LOGIN_USER;" + clean(userId) + ";" + clean(name));
            if (isOk(response)) {
                dashBoard.showLoginSuccess(true);
            } else {
                dashBoard.showLoginError(errorOf(response));
            }
        } catch (IOException e) {
            dashBoard.showLoginError("Error de conexión con el servidor.");
        }
    }

    public void logout() {
        try {
            client.sendRequest("LOGOUT");
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión en el servidor");
        }
    }


    public void updateBooksTable() {
        try {
            String response = client.sendRequest("GET_BOOKS");
            if (!checkOrShow(response)) {
                return;
            }
            Object[][] data = toRows(parseArray(response), "code", "title", "author", "genre", "year", "totalCopies");
            dashBoard.getPanelManagementBook().updateTableData(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void addBook(String code, String title, String author, String kind, String year, String copies) {
        if (!isInt(code) || !isInt(year) || !isInt(copies)) {
            dashBoard.showMessage("El código, el año y las copias deben ser números enteros.");
            return;
        }
        try {
            String response = client.sendRequest(String.format("ADD_BOOK;%s;%s;%s;%s;%s;%s",
                    clean(code), clean(title), clean(author), clean(kind), clean(year), clean(copies)));
            if (checkOrShow(response)) {
                dashBoard.showMessage("Libro agregado exitosamente.");
                updateBooksTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void editBook(String code, String title, String author, String kind, String year, String copies) {
        if (!isInt(code) || !isInt(year) || !isInt(copies)) {
            dashBoard.showMessage("El código, el año y las copias deben ser números enteros.");
            return;
        }
        try {
            String response = client.sendRequest(String.format("EDIT_BOOK;%s;%s;%s;%s;%s;%s",
                    clean(code), clean(title), clean(author), clean(kind), clean(year), clean(copies)));
            if (checkOrShow(response)) {
                updateBooksTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void removeBook(String code) {
        try {
            String response = client.sendRequest("DELETE_BOOK;" + clean(code));
            if (checkOrShow(response)) {
                updateBooksTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void searchBooks(String criterion, String search, boolean isAdmin) {
        lastCriterion = criterion == null ? "" : criterion;
        lastSearch = search == null ? "" : search;
        try {
            String response = client.sendRequest("SEARCH_BOOKS;" + clean(lastCriterion) + ";" + clean(lastSearch));
            if (!checkOrShow(response)) {
                return;
            }
            Object[][] data = toRows(parseArray(response),
                    "code", "title", "author", "genre", "year", "totalCopies", "availableCopies");
            if (isAdmin) {
                dashBoard.getPanelSearchBooksAdmin().updateSearchResults(data);
            } else {
                dashBoard.getPanelSearchBooksUser().updateSearchResults(data);
            }
        } catch (Exception e) {
            showError(e);
        }
    }


    public void refreshSearch(boolean isAdmin) {
        searchBooks(lastCriterion, lastSearch, isAdmin);
    }


    public void updateUsersTable() {
        try {
            String response = client.sendRequest("GET_USERS");
            if (!checkOrShow(response)) {
                return;
            }
            Object[][] data = toRows(parseArray(response), "userId", "name", "type", "activeLoans");
            dashBoard.getPanelManagementUsers().updateTableData(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void addUser(String id, String name, String type) {
        try {
            String response = client.sendRequest(String.format("ADD_USER;%s;%s;%s", clean(id), clean(name), clean(type)));
            if (checkOrShow(response)) {
                updateUsersTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void editUser(String id, String name, String type) {
        try {
            String response = client.sendRequest(String.format("EDIT_USER;%s;%s;%s", clean(id), clean(name), clean(type)));
            if (checkOrShow(response)) {
                updateUsersTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void removeUser(String id) {
        try {
            String response = client.sendRequest("DELETE_USER;" + clean(id));
            if (checkOrShow(response)) {
                updateUsersTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }


    public void loadBooksForLoan() {
        try {
            String usersResponse = client.sendRequest("GET_USERS");
            String booksResponse = client.sendRequest("GET_BOOKS");
            if (!checkOrShow(usersResponse) || !checkOrShow(booksResponse)) {
                return;
            }

            JsonArray users = parseArray(usersResponse);
            String[] userItems = new String[users.size() + 1];
            userItems[0] = "-- Seleccione --";
            for (int i = 0; i < users.size(); i++) {
                JsonObject o = users.get(i).getAsJsonObject();
                userItems[i + 1] = str(o, "userId") + " - " + str(o, "name");
            }

            JsonArray books = parseArray(booksResponse);
            java.util.List<String> bookItems = new java.util.ArrayList<>();
            bookItems.add("-- Seleccione --");
            for (int i = 0; i < books.size(); i++) {
                JsonObject o = books.get(i).getAsJsonObject();
                if (o.get("availableCopies").getAsInt() > 0) {
                    bookItems.add(str(o, "code") + " - " + str(o, "title"));
                }
            }

            dashBoard.getPanelRegisterLoan().updateUsersList(userItems);
            dashBoard.getPanelRegisterLoan().updateBooksList(bookItems.toArray(new String[0]));
            dashBoard.getPanelRegisterLoan().updateCopiesList(new String[] { "-- Seleccione --" });
        } catch (Exception e) {
            showError(e);
        }
    }

    public void loadAvailableCopies(String bookCode) {
        try {
            String response = client.sendRequest("GET_AVAILABLE_COPIES;" + clean(bookCode));
            if (!checkOrShow(response)) {
                return;
            }
            JsonArray ids = parseArray(response);
            String[] items = new String[ids.size() + 1];
            items[0] = "-- Seleccione --";
            for (int i = 0; i < ids.size(); i++) {
                items[i + 1] = "Copia " + (i + 1) + " (ID: " + ids.get(i).getAsInt() + ")";
            }
            dashBoard.getPanelRegisterLoan().updateCopiesList(items);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void registerLoan(String userId, String copyId, String date) {
        try {
            String response = client.sendRequest(String.format("REGISTER_LOAN;%s;%s;%s",
                    clean(userId), clean(copyId), clean(date)));
            if (checkOrShow(response)) {
                dashBoard.showMessage("Préstamo registrado.");
                dashBoard.getPanelRegisterLoan().clearForm();
                loadBooksForLoan(); // refresca disponibilidad
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void updateLoansTable() {
        try {
            String response = client.sendRequest("GET_LOANS");
            if (!checkOrShow(response)) {
                return;
            }
            Object[][] data = toRows(parseArray(response),
                    "loanId", "userName", "bookTitle", "copyId", "loanDate", "returnDate", "status");
            prefixLoanIds(data);
            dashBoard.getPanelRegisterRefund().updateTableData(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void registerReturn(int loanId) {
        try {
            String response = client.sendRequest("REGISTER_RETURN;" + loanId);
            if (checkOrShow(response)) {
                dashBoard.showMessage("Devolución registrada.");
                updateLoansTable();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    public void updateUserLoansTable() {
        try {
            String response = client.sendRequest("GET_MY_LOANS");
            if (!checkOrShow(response)) {
                return;
            }
            Object[][] data = toRows(parseArray(response),
                    "loanId", "bookTitle", "copyId", "loanDate", "returnDate", "status");
            prefixLoanIds(data);
            dashBoard.getPanelMyLoans().updateTableData(data);
        } catch (Exception e) {
            showError(e);
        }
    }

    // ==========================================
    // PERFIL Y ESTADISTICAS
    // ==========================================

    public void updateUserInfo() {
        try {
            String response = client.sendRequest("GET_MY_DETAILS");
            if (!checkOrShow(response)) {
                return;
            }
            JsonObject o = new JsonParser().parse(payload(response)).getAsJsonObject();
            String info = "ID: " + str(o, "userId")
                    + "\nNombre: " + str(o, "name")
                    + "\nTipo: " + str(o, "type")
                    + "\nPréstamos activos: " + str(o, "activeLoans");
            dashBoard.getPanelMyDetails().updateUserData(info);
        } catch (Exception e) {
            showError(e);
        }
    }

    public void updateStatistics() {
        try {
            String response = client.sendRequest("GET_STATISTICS");
            if (!checkOrShow(response)) {
                return;
            }
            JsonArray array = parseArray(response);
            int[] stats = new int[6];
            for (int i = 0; i < stats.length; i++) {
                stats[i] = array.get(i).getAsInt();
            }
            dashBoard.getPanelStatistics().updateStatistics(stats);
        } catch (Exception e) {
            showError(e);
        }
    }
}
