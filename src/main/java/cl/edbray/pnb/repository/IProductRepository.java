/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.Product;
import java.util.List;

/**
 * Contrato de operaciones para acceso a datos de producto
 * @author eduardo
 */
public interface IProductRepository {
    /**
     * Busca un producto por su ID
     * @param id Id a buscar
     */
    Product searchById(int id);

    /**
     * Lista todos los productos
     * @return La lista con todos los productos
     */
    List<Product> listAll();

    /**
     * Lista productos activos solamente
     * @return lista con los productos activos
     */
    List<Product> listActive();

    /**
     * Lista productos por categoría
     * @param category BEBIDA, SNACK, TIEMPO
     * @return lista con los productos de la categoria ingresada
     */
    List<Product> listByCategory(String category);

    /**
     * Busca productos por nombre (búsqueda parcial)
     * @param name Nombre parcial a buscar
     * @return lista de los productos coincidentes
     */
    List<Product> searchByName(String name);

    /**
     * Guarda un nuevo producto
     * @param product Producto a guardar
     * @return El id asignado del producto agregado
     */
    int save(Product product);

    /**
     * Actualiza un producto existente
     */
    void update(Product product);

    /**
     * Elimina un producto por ID
     */
    void delete(int id);

    /**
     * Cambia el estado activo/inactivo de un producto
     */
    void setState(int id, boolean state);
}
