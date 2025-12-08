/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.User;
import java.util.List;
import java.util.stream.Collectors;
import cl.edbray.pnb.repository.UserRepository;
import java.util.Optional;

/**
 *
 * @author eduardo
 */
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        User user = repository.searchByUsername(username.trim())
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Usuario inactivo. Contacta con el administrador.");
        }

        // TODO: use hash
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        return user;
    }

    public void create(String username, String password, String fullName, String role) {
        validateUserData(username, password, fullName, role);

        if (repository.searchByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya existe.");
        }

        User user = new User();
        user.setUsername(username.trim().toLowerCase());
        // TODO: use hash
        user.setPassword(password);
        user.setFullName(fullName.trim());
        user.setRole(role);
        user.setActive(true);

        repository.save(user);
    }

    public void update(
        int id, String username, String password, String fullName, String role, boolean active
    ) {
        validateUserData(username, password, fullName, role);

        User user = repository.searchById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        repository.searchByUsername(username).ifPresent(u -> {
            if (u.getId() != id) {
                throw new RuntimeException("El username '" + username + "' ya existe.");
            }
        });

        user.setUsername(username.trim().toLowerCase());
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password);
        }
        user.setFullName(fullName.trim());
        user.setRole(role);
        user.setActive(active);

        repository.update(user);
    }

    public void delete(int id) {
        User user = repository.searchById(id).orElseThrow(
            () -> new RuntimeException("No se puede eliminar el último administrador activo")
        );

        if (user.getRole().equals("ADMIN") && user.isActive()) {
            long activeAdmins = repository.listActive().stream()
                .filter(u -> u.getRole().equals("ADMIN"))
                .count();

            if (activeAdmins <= 1) {
                throw new RuntimeException("No se puede eliminar el último administrador activo");
            }
        }

        repository.delete(id);
    }

    public List<User> listAll() {
        return repository.listAll();
    }

    public List<User> listActives() {
        return repository.listAll().stream()
            .filter(User::isActive)
            .collect(Collectors.toList());
    }

    public List<User> search(String search) {
        if (search == null || search.isBlank()) {
            return listAll();
        }

        String searchLower = search.toLowerCase();
        return repository.listAll().stream()
            .filter(u -> u.getUsername().toLowerCase().contains(searchLower)
            || u.getFullName().toLowerCase().contains(searchLower))
            .collect(Collectors.toList());
    }

    private void validateUserData(String username, String password, String fullName, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (username.trim().length() < 4) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 4 caracteres.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
        if (!role.equals("ADMIN") && !role.equals("OPERADOR")) {
            throw new IllegalArgumentException("El rol debe ser ADMIN u OPERADOR");
        }
    }
}
