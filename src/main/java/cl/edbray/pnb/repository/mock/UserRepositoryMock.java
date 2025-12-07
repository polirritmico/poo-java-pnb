/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.mock;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.repository.IUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class UserRepositoryMock implements IUserRepository {
    private final List<User> users;
    private int nextId;

    public UserRepositoryMock() {
        users =new ArrayList<>();
        nextId = 1;
        loadInitialData();
    }

    private void loadInitialData() {
        users.add(new User(nextId++, "admin", "admin123", "Administrador del Sistema", "ADMIN", true));
        users.add(new User(nextId++, "operador1", "op1234", "Alan Brito", "OPERADOR", true));
        users.add(new User(nextId++, "operador2", "op4567", "María Dolores", "OPERADOR", true));
        users.add(new User(nextId++, "cajero", "caj123", "Pedro Ramírez", "OPERADOR", false));
    }

    @Override
    public User searchById(int id) {
        return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public User searchByUsername(String username) {
        return users.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<User> listAll() {
        return new ArrayList<>(users);
    }

    @Override
    public List<User> listByRole(String role) {
        return users.stream()
            .filter(u -> u.getRole().equalsIgnoreCase(role))
            .collect(Collectors.toList());
    }

    @Override
    public int save(User user) {
        user.setId(nextId++);
        users.add(user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        User storedUser = searchById(user.getId());
        if (storedUser != null) {
            int idx = users.indexOf(storedUser);
            users.set(idx, user);
        }
    }

    @Override
    public void delete(int id) {
        users.removeIf(u -> u.getId() == id);
    }

    @Override
    public boolean usernameExists(String username) {
        return users.stream()
            .anyMatch(u -> u.getUsername().equals(username));
    }

    @Override
    public int countActivesByRole(String role) {
        return (int) users.stream()
            .filter(u -> u.getRole().equalsIgnoreCase(role))
            .filter(User::isActive)
            .count();
    }
}
