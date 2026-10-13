package hotel.app;

import hotel.controller.MainController;
import hotel.view.MainView;

public class Main {

    public static void main(String[] args) {

        MainView mainView = new MainView();

        MainController mainController = new MainController(mainView);

        mainView.setVisible(true);
    }
}