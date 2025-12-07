/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.Sale;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de operaciones para acceso a datos de Venta
 * @author eduardo
 */
public interface ISaleRepository {

    /**
     * Busca una venta por su ID.
     * @param id ID de la venta
     * @return Venta encontrada o null
     */
    Sale searchById(int id);

    /**
     * Lista todas las ventas.
     * @return Lista de todas las ventas
     */
    List<Sale> listAll();

    /**
     * Lista ventas dentro de un rango de fechas.
     * @param from Fecha y hora de inicio (inclusive)
     * @param until Fecha y hora de fin (inclusive)
     * @return Lista de ventas en el rango especificado
     */
    List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until);

    /**
     * Lista ventas del día actual.
     * @return Lista de ventas realizadas hoy
     */
    List<Sale> listTodaySales();

    /**
     * Lista ventas realizadas por un usuario específico.
     * @param userId ID del usuario
     * @return Lista de ventas del usuario
     */
    List<Sale> listByUser(int userId);

    /**
     * Guarda una nueva venta.
     * @param sale Venta a guardar
     * @return ID asignado a la venta
     */
    int save(Sale sale);

    /**
     * Anula una venta (cambia estado a ANULADA).
     * @param id ID de la venta
     */
    void cancel(int id);

    /**
     * Calcula el total de ventas activas dentro de un rango de fechas.
     * @param from Fecha y hora de inicio (inclusive)
     * @param until Fecha y hora de fin (inclusive)
     * @return Total de ventas en pesos
     */
    double calculateTotalByDateRange(LocalDateTime from, LocalDateTime until);

    /**
     * Calcula el total de ventas activas del día actual.
     * @return Total de ventas de hoy en pesos
     */
    double calculateTodaySalesTotal();
}
