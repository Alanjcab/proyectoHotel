package hotel.controller;

import hotel.dao.RoomDao;
import hotel.validation.RoomValidator;
import hotel.view.RoomView;

import hotel.model.Room;
import hotel.exception.DaoException;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RoomController {

    private final RoomView view;
    private final RoomDao roomDao;
    private final RoomValidator validator;
    private Room selectedRoom;

    public RoomController(RoomView view, RoomDao roomDao) {
        this.view = view;
        this.roomDao = roomDao;
        this.validator = new RoomValidator();
        initController();
        loadRooms();
    }
    private void initController() {
        view.getBtnSave().addActionListener(e -> saveRoom());
        view.getBtnUpdate().addActionListener(e -> updateRoom());
        view.getBtnDelete().addActionListener(e -> deleteRoom());
        view.getBtnSearch().addActionListener(e -> searchRoom());
        view.getAllRoomTable().getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        selectRoom();
                    }
                });
    }


    private void saveRoom() {
        try {
            int number = Integer.parseInt(view.getNumberField().getText().trim());

            String type = view.getTypeCombo().getSelectedItem().toString();

            int capacity = (Integer) view.getCapacitySpinner().getValue();

            BigDecimal price = new BigDecimal(view.getPriceField().getText().trim());

            String status = view.getEstateCombo().getSelectedItem().toString();

            Room room = new Room(number, type, capacity, price, status);

            validator.validate(room);

            if (roomDao.findByNumber(number) != null) {
                JOptionPane.showMessageDialog(view, "Ya existe una habitacion con ese numero.");
                return;
            }

            roomDao.save(room);
            loadRooms();
            clearFields();

            JOptionPane.showMessageDialog(view, "Habitacion guardada correctamente");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Ingresa un numero de habitacion y un precio validos");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar la habitacion: " + ex.getMessage());
        }
    }



    private void searchRoom() {
        try {
            int number = Integer.parseInt(view.getSearchField().getText().trim());

            Room room = roomDao.findByNumber(number);

            if (room == null) {
                JOptionPane.showMessageDialog(view, "No se encontro una habitacion con ese numero");
                selectedRoom = null;
                return;
            }

            selectedRoom = room;

            view.getNumberField().setText(String.valueOf(room.getNumber()));

            view.getTypeCombo().setSelectedItem(room.getType());

            view.getCapacitySpinner().setValue(room.getCapacity());

            view.getPriceField().setText(room.getPricePerNight().toPlainString());

            view.getEstateCombo().setSelectedItem(room.getStatus());

            JOptionPane.showMessageDialog(view, "Habitacoin encontrada");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Ingresa un número de habitacion válido");

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(view, "Error al buscar la habitacion: " + ex.getMessage());
        }
    }



    private void updateRoom() {
        try {
            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(view, "Primero tenes que buscar una habitacion");
                return;
            }

            int number = Integer.parseInt(view.getNumberField().getText().trim());

            String type = view.getTypeCombo().getSelectedItem().toString();

            int capacity = (Integer) view.getCapacitySpinner().getValue();

            BigDecimal price = new BigDecimal(view.getPriceField().getText().trim());

            String status = view.getEstateCombo().getSelectedItem().toString();

            Room room = new Room(selectedRoom.getIdRoom(), number, type, capacity, price, status);

            validator.validate(room);

            Room existingRoom = roomDao.findByNumber(number);

            if (existingRoom != null && existingRoom.getIdRoom() != room.getIdRoom()) {
                JOptionPane.showMessageDialog(view, "Ya existe otra habitacion con ese numero");
                return;
            }

            roomDao.update(room);
            selectedRoom = room;
            loadRooms();
            clearFields();

            JOptionPane.showMessageDialog(view, "habitacion modificada correctamente");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Ingresaun numero y un precio valido");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(view, "Error al modificar la habitacionn: " + ex.getMessage());
        }
    }



    private void deleteRoom() {
        try {
            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(view, "Primero tenes que buscar una habitacion");
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    view, "Esta seguro de que quiere eliminar la habitacion" + selectedRoom.getNumber() + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION
            );

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            roomDao.delete(selectedRoom.getIdRoom());
            selectedRoom = null;
            loadRooms();
            clearFields();

            view.getNumberField().setText("");
            view.getPriceField().setText("");
            view.getSearchField().setText("");
            view.getTypeCombo().setSelectedIndex(0);
            view.getEstateCombo().setSelectedIndex(0);
            view.getCapacitySpinner().setValue(1);

            JOptionPane.showMessageDialog(
                    view, "Habitacion eliminada correctamente"
            );

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(
                    view, "No se pudo eliminar la habitacion. " + "Verificar si tiene reservas"
            );
        }
    }


    private void loadRooms() {
        try {
            List<Room> rooms = roomDao.findAll();
            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"ID", "Número", "Tipo", "Capacidad", "Precio por noche", "Estado"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Room room : rooms) {
                model.addRow(new Object[]{
                        room.getIdRoom(),
                        room.getNumber(),
                        room.getType(),
                        room.getCapacity(),
                        room.getPricePerNight(),
                        room.getStatus()
                });
            }

            view.getAllRoomTable().setModel(model);

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(
                    view, "Error al cargar las habitaciones: " + ex.getMessage()
            );
        }
    }



    /**
     * limpia los campos del formulario y la seleccion de la tabla
     */
    private void clearFields() {

        view.getNumberField().setText("");
        view.getPriceField().setText("");
        view.getSearchField().setText("");

        view.getTypeCombo().setSelectedIndex(0);
        view.getEstateCombo().setSelectedIndex(0);
        view.getCapacitySpinner().setValue(0);

        view.getAllRoomTable().clearSelection();

        selectedRoom = null;
    }



    /**
     *carga los datos de la habitacion seleccionada en la tabla
     */
    private void selectRoom() {

        int selectedRow = view.getAllRoomTable().getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        try {
            int modelRow = view.getAllRoomTable()
                    .convertRowIndexToModel(selectedRow);

            int roomId = (int) view.getAllRoomTable()
                    .getModel().getValueAt(modelRow, 0);

            selectedRoom = roomDao.findById(roomId);

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(
                        view, "No se encontro la habitacion seleccionada."
                );
                return;
            }

            view.getNumberField().setText(
                    String.valueOf(selectedRoom.getNumber())
            );

            view.getTypeCombo().setSelectedItem(
                    selectedRoom.getType()
            );

            view.getCapacitySpinner().setValue(
                    selectedRoom.getCapacity()
            );

            view.getPriceField().setText(
                    selectedRoom.getPricePerNight().toPlainString()
            );

            view.getEstateCombo().setSelectedItem(
                    selectedRoom.getStatus()
            );

        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(
                    view, "Error al seleccionar la habitacion" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


}
