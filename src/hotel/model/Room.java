package hotel.model;

import java.math.BigDecimal;

public class Room {

    private int idRoom;
    private int number;
    private String type;
    private int capacity;
    private BigDecimal pricePerNight;
    private String status;

    /*constructor para registrar una habitacion*/
    public Room(int number, String type, int capacity, BigDecimal pricePerNight, String status) {
        this.number = number;
        this.type = type;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public Room(int idRoom, int number, String type, int capacity,BigDecimal pricePerNight, String status) {
        this(number, type, capacity, pricePerNight, status);
        this.idRoom = idRoom;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}