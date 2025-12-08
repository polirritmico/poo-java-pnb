/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cl.edbray.pnb.repository;

import cl.edbray.pnb.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de operaciones para acceso a datos de Usuario
 *
 * @author eduardo
 */
public interface UserRepository {

    /**
     * Autentica un usuario por username y password.
     *
     * @param username usuario a autenticar
     * @param password password a utilizar
     * @return Optional con el usuario si las credenciales son válidas
     */
    Optional<User> authenticate(String username, String password);

    /**
     * Lista todos los usuarios (activos e inactivos)
     *
     * @return Lista de todos los usuarios
     */
    List<User> listAll();

    /**
     * Lista todos los usuarios activos.
     *
     * @return Lista de todos los usuarios activos
     */
    List<User> listActive();

    /**
     * Busca un usuario por su ID
     *
     * @param id ID del usuario
     * @return Optional del usuario encontrado
     */
    Optional<User> searchById(int id);

    /**
     * Busca un usuario por su username
     *
     * @param username Nombre de usuario a buscar
     * @return Optional con el usuario o vacío si no existe
     */
    Optional<User> searchByUsername(String username);

    /**
     * Guarda un nuevo usuario
     *
     * @param user Usuario a guardar
     * @return El usuario creado con su ID asignado
     */
    User save(User user);

    /**
     * Actualiza un usuario existente
     *
     * @param user Usuario con datos a actualizar
     */
    void update(User user);

    /**
     * Elimina un usuario por ID
     *
     * @param id ID del usuario a eliminar
     */
    void delete(int id);

    /**
     * Activa un usuario previamente desactivado
     * @param id ID del usuario a activar
     */
    void enable(int id);

    /**
     * Desactiva un usuario.
     * @param id ID del usuario a desactivar
     */
    void disable(int id);
}
