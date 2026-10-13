package hotel.view;

import javax.swing.*;

public class MainView extends JFrame {

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JLabel lblTitel;
    private JButton btnClients;
    private JButton btnRooms;
    private JButton btnReservations;
    private JButton btnExit;

    public MainView() {
        setTitle("Sistema de Hotelería");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    public void showView(JPanel view) {
        contentPanel.removeAll();
        contentPanel.setLayout(new java.awt.BorderLayout());
        contentPanel.add(view, java.awt.BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    public JButton getBtnClients() {
        return btnClients;
    }

    public JButton getBtnRooms() {
        return btnRooms;
    }

    public JButton getBtnReservations() {
        return btnReservations;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
}