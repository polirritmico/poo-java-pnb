package cl.edbray.pnb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private int id;
    private LocalDateTime dateTime;
    private int userId;
    private double total;
    private String state;
    private String comment;

    private List<SaleDetail> details;

    public Sale() {
        this.details = new ArrayList<>();
    }

    public Sale(int id, LocalDateTime dateTime, int userId, String userName, double total, String state, String comment) {
        this.id = id;
        this.dateTime = dateTime;
        this.userId = userId;
        this.total = total;
        this.state = state;
        this.comment = comment;

        this.details = new ArrayList<>();
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SaleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetail> details) {
        this.details = details;
    }

    // -------------------------------------------------------------------------

    public void addDetail(SaleDetail detail) {
        this.details.add(detail);
        calculateTotal();
    }

    public void removeDetail(SaleDetail detail) {
        this.details.remove(detail);
        calculateTotal();
    }

    public void calculateTotal() {
        this.total = details.stream()
            .mapToDouble(SaleDetail::getSubtotal)
            .sum();
    }

    public boolean isActive() {
        return "ACTIVA".equals(state);
    }

    public boolean isCanceled() {
        return "ANULADA".equals(state);
    }


    @Override
    public String toString() {
        return "Venta #" + id + " - $" + String.format("%,.0f", total);
    }
}
