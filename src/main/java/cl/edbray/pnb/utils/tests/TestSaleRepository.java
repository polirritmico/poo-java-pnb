/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.utils.tests;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.repository.SaleRepository;
import cl.edbray.pnb.repository.impl.SaleRepositoryMysql;
import cl.edbray.pnb.utils.MysqlDBConnectionManager;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class TestSaleRepository {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   TEST: VentaRepository (JDBC)           ");
        System.out.println("===========================================\n");


        MysqlDBConnectionManager connectionFactory = new MysqlDBConnectionManager();

        SaleRepository repository = new SaleRepositoryMysql(connectionFactory);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Test 1: Ventas del día
        System.out.println("Test 1: Ventas del día (activas)");
        List<Sale> hoy;
        hoy = repository.listToday();
        System.out.println("Total: " + hoy.size() + " ventas");

        hoy.forEach(v -> {
            System.out.printf("  #%d - %s - $%.2f",
                v.getId(),
                v.getDateTime().format(fmt),
                v.getTotal());
        });

        System.out.println("\n-------------------------------------------\n");

        // Test 2: Total del día
        System.out.println("Test 2: Total acumulado del día");
        double total;
        total = repository.listToday().stream()
            .mapToDouble(Sale::getTotal)
            .sum();
        System.out.printf("Total: $%.2f%n", total);

        System.out.println("\n===========================================");
        System.out.println("   TEST COMPLETADO                        ");
        System.out.println("===========================================");
    }
}
