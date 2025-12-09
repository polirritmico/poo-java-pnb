/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.impl;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.repository.UserRepository;
import cl.edbray.pnb.utils.MysqlDBConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author eduardo
 */
public class UserRepositoryMysql implements UserRepository {

    private static final String SQL_SELECT_ALL =
        "SELECT id, username, password, nombre_completo, rol, activo FROM usuario";

    private static final String SQL_SELECT_BY_ID =
        SQL_SELECT_ALL + " WHERE id = ?";

    private static final String SQL_SELECT_BY_USERNAME =
        SQL_SELECT_ALL + " WHERE username = ?";

    private static final String SQL_SELECT_ACTIVE =
        SQL_SELECT_ALL + " WHERE activo = TRUE";

    private static final String SQL_AUTHENTICATE =
        SQL_SELECT_ALL + " WHERE username = ? AND password = ? AND activo = TRUE";

    private static final String SQL_INSERT =
        "INSERT INTO usuario "
        + "(username, password, nombre_completo, rol, activo) "
        + "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_DELETE =
        "DELETE FROM usuario WHERE id = ?";

    private static final String SQL_UPDATE =
            "UPDATE usuario SET "
            + "username = ?, password = ?, nombre_completo = ?, rol = ?, activo = ? "
            + "WHERE id = ?";

    private static final String SQL_UPDATE_STATE =
        "UPDATE usuario SET activo = ? WHERE id = ?";

    // -------------------------------------------------------------------------

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("nombre_completo"));
        // TODO: use enum
        //user.setRole(Role.valueOf(rs.getString("rol")));
        user.setRole(rs.getString("rol"));
        user.setActive(rs.getBoolean("activo"));

        return user;
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_AUTHENTICATE)
        ) {
            ps.setString(1, username);
            // TODO: use secure hash
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + username);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<User> listAll() {
        List<User> users = new ArrayList<>();

        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todos los usuarios");
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<User> listActive() {
        List<User> users = new ArrayList<>();

        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ACTIVE);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los usuarios activos");
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Optional<User> searchById(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID);
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + id);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_USERNAME);
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por username: " + username);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUsername());
            // TODO: add hash
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            // TODO: use enum
            // ps.setString(4, user.getRole().name());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isActive());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + user.getUsername());
            e.printStackTrace();
            throw new RuntimeException("Error al crear usuario", e);
        }

        return user;
    }

    @Override
    public void update(User user) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole()); // TODO: use enum
            ps.setBoolean(5, user.isActive());
            ps.setInt(6, user.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Usuario no encontrado: " + user.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario ID: " + user.getId());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public void delete(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE)
        ) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Usuario no encontrado: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    public void enable(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATE)
        ) {
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al activar usuario ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al activar usuario", e);
        }
    }

    @Override
    public void disable(int id) {
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATE)
        ) {
            ps.setBoolean(1, false);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al desactivar usuario ID: " + id);
            e.printStackTrace();
            throw new RuntimeException("Error al desactivar usuario", e);
        }
    }

}
