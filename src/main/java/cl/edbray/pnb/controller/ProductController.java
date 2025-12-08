/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.controller;

import cl.edbray.pnb.model.Product;
import cl.edbray.pnb.service.ProductService;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    public void create(String name, String category, String type, double price) {
        service.create(name, category, type, price);
    }

    public void update(
        int id, String name, String category, String type, double price, boolean active
    ) {
        service.update(id, name, category, type, price, active);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<Product> listAll() {
        return service.listAll();
    }

    public List<Product> listActives() {
        return service.listActives();
    }

    public List<Product> searchByCategory(String category) {
        return service.searchByCategory(category);
    }

    public List<Product> searchByName(String name) {
        return service.searchByName(name);
    }

    public void enable(int id) {
        service.enable(id);
    }

    public void disable(int id) {
        service.disable(id);
    }
}
