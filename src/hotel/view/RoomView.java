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
    }
}