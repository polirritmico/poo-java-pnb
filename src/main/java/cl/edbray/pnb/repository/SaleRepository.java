/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.Sale;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de operaciones para acceso a datos de Venta
 *
 * @author eduardo
 */
public interface SaleRepository {

    /**
     * Busca una venta por su ID.
     *
     * @param id ID de la venta
     * @return Venta encontrada
     */
    Optional<Sale> searchById(int id) throws SQLException;

    /**
     * Lista todas las ventas.
     *
     * @return Lista de todas las ventas
     */
    List<Sale> listAll() throws SQLException;

    /**
     * Lista ventas activas dentro de un rango de fechas.
     *
     * @param from Fecha y hora de inicio (inclusive)
     * @param until Fecha y hora de fin (inclusive)
     * @return Lista de ventas activas en el rango especificado
     */
    List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until) throws SQLException;

    /**
     * Lista ventas del día actual.
     *
     * @return Lista de ventas realizadas hoy
     */
    List<Sale> listToday() throws SQLException;

    /**
     * Guarda una nueva venta.
     *
     * @param sale Venta a guardar
     * @return ID asignado a la venta
     */
    int save(Sale sale) throws SQLException;

    /**
     * Anula una venta (cambia estado a ANULADA).
     *
     * @param id ID de la venta
     */
    void cancel(int id) throws SQLException;
}
