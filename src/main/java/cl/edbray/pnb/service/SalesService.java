/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.Sale;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de ventas.
 * @author eduardo
 */
public interface SalesService {
    /**
     * Lista todas las ventas.
     * @return Lista de ventas
     */
    List<Sale> listAll();

    /**
     * Lista ventas de una fecha específica.
     * @param date Fecha a consultar
     * @return Lista de ventas del día
     */
    List<Sale> listByDate(LocalDate date);

    /**
     * Lista ventas del día actual.
     * @return Lista de ventas de hoy
     */
    List<Sale> listTodaySales();

    /**
     * Busca una venta por su ID.
     * @param id ID de la venta
     * @return Venta encontrada o null
     */
    Sale searchById(int id);

    /**
     * Registra una nueva venta.
     * @param sale Venta a registrar
     * @return Venta registrada con ID asignado
     */
    Sale save(Sale sale);

    /**
     * Anula la venta (cambia estado a ANULADA).
     * @param id ID de la venta
     */
    void cancel(int id);

    /**
     * Calcula el total de ventas activas de una fecha.
     * @param date Fecha a consultar
     * @return Total en pesos
     */
    double calculateTotalByDate(LocalDate date);
}
