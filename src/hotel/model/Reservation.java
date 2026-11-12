
package hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    private int idReservation;
    private Client client;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private BigDecimal totalPrice;

    public Reservation() {
    }

    public Reservation(Client client, Room room, LocalDate checkIn, LocalDate checkOut, String status, BigDecimal totalPrice) {
        this.client = client;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Reservation(int idReservation, Client client, Room room, LocalDate checkIn, LocalDate checkOut, String status, BigDecimal totalPrice) {
        this(client, room, checkIn, checkOut, status, totalPrice);
        this.idReservation = idReservation;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
