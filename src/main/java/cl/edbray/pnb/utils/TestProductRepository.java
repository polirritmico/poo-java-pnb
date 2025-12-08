/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.utils;

import cl.edbray.pnb.model.Product;
import cl.edbray.pnb.repository.ProductRepository;
import cl.edbray.pnb.repository.impl.ProductRepositoryMysql;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class TestProductRepository {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   TEST: ProductoRepository (JDBC)        ");
        System.out.println("===========================================\n");

        ProductRepository repo = new ProductRepositoryMysql();

        // Test 1: Listar productos activos
        System.out.println("Test 1: Listar productos activos");
        List<Product> activos = repo.listActive();
        System.out.println("Total: " + activos.size() + " productos");

        // Agrupar por categoría
        String categoriaActual = "";
        for (Product p : activos) {
            if (!p.getCategory().equals(categoriaActual)) {
                categoriaActual = p.getCategory();
                System.out.println("\n" + categoriaActual + ":");
            }
            System.out.printf("  - %s ($%.2f)%n", p.getName(), p.getPrice());
        }

        System.out.println("\n-------------------------------------------\n");

        // Test 2: Buscar por nombre
        System.out.println("Test 2: Buscar productos con 'café'");
        List<Product> cafes = repo.searchByName("café");
        System.out.println("Encontrados: " + cafes.size());
        cafes.forEach(p -> System.out.println("  - " + p.getName()));

        System.out.println("\n-------------------------------------------\n");

        // Test 3: Buscar por categoría
        System.out.println("Test 3: Productos de TIEMPO_ARCADE");
        List<Product> arcade = repo.searchByCategory("TIEMPO_ARCADE");
        arcade.forEach(p -> {
            System.out.printf("  - %s: $%.2f%n", p.getName(), p.getPrice());
        });

        System.out.println("\n===========================================");
        System.out.println("   TEST COMPLETADO                        ");
        System.out.println("===========================================");
    }

}
