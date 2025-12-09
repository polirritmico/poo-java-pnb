/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.utils.tests;

import cl.edbray.pnb.utils.MysqlDBConnectionManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase para probar la conexión a la DB.
 * Ejecutar como aplication Java independiente.
 * @author eduardo
 */
public class TestDBConnection {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   PRUEBA DE CONEXIÓN A BASE DE DATOS    ");
        System.out.println("===========================================\n");

        System.out.println("Test 1: Conexión básica");
        boolean connected = MysqlDBConnectionManager.testConnection();

        if (!connected) {
            System.err.println("\n❌ No se pudo conectar a la base de datos.");
            return;
        }

        System.out.println("\n-------------------------------------------\n");

        System.out.println("Test 2: Consultar usuarios");
        try (
            Connection conn = MysqlDBConnectionManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, rol FROM usuario")
        ) {
            System.out.println("\nUsuarios en la base de datos:");
            System.out.println("ID | Username       | Rol");
            System.out.println("---|----------------|----------");

            int count = 0;
            while (rs.next()) {
                count++;
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String rol = rs.getString("rol");
                System.out.printf("%-3d| %-15s| %s%n", id, username, rol);
            }

            System.out.println("\n✅ Total de usuarios: " + count);

        } catch (Exception e) {
            System.err.println("❌ Error al consultar usuarios:");
            e.printStackTrace();
        }

        System.out.println("\n-------------------------------------------\n");

        System.out.println("Test 3: Consultar productos");
        try (Connection conn = MysqlDBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT categoria, COUNT(*) as total FROM producto GROUP BY categoria")) {

            System.out.println("\nProductos por categoría:");
            System.out.println("Categoría       | Total");
            System.out.println("----------------|------");

            while (rs.next()) {
                String categoria = rs.getString("categoria");
                int total = rs.getInt("total");
                System.out.printf("%-16s| %d%n", categoria, total);
            }

            System.out.println("\n✅ Consulta exitosa");

        } catch (Exception e) {
            System.err.println("❌ Error al consultar productos:");
            e.printStackTrace();
        }

        System.out.println("\n===========================================");
        System.out.println("   PRUEBA COMPLETADA                      ");
    }
}
