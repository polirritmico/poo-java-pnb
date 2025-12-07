/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.controller;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.service.SaleService;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    public int registerSale(int userId, String userName, double total) {
        return service.registerSale(userId, userName, total);
    }

    public void cancel(int id) {
        service.cancel(id);
    }

    public List<Sale> listAll() {
        return service.listAll();
    }

    public List<Sale> listToday() {
        return service.listToday();
    }

    public List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until) {
        return service.listByDateRange(from, until);
    }

    public double calculateTodayTotal() {
        return service.calculateTodayTotal();
    }

    public double calculateTotalByRange(LocalDateTime from, LocalDateTime until) {
        return service.calculateTotalByRange(from, until);
    }
}
