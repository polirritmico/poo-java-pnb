/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.mock;

import cl.edbray.pnb.model.Product;
import cl.edbray.pnb.repository.IProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class ProductRepositoryMock implements IProductRepository {

    private List<Product> products;
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
        products.add(new Product(nextId++, "15 minutos", "TIEMPO", "ARCADE", 1500, true));
        products.add(new Product(nextId++, "30 minutos", "TIEMPO", "ARCADE", 2500, true));
        products.add(new Product(nextId++, "1 hora", "TIEMPO", "ARCADE", 4000, true));
        products.add(new Product(nextId++, "2 horas", "TIEMPO", "ARCADE", 7000, true));

    }

    @Override
    public Product searchById(int id) {
        return products.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
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
    public List<Product> listByCategory(String category) {
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
    public int save(Product product) {
        product.setId(nextId++);
        products.add(product);
        return product.getId();
    }

    @Override
    public void update(Product product) {
        Product storedProduct = searchById(product.getId());
        if (storedProduct != null) {
            int idx = products.indexOf(storedProduct);
            products.set(idx, product);
        }
    }

    @Override
    public void delete(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    @Override
    public void changeState(int id, boolean state) {
        Product storedProduct = searchById(id);
        if (storedProduct != null) {
            storedProduct.setActive(state);
        }
    }
}
