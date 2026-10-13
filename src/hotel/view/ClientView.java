package hotel.view;

import javax.swing.*;
import java.awt.*;

public class ClientView extends JPanel {

    private JPanel mainPanel;

    private JLabel titleLabel;
    private JLabel subTitleLabel;

    private JPanel clientForPanel;
    private JPanel clientsData;

    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField dniField;
    private JTextField phoneField;
    private JTextField emailField;

    private JPanel resultsPanel;
    private JTextField searchField;
    private JButton btnSearch;
    private JPanel tablePanel;
    private JTable clientTable;

    private JPanel abmClientPanel;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;

    public ClientView() {
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
}