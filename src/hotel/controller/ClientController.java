package hotel.controller;

import hotel.dao.ClientDao;
import hotel.model.Client;
import hotel.view.ClientView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class ClientController {

    private final ClientView view;
    private final ClientDao clientDao;

    private Client selectedClient;


    public ClientController(ClientView view, ClientDao clientDao) {
        this.view = view;
        this.clientDao = clientDao;

        configureTable();
        initController();
        loadClients();
    }

    private void initController() {
        view.getBtnSave().addActionListener(e -> saveClient());
        view.getBtnSearch().addActionListener(e -> searchClient());
        view.getBtnUpdate().addActionListener(e -> updateClient());
        view.getBtnDelete().addActionListener(e -> deleteClient());

        view.getClientTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectClientFromTable();
            }
        });
    }

    private void configureTable() {

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = view.getClientTable();

        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //oculto el id de la tbala de clientes
        table.removeColumn(table.getColumnModel().getColumn(0));
    }

    private void loadClients() {
        try {
            List<Client> clients = clientDao.findAll();

            DefaultTableModel model =
                    (DefaultTableModel) view.getClientTable().getModel();

            model.setRowCount(0);

            for (Client client : clients) {
                model.addRow(new Object[]{
                        client.getIdClient(),
                        client.getName(),
                        client.getLastName(),
                        client.getDni(),
                        client.getPhone(),
                        client.getEmail()
                });
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void saveClient() {
        try {
            String name = view.getNameField().getText().trim();
            String lastName = view.getLastNameField().getText().trim();
            String dni = view.getDniField().getText().trim();
            String phone = view.getPhoneField().getText().trim();
            String email = view.getEmailField().getText().trim();

            if (name.isEmpty() || lastName.isEmpty() || dni.isEmpty()) {
                showMessage("Nombre, apellido y dni son obligatorios");
                return;
            }

            Client existingClient = clientDao.findByDni(dni);

            if (existingClient != null) {
                if (!existingClient.getActive()) {
                    showMessage("El dni pertenece a un cliente dado de baja. Buscalo para habilitarlo");
                } else {
                    showMessage("Ya existe un cliente registrado con ese dni");
                }
                return;
            }

            Client client = new Client(name, lastName, dni, phone, email);

            clientDao.save(client);

            showMessage("Cliente guardado correctamente");
            clearFields();
            loadClients();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void searchClient() {
        try {
            String dni = view.getSearchField().getText().trim();

            if (dni.isEmpty()) {
                showMessage("Ingresá un dni para buscar");
                return;
            }

            Client client = clientDao.findByDni(dni);

            if (client == null) {
                selectedClient = null;
                showMessage("No se encontra ningún cliente con ese dni");
                return;
            }

            if (!client.getActive()) {
                int option = JOptionPane.showConfirmDialog(
                        view,
                        "Este cliente esta dado de baja. desea habilitarlo?",
                        "Habilitar cliente",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    clientDao.reactivate(client.getIdClient());
                    showMessage("Cliente habilitado correctamente");
                    loadClients();
                    client = clientDao.findByDni(dni);
                } else {
                    selectedClient = null;
                    return;
                }
            }

            selectedClient = client;
            fillFields(client);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void updateClient() {
        try {
            if (selectedClient == null) {
                showMessage("Primero selecciona un cliente");
                return;
            }

            String name = view.getNameField().getText().trim();
            String lastName = view.getLastNameField().getText().trim();
            String dni = view.getDniField().getText().trim();
            String phone = view.getPhoneField().getText().trim();
            String email = view.getEmailField().getText().trim();

            if (name.isEmpty() || lastName.isEmpty() || dni.isEmpty()) {
                showMessage("Nombre, apellido y dni son obligatorios");
                return;
            }

            Client existingClient = clientDao.findByDni(dni);

            if (existingClient != null &&
                    existingClient.getIdClient() != selectedClient.getIdClient()) {
                showMessage("El dni ya pertenece a otro cliente");
                return;
            }

            selectedClient.setName(name);
            selectedClient.setLastName(lastName);
            selectedClient.setDni(dni);
            selectedClient.setPhone(phone);
            selectedClient.setEmail(email);

            clientDao.update(selectedClient);

            showMessage("Cliente modificado correctamente");
            clearFields();
            loadClients();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void deleteClient() {
        try {
            if (selectedClient == null) {
                showMessage("Primero selecciona un cliente");
                return;
            }

            int option = JOptionPane.showConfirmDialog(
                    view,
                    "Queres dar de baja a este cliente?",
                    "Confirmar baja",
                    JOptionPane.YES_NO_OPTION
            );

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            clientDao.delete(selectedClient.getIdClient());

            showMessage("Cliente dado de baja correctamente");
            clearFields();
            loadClients();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void selectClientFromTable() {

        JTable table = view.getClientTable();
        int row = table.getSelectedRow();

        if (row == -1 || row >= table.getRowCount()) {
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            int modelRow = table.convertRowIndexToModel(row);

            //vrifico que la tabla exista en el modelo
            if (modelRow < 0 || modelRow >= model.getRowCount()) {
                return;
            }

            //obtengo el id cliente
            int id = (int) model.getValueAt(modelRow, 0);

            Client client = clientDao.findById(id);

            if (client != null) {
                selectedClient = client;
                fillFields(client);
            }

        } catch (IndexOutOfBoundsException ex) {
            return;

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void fillFields(Client client) {
        view.getNameField().setText(client.getName());
        view.getLastNameField().setText(client.getLastName());
        view.getDniField().setText(client.getDni());
        view.getPhoneField().setText(client.getPhone());
        view.getEmailField().setText(client.getEmail());
    }

    private void clearFields() {
        selectedClient = null;

        view.getClientTable().clearSelection();

        view.getNameField().setText("");
        view.getLastNameField().setText("");
        view.getDniField().setText("");
        view.getPhoneField().setText("");
        view.getEmailField().setText("");
        view.getSearchField().setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
                view,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}