/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.impl;

import cl.edbray.pnb.model.Product;
import cl.edbray.pnb.repository.ProductRepository;
import cl.edbray.pnb.utils.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de ProductRepository con JDBC Mysql
 *
 * @author eduardo
 */
public class ProductRepositoryMysql implements ProductRepository {

    private static final String SQL_SELECT_ALL =
        "SELECT id, nombre, categoria, tipo, descripcion, precio, activo FROM producto";

    private static final String SQL_SELECT_BY_ID =
        SQL_SELECT_ALL + " WHERE id = ?";

    private static final String SQL_SELECT_ACTIVE =
        SQL_SELECT_ALL + " WHERE activo = TRUE ORDER BY categoria, nombre";

    private static final String SQL_SELECT_BY_CATEGORY =
        SQL_SELECT_ALL + " WHERE categoria = ? AND activo = TRUE ORDER BY nombre";

    private static final String SQL_SELECT_BY_NAME =
        SQL_SELECT_ALL + " WHERE nombre LIKE ? ORDER BY nombre";

    private static final String SQL_INSERT =
        "INSERT INTO producto (nombre, categoria, tipo, descripcion, precio, activo) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE producto SET nombre = ?, categoria = ?, tipo = ?, descripcion = ?, precio = ?, activo = ? WHERE id = ?";

    private static final String SQL_DELETE =
        "DELETE FROM producto WHERE id = ?";

    private static final String SQL_UPDATE_STATE =
        "UPDATE producto SET activo = ? WHERE id = ?";

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("nombre"));
        product.setCategory(rs.getString("categoria"));
        product.setType(rs.getString("tipo"));
        product.setPrice(rs.getDouble("precio"));
        product.setActive(rs.getBoolean("activo"));

        return product;
    }

    @Override
    public List<Product> listAll() {
        List<Product> products = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL + " ORDER BY categoria, nombre");
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todos los productos");
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> listActive() {
        List<Product> products = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ACTIVE);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los productos activos");
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> searchByCategory(String category) {
        List<Product> products = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_CATEGORY);
        ) {
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar los productos por categoría: " + category);
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> searchByName(String name) {
        List<Product> products = new ArrayList<>();

        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_NAME);
        ) {
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + name);
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public Optional<Product> searchById(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID);
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por id: " + id);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getType());
            // TODO: implement
            //ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setBoolean(6, product.isActive());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + product.getName());
            e.printStackTrace();
            throw new RuntimeException("Error al crear producto", e);
        }

        return product;
    }

    @Override
    public void update(Product product) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE);
        ) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getType());
            // TODO: implement
            //ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setBoolean(6, product.isActive());
            ps.setInt(7, product.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto id: " + product.getId());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    @Override
    public void delete(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE);
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto id: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    @Override
    public void enable(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATE);
        ) {
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al activar producto id: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al activar producto", e);
        }
    }

    @Override
    public void disable(int id) {
        try (
            Connection conn = DatabaseConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATE);
        ) {
            ps.setBoolean(1, false);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al desactivar producto id: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al desactivar producto", e);
        }
    }
}
