/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service.impl;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.service.SalesService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class SalesServiceStub implements SalesService {
    private final List<Sale> sales;
    private int nextId;

    public SalesServiceStub() {
        this.sales = new ArrayList<>();
        this.nextId = 1;
        loadInitialData();
    }

    private void loadInitialData(){
        LocalDateTime now = LocalDateTime.now();

        sales.add(new Sale(nextId++, now.minusHours(3), 1, "admin", 5000, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(2), 2, "operador", 7500, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusHours(1), 1, "admin", 3000, "ACTIVA"));
        sales.add(new Sale(nextId++, now.minusMinutes(30), 2, "operador", 4500, "ACTIVA"));
    }

    @Override
    public List<Sale> listAll() {
        return new ArrayList<>(sales);
    }

    @Override
    public List<Sale> listByDate(LocalDate date) {
        return sales.stream()
            .filter(s -> s.getDateTime().toLocalDate().equals(date))
            .collect(Collectors.toList());
    }

    @Override
    public List<Sale> listTodaySales() {
        return listByDate(LocalDate.now());
    }

    @Override
    public Sale searchById(int id) {
        return sales.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public Sale save(Sale sale) {
        sale.setId(nextId++);
        sale.setDateTime(LocalDateTime.now());
        sale.setState("ACTIVA");
        sales.add(sale);
        System.out.println("[STUB] Venta registrada: " + sale);
        return sale;
    }

    @Override
    public void cancel(int id) {
        Sale sale = searchById(id);
        if (sale == null) { return; }

        sale.setState("ANULADA");
        System.out.println("[STUB] Venta anulada: " + id);
    }

    @Override
    public double calculateTotalByDate(LocalDate date) {
        return listByDate(date).stream()
            .filter(s -> "ACTIVA".equals(s.getState()))
            .mapToDouble(Sale::getTotal)
            .sum();
    }
}
