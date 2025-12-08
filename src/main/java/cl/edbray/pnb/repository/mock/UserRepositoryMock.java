/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.repository.mock;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class UserRepositoryMock implements UserRepository {

    private final List<User> users;
    private int nextId;

    public UserRepositoryMock() {
        users = new ArrayList<>();
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
    public Optional<User> searchById(int id) {
        return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return users.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst();
    }

    @Override
    public List<User> listAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User save(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }

    @Override
    public void update(User user) {
        searchById(user.getId()).ifPresent(storedUser -> {
            int idx = users.indexOf(storedUser);
            users.set(idx, user);
        });
    }

    @Override
    public void delete(int id) {
        users.removeIf(u -> u.getId() == id);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<User> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void enable(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void disable(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
