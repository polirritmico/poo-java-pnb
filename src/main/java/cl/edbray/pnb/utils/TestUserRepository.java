/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.utils;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.repository.UserRepository;
import cl.edbray.pnb.repository.impl.UserRepositoryMysql;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author eduardo
 */
public class TestUserRepository {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   TEST: UsuarioRepository (JDBC)         ");
        System.out.println("===========================================\n");

        UserRepository repo = new UserRepositoryMysql();

        // Test 1: Autenticar admin
        System.out.println("Test 1: Autenticar admin");
        Optional<User> admin = repo.authenticate("admin", "admin123");

        if (admin.isPresent()) {
            User u = admin.get();
            System.out.println("✅ Login exitoso:");
            System.out.println("   ID: " + u.getId());
            System.out.println("   Username: " + u.getUsername());
            System.out.println("   Nombre: " + u.getFullName());
            System.out.println("   Rol: " + u.getRole());
        } else {
            System.out.println("❌ Login falló");
        }

        System.out.println("\n-------------------------------------------\n");

        // Test 2: Listar todos los usuarios
        System.out.println("Test 2: Listar todos los usuarios");
        List<User> todos = repo.listAll();
        System.out.println("Total de usuarios: " + todos.size());

        todos.forEach(u -> {
            System.out.printf("%d - %s (%s) - %s%n",
                u.getId(), u.getUsername(), u.getRole(),
                u.isActive() ? "Activo" : "Inactivo");
        });

        System.out.println("\n-------------------------------------------\n");

        // Test 3: Buscar por username
        System.out.println("Test 3: Buscar por username");
        Optional<User> operador = repo.searchByUsername("operador");

        if (operador.isPresent()) {
            System.out.println("✅ Usuario encontrado:");
            System.out.println("   " + operador.get().getFullName());
        } else {
            System.out.println("❌ Usuario no encontrado");
        }

        System.out.println("\n===========================================");
        System.out.println("   TEST COMPLETADO                        ");
        System.out.println("===========================================");
    }

}
