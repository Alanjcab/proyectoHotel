package hotel.controller;

import hotel.dao.ClientDao;
import hotel.dao.RoomDao;
import hotel.model.Client;
import hotel.model.Room;
import hotel.view.ReservationView;
import hotel.service.ReservationService;
import hotel.dao.ReservationDao;
import hotel.dao.ReservationSqlDao;
import hotel.validation.ReservationValidator;
import hotel.model.Reservation;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import java.util.List;

public class ReservationController {

    private final ReservationView view;
    private final ClientDao clientDao;
    private final RoomDao roomDao;
    private final ReservationService reservationService;
    private final ReservationDao reservationDao;
    private Reservation selectedReservation;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT);

    private Client selectedClient;

    public ReservationController(
            ReservationView view,
            ClientDao clientDao,
            RoomDao roomDao) {

        this.view = view;
        this.clientDao = clientDao;
        this.roomDao = roomDao;

        this.reservationDao = new ReservationSqlDao();

        this.reservationService = new ReservationService(
                reservationDao,
                new ReservationValidator()
        );

        initController();
        loadRooms();
        loadReservations();
    }

    private void initController() {
        view.getBtnSearchClient().addActionListener(e -> searchClient());
        view.getRoomCombo().addActionListener(e -> calculateTotal());
        view.getDateOfAdmissionField().addPropertyChangeListener("value", e -> calculateTotal());
        view.getDepartureDateField().addPropertyChangeListener("value", e -> calculateTotal());
        view.getDateOfAdmissionField().addActionListener(e -> calculateTotal());
        view.getDepartureDateField().addActionListener(e -> calculateTotal());
        view.getBtnSave().addActionListener(e -> saveReservation());
        view.getReservationTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectReservation();
            }
        });
        view.getBtnUpdate().addActionListener(e -> updateReservation());
        view.getBtnCancel().addActionListener(e -> cancelReservation());
        view.getBtnSearch().addActionListener(e -> searchReservations());
        view.getBtnNew().addActionListener(e -> clearForm());
    }

    private void searchClient() {

        String dni = view.getTxtClientSearch().getText().trim();

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ingrese el dni del cliente");
            return;
        }

        try {
            Client client = clientDao.findByDni(dni);

            if (client == null || !client.getActive()) {
                selectedClient = null;

                view.getLblClientSelected().setText("Ningun cliente seleccionado");

                JOptionPane.showMessageDialog(view, "No se encontro un cliente activo con ese dni");
                return;
            }

            selectedClient = client;

            view.getLblClientSelected().setText(client.getName() + " " + client.getLastName() + " - dni: " + client.getDni());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRooms() {

        try {
            List<Room> rooms = roomDao.findAll();

            view.getRoomCombo().removeAllItems();

            for (Room room : rooms) {
                if (room.getStatus() != null && room.getStatus().equalsIgnoreCase("Disponible")) {
                    view.getRoomCombo().addItem(room);
                }
            }

            view.getRoomCombo().setSelectedIndex(-1);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "error al cargar las habitaciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void loadReservations() {

        String[] columns = {"ID", "Cliente", "Habitacion", "Ingreso", "Salida", "Estado", "Total"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            List<Reservation> reservations = reservationDao.findAll();

            for (Reservation reservation : reservations) {

                Object[] row = {
                        reservation.getIdReservation(),
                        reservation.getClient().getName() + " " + reservation.getClient().getLastName(),
                        reservation.getRoom().getNumber(),
                        reservation.getCheckIn().format(dateFormatter),
                        reservation.getCheckOut().format(dateFormatter),
                        reservation.getStatus(),
                        reservation.getTotalPrice()
                };

                model.addRow(row);
            }

            view.getReservationTable().setModel(model);
            //oculto la columna del id en la tabla
            view.getReservationTable().removeColumn(
                    view.getReservationTable().getColumnModel().getColumn(0)
            );


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar las reservas " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }



    private void calculateTotal() {
        Room room = (Room) view.getRoomCombo().getSelectedItem();

        String checkInText = view.getDateOfAdmissionField().getText().trim();
        String checkOutText = view.getDepartureDateField().getText().trim();

        view.getFinalPriceField().setText("");

        if (room == null || checkInText.isEmpty() || checkOutText.isEmpty()) {
            return;
        }

        try {
            LocalDate checkIn = LocalDate.parse(checkInText, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutText, dateFormatter);

            Reservation reservation = new Reservation();
            reservation.setRoom(room);
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);

            BigDecimal total = reservationService.calculateTotalPrice(reservation);

            view.getFinalPriceField().setText(total.toPlainString());

        } catch (DateTimeParseException ex) {
            view.getFinalPriceField().setText("");
        } catch (IllegalArgumentException ex) {
            view.getFinalPriceField().setText("");
        }
    }


    /**
     * obtengo los datos del formulario y guardo una nueva reserva
     */
    private void saveReservation() {

        if (selectedClient == null) {
            JOptionPane.showMessageDialog(view, "Primero debe buscar y seleccionar un cliente");
            return;
        }

        Room selectedRoom = (Room) view.getRoomCombo().getSelectedItem();

        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una habitacion.");
            return;
        }

        try {
            String checkInText = view.getDateOfAdmissionField().getText().trim();
            String checkOutText = view.getDepartureDateField().getText().trim();

            LocalDate checkIn = LocalDate.parse(checkInText, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutText, dateFormatter);

            String status = (String) view.getEstateCombo().getSelectedItem();

            Reservation reservation = new Reservation();

            reservation.setClient(selectedClient);
            reservation.setRoom(selectedRoom);
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);
            reservation.setStatus(status);

            int reservationId = reservationService.saveReservation(reservation);

            loadReservations();

            JOptionPane.showMessageDialog(view, "Reserva guardada correctamente");

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Ingrese las fechas con el formato dd/MM/yyyy.", "fecha incorrecta",
                    JOptionPane.WARNING_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void selectReservation() {

        int selectedRow = view.getReservationTable().getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        try {
            int modelRow = view.getReservationTable().convertRowIndexToModel(selectedRow);

            int reservationId = (int) view.getReservationTable().getModel().getValueAt(modelRow, 0);

            selectedReservation = reservationDao.findById(reservationId);

            if (selectedReservation == null) {
                JOptionPane.showMessageDialog(view, "No se encontro la reserva");
                return;
            }

            selectedClient = selectedReservation.getClient();

            view.getTxtClientSearch().setText(selectedClient.getDni());

            view.getLblClientSelected().setText(selectedClient.getName() + " " + selectedClient.getLastName() + " - dni: " + selectedClient.getDni()
            );

            Room reservationRoom = selectedReservation.getRoom();

            boolean roomFound = false;

            for (int i = 0; i < view.getRoomCombo().getItemCount(); i++) {
                if (reservationRoom.equals(view.getRoomCombo().getItemAt(i))) {
                    roomFound = true;
                    break;
                }
            }

            if (!roomFound) {
                view.getRoomCombo().addItem(reservationRoom);
            }

            view.getRoomCombo().setSelectedItem(selectedReservation.getRoom());

            view.getDateOfAdmissionField().setText(selectedReservation.getCheckIn().format(dateFormatter));

            view.getDepartureDateField().setText(selectedReservation.getCheckOut().format(dateFormatter));

            view.getEstateCombo().setSelectedItem(selectedReservation.getStatus());

            view.getFinalPriceField().setText(selectedReservation.getTotalPrice().toPlainString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "error al seleccionar la reserva: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    /**
     * obtengo los datos del formulario y modifica una reserva
     */
    private void updateReservation() {

        if (selectedReservation == null) {
            JOptionPane.showMessageDialog(
                    view, "Primero debe seleccionar una reserva de la tabla"
            );
            return;
        }

        if (selectedClient == null) {
            JOptionPane.showMessageDialog(
                    view, "Debe seleccionar un cliente"
            );
            return;
        }

        Room selectedRoom = (Room) view.getRoomCombo().getSelectedItem();

        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una habitacion"
            );
            return;
        }

        try {
            String checkInText = view.getDateOfAdmissionField().getText().trim();
            String checkOutText = view.getDepartureDateField().getText().trim();

            LocalDate checkIn = LocalDate.parse(checkInText, dateFormatter);
            LocalDate checkOut = LocalDate.parse(checkOutText, dateFormatter);

            String status = (String) view.getEstateCombo().getSelectedItem();

            Reservation reservation = new Reservation();

            reservation.setIdReservation(
                    selectedReservation.getIdReservation()
            );

            reservation.setClient(selectedClient);
            reservation.setRoom(selectedRoom);
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);
            reservation.setStatus(status);

            reservationService.updateReservation(reservation);

            loadReservations();

            JOptionPane.showMessageDialog(
                    view, "Reserva modificada correctamente."
            );

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    view, "ingrese las fechas con el formato dd/MM/yyyy.", "Fecha incorrecta", JOptionPane.WARNING_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view, ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view, "Error al modificar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }


    /**
     * cancela la reserva seleccionada sin eliminarla de la db
     */
    private void cancelReservation() {

        if (selectedReservation == null) {
            JOptionPane.showMessageDialog(
                    view, "Primero debe seleccionar una reserva de la tabla"
            );
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
                view, " seguro de que desea cancelar esta reserva?", "confirmar cancelacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            reservationService.cancelReservation(
                    selectedReservation.getIdReservation()
            );

            selectedReservation = null;

            loadReservations();

            JOptionPane.showMessageDialog(
                    view, "Reserva cancelada correctamente"
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view, ex.getMessage(), "Datos incorrectos", JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view, "Error al cancelar la reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Busca reservas por DNI del cliente.
     */
    private void searchReservations() {

        String dni = view.getSearchField().getText().trim();

        //si el campo esta vacio muestro todas las reservas
        if (dni.isEmpty()) {
            loadReservations();
            return;
        }

        try {
            List<Reservation> reservations = reservationDao.findAll();

            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"id", "Cliente", "Habitacion", "Ingreso", "Salida", "Estado", "Total"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Reservation reservation : reservations) {

                if (reservation.getClient().getDni().equals(dni)) {

                    model.addRow(new Object[]{
                            reservation.getIdReservation(),
                            reservation.getClient().getName() + " " + reservation.getClient().getLastName(),
                            reservation.getRoom().getNumber(),
                            reservation.getCheckIn().format(dateFormatter),
                            reservation.getCheckOut().format(dateFormatter),
                            reservation.getStatus(),
                            reservation.getTotalPrice()
                    });
                }
            }

            view.getReservationTable().setModel(model);

            //ocultar la columna id
            view.getReservationTable().removeColumn(
                    view.getReservationTable().getColumnModel().getColumn(0)
            );

            selectedReservation = null;

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        view, "No se encontraron reservas para ese dni"
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view, "Error al buscar reservas: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    /**
     * Limpia el formulario para crear una nueva reserva.
     */
    private void clearForm() {

        //limpia la reserva y el cliente seleccionado
        selectedReservation = null;

        if (selectedReservation != null) {
            JOptionPane.showMessageDialog(view, "ya hay una reserva seleccionada. presiona Nuevo para crear otra");
            return;
        }
        selectedClient = null;

        //limpia los datos del cliente
        view.getTxtClientSearch().setText("");
        view.getLblClientSelected().setText("Ningun cliente seleccionado");

        //lmpia los datos de la reserva
        view.getRoomCombo().setSelectedIndex(-1);
        view.getDateOfAdmissionField().setValue(null);
        view.getDateOfAdmissionField().setText("");
        view.getDepartureDateField().setValue(null);
        view.getDepartureDateField().setText("");
        view.getFinalPriceField().setText("");

        //restaurar el estado inicial
        view.getEstateCombo().setSelectedItem("Pendiente");

        view.getReservationTable().clearSelection();
    }


}
