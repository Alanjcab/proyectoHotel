package hotel.controller;

import hotel.view.ClientView;
import hotel.view.MainView;
import hotel.view.ReservationView;
import hotel.view.RoomView;


public class MainController {

    private MainView mainView;
    private ClientView clientView;
    private RoomView roomView;
    private ReservationView reservationView;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        this.clientView = new ClientView();
        this.roomView = new RoomView();
        this.reservationView = new ReservationView();

        initController();
        mainView.showView(clientView);
    }

    private void initController() {

        mainView.getBtnClients().addActionListener(e -> {
            mainView.showView(clientView);
        });

        mainView.getBtnRooms().addActionListener(e -> {
            mainView.showView(roomView);
        });

        mainView.getBtnReservations().addActionListener(e -> {
            mainView.showView(reservationView);
        });
    }
}