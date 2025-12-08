/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.mock;

import cl.edbray.pnb.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import cl.edbray.pnb.repository.ProductRepository;
import java.util.Optional;

/**
 *
 * @author eduardo
 */
public class ProductRepositoryMock implements ProductRepository {

    private final List<Product> products;
    private int nextId;

    public ProductRepositoryMock() {
        products = new ArrayList<>();
        nextId = 1;
        loadInitialData();
    }

    private void loadInitialData() {
        products.add(new Product(nextId++, "Espresso", "BEBIDA", "CAFE", 2500, true));
        products.add(new Product(nextId++, "Cappuccino", "BEBIDA", "CAFE", 3000, true));
        products.add(new Product(nextId++, "Latte", "BEBIDA", "CAFE", 3200, true));
        products.add(new Product(nextId++, "Americano", "BEBIDA", "CAFE", 2800, true));
        products.add(new Product(nextId++, "Coca-Cola", "BEBIDA", "GASEOSA", 1500, true));
        products.add(new Product(nextId++, "Sprite", "BEBIDA", "GASEOSA", 1500, true));

        // Snacks
        products.add(new Product(nextId++, "Brownie", "SNACK", "POSTRE", 2000, true));
        products.add(new Product(nextId++, "Cheesecake", "SNACK", "POSTRE", 2500, true));
        products.add(new Product(nextId++, "Papas Fritas", "SNACK", "SALADO", 1800, true));
        products.add(new Product(nextId++, "Nachos", "SNACK", "SALADO", 2200, true));

        // Tiempo de Arcade
        products.add(new Product(nextId++, "15 minutos", "TIEMPO_ARCADE", "ARCADE", 1500, true));
        products.add(new Product(nextId++, "30 minutos", "TIEMPO_ARCADE", "ARCADE", 2500, true));
        products.add(new Product(nextId++, "1 hora", "TIEMPO_ARCADE", "ARCADE", 4000, true));
        products.add(new Product(nextId++, "2 horas", "TIEMPO_ARCADE", "ARCADE", 7000, true));

    }

    @Override
    public Optional<Product> searchById(int id) {
        return products.stream()
            .filter(p -> p.getId() == id)
            .findFirst();
    }

    @Override
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    @Override
    public List<Product> listActive() {
        return products.stream()
            .filter(Product::isActive)
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByCategory(String category) {
        return products.stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByName(String name) {
        // TODO: check, unussed by the ProductService
        return products.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        product.setId(nextId++);
        products.add(product);
        return product;
    }

    @Override
    public void update(Product product) {

        searchById(product.getId()).ifPresent(storedProduct -> {
            int idx = products.indexOf(storedProduct);
            products.set(idx, product);
        });
    }

    @Override
    public void delete(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    @Override
    public void enable(int id) {
        searchById(id).ifPresent(p -> {
            p.setActive(true);
        });
    }

    @Override
    public void disable(int id) {
        searchById(id).ifPresent(p -> {
            p.setActive(false);
        });
    }
}
