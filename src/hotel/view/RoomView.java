package hotel.view;

import javax.swing.*;
import java.awt.*;

public class RoomView extends JPanel {

    private JLabel TitleField;
    private JLabel subTitleField;
    private JPanel roomForPanel;
    private JPanel roomDataPanel;
    private JPanel searchPanel;
    private JPanel abmRoomPanel;
    private JTextField numberField;
    private JTextField priceField;
    private JTextField searchField;
    private JButton btnSearch;
    private JTable allRoomTable;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JComboBox typeCombo;
    private JComboBox estateCombo;
    private JSpinner CapacitySpinner;
    private JPanel mainPanel;

    public RoomView() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        typeCombo.setModel(new DefaultComboBoxModel<>(
                new String[]{"Simple", "Doble", "Triple", "Suite"}
        ));
        estateCombo.setModel(new DefaultComboBoxModel<>(
                new String[]{"Disponible", "Ocupada", "Mantenimiento"}
        ));
    }

    public JTextField getNumberField() {
        return numberField;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JTable getAllRoomTable() {
        return allRoomTable;
    }

    public JComboBox getTypeCombo() {
        return typeCombo;
    }

    public JComboBox getEstateCombo() {
        return estateCombo;
    }

    public JSpinner getCapacitySpinner() {
        return CapacitySpinner;
    }

}