/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de operaciones para acceso a datos de producto
 *
 * @author eduardo
 */
public interface ProductRepository {

    /**
     * Lista todos los productos
     *
     * @return La lista con todos los productos
     */
    List<Product> listAll();

    /**
     * Lista productos activos solamente
     *
     * @return lista con los productos activos
     */
    List<Product> listActive();

    /**
     * Buscar productos por categoría
     *
     * @param category BEBIDA, SNACK, TIEMPO_ARCADE
     * @return lista con los productos encontrados de la categoria ingresada
     */
    List<Product> searchByCategory(String category);

    /**
     * Busca productos por nombre (búsqueda parcial)
     *
     * @param name Nombre parcial a buscar
     * @return lista de los productos coincidentes
     */
    List<Product> searchByName(String name);

    /**
     * Busca un producto por su ID
     *
     * @param id Id a buscar
     * @return Optional del producto encontrado
     */
    Optional<Product> searchById(int id);

    /**
     * Guarda un nuevo producto
     *
     * @param product Producto a guardar
     * @return El id asignado del producto agregado
     */
    Product save(Product product);

    /**
     * Actualiza un producto existente
     *
     * @param product El producto a actualizar
     */
    void update(Product product);

    /**
     * Elimina un producto por ID
     *
     * @param id La id del producto a eliminar
     */
    void delete(int id);

    /**
     * Cambia el estado de un producto a activo
     *
     * @param id Id del producto a actualizar
     */
    void enable(int id);

    /**
     * Cambia el estado de un producto a inactivo
     *
     * @param id Id del producto a actualizar
     */
    void disable(int id);
}
