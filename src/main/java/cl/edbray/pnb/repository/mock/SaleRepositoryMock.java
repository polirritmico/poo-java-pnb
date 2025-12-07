/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.mock;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.repository.ISaleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class SaleRepositoryMock implements ISaleRepository {
    private List<Sale> sales;
    private int nextId;

    public SaleRepositoryMock() {
        sales = new ArrayList<>();
        nextId = 1;
        loadInitialData();
    }

    private void loadInitialData() {
        LocalDateTime now = LocalDateTime.now();

        sales.add(new Sale(nextId++, now.minusHours(5), 1, "admin", 7500, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(4), 2, "operador1", 5000, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(3), 1, "admin", 12000, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(2), 3, "operador2", 3500, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(1), 2, "operador1", 8500, "ANULADA"));
        sales.add(new Sale(nextId++, now.minusMinutes(30), 1, "admin", 6000, "ACTIVA"));
    }

    @Override
    public Sale searchById(int id) {
        return sales.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Sale> listAll() {
        return new ArrayList<>(sales);
    }

    @Override
    public List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until) {
        return sales.stream()
            .filter(s -> !s.getDateTime().isBefore(from) && !s.getDateTime().isAfter(until))
            .collect(Collectors.toList());
    }

    @Override
    public List<Sale> listToday() {
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1).minusSeconds(1);
        return listByDateRange(dayStart, dayEnd);
    }

    @Override
    public List<Sale> listByUser(int userId) {
        return sales.stream()
            .filter(s -> s.getUserId() == userId)
            .collect(Collectors.toList());
    }

    @Override
    public int save(Sale sale) {
        sale.setId(nextId++);
        sale.setDateTime(LocalDateTime.now());
        sale.setState("ACTIVA");

        sales.add(sale);
        return sale.getId();
    }

    @Override
    public void cancel(int id) {
       Sale sale = searchById(id);
       if (sale != null) {
           sale.setState("ANULADA");
       }
    }

    @Override
    public double calculateTotalByDateRange(LocalDateTime from, LocalDateTime until) {
        return listByDateRange(from, until).stream()
            .filter(s -> "ACTIVA".equals(s.getState()))
            .mapToDouble(Sale::getTotal)
            .sum();
    }

    @Override
    public double calculateTodayTotal() {
        return listToday().stream()
            .filter(s -> "ACTIVA".equals(s.getState()))
            .mapToDouble(Sale::getTotal)
            .sum();
    }
}
