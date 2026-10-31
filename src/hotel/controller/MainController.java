package hotel.controller;

import hotel.dao.ClientDao;
import hotel.dao.ClientSqlDao;
import hotel.view.ClientView;
import hotel.view.MainView;
import hotel.view.ReservationView;
import hotel.view.RoomView;

public class MainController {

    private final MainView mainView;
    private final ClientView clientView;
    private final RoomView roomView;
    private final ReservationView reservationView;

    private final ClientController clientController;

    public MainController(MainView mainView) {
        this.mainView = mainView;

        this.clientView = new ClientView();
        this.roomView = new RoomView();
        this.reservationView = new ReservationView();

        ClientDao clientDao = new ClientSqlDao();

        this.clientController = new ClientController(clientView, clientDao);

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