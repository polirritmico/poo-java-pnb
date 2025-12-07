/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.controller;

import cl.edbray.pnb.model.User;
import cl.edbray.pnb.service.UserService;

/**
 *
 * @author eduardo
 */
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    public User authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }
}
