/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.impl;

import cl.edbray.pnb.model.Sale;
import cl.edbray.pnb.model.SaleDetail;
import cl.edbray.pnb.repository.SaleRepository;
import cl.edbray.pnb.utils.MysqlDBConnectionManager;
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

    private final MysqlDBConnectionManager connectionFactory;

    public SaleRepositoryMysql(MysqlDBConnectionManager connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

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

    private static final String SQL_INSERT_DETAIL =
        "INSERT INTO venta_detalle "
        + "(venta_id, producto_id, producto_nombre, cantidad, precio_unitario, subtotal) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

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

        return sale;
    }

    private SaleDetail mapDetail(ResultSet rs) throws SQLException {
        SaleDetail detail = new SaleDetail();
        detail.setId(rs.getInt("id"));
        detail.setSaleId(rs.getInt("venta_id"));
        detail.setProductId(rs.getInt("producto_id"));
        detail.setProductName(rs.getString("producto_nombre"));
        detail.setAmount(rs.getInt("amount"));
        detail.setUnitPrice(rs.getDouble("precio_unitario"));
        detail.setSubtotal(rs.getDouble("subtotal"));

        return detail;
    }

    @Override
    public Optional<Sale> searchById(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
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
            Connection conn = MysqlDBConnectionManager.getConnection();
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
            Connection conn = MysqlDBConnectionManager.getConnection();
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
            Connection conn = MysqlDBConnectionManager.getConnection();
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
        Connection conn = null;

        try {
            conn = connectionFactory.getConnection();
            conn.setAutoCommit(false);

            int saleId;
            try (
                PreparedStatement psSale = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)
            ) {
                psSale.setTimestamp(1, Timestamp.valueOf(sale.getDateTime()));
                psSale.setInt(2, sale.getUserId());
                psSale.setDouble(3, sale.getTotal());
                psSale.setString(4, sale.getState());

                psSale.executeUpdate();

                try (ResultSet rs = psSale.getGeneratedKeys()) {
                    if (rs.next()) {
                        saleId = rs.getInt(1);
                        sale.setId(saleId);
                    } else {
                        throw new SQLException("Error al obtener ID de venta");
                    }
                }
            }

            try(PreparedStatement prDetail = conn.prepareStatement(SQL_INSERT_DETAIL)) {
                for (SaleDetail detail : sale.getDetails()) {
                    prDetail.setInt(1, saleId);
                    prDetail.setInt(2, detail.getProductId());
                    prDetail.setString(3, detail.getProductName());
                    prDetail.setInt(4, detail.getAmount());
                    prDetail.setDouble(5, detail.getUnitPrice());
                    prDetail.setDouble(6, detail.getSubtotal());

                    prDetail.addBatch();
                }

                prDetail.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Error al guardar venta", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sale.getId();
    }

    @Override
    public void cancel(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
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

    private List<SaleDetail> findDetailsBySaleID(Connection conn, int saleId) {
        String sql = "SELECT * FROM venta_detalle WHERE venta_id = ?";

        List<SaleDetail> details = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    details.add(mapDetail(rs));
                }
                return details;
            }
        } catch (Exception e) {
            System.out.println("No se pudo buscar detalles por id de venta");
            e.printStackTrace();
        }
        return details;
    }
}
