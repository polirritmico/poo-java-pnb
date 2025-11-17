package cl.edbray.pnb.model;

/**
 *
 * @author eduardo
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String nombreCompleto;
    private String rol;
    private boolean activo;
    
    public User() {}

    public User(int id, String username, String password, String nombreCompleto, String rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return nombreCompleto;
    }

    public void setFullName(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRole() {
        return rol;
    }

    public void setRole(String rol) {
        this.rol = rol;
    }

    public boolean isActive() {
        return activo;
    }

    public void setActive(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username + "', rol='" + rol + "'}";
    }
}
