/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service.impl;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.service.UsersService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author eduardo
 */
public class UsersServiceStub implements UsersService {
    private List<User> users;
    private int nextId = 1;

    public UsersServiceStub() {
        this.users =new ArrayList<>();
        this.nextId = 1;
        loadInitialData();
    }

    private void loadInitialData() {
        users.add(new User(nextId++, "admin", "admin123", "Administrador del sistema", "ADMIN", true));
        users.add(new User(nextId++, "operador", "op123", "Alan Brito", "OPERADOR", true));
        users.add(new User(nextId++, "cajero", "caj123", "María Dolores", "OPERADOR", true));
    }

    @Override
    public List<User> listAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User searchById(int id) {
        return users.stream()
            .filter(u -> u.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<User> searchByUsername(String username) {
        String usernameLower = username.toLowerCase();
        return users.stream()
            .filter(u-> u.getUsername().toLowerCase().contains(usernameLower))
            .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        user.setId(nextId++);
        users.add(user);
        System.out.println("[STUB] Usuario guardado: " + user);
        return user;
    }

    @Override
    public void update(User user) {
        for (int i =0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                System.out.println("[STUB] Usuario actualizado: " + user);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        users.removeIf(u -> u.getId() == id);
        System.out.println("[STUB] Usuario eliminado:" + id);
    }

    @Override
    public void changeState(int id, boolean state) {
        User user = searchById(id);
        if (user == null) { return; }

        user.setActive(state);
        System.out.println("[STUB] Estado cambiado: " + id + " -> " + state);
    }

    @Override
    public User authenticate(String username, String password) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username) &&
                u.getPassword().equals(password) &&
                u.isActive())
            .findFirst()
            .orElse(null);
    }
}