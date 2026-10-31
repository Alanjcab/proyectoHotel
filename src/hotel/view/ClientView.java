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

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getDniField() {
        return dniField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getSearchField() {
        return searchField;
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

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JTable getClientTable() {
        return clientTable;
    }
}