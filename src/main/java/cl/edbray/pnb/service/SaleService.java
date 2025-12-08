/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.Sale;
import java.time.LocalDateTime;
import java.util.List;
import cl.edbray.pnb.repository.SaleRepository;

/**
 *
 * @author eduardo
 */
public class SaleService {

    private final SaleRepository repository;

    public SaleService(SaleRepository repository) {
        this.repository = repository;
    }

    public int registerSale(int userId, String username, double total) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Usuario inválido");
        }
        if (total <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a 0");
        }

        Sale sale = new Sale();
        sale.setDateTime(LocalDateTime.now());
        sale.setUserId(userId);
        sale.setTotal(total);
        sale.setState("ACTIVA");

        return repository.save(sale);
    }

    public void cancel(int id) {
        repository.cancel(id);
    }

    public List<Sale> listAll() {
        return repository.listAll();
    }

    public List<Sale> listToday() {
        return repository.listToday();
    }

    public List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until) {
        return repository.listByDateRange(from, until);
    }

    public double calculateTodayTotal() {
        return repository.listToday().stream()
            .mapToDouble(Sale::getTotal)
            .sum();
    }

    public double calculateTotalByRange(LocalDateTime from, LocalDateTime until) {
        return repository.listByDateRange(from, until).stream()
            .mapToDouble(Sale::getTotal)
            .sum();
    }
}
