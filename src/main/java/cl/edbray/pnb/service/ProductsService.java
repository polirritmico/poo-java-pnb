/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.Product;
import java.util.List;

/**
 * Servicio para gestión de productos.
 * @author eduardo
 */
public interface ProductsService {
    /**
     * Lista todos los productos.
     * @return Lista de productos
     */
    List<Product> listAll();

    /**
     * Lista solo productos activos.
     * @return Lista de productos activos
     */
    List<Product> listActive();

    /**
     * Busca un producto por su ID.
     * @param id ID del producto
     * @return Producto encontrado o null
     */
    Product searchById(int id);

    /**
     * Busca productos por nombre (parcial).
     * @param name Nombre a buscar
     * @return Lista de productos que coinciden
     */
    List<Product> searchByName(String name);

    /**
     * Filtra productos por categoría.
     * @param category Categoría (BEBIDA, SNACK, TIEMPO)
     * @return Lista de productos de la categoría.
     */
    List<Product> filterByCategory(String category);

    /**
     * Guardar un nuevo producto.
     * @param product Producto a guardar
     * @return Producto guardado con ID asignado
     */
    Product save(Product product);

    /**
     * Actualizar un producto existente.
     * @param product Producto con cambios
     */
    void actualizar(Product product);

    /**
     * Elimina un producto por su ID.
     * @param id ID del producto
     */
    void delete(int id);

    /**
     * Cambia el estado activo/inactivo de un producto.
     * @param id ID del producto
     * @param state Nuevo estado
     */
    void changeState(int id, boolean state);
}
