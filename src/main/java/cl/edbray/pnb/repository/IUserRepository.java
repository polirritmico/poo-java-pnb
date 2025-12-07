/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.User;
import java.util.List;

/**
 * Contrato de operaciones para acceso a datos de Usuario
 * @author eduardo
 */
public interface IUserRepository {
    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado o null
     */
    User searchById(int id);

    /**
     * Busca un usuario por su username
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado o null
     */
    User searchByUsername(String username);

    /**
     * Lista todos los usuarios
     * @return Lista de todos los usuarios
     */
    List<User> listAll();

    /**
     * Lista usuarios por rol
     * @param role Rol a filtrar (ADMIN, OPERADOR)
     * @return Lista de usuarios con ese rol
     */
    List<User> listByRole(String role);

    /**
     * Guarda un nuevo usuario
     * @param user Usuario a guardar
     * @return ID asignado
     */
    int save(User user);

    /**
     * Actualiza un usuario existente
     * @param user Usuario con datos actualizados
     */
    void update(User user);

    /**
     * Elimina un usuario por ID
     * @param id ID del usuario a eliminar
     */
    void delete(int id);

    /**
     * Verifica si existe un username
     * @param username Username a verificar
     * @return true si existe, false si no
     */
    boolean usernameExists(String username);

    /**
     * Cuenta usuarios activos por rol
     * @param role Rol a contar
     * @return Cantidad de usuarios activos con ese rol
     */
    int countActivesByRole(String role);
}
