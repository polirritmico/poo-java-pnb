/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.service;

import cl.edbray.pnb.model.User;
import java.util.List;

/**
 * Servicio para gestión de usuarios.
 * Define las operaciones CRUD y búsquedas disponibles.
 *
 * @author eduardo
 */
public interface UserService {

     /**
     * Lista todos los usuarios del sistema.
     * @return Lista de usuarios
     */
    List<User> listAll();

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    User searchById(int id);

    /**
     * Busca usuarios por username (parcial).
     * @param username Username a buscar (puede ser parcial)
     * @return Lista de usuarios que coinciden
     */
    List<User> searchByUsername(String username);

    /**
     * Guarda un nuevo usuario.
     * @param user Usuario a guardar (sin ID)
     * @return Usuario guardado con ID asignado
     */
    User save(User user);

    /**
     * Actualiza un usuario existente
     * @param user Usuario con cambios
     */
    void update(User user);

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar
     */
    void delete(int id);

    /**
     * Cambia el estado activo/inactivo de un usuario.
     * @param id ID del usuario
     * @param state Nuevo estado
     */
    void changeState(int id, boolean state);

    /**
     * Valida las credenciales de un usuario.
     * @param username Username
     * @param password Password
     * @return Usuario si las credenciales son válidas, null en caso contrario.
     */
    User authenticate(String username, String password);
}
