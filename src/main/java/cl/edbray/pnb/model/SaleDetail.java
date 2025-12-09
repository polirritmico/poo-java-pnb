/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.model;

import java.math.BigDecimal;

/**
 * Representa una línea de detalle de una venta
 * @author eduardo
 */
public class SaleDetail {
    private Integer id;
    private Integer saleId;
    private Integer productId;
    private String productName;
    private int amount;
    private double unitPrice;
    private double subtotal;

    public SaleDetail() {}

    public SaleDetail(Integer productId, String productName, int amount, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.amount = amount;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void calculateSubtotal() {
        this.subtotal = unitPrice * amount;
    }
}
