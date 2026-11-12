package hotel.view;
import javax.swing.*;
import java.awt.*;

import hotel.model.Room;

public class ReservationView extends JPanel {
    private JPanel mainPanel;
    private JPanel reservationForPanel;
    private JPanel dataPanel;
    private JPanel abmPanel;
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton btnSearch;
    private JTable table1;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnCancel;
    private JComboBox<Room> roomCombo;
    private JComboBox<String> estateCombo;
    private JFormattedTextField dateOfAdmissionField;
    private JFormattedTextField departureDateField;
    private JTextField finalPriceField;
    private JTextField txtClientSearch;
    private JLabel lblClientSelected;
    private JButton btnSearchClient;
    private JButton btnNew;

    public ReservationView() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        finalPriceField.setEditable(false);

        estateCombo.removeAllItems();

        estateCombo.addItem("Pendiente");
        estateCombo.addItem("Confirmada");
        estateCombo.addItem("Cancelada");
        estateCombo.addItem("Finalizada");

        estateCombo.setSelectedIndex(0);
    }


    public JTextField getTxtClientSearch() {
        return txtClientSearch;
    }

    public JButton getBtnSearchClient() {
        return btnSearchClient;
    }

    public JLabel getLblClientSelected() {
        return lblClientSelected;
    }
    public JComboBox<Room> getRoomCombo() {
        return roomCombo;
    }

    public JFormattedTextField getDateOfAdmissionField() {
        return dateOfAdmissionField;
    }

    public JFormattedTextField getDepartureDateField() {
        return departureDateField;
    }

    public JTextField getFinalPriceField() {
        return finalPriceField;
    }
    public JComboBox<String> getEstateCombo() {
        return estateCombo;
    }
    public JButton getBtnSave() {
        return btnSave;
    }

    public JTable getReservationTable() {
        return table1;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }
    public JButton getBtnNew() {
        return btnNew;
    }
}
