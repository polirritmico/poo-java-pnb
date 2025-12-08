/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Factory para obtener conextiones a la DB.
 *
 * @author eduardo
 */
public class DatabaseConnectionFactory {

    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("application.properties"));

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");

            // Optional with JDBC >4.0, but recomended
            Class.forName(driver);

            System.out.println("✅ Configuración de base de datos cargada correctamente");
            System.out.println("   URL: '" + url + "'");
            System.out.println("   Usuario: '" + username + "'");

        } catch (IOException e) {
            System.err.println("\n❌ Error al cargar application.properties");
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar la configuración de BD", e);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado: " + driver);
            e.printStackTrace();
            throw new RuntimeException("Driver JDBC no disponible", e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     *
     * El caller es responsable de cerrar la conexión cuando termine de usarla.
     * Se recomienda usar try-with-resources:
     *
     * <pre>
     * try (Connection conn = DatabaseConnectionFactory.getConnection()) {
     *     // Usar conexión
     * }
     * </pre>
     *
     * @return Connection activa a la base de datos
     * @throws SQLException si no se puede establecer conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Prueba la conexión a la base de datos.
     *
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean valid = conn != null && !conn.isClosed();
            if (valid) {
                System.out.println("✅ Conexión a base de datos exitosa");
                System.out.println("   Catálogo: " + conn.getCatalog());
            }
            return valid;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a base de datos");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la URL de conexión configurada. Útil para debugging o logging.
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Obtiene el usuario configurado.
     */
    public static String getUsername() {
        return username;
    }
}
