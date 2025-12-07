/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.controller;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.service.UserService;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void create(String username, String password, String fullName, String role) {
        service.create(username, password, fullName, role);
    }

    public void update(
        int id, String username, String password, String fullName, String role, boolean active
    ) {
        service.update(id, username, password, fullName, role, active);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<User> listAll() {
        return service.listAll();
    }

    public List<User> listActives() {
        return service.listActives();
    }

    public List<User> search(String search) {
        return service.search(search);
    }
}
