/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.Product;
import java.util.List;
import java.util.stream.Collectors;
import cl.edbray.pnb.repository.ProductRepository;

/**
 *
 * @author eduardo
 */
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void create(String name, String category, String type, double price) {
        validateProductData(name, category, type, price);

        Product product = new Product();
        product.setName(name.trim());
        product.setCategory(category);
        product.setType(type);
        product.setPrice(price);
        product.setActive(true);

        repository.save(product);
    }

    public void update(
        int id, String name, String category, String type, double price, boolean active
    ) {
        validateProductData(name, category, type, price);

        Product product = repository.searchById(id).orElseThrow(() -> {
            throw new RuntimeException("Producto no encontrado");
        });

        product.setName(name.trim());
        product.setCategory(category);
        product.setType(type);
        product.setPrice(price);
        product.setActive(active);

        repository.update(product);
    }

    public void delete(int id) {
        // TODO: add validations?
        repository.delete(id);
    }

    public void enable(int id) {
        repository.enable(id);
    }

    public void disable(int id) {
        repository.disable(id);
    }

    public List<Product> listAll() {
        return repository.listAll();
    }

    public List<Product> listActives() {
        return repository.listActive();
    }

    public List<Product> searchByCategory(String category) {
        return repository.searchByCategory(category);
    }

    public List<Product> searchByName(String name) {
        return repository.searchByName(name);
    }

    private void validateProductData(
        String name, String category, String type, double price
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        if (!category.equals("BEBIDA") && !category.equals("SNACK") && !category.equals("TIEMPO_ARCADE")) {
            throw new IllegalArgumentException("Categoría inválida. Debe ser BEBIDA, SNACK o TIEMPO_ARCADE");
        }

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
    }
}
