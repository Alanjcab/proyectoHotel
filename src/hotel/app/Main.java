package hotel.app;

import hotel.controller.MainController;
import hotel.view.MainView;
import hotel.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Conexion exitosa a mysql");
        } catch (SQLException e) {
            System.err.println("Error de conexion: " + e.getMessage());
        }

        MainView mainView = new MainView();

        MainController mainController = new MainController(mainView);

        mainView.setVisible(true);
    }
}