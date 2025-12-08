package cl.edbray.pnb.model;

import java.time.LocalDateTime;

public class Sale {

    private int id;
    private LocalDateTime dateTime;
    private int userId;
    private String userName;
    private double total;
    private String state;

    public Sale() {
    }

    public Sale(int id, LocalDateTime dateTime, int userId, String userName, double total, String state) {
        this.id = id;
        this.dateTime = dateTime;
        this.userId = userId;
        this.total = total;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getState() {
        return state;
    }

    public void setState(String estado) {
        this.state = estado;
    }

    @Override
    public String toString() {
        return "Venta #" + id + " - $" + String.format("%,.0f", total);
    }
}
