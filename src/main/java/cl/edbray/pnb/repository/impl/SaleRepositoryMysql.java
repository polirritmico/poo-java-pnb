/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.impl;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.repository.SaleRepository;
import cl.edbray.pnb.utils.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author eduardo
 */
public class SaleRepositoryMysql implements SaleRepository{

    private static final String SQL_SELECT_BASE =
        "SELECT v.id, v.fecha_hora, v.usuario_id, v.total, v.estado, v.observaciones, u.username "
        + "FROM venta v "
        + "JOIN usuario u ON v.usuario_id = u.id";

    private static final String SQL_SELECT_BY_ID =
        SQL_SELECT_BASE + " WHERE v.id = ?";

    private static final String SQL_SELECT_BY_DATE_RANGE =
        SQL_SELECT_BASE + " WHERE v.fecha_hora >= ? AND v.fecha_hora < ? AND v.estado = 'ACTIVA' ORDER BY v.fecha_hora DESC";

    private static final String SQL_SELECT_TODAY =
        SQL_SELECT_BASE + " WHERE DATE(v.fecha_hora) = CURDATE() AND v.estado = 'ACTIVA' ORDER BY v.fecha_hora DESC";

    private static final String SQL_TOTAL_DAY =
        "SELECT COALESCE(SUM(total), 0) as total FROM venta WHERE DATE(fecha_hora) = CURDATE() AND estado = 'ACTIVA'";

    private static final String SQL_INSERT =
        "INSERT INTO venta (fecha_hora, usuario_id, total, estado) "
        + "VALUES (?, ?, ?, ?)";

    private static final String SQL_CANCEL =
        "UPDATE venta SET estado = 'ANULADA' WHERE id = ?";


    private Sale mapSale(ResultSet rs) throws SQLException{
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setDateTime(rs.getTimestamp("fecha_hora").toLocalDateTime());
        sale.setUserId(rs.getInt("usuario_id"));
        sale.setTotal(rs.getDouble("total"));
        sale.setState(rs.getString("estado"));
        // TODO: add new attribute?
        // sale.setComments(rs.getString("observaciones"));
        sale.setUserName(rs.getString("username"));

        return sale;
    }

    @Override
    public Optional<Sale> searchById(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID);
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapSale(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta por ID: " + id);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Sale> listAll() {
        List<Sale> sales = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BASE);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                sales.add(mapSale(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las ventas");
            e.printStackTrace();
        }

        return sales;
    }

    @Override
    public List<Sale> listByDateRange(LocalDateTime from, LocalDateTime until) {
        // TODO: validate this
        List<Sale> sales = new ArrayList<>();
        Timestamp fromTS = Timestamp.valueOf(from);
        Timestamp untilTS = Timestamp.valueOf(until);

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_DATE_RANGE);
        ) {
            ps.setTimestamp(1, fromTS);
            ps.setTimestamp(2, untilTS);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapSale(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por rango de fechas: "
                + fromTS.toString() + "-" + untilTS.toString());
            e.printStackTrace();
        }

        return sales;
    }

    @Override
    public List<Sale> listToday() {
        List<Sale> sales = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_TODAY);
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapSale(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ventas de hoy");
            e.printStackTrace();
        }

        return sales;
    }

    @Override
    public int save(Sale sale) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS
            );
        ) {
            //"INSERT INTO venta (fecha_hora, usuario_id, total, estado) "
            //this.dateTime = dateTime;
            //this.userId = userId;
            //this.total = total;
            //this.state = state;
            ps.setTimestamp(1, Timestamp.valueOf(sale.getDateTime()));
            ps.setInt(2, sale.getUserId());
            ps.setDouble(3, sale.getTotal());
            ps.setString(4, sale.getState());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sale.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar venta");
            e.printStackTrace();
            throw new RuntimeException("Error al registrar venta", e);
        }

        return sale.getId();
    }

    @Override
    public void cancel(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_CANCEL);
        ) {
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Venta no encontrada: ID " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al cancelar venta");
            e.printStackTrace();
            throw new RuntimeException("Error al cancelar venta", e);
        }
    }
}
