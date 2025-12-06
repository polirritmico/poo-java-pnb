/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service.impl;

import cl.edbray.pnb.model.Product;
import cl.edbray.pnb.service.ProductsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class ProductsServiceStub implements ProductsService {
    private List<Product> products;
    private int nextId;

    public ProductsServiceStub() {
        this.products =new ArrayList<>();
        this.nextId = 1;
        loadInitialData();
    }

    private void loadInitialData() {
        products.add(new Product(nextId++, "Espresso", "BEBIDA", "CAFE", 2500.0, true));
        products.add(new Product(nextId++, "Cappuccino", "BEBIDA", "CAFE", 3000.0, true));
        products.add(new Product(nextId++, "Latte", "BEBIDA", "CAFE", 3200.0, true));

        products.add(new Product(nextId++, "Brownie", "SNACK", "POSTRE", 2000.0, true));
        products.add(new Product(nextId++, "Galletas", "SNACK", "POSTRE", 1500.0, true));
        products.add(new Product(nextId++, "Sandwich", "SNACK", "SALADO", 3500.0, true));

        products.add(new Product(nextId++, "15 minutos", "TIEMPO", "ARCADE", 1500.0, true));
        products.add(new Product(nextId++, "30 minutos", "TIEMPO", "ARCADE", 2500.0, true));
        products.add(new Product(nextId++, "60 minutos", "TIEMPO", "ARCADE", 4000.0, true));
        products.add(new Product(nextId++, "Pase Diario", "TIEMPO", "ARCADE", 10000.0, true));
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
    public Product searchById(int id) {
        return products.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Product> searchByName(String name) {
        String nameLower = name.toLowerCase();
        return products.stream()
            .filter(p -> p.getName().toLowerCase().contains(nameLower))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByCategory(String category) {
        return products.stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        product.setId(nextId++);
        products.add(product);
        System.out.println("[STUB] Producto guardado: " + product);
        return product;
    }

    @Override
    public void update(Product product) {
        for (int i=0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                System.out.println("[STUB] Producto actualizado: " + product);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        products.removeIf(p -> p.getId() == id);
        System.out.println("[STUB] Producto eliminado: " + id);
    }

    @Override
    public void changeState(int id, boolean state) {
        Product product = searchById(id);
        if (product != null) {
            product.setActive(state);
            System.out.println("[STUB] Estado cambiado: " + state);
        }
    }
}
