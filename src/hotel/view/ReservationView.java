package hotel.view;
import javax.swing.*;
import java.awt.*;

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
    private JButton btnDelete;
    private JComboBox clientCombo;
    private JComboBox roomCombo;
    private JComboBox estateCombo;
    private JFormattedTextField dateOfAdmissionField;
    private JFormattedTextField departureDateField;
    private JTextField finalPriceField;

    public ReservationView() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
}
